public class WildTurkey implements Turkey {
    @Override
    public void fly() {
        System.out.println("Short distance fly!");
    }

    @Override
    public void gobble() {
        System.out.println("Gobble...Gobble...");
    }
}
