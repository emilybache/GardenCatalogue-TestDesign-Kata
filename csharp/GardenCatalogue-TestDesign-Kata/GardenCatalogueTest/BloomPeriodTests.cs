using GardenCatalogue;

namespace GardenCatalogueTest;

public class BloomPeriodTests
{
    [Test]
    public void FromSeason_Spring_ReturnsCorrectMonths()
    {
        var period = BloomPeriod.FromSeason(Season.Spring);
        Assert.That(period.Months, Is.EquivalentTo(new[] { Month.March, Month.April, Month.May }));
    }

    [Test]
    public void FromSeason_Summer_ReturnsCorrectMonths()
    {
        var period = BloomPeriod.FromSeason(Season.Summer);
        Assert.That(period.Months, Is.EquivalentTo(new[] { Month.June, Month.July, Month.August }));
    }

    [Test]
    public void FromSeason_Autumn_ReturnsCorrectMonths()
    {
        var period = BloomPeriod.FromSeason(Season.Autumn);
        Assert.That(period.Months, Is.EquivalentTo(new[] { Month.September, Month.October, Month.November }));
    }

    [Test]
    public void FromSeason_Winter_ReturnsCorrectMonths()
    {
        var period = BloomPeriod.FromSeason(Season.Winter);
        Assert.That(period.Months, Is.EquivalentTo(new[] { Month.December, Month.January, Month.February }));
    }

    [Test]
    public void FromRange_SameMonth_ReturnsSingleMonth()
    {
        var period = BloomPeriod.FromRange(Month.June, Month.June);
        Assert.That(period.Months, Is.EquivalentTo(new[] { Month.June }));
    }

    [Test]
    public void FromRange_NormalRange_ReturnsMonthsInRange()
    {
        var period = BloomPeriod.FromRange(Month.April, Month.June);
        Assert.That(period.Months, Is.EquivalentTo(new[] { Month.April, Month.May, Month.June }));
    }

    [Test]
    public void FromRange_WrappingRange_ReturnsMonthsInRangeAcrossYear()
    {
        var period = BloomPeriod.FromRange(Month.November, Month.February);
        Assert.That(period.Months, Is.EquivalentTo(new[] { Month.November, Month.December, Month.January, Month.February }));
    }

    [Test]
    public void BloomsIn_WhenMonthIncluded_ReturnsTrue()
    {
        var period = new BloomPeriod(Month.May, Month.June);
        Assert.That(period.BloomsIn(Month.May), Is.True);
        Assert.That(period.BloomsIn(Month.June), Is.True);
    }

    [Test]
    public void BloomsIn_WhenMonthNotIncluded_ReturnsFalse()
    {
        var period = new BloomPeriod(Month.May, Month.June);
        Assert.That(period.BloomsIn(Month.April), Is.False);
        Assert.That(period.BloomsIn(Month.July), Is.False);
    }

    [Test]
    public void ImplicitOperator_FromMonth_CreatesBloomPeriodWithOneMonth()
    {
        BloomPeriod period = Month.September;
        Assert.That(period.Months, Is.EquivalentTo(new[] { Month.September }));
    }
}
