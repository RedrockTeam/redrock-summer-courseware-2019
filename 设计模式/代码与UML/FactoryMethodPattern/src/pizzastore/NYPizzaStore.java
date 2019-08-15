package pizzastore;

import pizza.NYCheesePizza;
import pizza.NYVeggiePizza;
import pizza.Pizza;

public class NYPizzaStore extends PizzaStore{
    @Override
    Pizza createPizza(String type) {
        Pizza pizza = null;
        if (type.equals("Cheese")){
            pizza = new NYCheesePizza();
        }else if (type.equals("Veggie")){
            pizza = new NYVeggiePizza();
        }
        return pizza;
    }
}
