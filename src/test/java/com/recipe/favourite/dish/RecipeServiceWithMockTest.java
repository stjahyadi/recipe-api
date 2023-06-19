package com.recipe.favourite.dish;

import com.recipe.favourite.api.v1.models.DishType;
import com.recipe.favourite.api.v1.models.RecipeRequest;
import com.recipe.favourite.common.exception.RecipeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.recipe.favourite.MockTestData.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RecipeServiceWithMockTest {

    @InjectMocks
    private RecipeService recipeService;
    @Mock
    private RecipeRepository recipeRepository;

    @BeforeEach
    void setupService() {
        recipeRepository = mock(RecipeRepository.class);
        recipeService = new RecipeService(recipeRepository);
    }

    @Test
    void testCreateRecipe() {
        Long recipeId = 1L;
        String title = "Spaggeti";
        Integer servings = 2;
        DishType dishType = DishType.NON_VEGETARIAN;
        String instructions = "Boil water and cook spaghetti. In a separate pan, heat olive oil and sauté garlic. Add tomatoes and simmer for 10 mins";
        List<String> ingredients = List.of("Spaghetti", "Olive Oil", "Garlic");
        RecipeRequest request = recipeRequest(recipeId, title, servings, dishType, instructions, ingredients);

        when(recipeRepository.findById(anyLong())).thenReturn(
                Optional.of(recipeEntity(recipeId, title, servings, dishType, instructions, ingredients)));

        RecipeEntity createdRecipe = recipeService.addRecipe(request);

        RecipeEntity savedRecipe = recipeRepository.findById(createdRecipe.getId()).orElse(null);

        assertNotNull(savedRecipe);
        assertEquals(createdRecipe.getId(), savedRecipe.getId());
        assertEquals(createdRecipe.getTitle(), savedRecipe.getTitle());
        assertEquals(createdRecipe.getServings(), savedRecipe.getServings());
        assertEquals(createdRecipe.getInstructions(), savedRecipe.getInstructions());
    }

    @Test
    void testUpdateRecipeById() {
        Long recipeId = 1L;
        String title = "Spaghetti";
        Integer servings = 2;
        DishType dishType = DishType.NON_VEGETARIAN;
        String instructions = "Boil water and cook spaghetti. In a separate pan, heat olive oil and sauté garlic. Add tomatoes and simmer for 10 mins";
        List<String> ingredients = List.of("Spaghetti", "Olive Oil", "Garlic");
        RecipeRequest request = recipeRequest(recipeId, title, servings, dishType, instructions, ingredients);

        recipeService.addRecipe(request);

        String newTitle = "Spaghetti Tomato";
        String newInstruction = "Boil water and cook spaghetti. In a separate pan, heat olive oil and sauté garlic. Add garlic and simmer for 10 mins";
        List<String> newIngredients = List.of("Spaghetti", "Vegetable Oil", "Tomatoes");
        Integer newServings = 4;
        DishType newDishType = DishType.VEGETARIAN;
        request.setId(recipeId);
        request.setTitle(newTitle);
        request.setServings(newServings);
        request.setIngredients(newIngredients);
        request.setInstructions(newInstruction);
        request.dishType(newDishType);

        when(recipeRepository.existsById(anyLong())).thenReturn(true);
        when(recipeRepository.findById(anyLong())).thenReturn(
                Optional.of(recipeEntity(recipeId, newTitle, newServings, newDishType, newInstruction, newIngredients)));

        recipeService.updateRecipeById(recipeId, request);
        RecipeEntity savedRecipe = recipeRepository.findById(recipeId).orElse(null);
        assertNotNull(savedRecipe);
        assertEquals(recipeId, savedRecipe.getId());
        assertEquals(newTitle, savedRecipe.getTitle());
        assertEquals(newServings, savedRecipe.getServings());
        assertEquals(newInstruction, savedRecipe.getInstructions());
        assertEquals(newIngredients, savedRecipe.getIngredients().stream().map(IngredientEntity::getName).toList());
    }

    @Test
    void testDeleteRecipeById() {
        Long recipeId = 1L;
        String title = "Spaghetti";
        Integer servings = 2;
        DishType dishType = DishType.NON_VEGETARIAN;
        String instructions = "Boil water and cook spaghetti. In a separate pan, heat olive oil and sauté garlic. Add tomatoes and simmer for 10 mins";
        List<String> ingredients = List.of("Spaghetti", "Olive Oil", "Garlic");
        RecipeRequest request = recipeRequest(recipeId, title, servings, dishType, instructions, ingredients);

        RecipeEntity createdRecipe = recipeService.addRecipe(request);
        Long id = createdRecipe.getId();

        when(recipeRepository.existsById(anyLong())).thenReturn(true);

        recipeService.deleteRecipeById(id);
        assertTrue(recipeRepository.existsById(recipeId));
    }

    @Test
    void testGetRecipeById() {
        Long recipeId = 1L;
        String title = "Spaghetti";
        Integer servings = 2;
        DishType dishType = DishType.NON_VEGETARIAN;
        String instructions = "Boil water and cook spaghetti. In a separate pan, heat olive oil and sauté garlic. Add tomatoes and simmer for 10 mins";
        List<String> ingredients = List.of("Spaghetti", "Olive Oil", "Garlic");
        RecipeRequest request = recipeRequest(recipeId, title, servings, dishType, instructions, ingredients);

        RecipeEntity createdRecipe = recipeService.addRecipe(request);
        Long id = createdRecipe.getId();

        when(recipeRepository.findById(anyLong())).thenReturn(
                Optional.of(recipeEntity(recipeId, title, servings, dishType, instructions, ingredients)));

        RecipeEntity savedRecipe = recipeService.getRecipeById(id);
        assertNotNull(savedRecipe);
        assertEquals(recipeId, savedRecipe.getId());
        assertEquals(title, savedRecipe.getTitle());
        assertEquals(servings, savedRecipe.getServings());
        assertEquals(instructions, savedRecipe.getInstructions());
        assertEquals(ingredients, savedRecipe.getIngredients().stream().map(IngredientEntity::getName).toList());
    }

    @Test
    void testGetRecipeById_RecipeNotFound() {
        when(recipeRepository.findById(any(Long.class)))
                .thenThrow(RecipeNotFoundException.class);

        assertThrows(RecipeNotFoundException.class, () -> {
            recipeService.getRecipeById(100L);
        });
    }

    @Test
    public void testFilterRecipesByCriteria() {
        // Prepare test data
        RecipeEntity recipe1 = new RecipeEntity();
        recipe1.setId(1L);
        recipe1.setTitle("Spaghetti");
        recipe1.setDishType(DishType.NON_VEGETARIAN);
        recipe1.setServings(2);
        recipe1.setInstructions("Boil water and cook spaghetti. In a separate pan, heat olive oil and sauté garlic. Add tomatoes and simmer for 10 mins");
        recipe1.setIngredients(ingredientEntity(List.of("Olive Oil", "Garlic", "Tomatoes"), recipe1));

        RecipeEntity recipe2 = new RecipeEntity();
        recipe2.setId(2L);
        recipe2.setTitle("Caesar Salad");
        recipe2.setDishType(DishType.VEGETARIAN);
        recipe2.setServings(4);
        recipe2.setInstructions("In a large bowl, combine romaine lettuce, croutons, Parmesan cheese, and Caesar dressing. Toss well to coat the lettuce leaves.");
        recipe2.setIngredients(ingredientEntity(List.of("Parmesan cheese", "Tomatoes", "Cucumber"), recipe2));

        RecipeEntity recipe3 = new RecipeEntity();
        recipe3.setId(3L);
        recipe3.setTitle("Pizza");
        recipe3.setDishType(DishType.NON_VEGETARIAN);
        recipe3.setServings(6);
        recipe3.setInstructions("Preheat oven. Roll out pizza dough. Add sauce, cheese, and toppings. Bake in the oven until crust is golden.");
        recipe3.setIngredients(ingredientEntity(List.of("Pizza Dough", "Cheese", "Butter", "Eggs", "Tomatoes"), recipe3));

        RecipeEntity recipe4 = new RecipeEntity();
        recipe4.setId(4L);
        recipe4.setTitle("Baked Salmon");
        recipe4.setDishType(DishType.NON_VEGETARIAN);
        recipe4.setServings(2);
        recipe4.setInstructions("Preheat oven. Squeeze the lemon on the salmon. Bake the salmon in the oven for 15 mins with 180 degree until crust is golden.");
        recipe4.setIngredients(ingredientEntity(List.of("Salmon", "Tomatoes", "Olive Oil", "Lemon"), recipe4));

        List<RecipeEntity> recipes = List.of(recipe1, recipe2, recipe3, recipe4);

        when(recipeRepository.findAll()).thenReturn(recipes);

        List<String> includeIngredients = List.of("tomatoes");
        List<String> excludeIngredients = List.of("Salmon");
        String instruction = "oven";

        List<RecipeEntity> filteredRecipes = recipeService.fetchRecipesByFilter(null, null, includeIngredients, excludeIngredients, instruction);

        assertEquals(1, filteredRecipes.size());
        assertEquals(recipe3, filteredRecipes.get(0));
    }

    @Test
    public void testFilterRecipesByCriteria_NoResult() {
        RecipeEntity recipe1 = new RecipeEntity();
        recipe1.setId(1L);
        recipe1.setTitle("Spaghetti");
        recipe1.setDishType(DishType.NON_VEGETARIAN);
        recipe1.setServings(2);
        recipe1.setInstructions("Boil water and cook spaghetti. In a separate pan, heat olive oil and sauté garlic. Add tomatoes and simmer for 10 mins");
        recipe1.setIngredients(ingredientEntity(List.of("Olive Oil", "Garlic"), recipe1));

        RecipeEntity recipe2 = new RecipeEntity();
        recipe2.setId(2L);
        recipe2.setTitle("Caesar Salad");
        recipe2.setDishType(DishType.VEGETARIAN);
        recipe2.setServings(4);
        recipe2.setIngredients(ingredientEntity(List.of("Dressing Salad", "Tomatoes", "Cucumber"), recipe2));

        recipe2.setInstructions("In a large bowl, combine romaine lettuce, croutons, Parmesan cheese, and Caesar dressing. Toss well to coat the lettuce leaves.");

        List<RecipeEntity> recipes = List.of(recipe1, recipe2);
        // Mock the repository method to return an empty list
        when(recipeRepository.findAll()).thenReturn(recipes);

        // Create a RecipeRequest object with filter criteria
        Integer servings = 2;
        DishType dishType = DishType.VEGETARIAN;
        List<String> includeIngredients = List.of("Spaghetti");

        List<RecipeEntity> filteredRecipes = recipeService.fetchRecipesByFilter(dishType, servings, includeIngredients, null, null);

        assertTrue(filteredRecipes.isEmpty());
    }
}