from garden_catalogue.manager import GardenCatalogueManager
from garden_catalogue.models import SoilCondition, LightCondition, Month, Plant, PlantType, BloomPeriod, Season
from .test_data import TestData

class TestGardenCatalogueManager:
    
    def test_get_plants_for_sandy_soil_and_full_sun_returns_lavender_beech_oak_and_silver_birch(self):
        manager = GardenCatalogueManager(TestData.DEFAULT_PLANTS)
        soil = SoilCondition.Sandy
        light = LightCondition.FullSun

        result = list(manager.get_plants_for_condition(soil, light))
    
        names = [p.name for p in result]
        assert sorted(names) == sorted(["Lavender", "Beech", "Oak", "Silver Birch"])

    def test_plan_bed_in_july_with_max_height_one_meter_returns_lavender(self):
        manager = GardenCatalogueManager(TestData.DEFAULT_PLANTS)
        month = Month.July
        max_height = 1.0

        result = list(manager.plan_bed(month, max_height))

        names = [p.name for p in result]
        assert names == ["Lavender"]

    def test_plan_bed_with_spring_season_plant_returns_crocus_in_spring_and_empty_in_winter(self):
        manager = GardenCatalogueManager(TestData.DEFAULT_PLANTS)
        manager.add_plant(Plant("Spring Crocus", PlantType.Flower, BloomPeriod.from_season(Season.Spring), 0.1, SoilCondition.Any, LightCondition.FullSun, "Crocus vernus", "FLO-002"))
        
        assert [p.name for p in manager.plan_bed(Month.March, 1.0)] == ["Spring Crocus"]
        assert [p.name for p in manager.plan_bed(Month.April, 1.0)] == ["Spring Crocus"]
        assert [p.name for p in manager.plan_bed(Month.May, 1.0)] == ["Spring Crocus"]
        assert list(manager.plan_bed(Month.February, 1.0)) == []

    def test_plan_bed_with_specific_months_plant_returns_lily_in_july_august_and_empty_in_june(self):
        manager = GardenCatalogueManager()
        manager.add_plant(Plant("Summer Lily", PlantType.Flower, BloomPeriod([Month.July, Month.August]), 0.5, SoilCondition.Any, LightCondition.FullSun, "Lilium", "FLO-003"))
        
        assert [p.name for p in manager.plan_bed(Month.July, 1.0)] == ["Summer Lily"]
        assert [p.name for p in manager.plan_bed(Month.August, 1.0)] == ["Summer Lily"]
        assert list(manager.plan_bed(Month.June, 1.0)) == []

    def test_plan_hedge_with_min_height_four_meters_returns_beech_oak_silver_birch_and_yew(self):
        manager = GardenCatalogueManager(TestData.DEFAULT_PLANTS)
        min_height = 4.0

        result = list(manager.plan_hedge(min_height))

        names = [p.name for p in result]
        assert sorted(names) == sorted(["Beech", "Oak", "Silver Birch", "Yew"])
