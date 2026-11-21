import cubk.annotations.CubkEventTarget;
import gr.larryngton.larrybus.annotations.EventTarget;

public class DummyListener {
    @EventTarget
    public void onTest(TestMain.TestEvent e) {
        TestMain.randomThing();
    }

    @CubkEventTarget
    public void onCubkTest(TestMain.CubkTestEvent e) {
        TestMain.randomThing();
    }
}
