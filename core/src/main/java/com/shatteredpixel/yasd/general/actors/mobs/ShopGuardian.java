package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.buffs.Aggression;
import com.shatteredpixel.yasd.general.actors.buffs.Bleeding;
import com.shatteredpixel.yasd.general.actors.buffs.Blindness;
import com.shatteredpixel.yasd.general.actors.buffs.Frost;
import com.shatteredpixel.yasd.general.actors.buffs.Paralysis;
import com.shatteredpixel.yasd.general.actors.buffs.Poison;
import com.shatteredpixel.yasd.general.actors.buffs.Terror;
import com.shatteredpixel.yasd.general.actors.buffs.Vertigo;
import com.shatteredpixel.yasd.general.effects.particles.ElmoParticle;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.StatueSprite;
import com.watabou.utils.PathFinder;

public class ShopGuardian extends Statue {

    private static final float SPAWN_DELAY	= 2f;
    {
        lootChance = 0.33f;//Rarer in Shop Guardians, as 4 spawn at a time. Average 1.33 Stones per shop rob
        spriteClass = ShopGuardianSprite.class;
        immunities.add(Aggression.class);
        immunities.add(Terror.class);
        immunities.add(Paralysis.class);
        immunities.add(Bleeding.class);
        immunities.add(Poison.class);
        resistances.add(Frost.class);
        resistances.add(Vertigo.class);
        resistances.add(Blindness.class);
    }

    public ShopGuardian() {
        super();
        ankhs = 0;
    }

    public void spawnAround( int pos ) {
        for (int n : PathFinder.NEIGHBOURS4) {
            int cell = pos + n;
            if (Dungeon.level.passable[cell] && Actor.findChar( cell ) == null) {
                spawnAt( cell );
            }
        }
    }

    public static ShopGuardian spawnAt( int pos ) {
        if (Dungeon.level.passable[pos] && Actor.findChar( pos ) == null) {

            ShopGuardian shopGuardian = new ShopGuardian();
            shopGuardian.pos = pos;
            shopGuardian.state = shopGuardian.HUNTING;
            GameScene.add( shopGuardian, SPAWN_DELAY );

            shopGuardian.sprite.emitter().burst(ElmoParticle.FACTORY, 5 );
            shopGuardian.next();
            return shopGuardian;
        } else {
            return null;
        }
    }

    @Override
    public void dropGear() {//Doesn't drop it's equipment
    }

    public static class ShopGuardianSprite extends StatueSprite {

        public ShopGuardianSprite(){
            super();
            tint(1, 1, 0, 0.2f);
        }

        @Override
        public void resetColor() {
            super.resetColor();
            tint(1, 1, 0, 0.2f);
        }
    }
}
