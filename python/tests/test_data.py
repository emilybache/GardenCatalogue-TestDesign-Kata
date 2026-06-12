from garden_catalogue.models import Plant, PlantType, BloomPeriod, SoilCondition, LightCondition, Month, create_plant


class TestData:
    DEFAULT_PLANTS = [
        create_plant(name="Lavender", latin_name="Lavandula angustifolia", article_number="FLO-001", bloom_period=BloomPeriod.from_range(Month.June, Month.August), max_height=0.6, soil=SoilCondition.Sandy, light=LightCondition.FullSun),
        create_plant(name="Rose", latin_name="Rosa", article_number="BSH-001", plant_type=PlantType.Bush, bloom_period=BloomPeriod.from_range(Month.June, Month.September), max_height=1.5, soil=SoilCondition.Loamy, light=LightCondition.FullSun),
        create_plant(name="Hydrangea", latin_name="Hydrangea macrophylla", article_number="BSH-002", plant_type=PlantType.Bush, bloom_period=BloomPeriod.from_range(Month.July, Month.September), max_height=1.2, soil=SoilCondition.Clay, light=LightCondition.PartialShade),
        create_plant(name="Boxwood", latin_name="Buxus sempervirens", article_number="BSH-003", plant_type=PlantType.Bush, max_height=1.0, soil=SoilCondition.Any, light=LightCondition.PartialShade),
        create_plant(name="Beech", latin_name="Fagus sylvatica", article_number="TRE-001", plant_type=PlantType.Tree, bloom_period=BloomPeriod([Month.May]), max_height=5.0, soil=SoilCondition.Any, light=LightCondition.FullSun),
        create_plant(name="Yew", latin_name="Taxus baccata", article_number="TRE-002", plant_type=PlantType.Tree, max_height=4.0, soil=SoilCondition.Any, light=LightCondition.PartialShade),
        create_plant(name="Oak", latin_name="Quercus robur", article_number="TRE-003", plant_type=PlantType.Tree, max_height=20.0, soil=SoilCondition.Any, light=LightCondition.FullSun),
        create_plant(name="Silver Birch", latin_name="Betula pendula", article_number="TRE-004", plant_type=PlantType.Tree, max_height=15.0, soil=SoilCondition.Sandy, light=LightCondition.FullSun),
        create_plant(name="Fern", latin_name="Polypodiopsida", article_number="BSH-004", plant_type=PlantType.Bush, max_height=0.5, soil=SoilCondition.Loamy, light=LightCondition.FullShade)
    ]
