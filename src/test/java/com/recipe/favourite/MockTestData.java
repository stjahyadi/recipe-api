package com.recipe.favourite;

import com.recipe.favourite.api.v1.models.DishType;
import com.recipe.favourite.api.v1.models.RecipeRequest;
import com.recipe.favourite.api.v1.models.RecipeResponse;
import com.recipe.favourite.dish.IngredientEntity;
import com.recipe.favourite.dish.RecipeEntity;

import java.util.List;

public class MockTestData {

    public static RecipeRequest recipeRequest(Long id, String title, int servings, DishType dishType, String instructions, List<String> ingredients) {
        RecipeRequest request = new RecipeRequest();
        if (id != null) request.setId(id);
        request.setTitle(title);
        request.setServings(servings);
        request.setDishType(dishType);
        request.setInstructions(instructions);
        request.setIngredients(ingredients);
        return request;
    }

    public static RecipeEntity recipeEntity(Long id, String title, int servings, DishType dishType, String instructions, List<String> ingredients) {
        RecipeEntity recipe = new RecipeEntity();
        recipe.setId(id);
        recipe.setTitle(title);
        recipe.setServings(servings);
        recipe.setDishType(dishType);
        recipe.setInstructions(instructions);
        recipe.setIngredients(ingredientEntity(ingredients, recipe));
        return recipe;
    }

    public static List<IngredientEntity> ingredientEntity(List<String> ingredients, RecipeEntity recipe) {
        return ingredients.stream().map(it -> {
            IngredientEntity ingredientEntity = new IngredientEntity();
            ingredientEntity.setName(it);
            ingredientEntity.setRecipe(recipe);
            return ingredientEntity;
        }).toList();
    }

    public static RecipeResponse recipeResponse(Long id, String title, DishType dishType, Integer servings, String instructions, List<String> ingredients){
        RecipeResponse recipeResponse = new RecipeResponse();
        recipeResponse.setId(id);
        recipeResponse.setTitle(title);
        recipeResponse.setDishType(dishType);
        recipeResponse.setServings(servings);
        recipeResponse.setInstructions(instructions);
        recipeResponse.setIngredients(ingredients);
        return recipeResponse;
    }
}
