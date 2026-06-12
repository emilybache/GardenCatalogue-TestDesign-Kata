from dataclasses import dataclass, field
from enum import Enum, IntEnum
from typing import List, Dict, Optional

class PlantType(Enum):
    Flower = 0
    Bush = 1
    Tree = 2

class SoilCondition(Enum):
    Sandy = 0
    Clay = 1
    Loamy = 2
    Any = 3

class LightCondition(Enum):
    FullSun = 0
    PartialShade = 1
    FullShade = 2

class Month(IntEnum):
    January = 1
    February = 2
    March = 3
    April = 4
    May = 5
    June = 6
    July = 7
    August = 8
    September = 9
    October = 10
    November = 11
    December = 12

class Season(Enum):
    Spring = 0
    Summer = 1
    Autumn = 2
    Winter = 3

@dataclass(frozen=True)
class BloomPeriod:
    months: List[Month] = field(default_factory=list)

    @classmethod
    def from_season(cls, season: Season) -> 'BloomPeriod':
        if season == Season.Spring:
            return cls([Month.March, Month.April, Month.May])
        elif season == Season.Summer:
            return cls([Month.June, Month.July, Month.August])
        elif season == Season.Autumn:
            return cls([Month.September, Month.October, Month.November])
        elif season == Season.Winter:
            return cls([Month.December, Month.January, Month.February])
        else:
            raise ValueError(f"Unknown season: {season}")

    @classmethod
    def from_range(cls, start: Month, end: Month) -> 'BloomPeriod':
        months = []
        s = int(start)
        e = int(end)
        
        if s <= e:
            for i in range(s, e + 1):
                months.append(Month(i))
        else:
            for i in range(s, 13):
                months.append(Month(i))
            for i in range(1, e + 1):
                months.append(Month(i))
        return cls(months)

    def blooms_in(self, month: Month) -> bool:
        return month in self.months

    def __contains__(self, month: Month) -> bool:
        return month in self.months

    def __repr__(self) -> str:
        return f"BloomPeriod(months={self.months})"

    def to_dict(self):
        return {
            "Months": [m.name for m in self.months]
        } if self.months else {}

@dataclass(frozen=True)
class Plant:
    name: str
    type: PlantType
    bloom_period: BloomPeriod
    max_height: float
    soil: SoilCondition
    light: LightCondition
    latin_name: str = ""
    article_number: str = ""
    properties: Dict[str, str] = field(default_factory=list)

    def to_dict(self):
        d = {
            "Name": self.name,
            "LatinName": self.latin_name,
            "ArticleNumber": self.article_number,
            "Type": self.type.name,
            "BloomPeriod": self.bloom_period.to_dict(),
            "MaxHeight": self.max_height,
            "Soil": self.soil.name,
            "Light": self.light.name,
        }
        # Filter out empty or default values if needed to match C# exactly?
        # Looking at C# verified file, it doesn't always have everything.
        return d

    def __repr__(self) -> str:
        return f"Plant(name='{self.name}', type={self.type}, ...)"


def create_plant(
    name: str = "Unknown",
    latin_name: str = "",
    article_number: str = "",
    plant_type: PlantType = PlantType.Flower,
    bloom_period: Optional[BloomPeriod] = None,
    max_height: float = 0.0,
    soil: SoilCondition = SoilCondition.Any,
    light: LightCondition = LightCondition.FullSun,
    properties: Optional[Dict[str, str]] = None
) -> Plant:
    """Helper to create a Plant with defaults, replacing the C#-style Builder."""
    return Plant(
        name=name,
        latin_name=latin_name,
        article_number=article_number,
        type=plant_type,
        bloom_period=bloom_period or BloomPeriod([]),
        max_height=max_height,
        soil=soil,
        light=light,
        properties=properties or {}
    )
