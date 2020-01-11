package com.shatteredpixel.yasd.sprites;

import com.shatteredpixel.yasd.Assets;
import com.shatteredpixel.yasd.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class PHRSprite extends MobSprite {
    PHRSprite() {
        super();
        texture( Assets.RAT );

        TextureFilm frames = new TextureFilm( texture, 16, 15 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0, 0, 0, 1 );

        run = new Animation( 10, true );
        run.frames( frames, 6, 7, 8, 9, 10 );

        attack = new Animation( 15, false );
        attack.frames( frames, 2, 3, 4, 5, 0 );

        die = new Animation( 10, false );
        die.frames( frames, 11, 12, 13, 14 );

        play( idle );
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
