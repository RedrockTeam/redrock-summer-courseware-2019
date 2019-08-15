import java.util.Iterator;

public class Waitress {
    private MenuComponent totalMenus = new Menu("Total Menus");

    Waitress(){
        MenuComponent pancakeMenu = new Menu("Pancake Menu");
        pancakeMenu.add(
                new MenuItem("Pancake", true, 8));
        pancakeMenu.add(
                new MenuItem("Bacon Pancake", false, 15));

        MenuComponent dinnerMenu = new Menu("Dinner Menu");
        dinnerMenu.add(
                new MenuItem("Standard Dinner", false, 35));
        dinnerMenu.add(
                new MenuItem("Vegetarian Dinner", true, 25));

        MenuComponent dessertMenu = new Menu("Dessert Menu");
        dessertMenu.add(new MenuItem("Milk Shake", false, 5));

        dinnerMenu.add(dessertMenu);

        totalMenus.add(pancakeMenu);
        totalMenus.add(dinnerMenu);
    }

    void printMenu() {
        //使用内部遍历打印全部菜单
        totalMenus.print();
    }

    void printVegetarianMenu(){
        //使用外部遍历打印素食菜单
        System.out.println("\n****Vegetarian****");
        Iterator iterator = totalMenus.createIterator();
        while (iterator.hasNext()){
            try {
                MenuComponent menuComponent = (MenuComponent)iterator.next();
                if (menuComponent.isVegetarian()){
                    System.out.println(menuComponent.getDescription());
                }
            }catch (UnsupportedOperationException e){}
        }
    }
}
