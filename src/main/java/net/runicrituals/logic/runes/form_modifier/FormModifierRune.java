package net.runicrituals.logic.runes.form_modifier;

import net.runicrituals.logic.runes.Rune;
import net.runicrituals.logic.runes.enums.RuneType;
import net.runicrituals.logic.runes.form.FormRune;

import java.util.Objects;

public abstract class FormModifierRune extends Rune {

    Rune targetRune = null;

    @Override
    public RuneType getType() {
        return RuneType.FORM_MODIFIER;
    }

    public Rune getRune() {
        return targetRune;
    }

    public void applyModification(Rune rune) {
        if (Objects.requireNonNull(rune.getType()) == RuneType.FORM) {
            applyModificationToForm((FormRune) rune);
        }
    }

    abstract void applyModificationToForm(FormRune rune);
}
