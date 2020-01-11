package com.shatteredpixel.yasd.actors.mobs;

import com.shatteredpixel.yasd.Assets;
import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.buffs.Buff;
import com.shatteredpixel.yasd.actors.buffs.Weakness;
import com.shatteredpixel.yasd.effects.MagicMissile;
import com.shatteredpixel.yasd.mechanics.Ballistica;
import com.shatteredpixel.yasd.sprites.RatSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class PlaceHolderRanged extends Rat implements Callback {
    {
        spriteClass = PHRSprite.class;
    }

    protected boolean doAttack(Char enemy ) {

        if (Dungeon.level.adjacent( pos, enemy.pos )) {

            return super.doAttack( enemy );

        } else {
            return doMagicAttack( enemy );
        }
    }

    @Override
    public boolean canAttack(Char enemy) {
        return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    @Override
    public int magicalDamageRoll() {
        return Random.Int( 12, 24 );
    }

    @Override
    public int magicalAttackProc(Char enemy, int damage) {
        if (Random.Int( 2 ) == 0) {
            Buff.prolong( enemy, Weakness.class, Weakness.DURATION );
        }
        return super.magicalAttackProc(enemy, damage);
    }

    @Override
    public void call() {
        onZapComplete();
    }

    private static class PHRSprite extends RatSprite {
        PHRSprite() {
            super();
            zap = attack.clone();
        }

        @Override
        public void zap(int cell) {
            turnTo( ch.pos , cell );
            play( zap );

            MagicMissile.boltFromChar( parent,
                    MagicMissile.SHADOW,
                    this,
                    cell,
                    new Callback() {
                        @Override
                        public void call() {
                            ch.onZapComplete();
                        }
                    } );
            Sample.INSTANCE.play( Assets.SND_ZAP );
        }

        @Override
        public void onComplete( Animation anim ) {
            if (anim == zap) {
                idle();
            }
            super.onComplete( anim );
        }
    }
}
