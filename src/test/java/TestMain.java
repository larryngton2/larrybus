import cubk.CubkEventManager;
import cubk.impl.CubkEvent;
import gr.larryngton.larrybus.EventManager;
import gr.larryngton.larrybus.impl.Event;

import java.util.ArrayList;
import java.util.List;

public class TestMain {
    static void main() {
        int totalSamples = 10000;
        int perEventSamples = 1000;

        List<Long> samples = new ArrayList<>();
        List<Long> eventSamples = new ArrayList<>();
        List<Long> cubkEventSamples = new ArrayList<>();

        for (int i = 0; i < totalSamples; i++) {
            long lastMS = System.nanoTime();
            for (int j = 0; j < perEventSamples; j++) {
                randomThing();
            }
            samples.add(System.nanoTime() - lastMS);
        }

        float sum1 = 0;
        for (Long sample : samples) {
            sum1 += sample;
        }
        float noEvent = (sum1 / samples.size()) / perEventSamples;
        System.out.println("Without event manager: " + noEvent);

        EventManager eventManager = new EventManager();
        eventManager.register(new DummyListener());

        for (int i = 0; i < totalSamples; i++) {
            long lastMS = System.nanoTime();
            for (int j = 0; j < perEventSamples; j++) {
                eventManager.call(new TestEvent());
            }
            eventSamples.add(System.nanoTime() - lastMS);
        }

        float sum = 0;
        for (Long sample : eventSamples) {
            sum += sample;
        }
        float event = (sum / eventSamples.size()) / perEventSamples;
        System.out.println("With larrybus event manager: " + event);

        CubkEventManager eventManagerC = new CubkEventManager();
        eventManagerC.register(new DummyListener());

        for (int i = 0; i < totalSamples; i++) {
            long lastMS = System.nanoTime();
            for (int j = 0; j < perEventSamples; j++) {
                eventManagerC.call(new CubkTestEvent());
            }
            cubkEventSamples.add(System.nanoTime() - lastMS);
        }

        float sum2 = 0;
        for (Long sample : cubkEventSamples) {
            sum2 += sample;
        }
        float eventC = (sum2 / cubkEventSamples.size()) / perEventSamples;
        System.out.println("With cubk event manager: " + eventC);

        System.out.println((event - noEvent) + " nanos slower than no event");
        System.out.println((eventC - event) + " nanos faster than cubk");
    }

    public static void randomThing() {
        int anInt = 0;
        for (int j = 0; j < 10; j++) {
            if (anInt % 2 == 0) {
                anInt -= j / 2;
            } else {
                anInt += j * 2;
            }
        }
    }

    public static class TestEvent extends Event {
    }

    public static class CubkTestEvent implements CubkEvent {
    }
}
