public class Coffee extends CaffeineBeverage{

    @Override
    void brew() {
        System.out.println("Dripping Coffee through filter");
    }

    @Override
    void addCondiments() {
        System.out.println("Adding milk and sugar");
    }
}
