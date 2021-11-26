/*
    Adds a Cutting Board recipe that outputs Cucumber slices when White Wool is cut.

    You can get a list of valid values for foodOutput by running '/ct dump sushigocrafting_food_ingredients'
*/

// <recipetype:sushigocrafting:cutting_board>.addRecipe(name as string, input as IIngredient, foodOutput as string);

<recipetype:sushigocrafting:cutting_board>.addRecipe("cutting", <item:minecraft:white_wool>, "cucumber");

/*
    Removes the Avacado Slices Cutting Board Recipe.
*/

// <recipetype:sushigocrafting:cutting_board>.removeByName(name as string);

<recipetype:sushigocrafting:cutting_board>.removeByName("sushigocrafting:cutting_board/avocado");