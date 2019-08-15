public class NYIngredientFactory extends PizzaIngredientFactory {
    @Override
    Cheese createCheese() {
        return new NYCheese();
    }

    @Override
    Veggies createVeggies() {
        return new NYVeggies();
    }

    @Override
    Sauce createSauce() {
        return new NYSauce();
    }
}
