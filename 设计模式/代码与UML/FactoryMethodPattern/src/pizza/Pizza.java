package pizza;

public class Pizza {
    public String name = "Untitled Pizza";

    public void bake(){
        System.out.println("Classic baking " + name + "...");
    }

    public void cut(){
        System.out.println("Classic cutting " + name + "...");
    }

    public void box(){
        System.out.println("Classic boxing " + name + "...");
    }
}
