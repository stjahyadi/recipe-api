CREATE TABLE IF NOT EXISTS RECIPE
(
    id               BIGSERIAL PRIMARY KEY,
    title            VARCHAR(30)  NOT NULL,
    dish_type        VARCHAR(15)  NOT NULL,
    servings         INT NOT NULL,
    instructions     TEXT
);

CREATE TABLE IF NOT EXISTS INGREDIENT
(
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    recipe_id        BIGINT,
    CONSTRAINT ingredient_recipe_id_fk FOREIGN KEY (recipe_id) REFERENCES RECIPE(id)
);