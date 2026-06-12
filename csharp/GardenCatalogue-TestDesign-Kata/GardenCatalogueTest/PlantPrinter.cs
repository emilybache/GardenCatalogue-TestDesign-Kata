using System.Text;
using System.Globalization;

namespace GardenCatalogue;

public class PlantPrinter
{
    public void Print(StringBuilder sb, Plant plant, string indent = "")
    {
        void AppendIfNotEmpty(string label, string? value)
        {
            if (!string.IsNullOrEmpty(value))
                sb.AppendLine($"{indent}{label}: {value}");
        }

        sb.AppendLine($"{indent}Name: {plant.Name}");
        AppendIfNotEmpty("Latin Name", plant.LatinName);
        AppendIfNotEmpty("Article #", plant.ArticleNumber);
        
        if (plant.Type != PlantType.Flower)
            sb.AppendLine($"{indent}Type: {plant.Type}");

        if (plant.BloomPeriod.Months.Any())
            sb.AppendLine($"{indent}Blooms: {string.Join(", ", plant.BloomPeriod.Months)}");

        sb.AppendLine($"{indent}Max Height: {plant.MaxHeight.ToString("F1", CultureInfo.InvariantCulture)}m");
        sb.AppendLine($"{indent}Soil: {plant.Soil}");
        sb.AppendLine($"{indent}Light: {plant.Light}");

        if (plant.Properties.Any())
        {
            sb.AppendLine($"{indent}Properties:");
            foreach (var (key, value) in plant.Properties)
                sb.AppendLine($"{indent}  - {key}: {value}");
        }
    }
}
