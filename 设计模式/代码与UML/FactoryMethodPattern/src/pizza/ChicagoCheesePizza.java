package pizza;

public class ChicagoCheesePizza extends Pizza{
    public ChicagoCheesePizza(){
        name = "Chicago Cheese Pizza";
    }

    @Override
    public void cut() {
        System.out.println("Chicago style cutting " + name + "...");
    }
}
