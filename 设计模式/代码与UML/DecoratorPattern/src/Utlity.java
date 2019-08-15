public class Utlity {
    public static void main(String[] args) {
        Espresso espresso = new Espresso();
        System.out.println("Your Beverage is " + espresso.getDescription() + " cost: " + espresso.cost() + " 元");
        System.out.println();

        Beverage darkRoast = new DarkRoast();
        darkRoast = new Milk(darkRoast);
        darkRoast = new Soy(darkRoast);
        darkRoast = new Whip(darkRoast);
        System.out.println("Your Beverage is " + darkRoast.getDescription() + " cost: " + darkRoast.cost() + " 元");
    }

}
