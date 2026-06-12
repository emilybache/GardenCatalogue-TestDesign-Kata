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

    @staticmethod
    def from_season(season: Season) -> 'BloomPeriod':
        if season == Season.Spring:
            return BloomPeriod([Month.March, Month.April, Month.May])
        elif season == Season.Summer:
            return BloomPeriod([Month.June, Month.July, Month.August])
        elif season == Season.Autumn:
            return BloomPeriod([Month.September, Month.October, Month.November])
        elif season == Season.Winter:
            return BloomPeriod([Month.December, Month.January, Month.February])
        else:
            raise ValueError(f"Unknown season: {season}")

    @staticmethod
    def from_range(start: Month, end: Month) -> 'BloomPeriod':
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
        return BloomPeriod(months)

    def blooms_in(self, month: Month) -> bool:
        return month in self.months

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
    properties: Dict[str, str] = field(default_factory=dict)
