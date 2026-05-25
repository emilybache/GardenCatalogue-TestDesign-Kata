using GardenCatalogue;

namespace GardenCatalogueTest;

public class GardenCatalogueManagerTests
{
    
    [Test]
    public void GetPlantsForCondition_ReturnsCorrectPlants()
    {
        // Arrange
        var manager = new GardenCatalogueManager(TestData.DefaultPlants);
        
        var soil = SoilCondition.Sandy;
        var light = LightCondition.FullSun;

        // Act
        var result = manager.GetPlantsForCondition(soil, light).ToList();
    
        // Assert
        Assert.That(result.Select(p => p.Name), 
            Is.EquivalentTo(new[] { "Lavender", "Beech", "Oak", "Silver Birch" }));
    }

    [Test]
    public void PlanBed_ReturnsFlowersInBloom()
    {
        // Arrange
        var manager = new GardenCatalogueManager(TestData.DefaultPlants);
 
        Month month = Month.July;
        double maxHeight = 1.0;

        // Act
        var result = manager.PlanBed(month, maxHeight).ToList();

        // Assert
        Assert.That(result.Select(p => p.Name), 
            Is.EquivalentTo(new[] { "Lavender" }));
    }

    [Test]
    public void PlanBed_WithSpringSeason_ReturnsSpringFlowers()
    {
        // Arrange
        var manager = new GardenCatalogueManager(TestData.DefaultPlants);
        manager.AddPlant(new Plant("Spring Crocus", "Crocus vernus", "FLO-002", PlantType.Flower, BloomPeriod.FromSeason(Season.Spring), 0.1, SoilCondition.Any, LightCondition.FullSun, new Dictionary<string, string>()));
        
        // Act & Assert
        Assert.That(manager.PlanBed(Month.March, 1.0).Select(p => p.Name), 
            Is.EquivalentTo(new[] { "Spring Crocus" }));
        Assert.That(manager.PlanBed(Month.April, 1.0).Select(p => p.Name), 
            Is.EquivalentTo(new[] { "Spring Crocus" }));
        Assert.That(manager.PlanBed(Month.May, 1.0).Select(p => p.Name), 
            Is.EquivalentTo(new[] { "Spring Crocus" }));
        Assert.That(manager.PlanBed(Month.February, 1.0).Select(p => p.Name), 
            Is.Empty);
    }

    [Test]
    public void PlanBed_WithSpecificMonths_ReturnsFlowers()
    {
        // Arrange
        var manager = new GardenCatalogueManager();
        manager.AddPlant(new Plant("Summer Lily", "Lilium", "FLO-003", PlantType.Flower, new BloomPeriod(Month.July, Month.August), 0.5, SoilCondition.Any, LightCondition.FullSun, new Dictionary<string, string>()));
        
        // Act & Assert
        Assert.That(manager.PlanBed(Month.July, 1.0).Select(p => p.Name), 
            Is.EquivalentTo(new[] { "Summer Lily" }));
        Assert.That(manager.PlanBed(Month.August, 1.0).Select(p => p.Name), 
            Is.EquivalentTo(new[] { "Summer Lily" }));
        Assert.That(manager.PlanBed(Month.June, 1.0).Select(p => p.Name), 
            Is.Empty);
    }

    [Test]
    public void PlanHedge_ReturnsTallBushesAndTrees()
    {
        // Arrange
        var manager = new GardenCatalogueManager(TestData.DefaultPlants);

        double minHeight = 4.0;

        // Act
        var result = manager.PlanHedge(minHeight).ToList();

        // Assert
        Assert.That(result.Select(p => p.Name), 
            Is.EquivalentTo(new[] { "Beech", "Oak", "Silver Birch", "Yew" }));
    }
    
}
