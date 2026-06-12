class BugConfigurations:
    bug1: bool = False
    bug2: bool = False
    bug3: bool = False
    bug4: bool = False
    bug5: bool = False

    @classmethod
    def reset(cls):
        cls.bug1 = False
        cls.bug2 = False
        cls.bug3 = False
        cls.bug4 = False
        cls.bug5 = False
