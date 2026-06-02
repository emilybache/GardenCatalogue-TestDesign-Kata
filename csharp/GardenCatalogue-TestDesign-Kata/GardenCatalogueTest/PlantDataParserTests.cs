using System.Text;
using GardenCatalogue;
using NUnit.Framework;
using VerifyNUnit;
using static VerifyNUnit.Verifier;

namespace GardenCatalogueTest;

[TestFixture]
public class PlantDataParserTests
{
    private PlantDataParser _dataParser;
    private PlantPrinter _plantPrinter;

    [SetUp]
    public void SetUp()
    {
        BugConfigurations.Reset();
        _dataParser = new PlantDataParser();
        _plantPrinter = new PlantPrinter();
    }

    [Test]
    public Task EmptyStream_EmptyList()
    {
        using var stream = new MemoryStream();
        var result = _dataParser.Parse(stream);
        return Verify(PrintScenario("Empty Stream", 2, result));
    }

    [Test]
    public Task V1_SinglePlant_ValidResult()
    {
        var plants = new List<Plant>
        {
            new PlantBuilder().WithName("Test Bush").WithType(PlantType.Bush).Build()
        };

        byte[] binaryData = CreateBinaryData(plants, version: 1);
        using var stream = new MemoryStream(binaryData);

        var result = _dataParser.Parse(stream).ToList();

        return Verify(PrintScenario(plants, 2, result));
    }

    [Test]
    public Task V1_TwoPlants_ValidResult()
    {
        var plants = new List<Plant>
        {
            new PlantBuilder().WithName("Flower1").WithBloomPeriod(BloomPeriod.FromRange(Month.May, Month.July)).WithMaxHeight(0.5).WithSoil(SoilCondition.Sandy).WithLight(LightCondition.FullSun).Build(),
            new PlantBuilder().WithName("Bush1").WithType(PlantType.Bush).WithBloomPeriod(new BloomPeriod(Month.August)).WithMaxHeight(2.0).WithSoil(SoilCondition.Clay).WithLight(LightCondition.PartialShade).Build()
        };

        byte[] binaryData = CreateBinaryData(plants, version: 1);
        using var stream = new MemoryStream(binaryData);

        var result = _dataParser.Parse(stream).ToList();

        return Verify(PrintScenario(plants, 2, result));
    }

    [Test]
    public Task V2_TwoPlants_ValidResult()
    {
        var plants = new List<Plant>
        {
            new PlantBuilder()
                .WithName("Lavender")
                .WithLatinName("Lavandula angustifolia")
                .WithArticleNumber("FLO-001")
                .WithBloomPeriod(BloomPeriod.FromRange(Month.June, Month.August))
                .WithMaxHeight(0.6)
                .WithSoil(SoilCondition.Sandy)
                .WithLight(LightCondition.FullSun)
                .WithProperty("Color", "Purple")
                .WithProperty("Fragrance", "High")
                .Build(),
             new PlantBuilder()
                .WithName("Minimal Plant")
                .Build()
        };

        byte[] binaryData = CreateBinaryData(plants, version: 2);
        using var stream = new MemoryStream(binaryData);

        var result = _dataParser.Parse(stream).ToList();
        
        return Verify(PrintScenario(plants, 2, result));
    }

    [Test]
    public Task V2_SinglePlant_ValidResult()
    {
        var plants = new List<Plant>
        {
            new PlantBuilder()
                .WithName("Red Rose")
                .WithLatinName("Rosa rubiginosa")
                .WithArticleNumber("ROS-001")
                .WithBloomPeriod(BloomPeriod.FromRange(Month.June, Month.September))
                .WithMaxHeight(1.5)
                .WithSoil(SoilCondition.Loamy)
                .WithLight(LightCondition.FullSun)
                .WithProperty("Color", "Deep Red")
                .WithProperty("Fragrance", "Strong")
                .WithProperty("Thorns", "Yes")
                .WithProperty("Difficulty", "Medium")
                .Build()
        };

        byte[] binaryData = CreateBinaryData(plants, version: 2);
        using var stream = new MemoryStream(binaryData);

        var result = _dataParser.Parse(stream).ToList();

        return Verify(PrintScenario(plants, 2, result));
    }

    private string PrintScenario(object input, int version, IEnumerable<Plant> result)
    {
        var sb = new StringBuilder();
        sb.AppendLine("=== INPUT DATA ===");
        if (input is string s)
        {
            sb.AppendLine(s);
        }
        else if (input is IEnumerable<Plant> plants)
        {
            var list = plants.ToList();
            for (int i = 0; i < list.Count; i++)
            {
                sb.AppendLine($"--- Item {i + 1} ---");
                _plantPrinter.Print(sb, list[i]);
            }
        }

        sb.AppendLine();
        sb.AppendLine($"--- Parser Version {version} ---");
        sb.AppendLine();

        var resultList = result.ToList();
        sb.AppendLine("=== OUTPUT PLANTS ===");
        if (resultList.Count == 0)
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

    private static byte[] CreateBinaryData(IEnumerable<Plant> plants, int version = 2)
    {
        using var stream = new MemoryStream();
        using var writer = new BinaryWriter(stream, Encoding.UTF8);

        if (version == 2)
        {
            writer.Write("PLNT"u8.ToArray());
        }

        foreach (var plant in plants)
        {
            byte[] nameBytes = new byte[PlantDataParser.NameLength];
            byte[] actualNameBytes = Encoding.UTF8.GetBytes(plant.Name);
            Array.Copy(actualNameBytes, 0, nameBytes, 0, Math.Min(actualNameBytes.Length, PlantDataParser.NameLength));
            writer.Write(nameBytes);

            writer.Write((int)plant.Type);
            writer.Write(plant.MaxHeight);
            writer.Write((int)plant.Soil);
            writer.Write((int)plant.Light);

            int bloomMask = 0;
            foreach (var month in plant.BloomPeriod.Months)
            {
                bloomMask |= (1 << ((int)month - 1));
            }
            writer.Write(bloomMask);

            if (version == 2)
            {
                // LatinName (Length-prefixed)
                writer.Write(plant.LatinName);
                // ArticleNumber (Length-prefixed)
                writer.Write(plant.ArticleNumber);
                // Properties count
                writer.Write(plant.Properties.Count);
                foreach (var kvp in plant.Properties)
                {
                    writer.Write(kvp.Key);
                    writer.Write(kvp.Value);
                }
            }
        }

        return stream.ToArray();
    }
}
