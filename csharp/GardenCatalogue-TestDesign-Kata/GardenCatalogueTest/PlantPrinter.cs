using System.Text;
using System.Globalization;

namespace GardenCatalogue;

public class PlantPrinter
{
    public void Print(StringBuilder sb, Plant plant, string indent = "")
    {
        sb.AppendLine($"{indent}Name: {plant.Name}");
        if (!string.IsNullOrEmpty(plant.LatinName))
            sb.AppendLine($"{indent}Latin Name: {plant.LatinName}");
        if (!string.IsNullOrEmpty(plant.ArticleNumber))
            sb.AppendLine($"{indent}Article #: {plant.ArticleNumber}");
        
        if (plant.Type != PlantType.Flower)
            sb.AppendLine($"{indent}Type: {plant.Type}");

        if (plant.BloomPeriod.Months.Any())
        {
            var months = string.Join(", ", plant.BloomPeriod.Months);
            sb.AppendLine($"{indent}Blooms: {months}");
        }

        sb.AppendLine($"{indent}Max Height: {plant.MaxHeight.ToString("F1", CultureInfo.InvariantCulture)}m");
        sb.AppendLine($"{indent}Soil: {plant.Soil}");
        sb.AppendLine($"{indent}Light: {plant.Light}");

        if (plant.Properties.Any())
        {
            sb.AppendLine($"{indent}Properties:");
            foreach (var prop in plant.Properties)
            {
                sb.AppendLine($"{indent}  - {prop.Key}: {prop.Value}");
            }
        }
    }
}
