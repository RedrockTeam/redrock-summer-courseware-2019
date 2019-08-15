public class Test {
    Duck modelDuck = new ModelDuck();

    public static void main(String[] args) {
        Duck modelDuck = new ModelDuck();
        modelDuck.setFlyBehavior(new FlyWithRocket());
        modelDuck.performFly();
    }
}
