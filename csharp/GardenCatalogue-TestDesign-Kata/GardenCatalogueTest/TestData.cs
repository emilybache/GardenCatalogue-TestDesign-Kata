using GardenCatalogue;

namespace GardenCatalogueTest;

public static class TestData
{
    public static IEnumerable<Plant> DefaultPlants => new List<Plant>
    {
        new PlantBuilder().WithName("Lavender").WithLatinName("Lavandula angustifolia").WithArticleNumber("FLO-001").WithBloomPeriod(BloomPeriod.FromRange(Month.June, Month.August)).WithMaxHeight(0.6).WithSoil(SoilCondition.Sandy).WithLight(LightCondition.FullSun).Build(),
        new PlantBuilder().WithName("Rose").WithLatinName("Rosa").WithArticleNumber("BSH-001").WithType(PlantType.Bush).WithBloomPeriod(BloomPeriod.FromRange(Month.June, Month.September)).WithMaxHeight(1.5).WithSoil(SoilCondition.Loamy).WithLight(LightCondition.FullSun).Build(),
        new PlantBuilder().WithName("Hydrangea").WithLatinName("Hydrangea macrophylla").WithArticleNumber("BSH-002").WithType(PlantType.Bush).WithBloomPeriod(BloomPeriod.FromRange(Month.July, Month.September)).WithMaxHeight(1.2).WithSoil(SoilCondition.Clay).WithLight(LightCondition.PartialShade).Build(),
        new PlantBuilder().WithName("Boxwood").WithLatinName("Buxus sempervirens").WithArticleNumber("BSH-003").WithType(PlantType.Bush).WithMaxHeight(1.0).WithSoil(SoilCondition.Any).WithLight(LightCondition.PartialShade).Build(),
        new PlantBuilder().WithName("Beech").WithLatinName("Fagus sylvatica").WithArticleNumber("TRE-001").WithType(PlantType.Tree).WithBloomPeriod(Month.May).WithMaxHeight(5.0).WithSoil(SoilCondition.Any).WithLight(LightCondition.FullSun).Build(),
        new PlantBuilder().WithName("Yew").WithLatinName("Taxus baccata").WithArticleNumber("TRE-002").WithType(PlantType.Tree).WithMaxHeight(4.0).WithSoil(SoilCondition.Any).WithLight(LightCondition.PartialShade).Build(),
        new PlantBuilder().WithName("Oak").WithLatinName("Quercus robur").WithArticleNumber("TRE-003").WithType(PlantType.Tree).WithMaxHeight(20.0).WithSoil(SoilCondition.Any).WithLight(LightCondition.FullSun).Build(),
        new PlantBuilder().WithName("Silver Birch").WithLatinName("Betula pendula").WithArticleNumber("TRE-004").WithType(PlantType.Tree).WithMaxHeight(15.0).WithSoil(SoilCondition.Sandy).WithLight(LightCondition.FullSun).Build(),
        new PlantBuilder().WithName("Fern").WithLatinName("Polypodiopsida").WithArticleNumber("BSH-004").WithType(PlantType.Bush).WithMaxHeight(0.5).WithSoil(SoilCondition.Loamy).WithLight(LightCondition.FullShade).Build()
    };
}
