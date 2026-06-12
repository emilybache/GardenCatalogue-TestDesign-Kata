from typing import Iterable, List, Optional
from .models import Plant, SoilCondition, LightCondition, Month, PlantType

class GardenCatalogueManager:
    def __init__(self, plants: Optional[Iterable[Plant]] = None):
        self._plants: List[Plant] = list(plants) if plants else []

    @property
    def plant_count(self) -> int:
        return len(self._plants)

    def __len__(self) -> int:
        return len(self._plants)

    def __iter__(self):
        return iter(self._plants)

    def __getitem__(self, name_or_index):
        if isinstance(name_or_index, int):
            return self._plants[name_or_index]
        for p in self._plants:
            if p.name == name_or_index:
                return p
        return None

    def __repr__(self) -> str:
        return f"GardenCatalogueManager(count={len(self)})"

    def get_plant_count(self) -> int:
        return self.plant_count

    def get_plant_by_name(self, name: str) -> Optional[Plant]:
        return self[name]

    def add_plant(self, plant: Plant):
        self._plants.append(plant)

    def get_plants_for_condition(self, soil: SoilCondition, light: LightCondition) -> Iterable[Plant]:
        return [p for p in self._plants if (p.soil == soil or p.soil == SoilCondition.Any) and p.light == light]

    def plan_bed(self, month: Month, max_height: float) -> Iterable[Plant]:
        return [p for p in self._plants if p.type == PlantType.Flower and 
                month in p.bloom_period and 
                p.max_height <= max_height]

    def plan_hedge(self, min_height: float) -> Iterable[Plant]:
        return [p for p in self._plants if (p.type == PlantType.Bush or p.type == PlantType.Tree) and 
                p.max_height >= min_height]
