public class NYPizzaStore extends PizzaStore{
    NYPizzaStore(){
        this.ingredientFactory = new NYIngredientFactory();
    }

    @Override
    public void prepare() {
        Cheese cheese = ingredientFactory.createCheese();
        Veggies veggies = ingredientFactory.createVeggies();
        Sauce sauce = ingredientFactory.createSauce();
    }
}
