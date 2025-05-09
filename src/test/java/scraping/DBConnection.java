package scraping;

import data.Recipe;
import utilities.LoggerLoad;

import java.sql.*;

public class DBConnection {

    private static String user = "postgres";
    private static String password = "yourpassword";
    private static String dbName = "Recipe";
    private static String serverUrl = "jdbc:postgresql://localhost:5432/";
    private static String dbUrl = serverUrl + dbName;

    public static void initConnection() {
        try (Connection testConn = DriverManager.getConnection(dbUrl, user, password)) {
            LoggerLoad.info("Connected to existing database '" + dbUrl + "'");
        } catch (SQLException e) {
            LoggerLoad.info("Database '" + dbUrl + "' does not exist. Creating...");
            try (Connection conn = DriverManager.getConnection(serverUrl + "postgres", user, password);
                 Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("CREATE DATABASE \"" + dbName + "\";");
                LoggerLoad.info("Database '" + dbName + "' created successfully.");
            } catch (SQLException ex) {
                LoggerLoad.info("Failed to create database:");
                ex.printStackTrace();
                return;
            }

        }

        try (Connection conn = DriverManager.getConnection(dbUrl, user, password);
             Statement stmt = conn.createStatement()) {

            String[] tableNames = {
                    "Recipes_without_filter",
                    "LFV_elemination",
                    "LFV_to_add",
                    "LFV_Allergy_Milk",
                    "LFV_NotFully_Vegan",
                    "LFV_Allergy_Nut",
                    "LCHF_elemination",
                    "LCHF_to_add",
                    "LCHF_Allergy_Milk",
                    "LCHF_Allergy_Nut",
                    "LCHF_Recipe_to_avoid",
                    "LFV_Recipe_to_avoid"
            };

            for (String tableName : tableNames) {
                String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                        "Recipe_ID VARCHAR(50) PRIMARY KEY, " +
                        "Recipe_Name VARCHAR(255) NOT NULL, " +
                        "Recipe_Category VARCHAR(300), " +
                        "Food_Category VARCHAR(300), " +
                        "Ingredients TEXT, " +
                        "Preparation_Time VARCHAR(50), " +
                        "Cooking_Time VARCHAR(50), " +
                        "Tag VARCHAR(300), " +
                        "No_of_servings VARCHAR(50), " +
                        "Cuisine_category VARCHAR(300), " +
                        "Recipe_Description TEXT, " +
                        "Preparation_method TEXT, " +
                        "Nutrient_values TEXT, " +
                        "Recipe_URL VARCHAR(500)" +
                        ");";
                stmt.executeUpdate(createTableSQL);
                LoggerLoad.info("Table '" + tableName + "' created successfully!");
            }

        } catch (SQLException e) {
            LoggerLoad.info("Error while creating tables:");
            e.printStackTrace();
        }

    }

    // Inserting data to database
    public static void saveRecipeToDatabase(Recipe recipe, String tableName) {
        String insertSQL = "INSERT INTO " + tableName + " (" +
                "recipe_id, recipe_name, recipe_category, food_category, ingredients, " +
                "preparation_time, cooking_time, tag, no_of_servings, cuisine_category, " +
                "recipe_description, preparation_method, nutrient_values, recipe_url) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?) " +
                "ON CONFLICT (recipe_id) DO UPDATE SET " +
                "recipe_name = EXCLUDED.recipe_name, " +
                "recipe_category = EXCLUDED.recipe_category, " +
                "food_category = EXCLUDED.food_category, " +
                "ingredients = EXCLUDED.ingredients, " +
                "preparation_time = EXCLUDED.preparation_time, " +
                "cooking_time = EXCLUDED.cooking_time, " +
                "tag = EXCLUDED.tag, " +
                "no_of_servings = EXCLUDED.no_of_servings, " +
                "cuisine_category = EXCLUDED.cuisine_category, " +
                "recipe_description = EXCLUDED.recipe_description, " +
                "preparation_method = EXCLUDED.preparation_method, " +
                "nutrient_values = EXCLUDED.nutrient_values, " +
                "recipe_url = EXCLUDED.recipe_url";
        try (Connection conn = DriverManager.getConnection(dbUrl, user, password);
             PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, recipe.getRecipeId());
            LoggerLoad.info("recipeId" + recipe.getRecipeId());
            preparedStatement.setString(2, recipe.getRecipeName());
            preparedStatement.setString(3, recipe.getRecipeCategory().name());
            preparedStatement.setString(4, recipe.getFoodCategory().name());
            preparedStatement.setString(5, recipe.getIngredients());
            preparedStatement.setString(6, recipe.getPrepTime());
            preparedStatement.setString(7, recipe.getCookingTime());
            preparedStatement.setString(8, recipe.getTag());
            preparedStatement.setString(9, recipe.getNoOfServings());
            preparedStatement.setString(10, recipe.getCuisineCategory().name());
            preparedStatement.setString(11, recipe.getRecipeDescription());
            preparedStatement.setString(12, recipe.getPreparationMethod());
            preparedStatement.setString(13, recipe.getNutritionValue());
            preparedStatement.setString(14, recipe.getRecipeURL());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
