package net.runicrituals.logic.runes;

import net.runicrituals.logic.runes.action.Manifest;
import net.runicrituals.logic.runes.action.Sacrifice;
import net.runicrituals.logic.runes.modifier.*;
import net.runicrituals.logic.runes.element.*;
import net.runicrituals.logic.runes.element.Void;
import net.runicrituals.logic.runes.enums.RuneInlayMaterial;
import net.runicrituals.logic.runes.enums.RuneSymbol;
import net.runicrituals.logic.runes.enums.RuneType;
import net.runicrituals.logic.runes.form.*;
import net.runicrituals.registries.blocks.rune_slate.RuneslateEntity;

public abstract class Rune {

    protected final double BASE_RUNE_MANA_COST = 10;

    protected RuneInlayMaterial material;
    protected RuneslateEntity runeslateEntity;

    private boolean canRunClientSide = false;

    public Rune() {
    }

    /**
     * running client side can only happen with Entity-Only runes. block-effect runes should run server side due to
     * @param canRunClientSide if this rune can run client side
     */
    public Rune(boolean canRunClientSide) {
        this.canRunClientSide = canRunClientSide;
    }


    public abstract RuneType getType();

    /**
     * applies this rune's efficiency to the current cost.
     * @param cost summed cost of all sub-runes
     * @return new cost
     */
    public abstract double applyEfficiencyToCost(double cost);

    protected double efficiency() {
        return 1 - material.getEfficiency();
    }
    protected double invertEfficiency() {
        return 1 + efficiency();
    }

    public static Rune create(RuneslateEntity rse) {

        RuneSymbol symbol = RuneSymbol.getSymbolFromId(rse.getRuneDataComponent().runeSymbol());
        RuneInlayMaterial material = RuneInlayMaterial.getElementFromId(rse.getRuneDataComponent().inlay());
        Rune createdRune;

        switch (symbol) {
//            Elements
            case ARCANE -> createdRune = new Arcane();
            case KINETIC -> createdRune = new Kinetic();
            case THERMAL -> createdRune = new Thermal();
            case ELECTRIC -> createdRune = new Electric();
            case LIGHT -> createdRune = new Light();
            case MATTER -> createdRune = new Matter();
            case TIME -> createdRune = new Time();

//            Actions
            case MANIFEST -> createdRune = new Manifest();
            case SACRIFICE -> createdRune = new Sacrifice();

//            Forms
            case CUBE -> createdRune = new Prism();
            case SHEET -> createdRune = new Sheet();
            case DOME -> createdRune = new Dome();

//            Modifier
            case GROW -> createdRune = new Grow();
            case SHRINK -> createdRune = new Shrink();
            case CONTROL -> createdRune = new Control();
            case STATIC -> createdRune = new Static();
            case BOUND -> createdRune = new Bound();
            case DELAY -> createdRune = new Delay();

//            Unimplemented / Default
            default -> createdRune = new Void();
        }

        createdRune.material = material;
        createdRune.runeslateEntity = rse;

        return createdRune;
    }

    public abstract String name();

    @Override
    public String toString() {
        return name();
    }

    public boolean canRunClientSide() {
        return canRunClientSide;
    }
}
