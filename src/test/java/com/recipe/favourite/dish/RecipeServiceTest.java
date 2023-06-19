package com.recipe.favourite.dish;

import com.recipe.favourite.UseTestcontainer;
import com.recipe.favourite.api.v1.models.DishType;
import com.recipe.favourite.api.v1.models.RecipeRequest;
import com.recipe.favourite.common.exception.RecipeNotFoundException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static com.recipe.favourite.MockTestData.recipeRequest;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@UseTestcontainer
@Disabled
public class RecipeServiceTest {

    private RecipeService recipeService;
    private RecipeRepository recipeRepository;

    @BeforeEach
    void setupService() {
        recipeRepository = mock(RecipeRepository.class);
        recipeService = new RecipeService(recipeRepository);
    }

    @Test
    public void testCreateRecipe() {
        RecipeRequest request = recipeRequest(
                null,
                "Spaggeti",
                2,
                DishType.NON_VEGETARIAN,
                "Boil water and cook spaghetti. In a separate pan, heat olive oil and sauté garlic. Add tomatoes and simmer for 10 mins",
                List.of("Spaghetti", "Olive Oil", "Garlic"));

        // Call the createRecipe method
        RecipeEntity createdRecipe = recipeService.addRecipe(request);

        // Fetch the recipe from the repository
        RecipeEntity savedRecipe = recipeRepository.findById(createdRecipe.getId()).orElse(null);

        // Assert the result
        assertNotNull(savedRecipe);
        assertEquals(createdRecipe.getId(), savedRecipe.getId());
        assertEquals(createdRecipe.getTitle(), savedRecipe.getTitle());
        assertEquals(createdRecipe.getServings(), savedRecipe.getServings());
        assertEquals(createdRecipe.getInstructions(), savedRecipe.getInstructions());
        assertEquals(createdRecipe, savedRecipe);
    }

    @Test
    public void testUpdateRecipeById() {
        RecipeRequest request = recipeRequest(
                null,
                "Spaggeti",
                2,
                DishType.NON_VEGETARIAN,
                "Boil water and cook spaghetti. In a separate pan, heat olive oil and sauté garlic. Add garlic and simmer for 10 mins",
                List.of("Spaghetti", "Olive Oil", "Garlic"));

        // Call the createRecipe method
        RecipeEntity createdRecipe = recipeService.addRecipe(request);
        Long id = createdRecipe.getId();
        String newTitle = "Spaggeti Tomato";
        String newInstruction = "Boil water and cook spaghetti. In a separate pan, heat olive oil and sauté garlic. Add garlic and simmer for 10 mins";
        List<String> newIngredients = List.of("Spaggeti", "Vegetable Oil", "Tomatoes");
        Integer newServings = 4;
        request.setId(id);
        request.setTitle(newTitle);
        request.setServings(newServings);
        request.setIngredients(newIngredients);
        request.setInstructions(newInstruction);
        request.dishType(DishType.VEGETARIAN);

        recipeService.updateRecipeById(id, request);
        RecipeEntity savedRecipe = recipeRepository.findById(id).orElse(null);
        assertNotNull(savedRecipe);
        assertEquals(id, savedRecipe.getId());
        assertEquals(newTitle, savedRecipe.getTitle());
        assertEquals(newServings, savedRecipe.getServings());
        assertEquals(newInstruction, savedRecipe.getInstructions());
        assertEquals(newIngredients, savedRecipe.getIngredients().stream().map(IngredientEntity::getName).toList());
    }

    @Test
    public void testDeleteRecipeById() {
        RecipeRequest request = recipeRequest(
                null,
                "Spaggeti",
                2,
                DishType.NON_VEGETARIAN,
                "Boil water and cook spaghetti. In a separate pan, heat olive oil and sauté garlic. Add garlic and simmer for 10 mins",
                List.of("Spaghetti", "Olive Oil", "Garlic"));

        // Call the createRecipe method
        RecipeEntity createdRecipe = recipeService.addRecipe(request);
        Long id = createdRecipe.getId();

        recipeService.deleteRecipeById(id);
        RecipeEntity savedRecipe = recipeRepository.findById(id).orElse(null);
        assertNull(savedRecipe);
    }

    @Test
    public void testGetRecipeById() {
        RecipeRequest request = recipeRequest(
                null,
                "Spaggeti",
                2,
                DishType.NON_VEGETARIAN,
                "Boil water and cook spaghetti. In a separate pan, heat olive oil and sauté garlic. Add garlic and simmer for 10 mins",
                List.of("Spaghetti", "Olive Oil", "Garlic"));

        // Call the createRecipe method
        RecipeEntity createdRecipe = recipeService.addRecipe(request);
        Long id = createdRecipe.getId();

        RecipeEntity savedRecipe = recipeService.getRecipeById(id);
        assertNotNull(savedRecipe);
        assertEquals(createdRecipe, savedRecipe);
    }

    @Test
    public void testGetRecipeById_RecipeNotFound() {
        assertThrows(RecipeNotFoundException.class, () -> {
            recipeService.getRecipeById(100L);
        });
    }
}
