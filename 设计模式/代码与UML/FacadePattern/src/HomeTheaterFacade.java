public class HomeTheaterFacade {
    private Amplifier amplifier;
    private PopcornPopper popcornPopper;
    private Projector projector;
    private Screen screen;
    private CD cd;

    HomeTheaterFacade(Amplifier amplifier, PopcornPopper popcornPopper,
                      Projector projector, Screen screen, CD cd){
        this.amplifier = amplifier;
        this.popcornPopper = popcornPopper;
        this.projector = projector;
        this.screen = screen;
        this.cd = cd;
    }

    public void watchMovie(){
        amplifier.on();
        amplifier.setVolume(10);
        popcornPopper.on();
        popcornPopper.pop();
        screen.up();
        projector.input(cd);
        projector.on();
    }
}
