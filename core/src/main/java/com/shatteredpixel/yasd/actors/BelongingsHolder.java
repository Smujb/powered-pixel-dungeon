package com.shatteredpixel.yasd.actors;

import com.shatteredpixel.yasd.actors.hero.Belongings;
import com.shatteredpixel.yasd.actors.mobs.Mob;
import com.shatteredpixel.yasd.messages.Messages;

import java.util.ArrayList;

public class BelongingsHolder extends Mob {
    public Belongings belongings;
    public BelongingsHolder() {
        super();
        name = Messages.get(this, "name");

        HP = HT = 20;

        belongings = new Belongings( this );

    }
}
