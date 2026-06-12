from garden_catalogue.models import BloomPeriod, Season, Month

class TestBloomPeriod:
    def test_from_season_spring_returns_correct_months(self):
        period = BloomPeriod.from_season(Season.Spring)
        assert sorted(period.months) == sorted([Month.March, Month.April, Month.May])

    def test_from_season_summer_returns_correct_months(self):
        period = BloomPeriod.from_season(Season.Summer)
        assert sorted(period.months) == sorted([Month.June, Month.July, Month.August])

    def test_from_season_autumn_returns_correct_months(self):
        period = BloomPeriod.from_season(Season.Autumn)
        assert sorted(period.months) == sorted([Month.September, Month.October, Month.November])

    def test_from_season_winter_returns_correct_months(self):
        period = BloomPeriod.from_season(Season.Winter)
        assert sorted(period.months) == sorted([Month.December, Month.January, Month.February])

    def test_from_range_same_month_returns_single_month(self):
        period = BloomPeriod.from_range(Month.June, Month.June)
        assert period.months == [Month.June]

    def test_from_range_normal_range_returns_months_in_range(self):
        period = BloomPeriod.from_range(Month.April, Month.June)
        assert period.months == [Month.April, Month.May, Month.June]

    def test_from_range_wrapping_range_returns_months_in_range_across_year(self):
        period = BloomPeriod.from_range(Month.November, Month.February)
        assert period.months == [Month.November, Month.December, Month.January, Month.February]

    def test_blooms_in_when_month_included_returns_true(self):
        period = BloomPeriod([Month.May, Month.June])
        assert period.blooms_in(Month.May) is True
        assert period.blooms_in(Month.June) is True

    def test_blooms_in_when_month_not_included_returns_false(self):
        period = BloomPeriod([Month.May, Month.June])
        assert period.blooms_in(Month.April) is False
        assert period.blooms_in(Month.July) is False
