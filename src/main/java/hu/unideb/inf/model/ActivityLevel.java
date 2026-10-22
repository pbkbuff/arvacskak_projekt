package hu.unideb.inf.model;

//a vegen a szorzo van mivel ha tobbet mozogsz tobbet egetsz es tobb kaloria kell a fenntartashoz
public enum ActivityLevel {
    ULO("Ülő életmód", 1.2),
    KONNYU("Enyhén aktív (heti 1-3 edzés)", 1.375),
    MERSEKELT("Mérsékelten aktív (heti 3-5 edzés)", 1.55),
    AKTIV("Aktív (heti 6-7 edzés)", 1.725),
    NAGYON_AKTIV("Nagyon aktív (fizikai munka + edzés)", 1.9);

    private final String label;
    private final double multiplier;

    ActivityLevel(String label, double multiplier) {
        this.label = label;
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }

    @Override
    public String toString() {
        return label;
    }
}
