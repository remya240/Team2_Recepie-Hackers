package scraping;


	public class InputVO {
	private String Recipe_ID;
	private String Recipe_Name;
	private String Recipe_Category;
	private String Food_Category;
	private String Ingredients;
	private String Preparation_Time;
	private String Cooking_Time;
	private String Tag;
	private String No_of_servings;
	private String Cuisine_category;
	private String Recipe_Description;
	private String Preparation_method;
	private String Nutrient_values;
	private String Recipe_URL;
	
	//Getter and Setter
	public String getRecipeId() {
		return Recipe_ID;
	}
	public void setRecipeId(String recipeId) {
		this.Recipe_ID = recipeId;
	}
	public String getRecipeName() {
		return Recipe_Name;
	}
	public void setRecipeName(String recipeName) {
		this.Recipe_Name = recipeName;
	}
	public String getRecipeCategory() {
		return Recipe_Category;
	}
	public void setRecipeCategory(String recipeCategory) {
		this.Recipe_Category = recipeCategory;
	}
	public String getFoodCategory() {
		return Food_Category;
	}
	public void setFoodCategory(String foodCategory) {
		this.Food_Category = foodCategory;
	}
	public String getNameOfIngredients() {
		return Ingredients;
	}
	public void setNameOfIngredients(String nameOfIngredients) {
		this.Ingredients = nameOfIngredients;
	}
	public String getPreparationTime() {
		return Preparation_Time;
	}
	public void setPreparationTime(String preparationTime) {
		this.Preparation_Time = preparationTime;
	}
	public String getCookTime() {
		return Cooking_Time;
	}
	public void setCookTime(String cookTime) {
		this.Cooking_Time = cookTime;
	}
	public String getTags() {
		return Tag;
	}
	public void setTags(String tags) {
		this.Tag = tags;
	}
	public String getNo_of_servings() {
		return No_of_servings;
	}
	public void setNo_of_servings(String no_of_servings) {
		No_of_servings = no_of_servings;
	}
	public String getCuisineCategory() {
		return Cuisine_category;
	}
	public void setCuisineCategory(String cuisineCategory) {
		this.Cuisine_category = cuisineCategory;
	}
	public String getRecipeDescription() {
		return Recipe_Description;
	}
	public void setRecipeDescription(String recipeDescription) {
		this.Recipe_Description = recipeDescription;
	}
	public String getPrepMethod() {
		return Preparation_method;
	}
	public void setPrepMethod(String prepMethod) {
		this.Preparation_method = prepMethod;
	}
	public String getNutrients() {
		return Nutrient_values;
	}
	public void setNutrients(String nutrients) {
		this.Nutrient_values = nutrients;
	}
	public String getRecipeUrl() {
		return Recipe_URL;
	}
	public void setRecipeUrl(String recipeUrl) {
		this.Recipe_URL = recipeUrl;
	}
	@Override
	public String toString() {
		return "InputVO [recipeId=" + Recipe_ID + ", recipeName=" + Recipe_Name + ", recipeCategory=" + Recipe_Category
				+ ", foodCategory=" + Food_Category + ", nameOfIngredients=" + Ingredients + ", preparationTime="
				+ Preparation_Time + ", cookTime=" + Cooking_Time + ", tags=" + Tag + ", No_of_servings=" + No_of_servings
				+ ", cuisineCategory=" + Cuisine_category + ", recipeDescription=" + Recipe_Description + ", prepMethod="
				+ Preparation_method + ", nutrients=" + Nutrient_values + ", recipeUrl=" + Recipe_URL + "]";
	}

	}
