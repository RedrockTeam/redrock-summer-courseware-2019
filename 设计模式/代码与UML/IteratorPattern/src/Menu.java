import java.util.ArrayList;
import java.util.Iterator;

public class Menu extends MenuComponent{
    private ArrayList<MenuComponent> children = new ArrayList<>();

    Menu(String description){
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void add(MenuComponent component) {
        children.add(component);
    }

    @Override
    public void print() {
        for (MenuComponent menuComponent: children){
            menuComponent.print();
        }
    }

    @Override
    public Iterator createIterator() {
        return new CompositeIterator(children.iterator());
    }
}
