package hu.unideb.inf.model;
// nincs tobb, lehet sirni
public enum Sex {
    FERFI("Férfi"),
    NO("Nő");

    private final String label;

    Sex(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
