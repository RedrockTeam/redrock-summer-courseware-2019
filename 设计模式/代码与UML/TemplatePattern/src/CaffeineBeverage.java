import java.util.ArrayList;

public abstract class CaffeineBeverage {
    //模版方法
    void prepareRecipe(){
        boilWater();
        brew();
        pourInCup();
        if (customerWantsCondiment()){
            addCondiments();
            ArrayList arrayList = new ArrayList();
            arrayList.sort();
        }

    }

    private void boilWater(){
        System.out.println("Boiling water!!");
    }

    abstract void brew();

    private void pourInCup(){
        System.out.println("Pouring into cup");
    }

    abstract void addCondiments();

    //钩子方法（子类可以覆盖这个方法，但是没有必要一定这么做）
    private boolean customerWantsCondiment(){
        return true;
    }
}
