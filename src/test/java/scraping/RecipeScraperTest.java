package scraping;

import data.Recipe;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import testBase.BaseClass;
import utilities.ExcelData;
import utilities.LoggerLoad;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RecipeScraperTest extends BaseClass {

    @Test
    public void GetRecipesOnPage() throws InterruptedException {
        List<Recipe> recipeList = new ArrayList<>();

        By recipesListLink = By.xpath("//a[text()='Recipes List' and @href='/recipes/']");
		try {
		    WebElement link = driver.findElement(recipesListLink);
		    link.click(); // Standard click attempt
		} catch (ElementClickInterceptedException e) {
		    LoggerLoad.info("Standard click failed, retrying with JavaScript...");
		    JavascriptExecutor js = (JavascriptExecutor) driver;
		    js.executeScript("arguments[0].click();", driver.findElement(recipesListLink));
		}
        
        String recipeTab = driver.getWindowHandle();
        int lastPage = getLastCompletedPage(); // <-- Start from here
        int totalPages = getNumOfPages();
        if (lastPage == totalPages) {
            lastPage = 0;
        }
        LoggerLoad.info("Resuming from page: " + (lastPage + 1) + " of " + totalPages);

        for (int k = lastPage + 1; k <= totalPages; k++) {
            try {

                List<WebElement> recipeBlocks = driver
                        .findElements(By.xpath("//main//div[contains(@class,'recipe-block')]"));

                for (WebElement recipeCard : recipeBlocks) {
                    Recipe recipe = new Recipe();

                    WebElement recipeLink = recipeCard.findElement(By.tagName("a")); // recipe link

                    ((JavascriptExecutor) driver).executeScript("window.open(arguments[0], '_blank');",
                            recipeLink.getAttribute("href"));
                    String recipeDetailTab = switchToTabByIndex(1);
                    String currentUrl = driver.getCurrentUrl();
                    recipe.recipeId = RecipeDetails.getRecipeID(currentUrl);
                    RecipeDetails.getRecipeName(currentUrl, recipe);

                    RecipeDetails.getCookingTime(recipe);
                    RecipeDetails.getPreparationTime(recipe);
                    RecipeDetails.getNoofserving(recipe);
                    RecipeDetails.getRecipeIngrediants(recipe);
                    RecipeDetails.getTags(recipe);
                    recipe.recipeCategory = RecipeDetails.getRecipeCategory(recipe.recipeName, recipe.tag);
                    recipe.foodCategory = RecipeDetails.getFoodCategory(recipe.recipeName, recipe.tag);
                    recipe.cuisineCategory = RecipeDetails.getCuisineCategory(recipe.tag);
                    RecipeDetails.GetPreparationMethod(recipe);
                    RecipeDetails.GetNuritientValue(recipe);
                    RecipeDetails.getRecipieDescription(recipe);
                    RecipeDetails.getUrl(recipe);
                    recipeList.add(recipe);
                    saveLastCompletedPage(k);
                    closeTab(recipeDetailTab);
                    driver.switchTo().window(recipeTab);
                }

            } catch (Exception e) {
                LoggerLoad.info("Error from tab: " + recipeTab);
                System.err.println("Error on page " + k + ": " + e.getMessage());
                break;
            }

            List<WebElement> nextRecipePageButton = driver
                    .findElements(By.xpath("//ul[contains(@class,'pagination')]//a[text()='Next']"));

            if (!nextRecipePageButton.isEmpty()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextRecipePageButton.get(0));
            }

        }
        recipeListWithoutFilter(recipeList);
        filterLFVElimination(recipeList);
        Set<Recipe> afterlfvAddRecipes = filterLFVAdd(recipeList);
        filterLFVAllergyNut(new ArrayList<>(afterlfvAddRecipes));
        filterLFVAllergyMilk(new ArrayList<>(afterlfvAddRecipes));
        filterLFVRecipestoavoid(new ArrayList<>(afterlfvAddRecipes));
        filterLFVNotFullyVegan(recipeList);

        filterLCHFElimination(recipeList);
        Set<Recipe> afterlchfAddRecipes = filterLCHFAdd(recipeList);
        filterLCHFAllergyNut(new ArrayList<>(afterlchfAddRecipes));
        filterLCHFAllergyMilk(new ArrayList<>(afterlchfAddRecipes));
        filterLCHFRecipesToAvoid(new ArrayList<>(afterlchfAddRecipes));
    }

    private int getLastCompletedPage() {
        try {
            File file = new File("progress.txt");
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                reader.close();
                return line != null ? Integer.parseInt(line) : 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void saveLastCompletedPage(int page) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("progress.txt"));
            writer.write(String.valueOf(page));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getNumOfPages() {
        List<WebElement> pageLinks = driver
                .findElements(By.xpath("//ul[contains(@class,'pagination')]//a[contains(@href,'?page=')]"));
        int totalPages = 1;
        for (WebElement link : pageLinks) {
            String pageText = link.getText().trim();
            try {
                int pageNum = Integer.parseInt(pageText);
                if (pageNum > totalPages) {
                    totalPages = pageNum;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        LoggerLoad.info("Total pages: " + totalPages);
        return totalPages;
    }

    private String switchToTabByIndex(int index) {
        Set<String> handles = driver.getWindowHandles();
        String target = handles.toArray(new String[0])[index];
        driver.switchTo().window(target);
        return target;
    }

    private void closeTab(String handleToClose) {
        Set<String> allWindows = driver.getWindowHandles();
        for (String handle : allWindows) {
            if (handle.equals(handleToClose)) {
                driver.switchTo().window(handle);
                driver.close();
                break;
            }
        }
    }

    public void recipeListWithoutFilter(List<Recipe> recipeList) {

        DBConnection.initConnection();

        for (Recipe recipe : recipeList) {
            DBConnection.saveRecipeToDatabase(recipe, "Recipes_without_filter");
        }
        LoggerLoad.info("\nTotal " + recipeList.size() + " recipes saved to database.");
    }

    public void filterLFVElimination(List<Recipe> recipeList) {
        ExcelData.LoadLFVData();
        DBConnection.initConnection();

        Set<Recipe> eliminateList = recipeList.stream().filter(recipe -> {
            boolean containsEliminate = ExcelData.LFVEliminate.stream()
                    .anyMatch(value -> recipe.ingredients.contains(value));

            if (containsEliminate)
                return false;

            boolean containsAdd = ExcelData.LFVAdd.stream().anyMatch(value -> recipe.ingredients.contains(value));

            return !containsAdd;
        }).collect(Collectors.toSet());

        LoggerLoad.info("Number of recipes for Eliminate table: " + eliminateList.size());

        for (Recipe recipe : eliminateList) {
            DBConnection.saveRecipeToDatabase(recipe, "LFV_elemination");
        }
    }

    public Set<Recipe> filterLFVAdd(List<Recipe> recipeList) {
        ExcelData.LoadLFVData();
        DBConnection.initConnection();
        Set<Recipe> addList = recipeList.stream().filter(recipe -> {
            boolean containsEliminate = ExcelData.LFVEliminate.stream()
                    .anyMatch(value -> recipe.ingredients.contains(value));
            if (containsEliminate)
                return false;
            boolean containsAdd = ExcelData.LFVAdd.stream().anyMatch(value -> recipe.ingredients.contains(value));
            return containsAdd; // Only difference is this return value
        }).collect(Collectors.toSet());
        LoggerLoad.info("Number of recipes for Add table: " + addList.size());
        LoggerLoad.info("\nTotal " + addList.size() + " recipes saved to database.");
        for (Recipe recipe : addList) {
            DBConnection.saveRecipeToDatabase(recipe, "LFV_to_add");
        }
        return addList;
    }

    public void filterLFVAllergyNut(List<Recipe> recipeList) {
        // Load Excel data
        ExcelData.LoadLFVData();
        DBConnection.initConnection();
        Set<Recipe> lfvAllergyNutRecipes = recipeList.stream().filter(
                        recipe -> ExcelData.LFVAllergyNut.stream().noneMatch(value -> recipe.ingredients.contains(value)))
                .collect(Collectors.toSet());
        LoggerLoad.info("Number of Allergy nut " + lfvAllergyNutRecipes.size());
        for (Recipe recipe : lfvAllergyNutRecipes) {
            DBConnection.saveRecipeToDatabase(recipe, "LFV_Allergy_Nut");
        }
        LoggerLoad.info("\nTotal " + lfvAllergyNutRecipes.size() + " recipes saved to database.");
    }

    public void filterLFVAllergyMilk(List<Recipe> recipeList) {
        ExcelData.LoadLFVData();
        DBConnection.initConnection();
        Set<Recipe> lfvAllergymilk = recipeList.stream().filter(
                        recipe -> ExcelData.LFVAllergyMilk.stream().noneMatch(value -> recipe.ingredients.contains(value)))
                .collect(Collectors.toSet());
        LoggerLoad.info("Number of Allergy Milk: " + lfvAllergymilk.size());
        for (Recipe recipe : lfvAllergymilk) {
            DBConnection.saveRecipeToDatabase(recipe, "LFV_Allergy_Milk");
        }
        LoggerLoad.info("\nTotal " + lfvAllergymilk.size() + " recipes saved to database.");
    }

    public void filterLFVRecipestoavoid(List<Recipe> recipeList) {

        ExcelData.LoadLFVData();
        DBConnection.initConnection();

        Set<Recipe> recipesToAvoid = recipeList.stream().filter(recipe -> recipe.recipeDescription != null)
                .filter(recipe -> {
                    String desc = recipe.recipeDescription.toLowerCase();
                    return ExcelData.LFVAvoid.stream().map(String::toLowerCase).anyMatch(desc::contains);
                }).collect(Collectors.toSet());

        for (Recipe recipe : recipesToAvoid) {
            DBConnection.saveRecipeToDatabase(recipe, "LFV_Recipe_to_avoid");
        }
    }

    // LFVNot fully Vegan
    public void filterLFVNotFullyVegan(List<Recipe> recipeList) {
        ExcelData.LoadLFVData();
        DBConnection.initConnection();
        Set<Recipe> lfvNotFullyVeganRecipes = recipeList.stream().filter(recipe -> {
            boolean containsEliminate = ExcelData.LFVEliminate.stream()
                    .anyMatch(value -> recipe.ingredients.contains(value));
            if (containsEliminate)
                return false;
            boolean containsAdd = ExcelData.LFVAddnotfullyvegan.stream().anyMatch(value -> recipe.ingredients.contains(value));
            return containsAdd;
        }).collect(Collectors.toSet());

        LoggerLoad.info("Number of recipes for LFVNot fully Vegan table: " + lfvNotFullyVeganRecipes.size());
        for (Recipe recipe : lfvNotFullyVeganRecipes) {
            DBConnection.saveRecipeToDatabase(recipe, "LFV_NotFully_Vegan");
        }
    }

    public void filterLCHFElimination(List<Recipe> recipeList) {
        ExcelData.LoadLCHFData();
        DBConnection.initConnection();

        Set<Recipe> eliminateList = recipeList.stream().filter(recipe -> {
            boolean containsEliminate = ExcelData.LCHFEliminate.stream()
                    .anyMatch(value -> recipe.ingredients.contains(value));

            if (containsEliminate)
                return false;

            boolean containsAdd = ExcelData.LCHFAdd.stream().anyMatch(value -> recipe.ingredients.contains(value));
            return !containsAdd;
        }).collect(Collectors.toSet());

        LoggerLoad.info("Number of recipes for Eliminate table: " + eliminateList.size());

        for (Recipe recipe : eliminateList) {
            DBConnection.saveRecipeToDatabase(recipe, "LCHF_elemination");
        }
    }

    public Set<Recipe> filterLCHFAdd(List<Recipe> recipeList) {
        // Load Excel data
        ExcelData.LoadLCHFData();
        DBConnection.initConnection();
        Set<Recipe> addList = recipeList.stream().filter(recipe -> {
            boolean containsEliminate = ExcelData.LCHFEliminate.stream()
                    .anyMatch(value -> recipe.ingredients.contains(value));
            if (containsEliminate)
                return false;
            boolean containsAdd = ExcelData.LCHFAdd.stream().anyMatch(value -> recipe.ingredients.contains(value));
            return containsAdd; // Only difference is this return value
        }).collect(Collectors.toSet());
        LoggerLoad.info("Number of recipes for Add table: " + addList.size());
        LoggerLoad.info("\nTotal " + addList.size() + " recipes saved to database.");
        for (Recipe recipe : addList) {
            DBConnection.saveRecipeToDatabase(recipe, "LCHF_to_add");
        }
        return addList;
    }

    public void filterLCHFAllergyNut(List<Recipe> recipeList) {
        ExcelData.LoadLCHFData();
        DBConnection.initConnection();
        Set<Recipe> lchfAllergyNutRecipes = recipeList.stream().filter(
                        recipe -> ExcelData.LCHFAllergyNut.stream().noneMatch(value -> recipe.ingredients.contains(value)))
                .collect(Collectors.toSet());
        LoggerLoad.info("Number of LCHFAllergyNut recipes: " + lchfAllergyNutRecipes.size());
        for (Recipe recipe : lchfAllergyNutRecipes) {
            DBConnection.saveRecipeToDatabase(recipe, "LCHF_Allergy_Nut");
        }
        LoggerLoad.info("\nTotal " + lchfAllergyNutRecipes.size() + " recipes saved to database.");
    }

    public void filterLCHFAllergyMilk(List<Recipe> recipeList) {
        ExcelData.LoadLCHFData();
        DBConnection.initConnection();
        Set<Recipe> lCHFAllergymilk = recipeList.stream().filter(
                        recipe -> ExcelData.LCHFAllergyMilk.stream().noneMatch(value -> recipe.ingredients.contains(value)))
                .collect(Collectors.toSet());
        LoggerLoad.info("Number of lCHFAllergymilk: " + lCHFAllergymilk.size());
        for (Recipe recipe : lCHFAllergymilk) {
            DBConnection.saveRecipeToDatabase(recipe, "LCHF_Allergy_Milk");
        }
        LoggerLoad.info("\nTotal " + lCHFAllergymilk.size() + " recipes saved to database.");
    }

    public void filterLCHFRecipesToAvoid(List<Recipe> recipeList) {

        ExcelData.LoadLCHFData();
        DBConnection.initConnection();

        Set<Recipe> recipesToAvoid = recipeList.stream().filter(recipe -> recipe.recipeDescription != null)
                .filter(recipe -> {
                    String desc = recipe.recipeDescription.toLowerCase();
                    return ExcelData.LCHAvoid.stream().map(String::toLowerCase).anyMatch(desc::contains);
                }).collect(Collectors.toSet());

        for (Recipe recipe : recipesToAvoid) {
            DBConnection.saveRecipeToDatabase(recipe, "LCHF_Recipe_to_avoid");
        }
    }

}
