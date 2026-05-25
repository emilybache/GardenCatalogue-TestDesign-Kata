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
        var hedge = _manager.PlanHedge(2.0);
        await Verifier.Verify(hedge);
    }

    [Test]
    public async Task ConditionFiltering()
    {
        var plants = _manager.GetPlantsForCondition(SoilCondition.Sandy, LightCondition.FullSun);
        await Verifier.Verify(plants);
    }

    [Test]
    public async Task BedPlanning()
    {
        var bedInJune = _manager.PlanBed(Month.June, 2.0);
        await Verifier.Verify(bedInJune);
    }
}
