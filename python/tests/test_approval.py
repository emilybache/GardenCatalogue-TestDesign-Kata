import pytest
from approvaltests import verify
from garden_catalogue.manager import GardenCatalogueManager
from garden_catalogue.helpers import PlantPrinter
from garden_catalogue.models import SoilCondition, LightCondition, Month
from .test_data import TestData

class TestGardenCatalogueManagerApproval:
    @pytest.fixture(autouse=True)
    def setup(self):
        self.manager = GardenCatalogueManager(TestData.DEFAULT_PLANTS)
        self.plant_printer = PlantPrinter()

    def test_hedge_planning(self):
        min_height = 2.0
        hedge = self.manager.plan_hedge(min_height)
        verify(self._print_scenario({"MinHeight": min_height}, hedge))

    def test_condition_filtering(self):
        soil = SoilCondition.Sandy
        light = LightCondition.FullSun
        plants = self.manager.get_plants_for_condition(soil, light)
        verify(self._print_scenario({"Soil": soil, "Light": light}, plants))

    def test_bed_planning(self):
        month = Month.June
        max_height = 2.0
        bed_in_june = self.manager.plan_bed(month, max_height)
        verify(self._print_scenario({"Month": month, "MaxHeight": max_height}, bed_in_june))

    def _print_scenario(self, input_data, result_plants):
        lines = ["=== INPUT ==="]
        # Mirroring C# anonymous object string representation roughly
        # C# ToString() for numbers often omits .0 if it's an integer value, but here it's double.
        # Actually in the verified file it was "{ MinHeight = 2 }"
        def format_val(v):
            if isinstance(v, float) and v.is_integer():
                return str(int(v))
            if hasattr(v, 'name'): # Enum
                return v.name
            return str(v)

        input_str = "{ " + ", ".join(f"{k} = {format_val(v)}" for k, v in input_data.items()) + " }"
        lines.append(input_str)
        lines.append("")
        
        lines.append("=== RESULT PLANTS ===")
        plants_list = list(result_plants)
        if not plants_list:
            lines.append("(None)")
        else:
            for i, plant in enumerate(plants_list):
                lines.append(f"--- Plant {i + 1} ---")
                lines.append(self.plant_printer.print(plant).rstrip())
        
        return "\n".join(lines) + "\n"
