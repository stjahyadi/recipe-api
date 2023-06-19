INSERT INTO RECIPE (title, dish_type, servings, instructions) VALUES
  ('Spaghetti Aglio e Olio', 'NON_VEGETARIAN', 4, '1. Cook spaghetti\n2. Heat olive oil and garlic in a pan\n3. Add red pepper flakes\n4. Toss cooked spaghetti in the pan\n5. Serve hot');

INSERT INTO INGREDIENT (name, recipe_id) VALUES
  ('spaghetti', 1),
  ('garlic', 1),
  ('olive oil', 1),
  ('red pepper flakes', 1);