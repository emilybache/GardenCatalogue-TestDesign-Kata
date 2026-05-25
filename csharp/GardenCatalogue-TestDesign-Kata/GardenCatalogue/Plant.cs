namespace GardenCatalogue;

public enum PlantType
{
    Flower,
    Bush,
    Tree
}

public enum SoilCondition
{
    Sandy,
    Clay,
    Loamy,
    Any
}

public enum LightCondition
{
    FullSun,
    PartialShade,
    FullShade
}

public enum Month
{
    January = 1,
    February = 2,
    March = 3,
    April = 4,
    May = 5,
    June = 6,
    July = 7,
    August = 8,
    September = 9,
    October = 10,
    November = 11,
    December = 12
}

public enum Season
{
    Spring,
    Summer,
    Autumn,
    Winter
}

public record BloomPeriod(params Month[] Months)
{
    public static BloomPeriod FromSeason(Season season) => season switch
    {
        Season.Spring => new BloomPeriod(Month.March, Month.April, Month.May),
        Season.Summer => new BloomPeriod(Month.June, Month.July, Month.August),
        Season.Autumn => new BloomPeriod(Month.September, Month.October, Month.November),
        Season.Winter => new BloomPeriod(Month.December, Month.January, Month.February),
        _ => throw new ArgumentOutOfRangeException(nameof(season), season, null)
    };

    public static BloomPeriod FromRange(Month start, Month end)
    {
        var months = new List<Month>();
        int s = (int)start;
        int e = (int)end;
        
        if (s <= e)
        {
            for (int i = s; i <= e; i++) months.Add((Month)i);
        }
        else
        {
            for (int i = s; i <= 12; i++) months.Add((Month)i);
            for (int i = 1; i <= e; i++) months.Add((Month)i);
        }
        return new BloomPeriod(months.ToArray());
    }

    public bool BloomsIn(Month month) => Months.Contains(month);
    
    public static implicit operator BloomPeriod(Month month) => new BloomPeriod(month);
}

public record Plant
{
    public string Name { get; init; }
    public string LatinName { get; init; } = string.Empty;
    public string ArticleNumber { get; init; } = string.Empty;
    public PlantType Type { get; init; }
    public BloomPeriod BloomPeriod { get; init; }
    public double MaxHeight { get; init; }
    public SoilCondition Soil { get; init; }
    public LightCondition Light { get; init; }
    public IReadOnlyDictionary<string, string> Properties { get; init; } = new Dictionary<string, string>();

    public Plant(
        string name,
        PlantType type,
        BloomPeriod bloomPeriod,
        double maxHeight,
        SoilCondition soil,
        LightCondition light)
    {
        Name = name;
        Type = type;
        BloomPeriod = bloomPeriod;
        MaxHeight = maxHeight;
        Soil = soil;
        Light = light;
    }

    public Plant(
        string name,
        string latinName,
        string articleNumber,
        PlantType type,
        BloomPeriod bloomPeriod,
        double maxHeight,
        SoilCondition soil,
        LightCondition light,
        IReadOnlyDictionary<string, string> properties)
    {
        Name = name;
        LatinName = latinName;
        ArticleNumber = articleNumber;
        Type = type;
        BloomPeriod = bloomPeriod;
        MaxHeight = maxHeight;
        Soil = soil;
        Light = light;
        Properties = properties;
    }
}
