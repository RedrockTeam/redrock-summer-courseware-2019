public class SoldOutState implements State{
    GumballMachine gumballMachine;

    SoldOutState(GumballMachine gumballMachine){
        this.gumballMachine = gumballMachine;
    }

    @Override
    public void insertQuarter() {
        System.out.println("You can't insert quarter, the machine is sold out!");
    }

    @Override
    public void ejectQuarter() {
        System.out.println("You haven't inserted a quarter!");
    }

    @Override
    public void turnCrank() {
        System.out.println("You turned, but there are no gumballs!");
    }

    @Override
    public void dispense() {
        throw new UnsupportedOperationException();
    }
}
