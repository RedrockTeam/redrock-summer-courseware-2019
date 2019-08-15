package pizzastore;

import pizza.Pizza;
import pizza.ChicagoCheesePizza;
import pizza.ChicagoVeggiePizza;

public class ChicagoPizzaStore extends PizzaStore{
    @Override
    Pizza createPizza(String type) {
        Pizza pizza = null;
        if (type.equals("Cheese")){
            pizza = new ChicagoCheesePizza();
        }else if (type.equals("Veggie")){
            pizza = new ChicagoVeggiePizza();
        }
        return pizza;
    }
}
