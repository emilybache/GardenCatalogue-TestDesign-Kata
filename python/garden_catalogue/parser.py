import struct
import io
from typing import Iterable, List
from .models import Plant, PlantType, BloomPeriod, Month, SoilCondition, LightCondition
from .bug_configurations import BugConfigurations

class PlantDataParser:
    NAME_LENGTH = 32
    TRIM_CHARS = " ,.-_\r\t\n\0"

    def parse(self, stream: io.BufferedIOBase) -> Iterable[Plant]:
        stream.seek(0, io.SEEK_END)
        length = stream.tell()
        stream.seek(0)
        
        if length == 0:
            return

        version = self._detect_version(stream, length)

        if version == 2:
            yield from self._parse_v2(stream, length)
        else:
            yield from self._parse_v1(stream, length)

    def _detect_version(self, stream: io.BufferedIOBase, length: int) -> int:
        if length < 4:
            return 1

        original_position = stream.tell()
        marker = stream.read(4)

        if marker == b'PLNT':
            if BugConfigurations.bug1:
                return 1
            return 2

        stream.seek(original_position)
        return 1

    def _parse_v1(self, stream: io.BufferedIOBase, length: int) -> Iterable[Plant]:
        while stream.tell() < length:
            if stream.tell() + 56 > length:
                break
            yield self._read_common_header(stream)

    def _parse_v2(self, stream: io.BufferedIOBase, length: int) -> Iterable[Plant]:
        while stream.tell() < length:
            if stream.tell() + 56 > length:
                break

            header_plant = self._read_common_header(stream)

            # Read extended fields for V2
            latin_name = self._read_string(stream)
            article_number = self._read_string(stream)
            properties = {}

            if not BugConfigurations.bug4:
                prop_count_bytes = stream.read(4)
                if len(prop_count_bytes) == 4:
                    prop_count, = struct.unpack('<i', prop_count_bytes)
                    for _ in range(prop_count):
                        key = self._read_string(stream)
                        value = self._read_string(stream)
                        properties[key] = value

            if BugConfigurations.bug5:
                yield Plant(
                    name=header_plant.name,
                    article_number=latin_name, # SWAPPED BUG
                    latin_name=article_number, # SWAPPED BUG
                    type=header_plant.type,
                    bloom_period=header_plant.bloom_period,
                    max_height=header_plant.max_height,
                    soil=header_plant.soil,
                    light=header_plant.light,
                    properties=properties
                )
            else:
                yield Plant(
                    name=header_plant.name,
                    latin_name=latin_name,
                    article_number=article_number,
                    type=header_plant.type,
                    bloom_period=header_plant.bloom_period,
                    max_height=header_plant.max_height,
                    soil=header_plant.soil,
                    light=header_plant.light,
                    properties=properties
                )

    def _read_common_header(self, stream: io.BufferedIOBase) -> Plant:
        header_bytes = stream.read(56)
        
        name_bytes = header_bytes[0:32]
        name = name_bytes.decode('utf-8')
        if BugConfigurations.bug3:
            name = name.strip()
        else:
            name = name.strip(self.TRIM_CHARS)

        plant_type_val, max_height, soil_val, light_val, bloom_mask = struct.unpack('<idiii', header_bytes[32:56])

        months = []
        for i in range(12):
            if (bloom_mask & (1 << i)) != 0:
                month_value = i + 1
                if BugConfigurations.bug2:
                    month_value = i
                months.append(Month(month_value))

        return Plant(
            name=name,
            type=PlantType(plant_type_val),
            bloom_period=BloomPeriod(months),
            max_height=max_height,
            soil=SoilCondition(soil_val),
            light=LightCondition(light_val)
        )

    def _read_string(self, stream: io.BufferedIOBase) -> str:
        # C# BinaryReader.ReadString() reads a LEB128 length-prefixed string
        length = self._read_7bit_encoded_int(stream)
        if length == 0:
            return ""
        return stream.read(length).decode('utf-8')

    def _read_7bit_encoded_int(self, stream: io.BufferedIOBase) -> int:
        result = 0
        shift = 0
        while True:
            byte_val = ord(stream.read(1))
            result |= (byte_val & 0x7f) << shift
            if (byte_val & 0x80) == 0:
                break
            shift += 7
        return result
