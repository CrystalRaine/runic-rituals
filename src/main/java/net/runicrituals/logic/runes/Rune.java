package net.runicrituals.logic.runes;

import net.runicrituals.logic.RuneInlayMaterial;
import net.runicrituals.logic.RuneSymbol;
import net.runicrituals.logic.runes.action.Manifest;
import net.runicrituals.logic.runes.action.Sacrifice;
import net.runicrituals.logic.runes.element.*;
import net.runicrituals.logic.runes.form.Cube;
import net.runicrituals.logic.runes.form.Sphere;

public abstract class Rune {

    protected final double BASE_RUNE_MANA_COST = 10;

    protected RuneInlayMaterial material;

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
        return 1 + (1 - material.getEfficiency());
    }
    protected double invertEfficiency() {
        return material.getEfficiency();
    }

    public static Rune create(RuneSymbol symbol, RuneInlayMaterial material) {
        Rune createdRune = new VoidRune();

        switch (symbol) {
            case ARCANE -> {
                createdRune = new Arcane();
            }
            case KINETIC -> {
                createdRune = new Kinetic();
            }
            case THERMAL -> {
                createdRune =  new Thermal();
            }
            case ELECTRIC -> {
                createdRune =  new Electric();
            }
            case LIGHT -> {
                createdRune =  new VoidRune();
            }
            case MATTER -> {
                createdRune =  new Matter();
            }
            case SPACE -> {
                createdRune =  new VoidRune();
            }
            case TIME -> {
                createdRune =  new Time();
            }
            case MANIFEST -> {
                createdRune =  new Manifest();
            }
            case SACRIFICE -> {
                createdRune =  new Sacrifice();
            }
            case BIND -> {
                createdRune = new VoidRune();
            }
            case BOLT -> {
                createdRune =  new VoidRune();
            }
            case SPHERE -> {
                createdRune =  new Sphere(8);
            }
            case CUBE -> {
                createdRune =  new Cube(8);
            }
            case CONTROL -> {
                createdRune =  new VoidRune();
            }
        }

        createdRune.material = material;

        return createdRune;
    }

    public boolean canRunClientSide() {
        return canRunClientSide;
    }
}
