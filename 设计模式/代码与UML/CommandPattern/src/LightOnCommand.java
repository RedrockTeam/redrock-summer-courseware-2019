public class LightOnCommand implements Command {
    private Light light;

    LightOnCommand(Light light){
        this.light = light;
    }

    @Override
    public void execute() {
        light.setOn();
    }


}
