public class Espresso extends Beverage{
    Espresso(){
        description = "Espresso";
    }

    @Override
    public float cost() {
        return 15.0f;
    }
}
