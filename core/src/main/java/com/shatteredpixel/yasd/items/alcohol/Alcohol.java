package com.shatteredpixel.yasd.items.alcohol;

import com.shatteredpixel.yasd.Assets;
import com.shatteredpixel.yasd.actors.buffs.Buff;
import com.shatteredpixel.yasd.actors.buffs.Drunk;
import com.shatteredpixel.yasd.actors.hero.Hero;
import com.shatteredpixel.yasd.items.Item;
import com.shatteredpixel.yasd.items.potions.Potion;
import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.scenes.GameScene;
import com.shatteredpixel.yasd.windows.WndItem;
import com.shatteredpixel.yasd.windows.WndOptions;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public abstract class Alcohol extends Item {
    {
        cursed = false;
        stackable = true;
    }
    public static final String AC_DRINK = "DRINK";
    private static final float TIME_TO_DRINK = 1f;
    float MoraleGain;
    float drunkTurns;

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_DRINK );
        return actions;
    }

    @Override
    public void execute(final Hero hero, String action ) {

        super.execute( hero, action );

         if (action.equals( AC_DRINK )) {

           drink(hero);

        }
    }
    public void drink(Hero hero) {

        detach(hero.belongings.backpack);

        hero.spend(TIME_TO_DRINK);
        hero.busy();
        hero.gainMorale(MoraleGain);

        Buff.affect(hero, Drunk.class, drunkTurns);

        Sample.INSTANCE.play(Assets.SND_DRINK);

        hero.sprite.operate(hero.pos);

    }

    @Override
    public boolean isIdentified() {
        return true;
    }
}
