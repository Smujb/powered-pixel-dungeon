package com.shatteredpixel.yasd.general.sprites;

import com.shatteredpixel.yasd.general.Assets;
import com.watabou.noosa.Group;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public abstract class MagicalMobSprite extends MobSprite {
    @Override
    public void zap(int cell) {
        turnTo( ch.pos , cell );
        play( zap );
        FX(parent, cell, new Callback() {
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

    public abstract void FX(Group group, int cell, Callback c);
}
