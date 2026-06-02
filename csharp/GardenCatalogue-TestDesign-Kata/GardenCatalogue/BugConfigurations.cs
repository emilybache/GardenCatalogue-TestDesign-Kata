namespace GardenCatalogue;

public static class BugConfigurations
{
    /// <summary>
    /// Bug1: Version detection fails for V2 files, falling back to V1.
    /// </summary>
    public static bool Bug1 { get; set; }

    /// <summary>
    /// Bug2: Bloom month calculation is off-by-one (0-indexed instead of 1-indexed).
    /// </summary>
    public static bool Bug2 { get; set; }

    /// <summary>
    /// Bug3: Name trimming is too aggressive or incorrect (standard Trim instead of specific chars).
    /// </summary>
    public static bool Bug3 { get; set; }
    
    /// <summary>
    /// Bug4: V2 extended properties are skipped.
    /// </summary>
    public static bool Bug4 { get; set; }

    /// <summary>
    /// Bug5: Name and LatinName are swapped in the returned Plant.
    /// </summary>
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
