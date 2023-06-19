package com.recipe.favourite.dish;

import com.recipe.favourite.api.v1.models.DishType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {

    @Query(value = "SELECT r.id, r.title, r.dish_type, r.servings, r.instructions, i.name, i.recipe_id FROM recipe r LEFT JOIN ingredient i ON r.id = i.recipe_id WHERE 1=1 " +
            //"AND (:dishType IS NULL OR r.dish_type = :dishType) " +
            "AND (:servings IS NULL OR r.servings = :servings) " //+
            //"AND (:includedIngredients IS NULL OR i.name IN (:includedIngredients)) " +
            //"AND (:excludedIngredients IS NULL OR i.name NOT IN (:excludedIngredients)) " +
            //"AND (:searchText IS NULL OR r.instructions LIKE %:searchText%)"
            , nativeQuery = true)
    List<RecipeEntity> filterRecipesByCriteria(
            //@Param("dishType") DishType dishType,
                                               @Param("servings") Integer servings
            //                                   @Param("includedIngredients") List<String> includedIngredients,
            //                                  @Param("excludedIngredients") List<String> excludedIngredients,
    //                                           @Param("searchText") String searchText
    );

}
