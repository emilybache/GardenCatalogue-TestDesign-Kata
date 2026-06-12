from garden_catalogue.models import Plant, PlantType

class PlantPrinter:
    def print(self, plant: Plant, indent: str = "") -> str:
        lines = [f"{indent}Name: {plant.name}"]
        if plant.latin_name:
            lines.append(f"{indent}Latin Name: {plant.latin_name}")
        if plant.article_number:
            lines.append(f"{indent}Article #: {plant.article_number}")
        
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
