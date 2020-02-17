package com.shatteredpixel.yasd.general.actors.buffs;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;

import java.text.DecimalFormat;

public class Wet extends FlavourBuff {
    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    public static final float DURATION = 5f;

    @Override
    public boolean attachTo(Char target) {
        if (target.buff(Frost.class) != null) return false;
        Buff chill = target.buff(Chill.class);
        if (chill != null) {//If enemy is chilled, freeze them instead.
            chill.detach();
            Buff.affect( target, Frost.class, DURATION );
            return false;
        }

        if (super.attachTo(target)){
            Buff.detach( target, Burning.class );
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add( CharSprite.State.WET );
        else if (target.invisible == 0) target.sprite.remove( CharSprite.State.WET );
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
