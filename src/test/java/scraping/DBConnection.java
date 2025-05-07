package scraping;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import data.Recipe;

import java.sql.SQLException;

public class DBConnection {
	private static Connection conn;
	public static void initConnection() {
        String user = "postgres";
        String password = "Hackathon3";
        String dbName = "Recipe";
        String serverUrl = "jdbc:postgresql://localhost:5432/";
        String dbUrl = serverUrl + dbName;
        
        	try (Connection testConn = DriverManager.getConnection(dbUrl, user, password)) {
                    System.out.println("Connected to existing database '" + dbUrl + "'");
                } catch (SQLException e) {
                    System.out.println("Database '" + dbUrl + "' does not exist. Creating...");
                    try (Connection conn = DriverManager.getConnection(serverUrl + "postgres", user, password);
                         Statement stmt = conn.createStatement()) {
                        stmt.executeUpdate("CREATE DATABASE \"" + dbName + "\";");
                        System.out.println("Database '" + dbName + "' created successfully.");
                    } catch (SQLException ex) {
                        System.out.println("Failed to create database:");
                        ex.printStackTrace();
                        return;
                    }
    
                }
        
                try (Connection conn = DriverManager.getConnection(dbUrl, user, password);
                     Statement stmt = conn.createStatement()) {

                    // Create LFV_elemination
                    String table1 = "CREATE TABLE IF NOT EXISTS LFV_elemination (" +
                            "Recipe_ID SERIAL PRIMARY KEY, " +
                            "Recipe_Name VARCHAR(255) NOT NULL, " +
                            "Recipe_Category VARCHAR(100), " +
                            "Food_Category VARCHAR(100), " +
                            "Ingredients TEXT, " +
                            "Preparation_Time INTEGER, " +
                            "Cooking_Time INTEGER, " +
                            "Tag VARCHAR(100), " +
                            "No_of_servings INTEGER, " +
                            "Cuisine_category VARCHAR(100), " +
                            "Recipe_Description TEXT, " +
                            "Preparation_method TEXT, " +
                            "Nutrient_values TEXT, " +
                            "Recipe_URL VARCHAR(500)" +
                            ");";
                    stmt.executeUpdate(table1);
                    System.out.println("Table 'LFV_elemination' created successfully!");

                    // Create LFV_to_add
                    String table2 = "CREATE TABLE IF NOT EXISTS LFV_to_add (" +
                            "Recipe_ID SERIAL PRIMARY KEY, " +
                            "Recipe_Name VARCHAR(255) NOT NULL, " +
                            "Recipe_Category VARCHAR(100), " +
                            "Food_Category VARCHAR(100), " +
                            "Ingredients TEXT, " +
                            "Preparation_Time INTEGER, " +
                            "Cooking_Time INTEGER, " +
                            "Tag VARCHAR(100), " +
                            "No_of_servings INTEGER, " +
                            "Cuisine_category VARCHAR(100), " +
                            "Recipe_Description TEXT, " +
                            "Preparation_method TEXT, " +
                            "Nutrient_values TEXT, " +
                            "Recipe_URL VARCHAR(500)" +
                            ");";
                    stmt.executeUpdate(table2);
                    System.out.println("Table 'LFV_to_add' created successfully!");

                    // Create LFV_Allergy_Milk
                    String table3 = "CREATE TABLE IF NOT EXISTS LFV_Allergy_Milk (" +
                            "Recipe_ID SERIAL PRIMARY KEY, " +
                            "Recipe_Name VARCHAR(255) NOT NULL, " +
                            "Recipe_Category VARCHAR(100), " +
                            "Food_Category VARCHAR(100), " +
                            "Ingredients TEXT, " +
                            "Preparation_Time INTEGER, " +
                            "Cooking_Time INTEGER, " +
                            "Tag VARCHAR(100), " +
                            "No_of_servings INTEGER, " +
                            "Cuisine_category VARCHAR(100), " +
                            "Recipe_Description TEXT, " +
                            "Preparation_method TEXT, " +
                            "Nutrient_values TEXT, " +
                            "Recipe_URL VARCHAR(500)" +
                            ");";
                    stmt.executeUpdate(table3);
                    System.out.println("Table 'LFV_Allergy_Milk' created successfully!");

                    // Create LFV_Allergy_Nut
                    String table4 = "CREATE TABLE IF NOT EXISTS LFV_Allergy_Nut (" +
                            "Recipe_ID SERIAL PRIMARY KEY, " +
                            "Recipe_Name VARCHAR(255) NOT NULL, " +
                            "Recipe_Category VARCHAR(100), " +
                            "Food_Category VARCHAR(100), " +
                            "Ingredients TEXT, " +
                            "Preparation_Time INTEGER, " +
                            "Cooking_Time INTEGER, " +
                            "Tag VARCHAR(100), " +
                            "No_of_servings INTEGER, " +
                            "Cuisine_category VARCHAR(100), " +
                            "Recipe_Description TEXT, " +
                            "Preparation_method TEXT, " +
                            "Nutrient_values TEXT, " +
                            "Recipe_URL VARCHAR(500)" +
                            ");";
                    stmt.executeUpdate(table4);
                    System.out.println("Table 'LFV_Allergy_Nut' created successfully!");

                } catch (SQLException e) {
                    System.out.println("Error while creating tables:");
                    e.printStackTrace();
                }
    } 
    
  
  // Inserting data to database
	public static void saveRecipeToDatabase(Recipe recipe, String tableName) {
	    String insertSQL = "INSERT INTO " + tableName + 
	        "(recipe_id, recipe_name, recipe_category, food_category, ingredients, " +
	        "preparation_time, cooking_time, tag, no_of_servings, cuisine_category, " +
	        "recipe_description, preparation_method, nutrient_values, recipe_url) " +
	        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try (PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {
			preparedStatement.setString(1, recipe.getRecipeId());
			System.out.println("recipeId" + recipe.getRecipeId());
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
            