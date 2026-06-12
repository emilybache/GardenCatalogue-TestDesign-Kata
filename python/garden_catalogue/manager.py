from typing import Iterable, List, Optional
from .models import Plant, SoilCondition, LightCondition, Month, PlantType

class GardenCatalogueManager:
    def __init__(self, plants: Optional[Iterable[Plant]] = None):
        self._plants: List[Plant] = list(plants) if plants else []

    def get_plant_count(self) -> int:
        return len(self._plants)

    def get_plant_by_name(self, name: str) -> Optional[Plant]:
        for p in self._plants:
            if p.name == name:
                return p
        return None

    def add_plant(self, plant: Plant):
        self._plants.append(plant)

    def get_plants_for_condition(self, soil: SoilCondition, light: LightCondition) -> Iterable[Plant]:
        return [p for p in self._plants if (p.soil == soil or p.soil == SoilCondition.Any) and p.light == light]

    def plan_bed(self, month: Month, max_height: float) -> Iterable[Plant]:
        return [p for p in self._plants if p.type == PlantType.Flower and 
                p.bloom_period.blooms_in(month) and 
                p.max_height <= max_height]

    def plan_hedge(self, min_height: float) -> Iterable[Plant]:
        return [p for p in self._plants if (p.type == PlantType.Bush or p.type == PlantType.Tree) and 
                p.max_height >= min_height]
