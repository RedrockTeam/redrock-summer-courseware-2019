package pizzastore;

import pizza.Pizza;

public abstract class PizzaStore {
    abstract Pizza createPizza(String type);

    public void orderPizza(String type){
        Pizza pizza = createPizza(type);

        pizza.bake();
        pizza.cut();
        pizza.box();
    }
}
