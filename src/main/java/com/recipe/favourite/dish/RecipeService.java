package com.recipe.favourite.dish;

import com.recipe.favourite.api.v1.models.DishType;
import com.recipe.favourite.api.v1.models.RecipeRequest;
import com.recipe.favourite.common.exception.RecipeNotFoundException;
import com.recipe.favourite.common.model.RecipeMapperFactory;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public RecipeEntity addRecipe(RecipeRequest recipeRequest) {
        RecipeEntity recipe = RecipeMapperFactory.fromRequest(recipeRequest);
        recipeRepository.save(recipe);
        return recipe;
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
            Integer servings, List<String> ingredients, List<String> excludeIngredients, String search){
        return recipeRepository.findAll().stream()
                .filter(recipe -> (dishType == null || recipe.getDishType() == dishType)
                        && (servings == null || recipe.getServings() == servings)
                        && (ingredients == null || ingredients.isEmpty() || recipeContainsIngredients(recipe, ingredients))
                        && (excludeIngredients == null || excludeIngredients.isEmpty() || !recipeContainsIngredients(recipe, excludeIngredients))
                        && (search == null || search.isEmpty() || recipe.getInstructions().contains(search))
                )
                .collect(Collectors.toList());
    }

    private boolean recipeContainsIngredients(RecipeEntity recipe, List<String> ingredients) {
        List<String> recipeIngredients = recipe.getIngredients().stream()
                .map(IngredientEntity::getName)
                .collect(Collectors.toList());
        return recipeIngredients.containsAll(ingredients);
    }

}
