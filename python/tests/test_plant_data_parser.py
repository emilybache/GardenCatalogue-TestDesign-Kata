import pytest
import io
import struct
from garden_catalogue.parser import PlantDataParser
from garden_catalogue.models import Plant, PlantType, BloomPeriod, Month, SoilCondition, LightCondition, create_plant
from garden_catalogue.bug_configurations import BugConfigurations

class TestPlantDataParser:
    @pytest.fixture(autouse=True)
    def setup(self):
        BugConfigurations.reset()
        self.data_parser = PlantDataParser()

    def test_empty_stream_empty_list(self):
        stream = io.BytesIO()
        result = self.data_parser.parse(stream)
        assert list(result) == []

    def test_v1_single_plant_valid_result(self):
        plants = [
            create_plant(name="Test Bush", plant_type=PlantType.Bush)
        ]
        binary_data = self._create_binary_data(plants, version=1)
        stream = io.BytesIO(binary_data)
        result = list(self.data_parser.parse(stream))
        
        assert len(result) == 1
        assert result[0].name == "Test Bush"
        assert result[0].type == PlantType.Bush

    def test_v1_two_plants_valid_result(self):
        plants = [
            create_plant(name="Flower1", bloom_period=BloomPeriod.from_range(Month.May, Month.July), max_height=0.5, soil=SoilCondition.Sandy, light=LightCondition.FullSun),
            create_plant(name="Bush1", plant_type=PlantType.Bush, bloom_period=BloomPeriod([Month.August]), max_height=2.0, soil=SoilCondition.Clay, light=LightCondition.PartialShade)
        ]
        binary_data = self._create_binary_data(plants, version=1)
        stream = io.BytesIO(binary_data)
        result = list(self.data_parser.parse(stream))
        
        assert len(result) == 2
        assert result[0].name == "Flower1"
        assert result[0].type == PlantType.Flower
        assert result[0].max_height == 0.5
        assert result[0].soil == SoilCondition.Sandy
        assert result[0].light == LightCondition.FullSun
        assert result[0].bloom_period.months == [Month.May, Month.June, Month.July]

        assert result[1].name == "Bush1"
        assert result[1].type == PlantType.Bush
        assert result[1].max_height == 2.0
        assert result[1].soil == SoilCondition.Clay
        assert result[1].light == LightCondition.PartialShade
        assert result[1].bloom_period.months == [Month.August]

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
        result = list(self.data_parser.parse(stream))
        
        assert len(result) == 2
        plant = result[0]
        assert plant.name == "Lavender"
        assert plant.latin_name == "Lavandula angustifolia"
        assert plant.article_number == "FLO-001"
        assert plant.properties["Color"] == "Purple"
        assert plant.properties["Fragrance"] == "High"

        minimal = result[1]
        assert minimal.name == "Minimal Plant"
        assert minimal.latin_name == ""
        assert minimal.article_number == ""
        assert minimal.properties == {}

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
        result = list(self.data_parser.parse(stream))
        
        assert len(result) == 1
        plant = result[0]
        assert plant.name == "Red Rose"
        assert plant.latin_name == "Rosa rubiginosa"
        assert plant.article_number == "ROS-001"
        assert plant.max_height == 1.5
        assert plant.soil == SoilCondition.Loamy
        assert plant.light == LightCondition.FullSun
        assert plant.bloom_period.months == [Month.June, Month.July, Month.August, Month.September]
        assert plant.properties == {
            "Color": "Deep Red",
            "Fragrance": "Strong",
            "Thorns": "Yes",
            "Difficulty": "Medium"
        }

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
