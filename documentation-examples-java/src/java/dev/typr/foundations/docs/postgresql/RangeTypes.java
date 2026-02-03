package dev.typr.foundations.docs.postgresql;

import dev.typr.foundations.PgType;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.data.Range;
import dev.typr.foundations.data.RangeBound;

import java.time.LocalDate;

@SuppressWarnings("unused")
public class RangeTypes {
    //start
    PgType<Range<Integer>> intRangeType = PgTypes.int4range;
    PgType<Range<LocalDate>> dateRangeType = PgTypes.daterange;

    // Create ranges with explicit bounds
    Range<Integer> range = Range.int4(
        new RangeBound.Closed<>(1),
        new RangeBound.Closed<>(10)
    );  // [1, 11) after normalization

    // Check containment
    boolean contains = range.contains(5);  // true
    //stop
}
