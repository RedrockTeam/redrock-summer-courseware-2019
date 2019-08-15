import pizza.Pizza;
import pizzastore.ChicagoPizzaStore;
import pizzastore.NYPizzaStore;

public class OrderPizza {
    public static void main(String[] args) {
        NYPizzaStore nyPizzaStore = new NYPizzaStore();
        ChicagoPizzaStore chicagoPizzaStore = new ChicagoPizzaStore();

        nyPizzaStore.orderPizza("Veggie");
        System.out.println();
        chicagoPizzaStore.orderPizza("Cheese");
    }
}
