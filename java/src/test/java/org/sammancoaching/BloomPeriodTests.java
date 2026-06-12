package org.sammancoaching;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class BloomPeriodTests {

    @Test
    public void FromSeason_Spring_ReturnsCorrectMonths() {
        BloomPeriod period = BloomPeriod.fromSeason(Season.Spring);

        assertEquals(List.of(Month.March, Month.April, Month.May), period.months());
    }

    @Test
    public void FromSeason_Summer_ReturnsCorrectMonths() {
        BloomPeriod period = BloomPeriod.fromSeason(Season.Summer);

        assertEquals(List.of(Month.June, Month.July, Month.August), period.months());
    }

    @Test
    public void FromSeason_Autumn_ReturnsCorrectMonths() {
        BloomPeriod period = BloomPeriod.fromSeason(Season.Autumn);

        assertEquals(List.of(Month.September, Month.October, Month.November), period.months());
    }

    @Test
    public void FromSeason_Winter_ReturnsCorrectMonths() {
        BloomPeriod period = BloomPeriod.fromSeason(Season.Winter);

        assertEquals(List.of(Month.December, Month.January, Month.February), period.months());
    }

    @Test
    public void FromRange_SameMonth_ReturnsSingleMonth() {
        BloomPeriod period = BloomPeriod.fromRange(Month.June, Month.June);

        assertEquals(List.of(Month.June), period.months());
    }

    @Test
    public void FromRange_NormalRange_ReturnsMonthsInRange() {
        BloomPeriod period = BloomPeriod.fromRange(Month.April, Month.June);

        assertEquals(List.of(Month.April, Month.May, Month.June), period.months());
    }

    @Test
    public void FromRange_WrappingRange_ReturnsMonthsInRangeAcrossYear() {
        BloomPeriod period = BloomPeriod.fromRange(Month.November, Month.February);

        assertEquals(List.of(Month.November, Month.December, Month.January, Month.February), period.months());
    }

    @Test
    public void BloomsIn_WhenMonthIncluded_ReturnsTrue() {
        BloomPeriod period = new BloomPeriod(List.of(Month.May, Month.June));

        assertTrue(period.bloomsIn(Month.May));
        assertTrue(period.bloomsIn(Month.June));
    }

    @Test
    public void BloomsIn_WhenMonthNotIncluded_ReturnsFalse() {
        BloomPeriod period = new BloomPeriod(List.of(Month.May, Month.June));

        assertFalse(period.bloomsIn(Month.April));
        assertFalse(period.bloomsIn(Month.July));
    }
}
