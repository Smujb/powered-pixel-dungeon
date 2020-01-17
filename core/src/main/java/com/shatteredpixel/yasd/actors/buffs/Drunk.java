package com.shatteredpixel.yasd.actors.buffs;

import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.ui.BuffIndicator;
import com.watabou.utils.Random;

public class Drunk extends FlavourBuff {

    public float accuracyFactor() {
        return (float) Math.max(Math.pow( 0.95, cooldown()), 0.5f);
    }

    public float evasionFactor() {
        return (float) Math.max(Math.pow( 0.95, cooldown()), 0.5f);
    }

    public boolean stumbleChance() {
        return Random.Int(5 + Math.round(cooldown()/15)) < cooldown()/15;
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
