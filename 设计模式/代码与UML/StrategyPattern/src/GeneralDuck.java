public class GeneralDuck extends Duck{
    GeneralDuck(){
        flyBehavior = new FlyWithWings();
        quackBehavior = new CanQuack();
    }
}
