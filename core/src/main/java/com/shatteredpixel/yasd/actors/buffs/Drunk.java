package com.shatteredpixel.yasd.actors.buffs;

import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.ui.BuffIndicator;

public class Drunk extends FlavourBuff {

    public float accuracyFactor() {
        return (float) Math.max(Math.pow( 0.90, cooldown()), 0.5f);
    }

    public float evasionFactor() {
        return (float) Math.max(Math.pow( 0.90, cooldown()), 0.5f);
    }

    @Override
    public int icon() {
        return BuffIndicator.RAGE;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns());
    }
}
