package jhaman.das.challangeubitricity.services;


import jhaman.das.challangeubitricity.EvUbiState;
import jhaman.das.challangeubitricity.configuration.EvUbiConfiguration;
import jhaman.das.challangeubitricity.enums.EvChargingUnits;
import jhaman.das.challangeubitricity.repository.EvUbiRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;


@Service
@AllArgsConstructor
public class EvUbiService {
    private final EvUbiRepository repo;
    private final EvUbiState state;

    public synchronized void add(int chargingPoint) {
        if (repo.getStationValue(chargingPoint) != EvChargingUnits.EV_CHARGING_POINT_AVAILABLE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "the point is already connected");
        }
        if (state.getCurrentPower().get() < EvUbiConfiguration.MAX_POWER) {
            givePowerFromReserve(chargingPoint);
        } else if (state.getCurrentCars().get() != EvUbiConfiguration.MAX_CARS) {

            if (state.getCurrentCars().get() == EvUbiConfiguration.MAX_CARS - 1) {
                divertPowerFromOtherCars(1);
                repo.addVehicle(chargingPoint, EvChargingUnits.EV_CHARGE_LOWER_RATE);
                repo.addToLowerChargeRate(chargingPoint);

            } else {
                divertPowerFromOtherCars(2);
                repo.addVehicle(chargingPoint, EvChargingUnits.EV_CHARGE_HIGHER_RATE);
                repo.addToHigherChargeRate(chargingPoint);

            }
            state.getCurrentCars().incrementAndGet();
        } else {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE, "the park is already full");
        }
    }

    public synchronized void delete(int n) {
        if (repo.getStationValue(n) == EvChargingUnits.EV_CHARGING_POINT_AVAILABLE) throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "the point is already removed");

        EvChargingUnits vehiclePower = repo.addVehicle(n, EvChargingUnits.EV_CHARGING_POINT_AVAILABLE);
        state.getCurrentCars().decrementAndGet();
        if (vehiclePower == EvChargingUnits.EV_CHARGE_LOWER_RATE) {
            repo.removeFromLowerChargeRate(n);
            shareChargingPower(1);
        } else {
            repo.removeFromHigherChargeRate(n);
            if (repo.doesAnyLowerChargeRateExists()) {
                shareChargingPower(2);

            } else {
                state.getCurrentPower().addAndGet(-1 * vehiclePower.getChargingUnit());
            }
        }
    }


    public Map<Integer, String> getUbiStatus() {
        return repo.getUbiStatus();
    }

    private synchronized void shareChargingPower(int num) {
        for (int i = 0; i < num; i++) {
            int index = repo.getCarToBeUpgraded();
            repo.addVehicle(index, EvChargingUnits.EV_CHARGE_HIGHER_RATE);
            repo.addToHigherChargeRate(index);
        }
    }

    private void givePowerFromReserve(int n) {
        repo.addVehicle(n, EvChargingUnits.EV_CHARGE_HIGHER_RATE);
        repo.addToHigherChargeRate(n);
        state.getCurrentPower().addAndGet(EvChargingUnits.EV_CHARGE_HIGHER_RATE.getChargingUnit());
        state.getCurrentCars().incrementAndGet();
    }

    private void divertPowerFromOtherCars(int num) {
        for (int i = 0; i < num; i++) {
            Integer old = repo.getCarToBeChargedLowerRate();
            repo.addToLowerChargeRate(old);
            repo.addVehicle(old, EvChargingUnits.EV_CHARGE_LOWER_RATE);
        }

    }
}