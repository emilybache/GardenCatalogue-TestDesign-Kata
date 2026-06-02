using System.Text;
using GardenCatalogue;
using NUnit.Framework;

namespace GardenCatalogueTest;

[TestFixture]
public class PlantDataParserTests
{
    private PlantDataParser _dataParser;

    [SetUp]
    public void SetUp()
    {
        BugConfigurations.Reset();
        _dataParser = new PlantDataParser();
    }

    [Test]
    public void Parse_EmptyStream_ReturnsEmptyList()
    {
        using var stream = new MemoryStream();
        var result = _dataParser.Parse(stream);
        Assert.That(result, Is.Empty);
    }

    [Test]
    public void Parse_SingleV1Plant_ReturnsCorrectData()
    {
        var plants = new List<Plant>
        {
            new PlantBuilder().WithName("Test Bush").WithType(PlantType.Bush).Build()
        };

        byte[] binaryData = CreateBinaryData(plants, version: 1);
        using var stream = new MemoryStream(binaryData);

        var result = _dataParser.Parse(stream).ToList();

        Assert.That(result.Count, Is.EqualTo(1));
        Assert.That(result[0].Name, Is.EqualTo("Test Bush"));
        Assert.That(result[0].Type, Is.EqualTo(PlantType.Bush));
    }

    [Test]
    public void Parse_ValidV1BinaryData_ReturnsPlants()
    {
        var plants = new List<Plant>
        {
            new PlantBuilder().WithName("Flower1").WithBloomPeriod(BloomPeriod.FromRange(Month.May, Month.July)).WithMaxHeight(0.5).WithSoil(SoilCondition.Sandy).WithLight(LightCondition.FullSun).Build(),
            new PlantBuilder().WithName("Bush1").WithType(PlantType.Bush).WithBloomPeriod(new BloomPeriod(Month.August)).WithMaxHeight(2.0).WithSoil(SoilCondition.Clay).WithLight(LightCondition.PartialShade).Build()
        };

        byte[] binaryData = CreateBinaryData(plants, version: 1);
        Console.WriteLine($"[DEBUG_LOG] Binary Data Length: {binaryData.Length}");
        using var stream = new MemoryStream(binaryData);

        var result = _dataParser.Parse(stream).ToList();

        Assert.That(result.Count, Is.EqualTo(2));
        Assert.That(result[0].Name, Is.EqualTo("Flower1"));
        Assert.That(result[0].Type, Is.EqualTo(PlantType.Flower), "First plant type should be Flower");
        Assert.That(result[0].MaxHeight, Is.EqualTo(0.5));
        Assert.That(result[0].Soil, Is.EqualTo(SoilCondition.Sandy));
        Assert.That(result[0].Light, Is.EqualTo(LightCondition.FullSun));
        Assert.That(result[0].BloomPeriod.Months, Is.EquivalentTo(new[] { Month.May, Month.June, Month.July }));
        
        Assert.That(result[1].Name, Is.EqualTo("Bush1"), "Second plant name mismatch");
        Assert.That(result[1].Type, Is.EqualTo(PlantType.Bush), "Second plant type should be Bush");
        Assert.That(result[1].MaxHeight, Is.EqualTo(2.0));
        Assert.That(result[1].Soil, Is.EqualTo(SoilCondition.Clay));
        Assert.That(result[1].Light, Is.EqualTo(LightCondition.PartialShade));
        Assert.That(result[1].BloomPeriod.Months, Is.EquivalentTo(new[] { Month.August }));
    }

    [Test]
    public void Parse_V2ExtendedData_ReturnsPlantsWithAllFields()
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

        Assert.That(result.Count, Is.EqualTo(2));
        var plant = result[0];
        Assert.That(plant.Name, Is.EqualTo("Lavender"));
        Assert.That(plant.LatinName, Is.EqualTo("Lavandula angustifolia"));
        Assert.That(plant.ArticleNumber, Is.EqualTo("FLO-001"));
        Assert.That(plant.Properties["Color"], Is.EqualTo("Purple"));
        Assert.That(plant.Properties["Fragrance"], Is.EqualTo("High"));

        var minimal = result[1];
        Assert.That(minimal.Name, Is.EqualTo("Minimal Plant"));
        Assert.That(minimal.LatinName, Is.Empty);
        Assert.That(minimal.ArticleNumber, Is.Empty);
        Assert.That(minimal.Properties, Is.Empty);
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
