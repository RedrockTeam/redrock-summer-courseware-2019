public class SoldState implements State {
    GumballMachine gumballMachine;

    SoldState(GumballMachine gumballMachine){
        this.gumballMachine = gumballMachine;
    }

    @Override
    public void insertQuarter() {
        System.out.println("Please wait, we're already giving you a gumball!");
    }

    @Override
    public void ejectQuarter() {
        System.out.println("Sorry, you already turned the crank!");
    }

    @Override
    public void turnCrank() {
        System.out.println("Turning twice doesn't get you another gumball!");
    }

    @Override
    public void dispense() {
        System.out.println("A gumball comes rolling out the slot!");
        gumballMachine.count--;
        if (gumballMachine.count == 0){
            gumballMachine.state = gumballMachine.soldOutState;
        }else {
            gumballMachine.state = gumballMachine.noQuarterState;
        }
    }
}
