package hu.unideb.inf.model;
//egyelore 3
public enum Goal {
    FOGYAS("Fogyás", -500),
    TARTAS("Súlytartás / csak naplózás", 0),
    HIZAS("Hízás", 300);

    private final String label;
    private final int kcalAdjustment;

    Goal(String label, int kcalAdjustment) {
        this.label = label;
        this.kcalAdjustment = kcalAdjustment;
    }

    public int getKcalAdjustment() {
        return kcalAdjustment;
    }

    @Override
    public String toString() {
        return label;
    }
}
