public class MacroCommand implements Command {
    private Command commands[];

    MacroCommand(Command[] commands){
        this.commands = commands;
    }

    @Override
    public void execute() {
        for (Command command: commands){
            command.execute();
        }
    }
}
