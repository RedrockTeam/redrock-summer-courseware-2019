import java.util.Iterator;

public class MenuItem extends MenuComponent{
    private boolean vegetarian;
    private float price;

    MenuItem(String description, boolean vegetarian, float price){
        this.description = description;
        this.vegetarian = vegetarian;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public float getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return description + " $" + price;
    }

    @Override
    public Iterator createIterator() {
        return new NullIterator();
    }

    @Override
    public void print(){
        System.out.println(getDescription());
        System.out.println(getPrice());
        System.out.println("Vegetarian:" + isVegetarian());
        System.out.println("-----------------");
    }
}
