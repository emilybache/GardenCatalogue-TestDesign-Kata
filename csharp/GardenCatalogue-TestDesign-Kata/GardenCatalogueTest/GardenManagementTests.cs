using GardenCatalogue;

namespace GardenCatalogueTest;

public class GardenManagementTests
{
    [Test]
    public void ManagingCatalogueScenario()
    {
        var manager = new GardenCatalogueManager(TestData.DefaultPlants);
        
        Assert.That(manager.GetPlantCount(), Is.EqualTo(9));
        
        manager.AddPlant(new PlantBuilder().WithName("Tulip").WithLatinName("Tulipa").WithArticleNumber("FLO-004").WithBloomPeriod(BloomPeriod.FromRange(Month.April, Month.May)).WithMaxHeight(0.4).WithSoil(SoilCondition.Loamy).WithLight(LightCondition.FullSun));
        Assert.That(manager.GetPlantCount(), Is.EqualTo(10));
        Assert.That(manager.GetPlantByName("Tulip"), Is.Not.Null);
        
        var springBed = manager.PlanBed(Month.April, 0.5).ToList();
        Assert.That(springBed, Has.Count.EqualTo(1));
        Assert.That(springBed[0].Name, Is.EqualTo("Tulip"));
        
        var privacyHedge = manager.PlanHedge(1.5).ToList();
        
        Assert.That(privacyHedge, Has.Count.EqualTo(5)); 
        Assert.That(privacyHedge.Any(p => p.Name == "Rose"), Is.True);
        Assert.That(privacyHedge.Any(p => p.Name == "Beech"), Is.True);
        
        var shadePlants = manager.GetPlantsForCondition(SoilCondition.Loamy, LightCondition.FullShade).ToList();
        Assert.That(shadePlants, Has.Count.EqualTo(1));
        Assert.That(shadePlants[0].Name, Is.EqualTo("Fern"));
    }
}
