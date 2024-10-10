# Recipe API

The Recipe API allows users to manage recipes, including adding, updating, deleting, and fetching recipes. 
The API provides various filtering options to retrieve recipes based on specific criteria.

## Endpoints
### Create Recipe
Endpoint: `POST /v1/recipes`

This endpoint allows to create a new recipe by providing the necessary details in the request body. The recipe will be saved in the database and a unique id will be assigned to it.

### Update Recipe
Endpoint: `PUT /v1/recipes/{id}`

This endpoint allows to update an existing recipe by the given id. Users can provide the updated recipe details in the request body and the changes will saved to the database.

### Delete Recipe
Endpoint: `DELETE /v1/recipes/{id}`

This endpoint allows to delete a recipe by the given id. The recipe will be removed from the database permanently.

### Fetch Recipe
Endpoint: `GET /v1/recipes/{id}`

This endpoint allows to fetch a recipe by the given id. The API will return the recipe details in the response body.

### Filter Recipes
Endpoint: `GET /v1/recipes`

This endpoint allows users to filter recipes based on various criteria. Users can include query parameters to specify the filtering options. Available filters include:
- dish-type: Filter recipes based on whether they are vegetarian or not.
- serving: Filter recipes based on the number of servings.
- ingredients: Filter recipes that include specific ingredients.
- exclude-ingredients: Filter recipes that exclude specific ingredients.
- search: Filter recipes based on a text search within the recipe instructions.

## Table Design
The recipe functionality is implemented using a PostgreSQL database. The following tables are used to store the recipe data:

### Recipe Table
- **id**: The unique id of the recipe (primary key) such as 1, 2, 3...
- **title**: The title of the recipe.
- **dish_type**: An enum value indicating whether the recipe is VEGETARIAN or NON_VEGETARIAN.
- **servings**: The number of servings for the recipe.
- **instructions**: The instructions for preparing the recipe.

### Ingredient Table
- **id**: The unique identifier of the ingredient (primary key) such as 1, 2, 3...
- **name**: The name of the ingredient.
- **recipe_id**: The foreign key reference to the recipe that the ingredient belongs to.

The `recipe_id` field in the `Ingredient` table establishes a relationship between the `Recipe` and `Ingredient` tables. Each recipe can have multiple ingredients associated with it.

## Tech Stack
- Java 17
- Spring Boot 3
- Spring Data JPA
- PostgreSQL 14
- Hibernate
- OpenAPI (Swagger)
- Maven
- Docker compose

## How to Start / Stop
To start the application with
```shell
./start.sh
```
And stop the application with
```shell
./stop.sh
```

## Access the Swagger UI in Local
http://localhost:8080/swagger-ui/index.html

## Improvement
- More test cases to cover all possible cases
- Filter the recipe by criteria with query to increase the performance
- Use testcontainer to do integration testing
- Dockerize the application and use docker compose to run the application as container

## Testing
Add 1
Add 2
Add 3
Add 4