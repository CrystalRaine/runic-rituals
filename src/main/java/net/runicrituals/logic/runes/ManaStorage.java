package net.runicrituals.logic.runes;

public class ManaStorage {
    private static final double DEFAULT_MAX_MANA = 1500;
    private final double MAX_MANA;
    private double mana = 0;

    public ManaStorage() {
        mana = 0;
        MAX_MANA = DEFAULT_MAX_MANA;
    }

    public ManaStorage(double mana) {
        this.mana = mana;
        MAX_MANA = DEFAULT_MAX_MANA;
    }

    public ManaStorage(double mana, double max_mana) {
        this.mana = mana;
        MAX_MANA = max_mana;
    }

    public double getMana() {
        return mana;
    }

    private boolean addMana(double mana) {
        if(mana < 0) return false;
        if((mana + this.mana) > MAX_MANA) return false;
        this.mana = this.mana + mana;
        return true;
    }

    private boolean removeMana(double mana) {
        if(mana < 0) return false;
        if((this.mana - mana) < 0) return false;
        this.mana = this.mana - mana;
        return true;
    }

    public boolean applyManaValue(double mana) {
        if(mana > 0) {
            return removeMana(mana);
        } else {
            return addMana(-mana);
        }
    }

}
