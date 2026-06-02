using System.Text;
using GardenCatalogue;
using VerifyNUnit;
using static VerifyNUnit.Verifier;

namespace GardenCatalogueTest;

[TestFixture]
public class GardenCatalogueManagerApprovalTests
{
    private GardenCatalogueManager _manager;
    private PlantPrinter _plantPrinter;

    [SetUp]
    public void Setup()
    {
        _manager = new GardenCatalogueManager(TestData.DefaultPlants);
        _plantPrinter = new PlantPrinter();
    }

    [Test]
    public Task HedgePlanning()
    {
        var minHeight = 2.0;
        var hedge = _manager.PlanHedge(minHeight);
        return Verify(PrintScenario(new { MinHeight = minHeight }, hedge));
    }

    [Test]
    public Task ConditionFiltering()
    {
        var soil = SoilCondition.Sandy;
        var light = LightCondition.FullSun;
        var plants = _manager.GetPlantsForCondition(soil, light);
        return Verify(PrintScenario(new { Soil = soil, Light = light }, plants));
    }

    [Test]
    public Task BedPlanning()
    {
        var month = Month.June;
        var maxHeight = 2.0;
        var bedInJune = _manager.PlanBed(month, maxHeight);
        return Verify(PrintScenario(new { Month = month, MaxHeight = maxHeight }, bedInJune));
    }

    private string PrintScenario(object input, IEnumerable<Plant> result)
    {
        var sb = new StringBuilder();
        sb.AppendLine("=== INPUT ===");
        sb.AppendLine(input.ToString());
        sb.AppendLine();

        var resultList = result.ToList();
        sb.AppendLine("=== RESULT PLANTS ===");
        if (!resultList.Any())
        {
            sb.AppendLine("(None)");
        }
        else
        {
            for (int i = 0; i < resultList.Count; i++)
            {
                sb.AppendLine($"--- Plant {i + 1} ---");
                _plantPrinter.Print(sb, resultList[i]);
            }
        }

        return sb.ToString();
    }
}
