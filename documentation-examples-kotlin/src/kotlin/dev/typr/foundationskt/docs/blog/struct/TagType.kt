package dev.typr.foundationskt.docs.blog.struct

import dev.typr.foundationskt.*

@Suppress("unused")
class TagType {
    val tx: Transactor = null!! // placeholder

    //start
    @JvmInline
    value class Tag(val value: String)

    val tagType: PgType<Tag> = PgTypes.text.transform(::Tag, Tag::value)
    val tagArrayType: PgType<Array<Tag>> = PgTypes.text.array().transform(
        { strings -> Array(strings.size) { Tag(strings[it]) } },
        { tags -> Array(tags.size) { tags[it].value } }
    )

    fun findByTags(tags: Array<Tag>): List<String> =
        sql { "SELECT name FROM products WHERE tags && ${tagArrayType(tags)}" }
            .queryAll(RowCodec.of(PgTypes.text))
            .transact(tx)
    //stop
}
