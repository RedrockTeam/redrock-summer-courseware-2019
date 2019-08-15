import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class CompositeIterator implements Iterator {
    private Stack<Iterator> iterators = new Stack<>();

    //构造器将我们将要遍历的组合构造器传入
    CompositeIterator(Iterator iterator){
        //将顶层构造器压栈
        iterators.push(iterator);
    }

    @Override
    public boolean hasNext() {
        //先判断栈中还有没有迭代器
        if (iterators.empty()){
            return false;
        }else {
            //取出当前层的迭代器
            Iterator iterator = iterators.peek();
            //如果还有迭代器，查看这个迭代器还有没有多的元素
            if (!iterator.hasNext()){
                //如果这个迭代器没有多的元素，说明该层没有元素，则将该迭代器出栈
                iterators.pop();
                //回到上一层继续判断
                return hasNext();
            }else {
                return true;
            }
        }
    }

    @Override
    public Object next() {
        if (hasNext()){
            //取出当前层的迭代器
            Iterator iterator = iterators.peek();
            MenuComponent component = (MenuComponent)iterator.next();
            //判断取出的元素是菜单还是菜品
            if (component instanceof Menu){
                //如果是菜单的话则将菜单的迭代器压栈，下一次即在这个菜单中取元素
                iterators.push(component.createIterator());
            }
            //返回菜单或者菜品
            return component;
        }else {
            throw new NoSuchElementException();
        }
    }
}
