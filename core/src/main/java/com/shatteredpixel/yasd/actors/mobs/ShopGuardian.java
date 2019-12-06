package com.shatteredpixel.yasd.actors.mobs;

import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.Actor;
import com.shatteredpixel.yasd.effects.particles.ElmoParticle;
import com.shatteredpixel.yasd.effects.particles.ShadowParticle;
import com.shatteredpixel.yasd.scenes.GameScene;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.PathFinder;

public class ShopGuardian extends Statue {

    private static final float SPAWN_DELAY	= 2f;

    public ShopGuardian() {
        super();
        HT = HP*=2;//Double health
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
}
