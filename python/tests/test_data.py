from garden_catalogue.models import Plant, PlantType, BloomPeriod, SoilCondition, LightCondition, Month
from garden_catalogue.helpers import PlantBuilder

class TestData:
    DEFAULT_PLANTS = [
        PlantBuilder().with_name("Lavender").with_latin_name("Lavandula angustifolia").with_article_number("FLO-001").with_bloom_period(BloomPeriod.from_range(Month.June, Month.August)).with_max_height(0.6).with_soil(SoilCondition.Sandy).with_light(LightCondition.FullSun).build(),
        PlantBuilder().with_name("Rose").with_latin_name("Rosa").with_article_number("BSH-001").with_type(PlantType.Bush).with_bloom_period(BloomPeriod.from_range(Month.June, Month.September)).with_max_height(1.5).with_soil(SoilCondition.Loamy).with_light(LightCondition.FullSun).build(),
        PlantBuilder().with_name("Hydrangea").with_latin_name("Hydrangea macrophylla").with_article_number("BSH-002").with_type(PlantType.Bush).with_bloom_period(BloomPeriod.from_range(Month.July, Month.September)).with_max_height(1.2).with_soil(SoilCondition.Clay).with_light(LightCondition.PartialShade).build(),
        PlantBuilder().with_name("Boxwood").with_latin_name("Buxus sempervirens").with_article_number("BSH-003").with_type(PlantType.Bush).with_max_height(1.0).with_soil(SoilCondition.Any).with_light(LightCondition.PartialShade).build(),
        PlantBuilder().with_name("Beech").with_latin_name("Fagus sylvatica").with_article_number("TRE-001").with_type(PlantType.Tree).with_bloom_period(BloomPeriod([Month.May])).with_max_height(5.0).with_soil(SoilCondition.Any).with_light(LightCondition.FullSun).build(),
        PlantBuilder().with_name("Yew").with_latin_name("Taxus baccata").with_article_number("TRE-002").with_type(PlantType.Tree).with_max_height(4.0).with_soil(SoilCondition.Any).with_light(LightCondition.PartialShade).build(),
        PlantBuilder().with_name("Oak").with_latin_name("Quercus robur").with_article_number("TRE-003").with_type(PlantType.Tree).with_max_height(20.0).with_soil(SoilCondition.Any).with_light(LightCondition.FullSun).build(),
        PlantBuilder().with_name("Silver Birch").with_latin_name("Betula pendula").with_article_number("TRE-004").with_type(PlantType.Tree).with_max_height(15.0).with_soil(SoilCondition.Sandy).with_light(LightCondition.FullSun).build(),
        PlantBuilder().with_name("Fern").with_latin_name("Polypodiopsida").with_article_number("BSH-004").with_type(PlantType.Bush).with_max_height(0.5).with_soil(SoilCondition.Loamy).with_light(LightCondition.FullShade).build()
    ]
