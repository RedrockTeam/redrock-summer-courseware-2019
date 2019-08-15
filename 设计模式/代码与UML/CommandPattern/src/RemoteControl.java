public class RemoteControl {
    private Command[] onCommands;
    private Command[] offCommands;
    private Command preCommand;

    RemoteControl(){
        onCommands = new Command[3];
        offCommands = new Command[3];

        Command noCommand = new noCommand();
        preCommand = noCommand;
        for (int i = 0; i < 3; i++){
            onCommands[i] = noCommand;
            offCommands[i] = noCommand;
        }
    }

    public void setCommand(int slot, Command onCommand, Command offCommand){
        onCommands[slot] = onCommand;
        offCommands[slot] = offCommand;
    }

    public void onButtonPressed(int slot){
        onCommands[slot].execute();
        preCommand = onCommands[slot];
    }

    public void offButtonPressed(int slot){
        offCommands[slot].execute();
        preCommand = offCommands[slot];
    }

    public void undoButtonPressed(){
        preCommand.undo();
    }
}
