using GardenCatalogue;
using VerifyNUnit;

namespace GardenCatalogueTest;

[TestFixture]
public class GardenCatalogueManagerApprovalTests
{
    private GardenCatalogueManager _manager;

    [SetUp]
    public void Setup()
    {
        _manager = new GardenCatalogueManager(TestData.DefaultPlants);
    }

    [Test]
    public async Task HedgePlanning()
    {
        var minHeight = 2.0;
        var hedge = _manager.PlanHedge(minHeight);
        await Verifier.Verify(new
        {
            MinHeight = minHeight,
            Result = hedge
        });
    }

    [Test]
    public async Task ConditionFiltering()
    {
        var soil = SoilCondition.Sandy;
        var light = LightCondition.FullSun;
        var plants = _manager.GetPlantsForCondition(soil, light);
        await Verifier.Verify(new
        {
            Result = plants
        });
    }

    [Test]
    public async Task BedPlanning()
    {
        var month = Month.June;
        var maxHeight = 2.0;
        var bedInJune = _manager.PlanBed(month, maxHeight);
        await Verifier.Verify(new
        {
            Month = month,
            MaxHeight = maxHeight,
            Result = bedInJune
        });
    }
}
