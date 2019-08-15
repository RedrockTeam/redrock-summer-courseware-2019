public class ModelDuck extends Duck {
    ModelDuck(){
        flyBehavior = new FlyNoWay();
        quackBehavior = new Mute();
    }
}
