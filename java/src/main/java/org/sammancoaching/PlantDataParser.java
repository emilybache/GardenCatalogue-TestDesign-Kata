package org.sammancoaching;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlantDataParser {
    private static final int NAME_LENGTH = 32;
    private static final String TRIM_CHARS = " ,.-_\r\t\n\0";

    public List<Plant> parse(InputStream stream) throws IOException {
        byte[] data = stream.readAllBytes();
        if (data.length == 0) {
            return new ArrayList<>();
        }

        ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        int version = detectVersion(buffer);

        if (version == 2) {
            return parseV2(buffer);
        } else {
            return parseV1(buffer);
        }
    }

    private int detectVersion(ByteBuffer buffer) {
        if (buffer.remaining() < 4) {
            return 1;
        }

        byte[] marker = new byte[4];
        buffer.mark();
        buffer.get(marker);
        String markerStr = new String(marker, StandardCharsets.UTF_8);

        if ("PLNT".equals(markerStr)) {
            if (BugConfigurations.bug1) {
                buffer.reset();
                return 1;
            }
            return 2;
        }

        buffer.reset();
        return 1;
    }

    private List<Plant> parseV1(ByteBuffer buffer) {
        List<Plant> plants = new ArrayList<>();
        while (buffer.remaining() >= 56) {
            plants.add(readCommonHeader(buffer));
        }
        return plants;
    }

    private List<Plant> parseV2(ByteBuffer buffer) {
        List<Plant> plants = new ArrayList<>();
        while (buffer.remaining() >= 56) {
            Plant headerPlant = readCommonHeader(buffer);

            String latinName = readString(buffer);
            String articleNumber = readString(buffer);
            Map<String, String> properties = new HashMap<>();

            if (!BugConfigurations.bug4) {
                if (buffer.remaining() >= 4) {
                    int propCount = buffer.getInt();
                    for (int i = 0; i < propCount; i++) {
                        String key = readString(buffer);
                        String value = readString(buffer);
                        properties.put(key, value);
                    }
                }
            }

            if (BugConfigurations.bug5) {
                plants.add(new Plant(
                        headerPlant.name(),
                        articleNumber, // SWAPPED BUG
                        latinName,     // SWAPPED BUG
                        headerPlant.type(),
                        headerPlant.bloomPeriod(),
                        headerPlant.maxHeight(),
                        headerPlant.soil(),
                        headerPlant.light(),
                        properties
                ));
            } else {
                plants.add(new Plant(
                        headerPlant.name(),
                        latinName,
                        articleNumber,
                        headerPlant.type(),
                        headerPlant.bloomPeriod(),
                        headerPlant.maxHeight(),
                        headerPlant.soil(),
                        headerPlant.light(),
                        properties
                ));
            }
        }
        return plants;
    }

    private Plant readCommonHeader(ByteBuffer buffer) {
        byte[] nameBytes = new byte[NAME_LENGTH];
        buffer.get(nameBytes);
        String name = new String(nameBytes, StandardCharsets.UTF_8);
        if (BugConfigurations.bug3) {
            name = name.strip();
        } else {
            name = stripCustom(name, TRIM_CHARS);
        }

        int plantTypeVal = buffer.getInt();
        double maxHeight = buffer.getDouble();
        int soilVal = buffer.getInt();
        int lightVal = buffer.getInt();
        int bloomMask = buffer.getInt();

        List<Month> months = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            if ((bloomMask & (1 << i)) != 0) {
                int monthValue = i + 1;
                if (BugConfigurations.bug2) {
                    monthValue = i;
                }
                months.add(Month.fromInt(monthValue));
            }
        }

        return new Plant(
                name,
                "",
                "",
                PlantType.values()[plantTypeVal],
                new BloomPeriod(months),
                maxHeight,
                SoilCondition.values()[soilVal],
                LightCondition.values()[lightVal],
                new HashMap<>()
        );
    }

    private String stripCustom(String s, String trimChars) {
        int start = 0;
        int end = s.length();
        while (start < end && trimChars.indexOf(s.charAt(start)) != -1) {
            start++;
        }
        while (end > start && trimChars.indexOf(s.charAt(end - 1)) != -1) {
            end--;
        }
        return s.substring(start, end);
    }

    private String readString(ByteBuffer buffer) {
        int length = read7BitEncodedInt(buffer);
        if (length == 0) return "";
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private int read7BitEncodedInt(ByteBuffer buffer) {
        int result = 0;
        int shift = 0;
        while (true) {
            byte b = buffer.get();
            result |= (b & 0x7f) << shift;
            if ((b & 0x80) == 0) break;
            shift += 7;
        }
        return result;
    }
}
