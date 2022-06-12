package jhaman.das.challangeubitricity.repository;

import jhaman.das.challangeubitricity.enums.EvChargingUnits;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Repository
public class EvUbiRepository {
    private final ConcurrentHashMap<Integer, EvChargingUnits> map = new ConcurrentHashMap<>();
    private final CopyOnWriteArrayList<Integer> fastQueue = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Integer> slowQueue = new CopyOnWriteArrayList<>();

    public EvUbiRepository() {
        for (int i = 1; i <= 10; i++) {
            map.put(i, EvChargingUnits.EV_CHARGING_POINT_AVAILABLE);
        }
    }

    public  void addToLowerChargeRate(int n) {
        slowQueue.add(n);
    }

    public  void addToHigherChargeRate(int n) {
        fastQueue.add(n);
    }

    public boolean doesAnyLowerChargeRateExists() {
        return slowQueue.size() != 0;
    }

    public void removeFromHigherChargeRate(int n) {
        fastQueue.remove(fastQueue.indexOf(n));
    }

    public void removeFromLowerChargeRate(int n) {
        slowQueue.remove(slowQueue.indexOf(n));
    }

    public synchronized Map<Integer, String> getUbiStatus() {
        return map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getChargingValue()));
    }

    public EvChargingUnits getStationValue(int n) {
        return map.get(n);
    }

    public Integer getCarToBeChargedLowerRate() {
        return fastQueue.remove(0);
    }

    public EvChargingUnits addVehicle(int n, EvChargingUnits fastCharge) {
        return map.put(n, fastCharge);
    }

    public int getCarToBeUpgraded() {
        return slowQueue.remove(slowQueue.size() - 1);
    }
}
