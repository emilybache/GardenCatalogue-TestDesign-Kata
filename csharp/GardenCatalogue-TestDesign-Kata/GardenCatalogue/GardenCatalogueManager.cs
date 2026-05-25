namespace GardenCatalogue;

public class GardenCatalogueManager
{
    private readonly List<Plant> _plants = new();

    public GardenCatalogueManager()
    {
    }

    public GardenCatalogueManager(IEnumerable<Plant> plants)
    {
        _plants.AddRange(plants);
    }

    public int GetPlantCount() => _plants.Count;

    public Plant? GetPlantByName(string name) => _plants.FirstOrDefault(p => p.Name == name);

    public void AddPlant(Plant plant) => _plants.Add(plant);

    public IEnumerable<Plant> GetPlantsForCondition(SoilCondition soil, LightCondition light)
    {
        return _plants.Where(p => (p.Soil == soil || p.Soil == SoilCondition.Any) && p.Light == light);
    }

    public IEnumerable<Plant> PlanBed(Month month, double maxHeight)
    {
        return _plants.Where(p => p.Type == PlantType.Flower && 
                                 p.BloomPeriod.BloomsIn(month) && 
                                 p.MaxHeight <= maxHeight);
    }

    public IEnumerable<Plant> PlanHedge(double minHeight)
    {
        return _plants.Where(p => (p.Type == PlantType.Bush || p.Type == PlantType.Tree) && 
                                 p.MaxHeight >= minHeight);
    }
}
