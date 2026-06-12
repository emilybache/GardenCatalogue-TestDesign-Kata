package org.sammancoaching;

import java.util.List;

public record BloomPeriod(List<Month> months) {
    public BloomPeriod {
        months = List.copyOf(months);
    }

    public static BloomPeriod fromSeason(Season season) {
        return switch (season) {
            case Spring -> new BloomPeriod(List.of(Month.March, Month.April, Month.May));
            case Summer -> new BloomPeriod(List.of(Month.June, Month.July, Month.August));
            case Autumn -> new BloomPeriod(List.of(Month.September, Month.October, Month.November));
            case Winter -> new BloomPeriod(List.of(Month.December, Month.January, Month.February));
        };
    }

    public static BloomPeriod fromRange(Month start, Month end) {
        int s = start.getValue();
        int e = end.getValue();
        java.util.List<Month> months = new java.util.ArrayList<>();

        if (s <= e) {
            for (int i = s; i <= e; i++) {
                months.add(Month.fromInt(i));
            }
        } else {
            for (int i = s; i <= 12; i++) {
                months.add(Month.fromInt(i));
            }
            for (int i = 1; i <= e; i++) {
                months.add(Month.fromInt(i));
            }
        }
        return new BloomPeriod(months);
    }

    public boolean bloomsIn(Month month) {
        return months.contains(month);
    }
}
