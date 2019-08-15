public class Test {
    public static void main(String[] args) {
        WildTurkey wildTurkey = new WildTurkey();
        //客户并不知道这是真的鸭子还是适配器
        Duck possibleDuck = new TurkeyAdapter(wildTurkey);
        possibleDuck.fly();
        possibleDuck.quack();
    }
}
