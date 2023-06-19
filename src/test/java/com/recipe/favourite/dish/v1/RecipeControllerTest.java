package com.recipe.favourite.dish.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipe.favourite.MockTestData;
import com.recipe.favourite.api.v1.models.DishType;
import com.recipe.favourite.api.v1.models.RecipeRequest;
import com.recipe.favourite.api.v1.models.RecipeResponse;
import com.recipe.favourite.common.model.RecipeMapperFactory;
import com.recipe.favourite.dish.RecipeEntity;
import com.recipe.favourite.dish.RecipeService;
import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static com.recipe.favourite.MockTestData.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class RecipeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RecipeService recipeService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getRecipeById_returnOK() throws Exception {
        Long id = 1L;
        RecipeEntity recipeEntity = recipeEntity(id, "Spaggeti", 2, DishType.NON_VEGETARIAN,
                "Boil water and cook spaghetti. In a separate pan, heat olive oil and sauté garlic. Add tomatoes and simmer for 10 mins",
                List.of("Olive Oil", "Garlic"));

        when(recipeService.getRecipeById(anyLong())).thenReturn(recipeEntity);

        MvcResult result = mockMvc.perform(get("/v1/recipes/{id}", id)).andExpect(status().isOk()).andReturn();
        RecipeResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), RecipeResponse.class);
        assertEquals(RecipeMapperFactory.fromRecipeEntity(recipeEntity), response);
    }

    @Test
    void addRecipe_returnCreated() throws Exception {
        Long id = 1L;
        RecipeRequest recipeRequest = recipeRequest(id, "Spaggeti", 2, DishType.NON_VEGETARIAN,
                "Boil water and cook spaghetti. In a separate pan, heat olive oil and sauté garlic. Add tomatoes and simmer for 10 mins",
                List.of("Olive Oil", "Garlic"));

        RecipeEntity recipeEntity = RecipeMapperFactory.fromRequest(recipeRequest);

        when(recipeService.addRecipe(any())).thenReturn(recipeEntity);

        MvcResult result = mockMvc.perform(post("/v1/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipeRequest))
        ).andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().containsHeader("Location"));
        String expectedLocation = "/v1/recipes/1";
        URI locationUri = new URI(result.getResponse().getHeader("Location"));
        assertEquals(expectedLocation, locationUri.getPath());
    }

    @Test
    void updateRecipe_returnNoContent() throws Exception {
        Long id = 1L;
        RecipeRequest recipeRequest = recipeRequest(id, "Spaggeti", 2, DishType.NON_VEGETARIAN,
                "Boil water and cook spaghetti. In a separate pan, heat olive oil and sauté garlic. Add tomatoes and simmer for 10 mins",
                List.of("Olive Oil", "Garlic"));

        mockMvc.perform(put("/v1/recipes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipeRequest))
        ).andExpect(status().isNoContent());
    }

    @Test
    @Disabled
    void updateRecipe_returnBadRequest() throws Exception {
        Long id = -1L;
        RecipeRequest recipeRequest = recipeRequest(id, "Spaggeti", 2, DishType.NON_VEGETARIAN,
                "Boil water and cook spaghetti. In a separate pan, heat olive oil and sauté garlic. Add tomatoes and simmer for 10 mins",
                List.of("Olive Oil", "Garlic"));

        mockMvc.perform(put("/v1/recipes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipeRequest))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void deleteRecipe_returnNoContent() throws Exception {
        Long id = 1L;
        RecipeRequest recipeRequest = recipeRequest(id, "Spaggeti", 2, DishType.NON_VEGETARIAN,
                "Boil water and cook spaghetti. In a separate pan, heat olive oil and sauté garlic. Add tomatoes and simmer for 10 mins",
                List.of("Olive Oil", "Garlic"));

        mockMvc.perform(delete("/v1/recipes/{id}", id)).andExpect(status().isNoContent());
    }

    @Test
    public void testFetchRecipesWithCriteria() throws Exception {
        // Prepare the expected recipe list
        List<RecipeEntity> expectedRecipes = List.of(
                recipeEntity(
                        1L,
                        "Pizza",
                        6,
                        DishType.NON_VEGETARIAN,
                        "Preheat oven. Roll out pizza dough. Add sauce, cheese, and toppings. Bake in the oven until crust is golden.",
                        List.of("Pizza Dough", "Cheese", "Butter", "Eggs", "Tomatoes")),
                recipeEntity(
                        2L,
                        "Baked Salmon",
                        2,
                        DishType.NON_VEGETARIAN,
                        "Preheat oven. Squeeze the lemon on the salmon. Bake the salmon in the oven for 15 mins with 180 degree until crust is golden.",
                        List.of("Salmon", "Tomatoes", "Olive Oil", "Lemon"))
        );

        // Mock the service response
        when(recipeService.fetchRecipesByFilter(any(), any(), any(), any(), any())).thenReturn(expectedRecipes);

        // Perform the GET request
        MvcResult result = mockMvc.perform(get("/v1/recipes")
                        .param("dish-type", DishType.NON_VEGETARIAN.name())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        List<RecipeResponse> responses = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<RecipeResponse>>() {});
        assertEquals(2, responses.size());
        List<RecipeResponse> expectedResponses = expectedRecipes.stream().map(RecipeMapperFactory::fromRecipeEntity).toList();
        assertIterableEquals(expectedResponses, responses);
    }
}
