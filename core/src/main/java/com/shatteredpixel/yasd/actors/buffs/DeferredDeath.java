package com.shatteredpixel.yasd.actors.buffs;

import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.sprites.CharSprite;
import com.shatteredpixel.yasd.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class DeferredDeath extends FlavourBuff {

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    private static final String COUNTDOWN    = "countdown";

    int countdown = 0;

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.DARKENED);
        else if (target.invisible == 0) target.sprite.remove(CharSprite.State.DARKENED);
    }

    @Override
    public int icon() {
        return BuffIndicator.CORRUPT;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc");
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( COUNTDOWN, countdown );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        countdown = bundle.getInt( COUNTDOWN );
    }
}