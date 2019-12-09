package com.shatteredpixel.yasd.actors.buffs;

import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.blobs.WandOfStenchGas;
import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class StenchHolder extends FlavourBuff {

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    public int minDamage = 0;
    public int maxDamage = 0;
    private static final String MIN_DMG = "minDamage";
    private static final String MAX_DMG = "maxDamage";
    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put(MAX_DMG, maxDamage);
        bundle.put( MIN_DMG, minDamage );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        minDamage = bundle.getInt(MIN_DMG);
        maxDamage = bundle.getInt( MAX_DMG );
    }

    @Override
    public void detach() {
        super.detach();
        Dungeon.observe();
    }

    {
        immunities.add(WandOfStenchGas.class);
    }

    @Override
    public int icon() {
        return BuffIndicator.OOZE;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String heroMessage() {
        return Messages.get(this, "heromsg");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns());
    }
}