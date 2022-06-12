package jhaman.das.challangeubitricity.enums;

public enum EvChargingUnits {
    EV_CHARGE_LOWER_RATE(10, "OCCUPIED 10A"),
    EV_CHARGE_HIGHER_RATE(20, "OCCUPIED 20A"),
    EV_CHARGING_POINT_AVAILABLE(0, "AVAILABLE");

    private final int chargingUnit;
    private final String chargingValue;

    EvChargingUnits(int unit, String value) {
        this.chargingUnit = unit;
        this.chargingValue = value;
    }

    public int getChargingUnit() {
        return chargingUnit;
    }


    public String getChargingValue() {
        return chargingValue;
    }
}
