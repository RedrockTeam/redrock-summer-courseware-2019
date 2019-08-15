public class LightOffCommand implements Command{
    private Light light;

    LightOffCommand(Light light){
        this.light = light;
    }

    @Override
    public void execute() {
        light.setOff();
    }

    @Override
    public void undo() {
        light.setOn();
    }
}


