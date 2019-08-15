public class Test {
    public static void main(String[] args) {
        //卧室灯
        Light livingRoomLight = new Light();
        LightOnCommand livingRoomLightOn = new LightOnCommand(livingRoomLight);
        LightOffCommand livingRoomLightOff = new LightOffCommand(livingRoomLight);

        //厨房灯
        Light kitchenLight = new Light();
        LightOnCommand kictenLightOn = new LightOnCommand(kitchenLight);
        LightOffCommand kictenLightOff = new LightOffCommand(kitchenLight);


        //RemoteControl是设定动作的调用者
        RemoteControl remoteControl = new RemoteControl();
        //设定第一个动作是开关卧室的灯
        remoteControl.setCommand(0, new LightOnCommand(livingRoomLight),
                new LightOffCommand(livingRoomLight));
        //设定第二个动作是开关厨房的灯
        remoteControl.setCommand(1, new LightOffCommand(kitchenLight),
                new LightOffCommand(kitchenLight));

        //设定宏命令
        Command[] allLightOn = {livingRoomLightOn, kictenLightOn};
        Command[] allLightOff = {livingRoomLightOff, kictenLightOff};
        MacroCommand allLightOnCommand = new MacroCommand(allLightOn);
        MacroCommand allLightOffCommand = new MacroCommand(allLightOff);
        //设定第三个动作是使用宏指令开关全部的灯
        remoteControl.setCommand(2, allLightOnCommand, allLightOffCommand);


    }
}
