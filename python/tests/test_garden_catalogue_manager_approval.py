import pytest
import json
from approvaltests import verify
from garden_catalogue.manager import GardenCatalogueManager
from garden_catalogue.models import SoilCondition, LightCondition, Month
from .test_data import TestData

class TestGardenCatalogueManagerApproval:
    @pytest.fixture(autouse=True)
    def _setup(self):
        self.manager = GardenCatalogueManager(TestData.DEFAULT_PLANTS)

    def test_hedge_planning(self):
        hedge = self.manager.plan_hedge(2.0)
        verify(self._to_json(hedge))

    def test_condition_filtering(self):
        plants = self.manager.get_plants_for_condition(SoilCondition.Sandy, LightCondition.FullSun)
        verify(self._to_json(plants))

    def test_bed_planning(self):
        bed_in_june = self.manager.plan_bed(Month.June, 2.0)
        verify(self._to_json(bed_in_june))

    def _to_json(self, plants):
        return json.dumps(plants, default=lambda x: x.to_dict(), indent=2)
