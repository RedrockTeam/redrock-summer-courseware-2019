import java.util.Iterator;

public abstract class MenuComponent {
    String description;

    public String getDescription() {
        //如果继承的子类没有实现该方法，则抛出异常说明不支持该方法
        throw new UnsupportedOperationException();
    }

    public boolean isVegetarian() {
        throw new UnsupportedOperationException();
    }

    public float getPrice() {
        throw new UnsupportedOperationException();
    }

    public void add(MenuComponent component){
        throw new UnsupportedOperationException();
    }

    public Iterator createIterator(){
        throw new UnsupportedOperationException();
    }

    public void print(){
        throw new UnsupportedOperationException();
    }
}
