/*
    Adds a Cutting Board recipe that outputs Cucumber slices when White Wool is cut.
*/

// <recipetype:sushigocrafting:fermenting_barrel>.addRecipe(name as string, input as IIngredient, stack as IFluidStack, output as IItemStack);

<recipetype:sushigocrafting:fermenting_barrel>.addRecipe("fermenting", <item:minecraft:diamond>, <fluid:minecraft:lava>, <item:minecraft:dirt>);

/*
    Removes the Cheese Fermenting Barrel Recipe.
*/

// <recipetype:sushigocrafting:fermenting_barrel>.removeByName(name as string);

<recipetype:sushigocrafting:fermenting_barrel>.removeByName("sushigocrafting:fermenting_barrel/cheese");
