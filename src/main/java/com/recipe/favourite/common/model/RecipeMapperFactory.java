package com.recipe.favourite.common.model;

import com.recipe.favourite.api.v1.models.RecipeRequest;
import com.recipe.favourite.api.v1.models.RecipeResponse;
import com.recipe.favourite.dish.IngredientEntity;
import com.recipe.favourite.dish.RecipeEntity;

import java.util.List;

public class RecipeMapperFactory {

    public static RecipeEntity fromRequest(RecipeRequest recipeRequest) {
        RecipeEntity recipe = new RecipeEntity();
        recipe.setId(recipeRequest.getId());
        recipe.setTitle(recipeRequest.getTitle());
        recipe.setDishType(recipeRequest.getDishType());
        recipe.setServings(recipeRequest.getServings());
        recipe.setInstructions(recipeRequest.getInstructions());
        List<IngredientEntity> ingredientEntities = recipeRequest.getIngredients().stream().map(it -> {
            IngredientEntity ingredientEntity = new IngredientEntity();
            ingredientEntity.setName(it);
            ingredientEntity.setRecipe(recipe);
            return ingredientEntity;
        }).toList();
        recipe.setIngredients(ingredientEntities);
        return recipe;
    }

    public static RecipeResponse fromRecipeEntity(RecipeEntity recipeEntity){
        RecipeResponse recipeResponse = new RecipeResponse();
        recipeResponse.setId(recipeEntity.getId());
        recipeResponse.setTitle(recipeEntity.getTitle());
        recipeResponse.setDishType(recipeEntity.getDishType());
        recipeResponse.setServings(recipeEntity.getServings());
        recipeResponse.setInstructions(recipeEntity.getInstructions());
        List<String> ingredients = recipeEntity.getIngredients().stream().map(IngredientEntity::getName).toList();
        recipeResponse.setIngredients(ingredients);
        return recipeResponse;

    }
}
