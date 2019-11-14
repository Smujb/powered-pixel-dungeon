package com.shatteredpixel.yasd.actors.buffs;

import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.sprites.CharSprite;
import com.shatteredpixel.yasd.ui.BuffIndicator;

public class DeferredDeath extends Buff {

        {
            type = buffType.NEGATIVE;
            announced = true;
        }
        int countdown = 0;

        @Override
        public void fx(boolean on) {
            if (on) target.sprite.add( CharSprite.State.DARKENED );
            else if (target.invisible == 0) target.sprite.remove( CharSprite.State.DARKENED );
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

}
