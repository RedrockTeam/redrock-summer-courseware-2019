//import java.util.Iterator;
//import java.util.NoSuchElementException;
//
//public class PancakeMenuIterator implements Iterator {
//    private MenuItem[] menuItems;
//    private int currentIndex = -1;
//
//    PancakeMenuIterator(MenuItem[] menuItems){
//        this.menuItems = menuItems;
//    }
//
//    @Override
//    public boolean hasNext() {
//        return (menuItems[currentIndex + 1] != null);
//    }
//
//    @Override
//    public Object next() {
//        MenuItem next = menuItems[++currentIndex];
//        if (next != null){
//            return next;
//        }else {
//            throw new NoSuchElementException();
//        }
//    }
//}
