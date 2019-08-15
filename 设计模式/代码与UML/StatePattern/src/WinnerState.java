public class WinnerState implements State {
    GumballMachine gumballMachine;

    WinnerState(GumballMachine gumballMachine){
        this.gumballMachine = gumballMachine;
    }

    @Override
    public void insertQuarter() {
        System.out.println("You can't insert another quarter!");
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
        System.out.println("You're winner! Two gumball comes rolling out the slot!");
        if (gumballMachine.count >= 2){
            gumballMachine.count = gumballMachine.count - 2;
        }else {
            System.out.println("Oops, out of gumballs");
        }

        if (gumballMachine.count == 0){
            gumballMachine.state = gumballMachine.soldOutState;
        }else {
            gumballMachine.state = gumballMachine.noQuarterState;
        }
    }
}
