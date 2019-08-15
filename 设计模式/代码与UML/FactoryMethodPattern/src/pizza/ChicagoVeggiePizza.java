package pizza;

public class ChicagoVeggiePizza extends Pizza{
    public ChicagoVeggiePizza(){
        name = "Chicago Veggie Pizza";
    }

    @Override
    public void cut() {
        System.out.println("Chicago style cutting " + name + "...");
    }
}
