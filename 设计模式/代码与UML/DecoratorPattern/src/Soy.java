public class Soy extends Condiment{
    Beverage beverage;

    Soy(Beverage beverage){
        this.beverage = beverage;
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + ", Soy";
    }

    @Override
    public float cost() {
        return beverage.cost() + 2.5f;
    }
}
