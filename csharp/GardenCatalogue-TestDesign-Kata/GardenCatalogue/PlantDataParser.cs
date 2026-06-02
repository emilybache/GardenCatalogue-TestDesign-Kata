using System.Text;

namespace GardenCatalogue;

public class PlantDataParser
{
    public const int NameLength = 32;

    private static readonly char[] TrimChars = " ,.-_\r\t\n\0".ToCharArray();

    public IEnumerable<Plant> Parse(Stream stream)
    {
        if (stream.Length == 0) return new List<Plant>();

        int version = DetectVersion(stream);

        return version switch
        {
            2 => ParseV2(stream),
            _ => ParseV1(stream)
        };
    }

    private int DetectVersion(Stream stream)
    {
        if (stream.Length < 4) return 1;

        long originalPosition = stream.Position;
        using var reader = new BinaryReader(stream, Encoding.UTF8, leaveOpen: true);
        byte[] marker = reader.ReadBytes(4);

        if (marker.Length == 4 && marker[0] == (byte)'P' && marker[1] == (byte)'L' && marker[2] == (byte)'N' &&
            marker[3] == (byte)'T')
        {
            if (BugConfigurations.Bug1) return 1;
            return 2;
        }

        stream.Position = originalPosition;
        return 1;
    }

    private IEnumerable<Plant> ParseV1(Stream stream)
    {
        var plants = new List<Plant>();
        using var reader = new BinaryReader(stream, Encoding.UTF8, leaveOpen: true);

        while (stream.Position < stream.Length)
        {
            if (stream.Position + 56 > stream.Length) break;

            var plant = ReadCommonHeader(reader);
            plants.Add(plant);
        }

        return plants;
    }

    private IEnumerable<Plant> ParseV2(Stream stream)
    {
        var plants = new List<Plant>();
        using var reader = new BinaryReader(stream, Encoding.UTF8, leaveOpen: true);

        while (stream.Position < stream.Length)
        {
            if (stream.Position + 56 > stream.Length) break;

            var headerPlant = ReadCommonHeader(reader);

            // Read extended fields for V2
            string latinName = reader.ReadString();
            string articleNumber = reader.ReadString();
            var properties = new Dictionary<string, string>();

            if (!BugConfigurations.Bug4)
            {
                int propCount = reader.ReadInt32();
                for (int i = 0; i < propCount; i++)
                {
                    string key = reader.ReadString();
                    string value = reader.ReadString();
                    properties[key] = value;
                }
            }

            if (BugConfigurations.Bug5)
            {
                plants.Add(new Plant(headerPlant.Name, articleNumber, latinName, headerPlant.Type,
                    headerPlant.BloomPeriod, headerPlant.MaxHeight, headerPlant.Soil, headerPlant.Light, properties));
            }
            else
            {
                plants.Add(new Plant(headerPlant.Name, latinName, articleNumber, headerPlant.Type,
                    headerPlant.BloomPeriod, headerPlant.MaxHeight, headerPlant.Soil, headerPlant.Light, properties));
            }
        }

        return plants;
    }

    private Plant ReadCommonHeader(BinaryReader reader)
    {
        byte[] headerBytes = reader.ReadBytes(56);
        using var headerStream = new MemoryStream(headerBytes);
        using var headerReader = new BinaryReader(headerStream, Encoding.UTF8);

        string name = Encoding.UTF8.GetString(headerReader.ReadBytes(NameLength));
        if (BugConfigurations.Bug3)
        {
            name = name.Trim(); // Standard trim instead of specific TrimChars
        }
        else
        {
            name = name.Trim(TrimChars);
        }

        PlantType type = (PlantType)headerReader.ReadInt32();
        double maxHeight = headerReader.ReadDouble();
        SoilCondition soil = (SoilCondition)headerReader.ReadInt32();
        LightCondition light = (LightCondition)headerReader.ReadInt32();

        int bloomMask = headerReader.ReadInt32();

        var months = new List<Month>();
        for (int i = 0; i < 12; i++)
        {
            if ((bloomMask & (1 << i)) != 0)
            {
                int monthValue = i + 1;
                if (BugConfigurations.Bug2)
                {
                    monthValue = i; // 0-indexed bug
                }

                months.Add((Month)monthValue);
            }
        }

        return new Plant(
            name, 
            string.Empty, 
            string.Empty, 
            type, 
            new BloomPeriod(months.ToArray()), 
            maxHeight, 
            soil,
            light,
            new Dictionary<string, string>());
    }
}