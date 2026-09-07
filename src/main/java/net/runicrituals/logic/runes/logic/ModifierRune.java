package net.runicrituals.logic.runes.logic;

import net.runicrituals.logic.runes.Rune;
import net.runicrituals.logic.runes.RuneType;
import net.runicrituals.logic.runes.action.ActionRune;
import net.runicrituals.logic.runes.element.ElementRune;
import net.runicrituals.logic.runes.form.FormRune;

public abstract class ModifierRune extends Rune {

    Rune targetRune = null;

    @Override
    public RuneType getType() {
        return RuneType.MODIFIER;
    }

    public Rune getRune() {
        return targetRune;
    }

    public void applyModification(Rune rune) {
        switch (rune.getType()) {
            case ELEMENT -> applyModificationToElement((ElementRune)rune);
            case ACTION -> applyModificationToAction((ActionRune) rune);
            case FORM -> applyModificationToForm((FormRune) rune);
            case MODIFIER -> {
                this.applyModification(((ModifierRune)rune).getRune());
            }
        }
    }

    abstract void applyModificationToElement(ElementRune rune);
    abstract void applyModificationToAction(ActionRune rune);
    abstract void applyModificationToForm(FormRune rune);
}
