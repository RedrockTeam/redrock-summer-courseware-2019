//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.NoSuchElementException;
//
//public class DinnerMenuIterator implements Iterator {
//    private ArrayList<MenuItem> menuItems;
//    private int currentIndex = -1;
//
//    DinnerMenuIterator(ArrayList menuItems){
//        this.menuItems = menuItems;
//    }
//
//    @Override
//    public boolean hasNext() {
//        return (currentIndex + 1 < menuItems.size()
//                && menuItems.get(currentIndex + 1) != null);
//    }
//
//    @Override
//    public Object next() {
//        MenuItem next = menuItems.get(++currentIndex);
//        if (next != null){
//            return next;
//        }else {
//            throw new NoSuchElementException();
//        }
//    }
//}
