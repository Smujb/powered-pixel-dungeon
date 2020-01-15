package com.shatteredpixel.yasd.actors.buffs;

import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.items.rings.RingOfEvasion;
import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.sprites.CharSprite;
import com.shatteredpixel.yasd.ui.BuffIndicator;

import java.text.DecimalFormat;

public class Wet extends FlavourBuff {
    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    public static final float DURATION = 5f;

    @Override
    public boolean attachTo(Char target) {
        //can't chill what's frozen!
        if (target.buff(Frost.class) != null) return false;
        Buff chill = target.buff(Chill.class);
        if (chill != null) {//If enemy is chilled, freeze them instead.
            chill.detach();
            Buff.affect(target,Frost.class,3f);
            return false;
        }

        if (super.attachTo(target)){
            Buff.detach( target, Burning.class );
            return true;
        } else {
            return false;
        }
    }

    public float evasionFactor(){
        return (float) Math.pow( 0.85, cooldown());
    }

    @Override
    public int icon() {
        return BuffIndicator.FROST;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns(), new DecimalFormat("#.##").format((1f-evasionFactor())*100f));
    }
}
