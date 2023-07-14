package com.recipe.favourite.dish;

import com.recipe.favourite.api.v1.models.DishType;
import com.recipe.favourite.api.v1.models.RecipeRequest;
import com.recipe.favourite.common.exception.RecipeNotFoundException;
import com.recipe.favourite.common.model.RecipeMapperFactory;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;


@Service
@Transactional
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public RecipeEntity addRecipe(RecipeRequest recipeRequest) {
        RecipeEntity recipe = RecipeMapperFactory.fromRequest(recipeRequest);
        RecipeEntity savedRecipe = recipeRepository.save(recipe);
        return savedRecipe;
    }

    public RecipeEntity getRecipeById(Long id) {
        return recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException("Could not find recipe with id=" + id));
    }

    public void deleteRecipeById(Long id) {
        if (!recipeRepository.existsById(id)) {
            throw new RecipeNotFoundException("Could not find recipe with id=" + id);
        }
        recipeRepository.deleteById(id);
    }

    public void updateRecipeById(Long id, RecipeRequest recipeRequest) {
        if (!recipeRepository.existsById(id)) {
            throw new RecipeNotFoundException("Could not find recipe with id=" + id);
        }
        RecipeEntity recipe = RecipeMapperFactory.fromRequest(recipeRequest);
        recipe.setId(id);

        recipeRepository.save(recipe);
    }

    public List<RecipeEntity> fetchRecipesByQuery(
            DishType dishType,
            Integer servings, List<String> ingredients, List<String> excludeIngredients, String search) {
        return recipeRepository.filterRecipesByCriteria(servings);
    }

    public List<RecipeEntity> fetchRecipesByFilter(
            DishType dishType,
            Integer servings, List<String> ingredients, List<String> excludeIngredients, String search) {
        return recipeRepository.findAll().stream()
                .filter(recipe -> (dishType == null || recipe.getDishType() == dishType)
                        && (servings == null || recipe.getServings() == servings)
                        && (ingredients == null || ingredients.isEmpty() || recipeContainsIngredients(recipe, ingredients))
                        && (excludeIngredients == null || excludeIngredients.isEmpty() || !recipeContainsIngredients(recipe, excludeIngredients))
                        && isContainInstruction(search, recipe)
                )
                .toList();
    }

    private static boolean isContainInstruction(String search, RecipeEntity recipe) {
        return search == null || search.isEmpty() || recipe.getInstructions().toLowerCase().contains(search.toLowerCase());
    }

    private boolean recipeContainsIngredients(RecipeEntity recipe, List<String> ingredients) {
        List<String> recipeIngredients = recipe.getIngredients().stream()
                .map(IngredientEntity::getName)
                .map(String::toLowerCase)
                .toList();
        return new HashSet<>(recipeIngredients).containsAll(ingredients.stream()
                .map(String::toLowerCase)
                .toList());
    }

}
