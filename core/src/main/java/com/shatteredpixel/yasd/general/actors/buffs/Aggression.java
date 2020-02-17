package com.shatteredpixel.yasd.general.actors.buffs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.watabou.utils.Bundle;


public class Aggression extends FlavourBuff {

    public static final float DURATION = 20f;

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
    }

    @Override
    public void detach() {
        //if our target is an enemy, reset the aggro of any enemies targeting it
        if (target.isAlive()) {
            if (target.alignment == Char.Alignment.ENEMY) {
                for (Mob m : Dungeon.level.mobs) {
                    if (m.alignment == Char.Alignment.ENEMY && m.isTargeting(target)) {
                        m.aggro(null);
                    }
                }
            }
        }
        super.detach();

    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

}
