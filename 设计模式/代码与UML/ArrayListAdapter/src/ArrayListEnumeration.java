import java.util.Enumeration;

public class ArrayListEnumeration implements Enumeration {

    /**
     * Tests if this enumeration contains more elements.
     *
     * @return {@code true} if and only if this enumeration object
     * contains at least one more element to provide;
     * {@code false} otherwise.
     */
    @Override
    public boolean hasMoreElements() {
        return false;
    }

    /**
     * Returns the next element of this enumeration if this enumeration
     * object has at least one more element to provide.
     *
     * @return the next element of this enumeration.
     * @throws NoSuchElementException if no more elements exist.
     */
    @Override
    public Object nextElement() {
        return null;
    }
}
