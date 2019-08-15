public class GumballMachine {
    State soldOutState = new SoldOutState(this);
    State soldState = new SoldState(this);
    State noQuarterState = new NoQuarterState(this);
    State hasQuarterState = new HasQuarterState(this);
    State winnerState = new WinnerState(this);

    State state = soldState;
    int count;

    GumballMachine(int count){
        this.count = count;
        if (count > 0){
            state = new NoQuarterState(this);
        }
    }

    //顾客投入25分执行的方法
    public void insertQuarter(){
        state.insertQuarter();
    }

    //顾客试着退款的方法
    public void ejectQuarter(){
        state.ejectQuarter();
    }

    //顾客转动手柄的方法
    public void turnCrank(){
        state.turnCrank();
        state.dispense();
    }

    @Override
    public String toString() {
        return "count:" + count + "\nstate:" + state;
    }
}
