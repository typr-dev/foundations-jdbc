package dev.typr.foundationskt.docs.blog.struct

import dev.typr.foundationskt.*

@Suppress("unused")
class TagType {
    val tx: Transactor = null!! // placeholder

    //start
    @JvmInline
    value class Tag(val value: String)

    val tagType: PgType<Tag> = PgTypes.text.transform(::Tag, Tag::value)
    val tagArrayType: PgType<List<Tag>> = PgTypes.text.array().transform(
        { strings -> strings.map(::Tag) },
        { tags -> tags.map(Tag::value) }
    )

    fun findByTags(tags: List<Tag>): List<String> =
        sql { "SELECT name FROM products WHERE tags && ${tagArrayType(tags)}" }
            .queryAll(RowCodec.of(PgTypes.text))
            .transact(tx)
    //stop
}
