namespace GardenCatalogue;
public static class BugConfigurations
{
    public static bool Bug1 { get; set; }
    public static bool Bug2 { get; set; }
    public static bool Bug3 { get; set; }
    public static bool Bug4 { get; set; }
    public static bool Bug5 { get; set; }
    public static void Reset()
    {
        Bug1 = false;
        Bug2 = false;
        Bug3 = false;
        Bug4 = false;
        Bug5 = false;
    }
}
