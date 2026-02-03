package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.PgType
import dev.typr.foundations.PgTypes
import dev.typr.foundations.data.Range
import dev.typr.foundations.data.RangeBound
import java.time.LocalDate

@Suppress("unused")
class RangeTypes {
    //start
    val intRangeType: PgType<Range<Int>> = PgTypes.int4range
    val dateRangeType: PgType<Range<LocalDate>> = PgTypes.daterange

    // Create ranges with explicit bounds
    val range: Range<Int> = Range.int4(
        RangeBound.Closed(1),
        RangeBound.Closed(10)
    )  // [1, 11) after normalization

    // Check containment
    val contains: Boolean = range.contains(5)  // true
    //stop
}
