package scraping;

import data.Recipe;
import enums.CuisineCategory;
import enums.FoodCategory;
import enums.RecipeCategory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import testBase.BaseClass;
import utilities.LoggerLoad;

public class RecipeDetails extends BaseClass {

    public static Recipe getUrl(Recipe recipe) {

        try {
            recipe.recipeURL = driver.getCurrentUrl();

        } catch (Exception ex) {
            LoggerLoad.error("recipeURL element not found: " + ex.getMessage());
        }

        LoggerLoad.info("Recipe URL: " + recipe.recipeURL);

        return recipe;
    }

    public static void getCookingTime(Recipe recipe) {

        try {
            WebElement cookTime = driver.findElement(By.xpath("//div[@class='box-time']//div[2]//p[1]"));

            recipe.cookingTime = cookTime.getText().trim();
        } catch (Exception Ex) {
            LoggerLoad.error("Cooking time element not found: " + Ex.getMessage());
            recipe.cookingTime = "N/A";
        }

    }

    public static void getPreparationTime(Recipe recipe) {

        try {
            WebElement prepTime = driver.findElement(By.xpath("//div[@class='box-time']//div[3]//p[1]"));
            recipe.prepTime = prepTime.getText().trim();
        } catch (Exception Ex) {
            LoggerLoad.error("Preparation time element not found: " + Ex.getMessage());
            recipe.prepTime = "N/A";
        }
    }

    public static void GetPreparationMethod(Recipe recipe) {
        try {
            WebElement preparationMethod = driver.findElement(By.xpath("//div[@class='rsepc']//ol"));
            recipe.preparationMethod = preparationMethod.getText().trim();
        } catch (Exception Ex) {
            LoggerLoad.error("preparationMethod element not found: " + Ex.getMessage());
            recipe.preparationMethod = "N/A";
        }

    }

    public static void GetNuritientValue(Recipe recipe) {
        try {
            WebElement nutrValue = driver
                    .findElement(By.xpath("//figure[@class='table']/table | //table[@id='rcpnutrients']"));
            recipe.nutritionValue = nutrValue.getText().trim();
        } catch (Exception Ex) {
            LoggerLoad.error("NutritionValue element not found: " + Ex.getMessage());
            recipe.nutritionValue = "N/A";
        }

    }

    public static String getRecipeID(String url) {

        String[] parts = url.split("-");
        String lastPart = parts[parts.length - 1];
        String number = lastPart.substring(0, lastPart.length() - 1);
        LoggerLoad.info("RECIPE ID" + number); // Output:
        return number;

    }

    public static void getRecipeName(String url, Recipe recipe) {

        String baseName = url.substring(url.lastIndexOf('/') + 1);
        String namePart = baseName.substring(0, baseName.lastIndexOf('-'));

        // Replace hyphens with spaces
        recipe.recipeName = namePart.replace("-", " ");
    }

    public static void getNoofserving(Recipe recipe) {

        try {
            WebElement noOfServings = driver.findElement(By.xpath("//p[@class='mb-0 font-size-13 font-size-13']"));
            recipe.noOfServings = noOfServings.getText().trim();
        } catch (Exception Ex) {
            LoggerLoad.error("No of servings element not found: " + Ex.getMessage());
            recipe.noOfServings = "N/A";
        }

    }

    public static void getRecipieDescription(Recipe recipe) {

        try {
            WebElement recipeDescription = driver.findElement(By.xpath("//p[contains(text(),'|')]"));
            recipe.recipeDescription = recipeDescription.getText().trim();
        } catch (Exception Ex) {
            LoggerLoad.error("Description element not found: " + Ex.getMessage());
            recipe.recipeDescription = "N/A";
        }

    }

    public static void getTags(Recipe recipe) {
        try {
            WebElement tagElements = driver.findElement(By.className("tags-list"));
            recipe.tag = tagElements.getText();
        } catch (Exception e) {
            LoggerLoad.error("Tags element not found: " + e.getMessage());
        }
    }

    public static FoodCategory getFoodCategory(String recipeName, String recipeTag) {
        {
            if ((recipeName != null && recipeName.contains("Vegan")) || (recipeTag != null && recipeTag.contains("Vegan")))
                return FoodCategory.VEGAN;
            else if ((recipeName != null && recipeName.contains("Jain")) || (recipeTag != null && recipeTag.contains("Jain")))
                return FoodCategory.JAIN;
            else if ((recipeName != null && recipeName.contains("Egg ")) || (recipeTag != null && recipeTag.contains("Egg ")))
                return FoodCategory.EGGITARIAN;
            else if ((recipeName != null && recipeName.contains("NonVeg")) || (recipeTag != null && recipeTag.contains("NonVeg")))
                return FoodCategory.NONVEGETARIAN;
            else
                return FoodCategory.VEGETARIAN;
        }

    }

    public static RecipeCategory getRecipeCategory(String recipeName, String recipeTags) {
        if ((recipeName != null && recipeName.contains("Breakfast")) || (recipeTags != null && recipeTags.contains("Breakfast")))
            return RecipeCategory.BREAKFAST;
        else if ((recipeName != null && recipeName.contains("Lunch")) || (recipeTags != null && recipeTags.contains("Lunch")))
            return RecipeCategory.LUNCH;
        else if ((recipeName != null && recipeName.contains("Dinner")) || (recipeTags != null && recipeTags.contains("Dinner")))
            return RecipeCategory.DINNER;
        else
            return RecipeCategory.SNACKS;
    }

    public static CuisineCategory getCuisineCategory(String tag) {
        if (tag == null) {
            return CuisineCategory.INDIAN;
        }
        String clearedTag = tag.replaceAll("\\s+", "_");
        for (CuisineCategory cuisineCategory : CuisineCategory.values()) {
            if (clearedTag.toUpperCase().contains(cuisineCategory.name())) {
                return cuisineCategory;
            }
        }
        return CuisineCategory.INDIAN;

    }

    public static void getRecipeIngrediants(Recipe recipe) {
        try {
            WebElement ingredientSection = driver.findElement(By.id("ingredients"));
            recipe.ingredients = ingredientSection.getText().trim();
        } catch (Exception e) {
            LoggerLoad.error("ingredients element not found: " + e.getMessage());
        }
    }

}

