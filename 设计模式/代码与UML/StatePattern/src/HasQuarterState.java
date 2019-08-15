public class HasQuarterState implements State {
    GumballMachine gumballMachine;

    HasQuarterState(GumballMachine gumballMachine){
        this.gumballMachine = gumballMachine;
    }

    @Override
    public void insertQuarter() {
        System.out.println("You can't insert another quarter!");
    }

    @Override
    public void ejectQuarter() {
        System.out.println("Quarter returned!");
        gumballMachine.state = gumballMachine.noQuarterState;
    }

    @Override
    public void turnCrank() {
        System.out.println("Please wait...");
        gumballMachine.state = gumballMachine.soldState;
    }

    @Override
    public void dispense() {
        throw new UnsupportedOperationException();
    }
}
