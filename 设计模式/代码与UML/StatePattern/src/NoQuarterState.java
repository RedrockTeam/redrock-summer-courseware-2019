public class NoQuarterState implements State {
    GumballMachine gumballMachine;

    NoQuarterState(GumballMachine gumballMachine){
        this.gumballMachine = gumballMachine;
    }

    @Override
    public void insertQuarter() {
        System.out.println("You inserted a quarter!");
        gumballMachine.state = gumballMachine.hasQuarterState;
    }

    @Override
    public void ejectQuarter() {
        System.out.println("You haven't inserted a quarter!");
    }

    @Override
    public void turnCrank() {
        System.out.println("You turned, but there are not quarter!");
    }

    @Override
    public void dispense() {
        throw new UnsupportedOperationException();
    }
}
