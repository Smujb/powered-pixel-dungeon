package com.shatteredpixel.yasd.general.sprites;

import com.shatteredpixel.yasd.general.Assets;
import com.watabou.noosa.TextureFilm;

public class ThornVineSprite extends MobSprite {

    public ThornVineSprite() {
        super();

        texture( Assets.THORNVINE );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 5, true );
        idle.frames( frames, 4, 5, 6, 7 );

        run = new Animation( 5, true );
        run.frames( frames, 4, 5, 6, 7 );

        attack = new Animation( 15, false );
        attack.frames( frames, 8, 9, 10, 11, 12, 13 );

        die = new Animation( 10, false );
        die.frames( frames, 3, 2, 1, 0 );

        play( idle );
    }


    @Override
    public int blood() {
        return 0x515c20;
    }
}
