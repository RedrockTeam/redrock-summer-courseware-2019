import java.util.Enumeration;
import java.util.Iterator;

public class EnumeratorToIteratorAdapter implements Iterator {
    Enumeration enumeration;
    EnumeratorToIteratorAdapter(Enumeration enumeration){
        this.enumeration = enumeration;
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return enumeration.hasMoreElements();
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public Object next() {
        return enumeration.nextElement();
    }
}
