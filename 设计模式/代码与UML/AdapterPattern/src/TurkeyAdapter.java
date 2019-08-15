public class TurkeyAdapter implements Duck {
    private Turkey turkey;

    TurkeyAdapter(Turkey turkey){
        this.turkey = turkey;
    }

    @Override
    public void fly() {
        //火鸡要飞五次才能顶上鸭子飞
        for (int i = 0; i < 5; ++i){
            turkey.fly();
        }
    }

    @Override
    public void quack() {
        turkey.gobble();
    }
}
