package jhaman.das.challangeubitricity;

import java.util.concurrent.atomic.AtomicInteger;

public class EvUbiState {
    private AtomicInteger currentCars;
    private AtomicInteger currentPower;

    public EvUbiState() {
        this.currentCars= new AtomicInteger(0);
        this.currentPower= new AtomicInteger(0);
    }

    public AtomicInteger getCurrentCars() {
        return currentCars;
    }

    public AtomicInteger getCurrentPower() {
        return currentPower;
    }
}
