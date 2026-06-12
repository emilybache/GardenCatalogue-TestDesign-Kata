from typing import List, Dict, Optional
from .models import Plant, PlantType, BloomPeriod, SoilCondition, LightCondition, Month

class PlantBuilder:
    def __init__(self):
        self._name = "Unknown"
        self._latin_name = ""
        self._article_number = ""
        self._type = PlantType.Flower
        self._bloom_period = BloomPeriod([])
        self._max_height = 0.0
        self._soil = SoilCondition.Any
        self._light = LightCondition.FullSun
        self._properties = {}

    def with_name(self, name: str) -> 'PlantBuilder':
        self._name = name
        return self

    def with_latin_name(self, latin_name: str) -> 'PlantBuilder':
        self._latin_name = latin_name
        return self

    def with_article_number(self, article_number: str) -> 'PlantBuilder':
        self._article_number = article_number
        return self

    def with_type(self, plant_type: PlantType) -> 'PlantBuilder':
        self._type = plant_type
        return self

    def with_bloom_period(self, bloom_period: BloomPeriod) -> 'PlantBuilder':
        self._bloom_period = bloom_period
        return self

    def with_max_height(self, max_height: float) -> 'PlantBuilder':
        self._max_height = max_height
        return self

    def with_soil(self, soil: SoilCondition) -> 'PlantBuilder':
        self._soil = soil
        return self

    def with_light(self, light: LightCondition) -> 'PlantBuilder':
        self._light = light
        return self

    def with_property(self, key: str, value: str) -> 'PlantBuilder':
        self._properties[key] = value
        return self

    def build(self) -> Plant:
        return Plant(
            name=self._name,
            latin_name=self._latin_name,
            article_number=self._article_number,
            type=self._type,
            bloom_period=self._bloom_period,
            max_height=self._max_height,
            soil=self._soil,
            light=self._light,
            properties=self._properties
        )

class PlantPrinter:
    def print(self, plant: Plant, indent: str = "") -> str:
        lines = []
        
        def append_if_not_empty(label, value):
            if value:
                lines.append(f"{indent}{label}: {value}")

        lines.append(f"{indent}Name: {plant.name}")
        append_if_not_empty("Latin Name", plant.latin_name)
        append_if_not_empty("Article #", plant.article_number)
        
        if plant.type != PlantType.Flower:
            lines.append(f"{indent}Type: {plant.type.name}")

        if plant.bloom_period.months:
            months_str = ", ".join(m.name for m in plant.bloom_period.months)
            lines.append(f"{indent}Blooms: {months_str}")

        lines.append(f"{indent}Max Height: {plant.max_height:.1f}m")
        lines.append(f"{indent}Soil: {plant.soil.name}")
        lines.append(f"{indent}Light: {plant.light.name}")

        if plant.properties:
            lines.append(f"{indent}Properties:")
            for key, value in sorted(plant.properties.items()):
                lines.append(f"{indent}  - {key}: {value}")
        
        return "\n".join(lines) + "\n"
