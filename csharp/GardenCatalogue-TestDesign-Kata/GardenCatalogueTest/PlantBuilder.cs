using System.Collections.ObjectModel;
using GardenCatalogue;

namespace GardenCatalogueTest;

public class PlantBuilder
{
    private string _name = "Default Plant";
    private string _latinName = string.Empty;
    private string _articleNumber = string.Empty;
    private PlantType _type = PlantType.Flower;
    private BloomPeriod _bloomPeriod = new BloomPeriod();
    private double _maxHeight = 1.0;
    private SoilCondition _soil = SoilCondition.Any;
    private LightCondition _light = LightCondition.FullSun;
    private Dictionary<string, string> _properties = new();

    public PlantBuilder WithName(string name)
    {
        _name = name;
        return this;
    }

    public PlantBuilder WithLatinName(string latinName)
    {
        _latinName = latinName;
        return this;
    }

    public PlantBuilder WithArticleNumber(string articleNumber)
    {
        _articleNumber = articleNumber;
        return this;
    }

    public PlantBuilder WithType(PlantType type)
    {
        _type = type;
        return this;
    }

    public PlantBuilder WithBloomPeriod(BloomPeriod bloomPeriod)
    {
        _bloomPeriod = bloomPeriod;
        return this;
    }

    public PlantBuilder WithMaxHeight(double maxHeight)
    {
        _maxHeight = maxHeight;
        return this;
    }

    public PlantBuilder WithSoil(SoilCondition soil)
    {
        _soil = soil;
        return this;
    }

    public PlantBuilder WithLight(LightCondition light)
    {
        _light = light;
        return this;
    }

    public PlantBuilder WithProperty(string key, string value)
    {
        _properties[key] = value;
        return this;
    }

    public static PlantBuilder Flower() => new PlantBuilder().WithType(PlantType.Flower);
    public static PlantBuilder Bush() => new PlantBuilder().WithType(PlantType.Bush);
    public static PlantBuilder Tree() => new PlantBuilder().WithType(PlantType.Tree);

    public Plant Build()
    {
        return new Plant(
            _name,
            _latinName,
            _articleNumber,
            _type,
            _bloomPeriod,
            _maxHeight,
            _soil,
            _light,
            new ReadOnlyDictionary<string, string>(_properties));
    }

    public static implicit operator Plant(PlantBuilder builder) => builder.Build();
}
