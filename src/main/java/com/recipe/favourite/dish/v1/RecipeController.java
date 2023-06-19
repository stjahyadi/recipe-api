package com.recipe.favourite.dish.v1;

import com.recipe.favourite.api.v1.RecipeApi;
import com.recipe.favourite.api.v1.models.DishType;
import com.recipe.favourite.api.v1.models.RecipeRequest;
import com.recipe.favourite.api.v1.models.RecipeResponse;
import com.recipe.favourite.common.model.RecipeMapperFactory;
import com.recipe.favourite.dish.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class RecipeController implements RecipeApi {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Override
    public ResponseEntity<Void> addRecipe(RecipeRequest recipeRequest) {
        RecipeResponse recipeResponse = RecipeMapperFactory.fromRecipeEntity(recipeService.addRecipe(recipeRequest));
        URI createdResourceUri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(recipeResponse.getId())
                .toUri();
        return ResponseEntity.created(createdResourceUri).build();
    }

    @Override
    public ResponseEntity<Void> deleteRecipe(Long id) {
        recipeService.deleteRecipeById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<RecipeResponse> getRecipe(Long id) {
        RecipeResponse recipeResponse = RecipeMapperFactory.fromRecipeEntity(recipeService.getRecipeById(id));
        return ResponseEntity.ok().body(recipeResponse);
    }

    @Override
    public ResponseEntity<Void> updateRecipe(Long id, RecipeRequest recipeRequest) {
        recipeService.updateRecipeById(id, recipeRequest);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<RecipeResponse>> fetchRecipes(DishType dishType, Integer servings, List<String> ingredients, List<String> excludeIngredients, String search) {
        List<RecipeResponse> recipeResponses = recipeService.fetchRecipesByFilter(dishType, servings, ingredients, excludeIngredients, search)
                .stream()
                .map(RecipeMapperFactory::fromRecipeEntity)
                .toList();
        return ResponseEntity.ok().body(recipeResponses);
    }
}
