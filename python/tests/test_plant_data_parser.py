import pytest
import io
import struct
from approvaltests import verify
from garden_catalogue.parser import PlantDataParser
from garden_catalogue.models import Plant, PlantType, BloomPeriod, Month, SoilCondition, LightCondition, create_plant
from garden_catalogue.bug_configurations import BugConfigurations
from .plant_printer import PlantPrinter

class TestPlantDataParser:
    @pytest.fixture(autouse=True)
    def setup(self):
        BugConfigurations.reset()
        self.data_parser = PlantDataParser()
        self.plant_printer = PlantPrinter()

    def test_empty_stream_empty_list(self):
        stream = io.BytesIO()
        result = self.data_parser.parse(stream)
        verify(self._print_scenario("Empty Stream", b"", 2, result))

    def test_v1_single_plant_valid_result(self):
        plants = [
            create_plant(name="Test Bush", plant_type=PlantType.Bush)
        ]
        binary_data = self._create_binary_data(plants, version=1)
        stream = io.BytesIO(binary_data)
        result = self.data_parser.parse(stream)
        verify(self._print_scenario(plants, binary_data, 1, result))

    def test_v1_two_plants_valid_result(self):
        plants = [
            create_plant(name="Flower1", bloom_period=BloomPeriod.from_range(Month.May, Month.July), max_height=0.5, soil=SoilCondition.Sandy, light=LightCondition.FullSun),
            create_plant(name="Bush1", plant_type=PlantType.Bush, bloom_period=BloomPeriod([Month.August]), max_height=2.0, soil=SoilCondition.Clay, light=LightCondition.PartialShade)
        ]
        binary_data = self._create_binary_data(plants, version=1)
        stream = io.BytesIO(binary_data)
        result = self.data_parser.parse(stream)
        verify(self._print_scenario(plants, binary_data, 1, result))

    def test_v2_two_plants_valid_result(self):
        plants = [
            create_plant(
                name="Lavender",
                latin_name="Lavandula angustifolia",
                article_number="FLO-001",
                bloom_period=BloomPeriod.from_range(Month.June, Month.August),
                max_height=0.6,
                soil=SoilCondition.Sandy,
                light=LightCondition.FullSun,
                properties={"Color": "Purple", "Fragrance": "High"}
            ),
             create_plant(name="Minimal Plant")
        ]
        binary_data = self._create_binary_data(plants, version=2)
        stream = io.BytesIO(binary_data)
        result = self.data_parser.parse(stream)
        verify(self._print_scenario(plants, binary_data, 2, result))

    def test_v2_single_plant_valid_result(self):
        plants = [
            create_plant(
                name="Red Rose",
                latin_name="Rosa rubiginosa",
                article_number="ROS-001",
                bloom_period=BloomPeriod.from_range(Month.June, Month.September),
                max_height=1.5,
                soil=SoilCondition.Loamy,
                light=LightCondition.FullSun,
                properties={
                    "Color": "Deep Red",
                    "Fragrance": "Strong",
                    "Thorns": "Yes",
                    "Difficulty": "Medium"
                }
            )
        ]
        binary_data = self._create_binary_data(plants, version=2)
        stream = io.BytesIO(binary_data)
        result = self.data_parser.parse(stream)
        verify(self._print_scenario(plants, binary_data, 2, result))

    def _print_scenario(self, input_data, binary_data, version, result_plants):
        lines = ["=== INPUT DATA ==="]
        if isinstance(input_data, str):
            lines.append(input_data)
        else:
            for i, plant in enumerate(input_data):
                lines.append(f"--- Item {i + 1} ---")
                lines.append(self.plant_printer.print(plant).rstrip())

        if binary_data:
            lines.append("")
            # In C# it's Convert.ToHexString which doesn't have spaces
            lines.append(f"Binary: {binary_data.hex().upper()}")

        lines.append("")
        lines.append(f"--- Parser Version {version} ---")
        lines.append("")

        lines.append("=== OUTPUT PLANTS ===")
        plants_list = list(result_plants)
        if not plants_list:
            lines.append("(None)")
        else:
            for i, plant in enumerate(plants_list):
                lines.append(f"--- Plant {i + 1} ---")
                lines.append(self.plant_printer.print(plant).rstrip())

        return "\n".join(lines) + "\n"

    def _create_binary_data(self, plants, version=2):
        stream = io.BytesIO()
        if version == 2:
            stream.write(b"PLNT")
        
        for plant in plants:
            name_bytes = plant.name.encode('utf-8')
            stream.write(name_bytes.ljust(32, b'\0')[:32])
            stream.write(struct.pack('<i', plant.type.value))
            stream.write(struct.pack('<d', plant.max_height))
            stream.write(struct.pack('<i', plant.soil.value))
            stream.write(struct.pack('<i', plant.light.value))
            
            bloom_mask = 0
            for month in plant.bloom_period.months:
                bloom_mask |= (1 << (int(month) - 1))
            stream.write(struct.pack('<i', bloom_mask))
            
            if version == 2:
                self._write_string(stream, plant.latin_name)
                self._write_string(stream, plant.article_number)
                stream.write(struct.pack('<i', len(plant.properties)))
                for key, value in sorted(plant.properties.items()):
                    self._write_string(stream, key)
                    self._write_string(stream, value)
        
        return stream.getvalue()

    def _write_string(self, stream, s):
        data = s.encode('utf-8')
        self._write_7bit_encoded_int(stream, len(data))
        stream.write(data)

    def _write_7bit_encoded_int(self, stream, value):
        while value >= 0x80:
            stream.write(struct.pack('B', (value & 0x7f) | 0x80))
            value >>= 7
        stream.write(struct.pack('B', value))
