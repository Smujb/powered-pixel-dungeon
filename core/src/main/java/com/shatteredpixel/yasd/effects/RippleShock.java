package com.shatteredpixel.yasd.effects;

import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.blobs.DarkGas;
import com.shatteredpixel.yasd.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;

public class RippleShock extends Image {

    private static final float TIME_TO_FADE = 0.5f;

    private float time;

    public RippleShock() {
        super( Effects.get( Effects.Type.SHOCK) );
    }

    public void reset( int p ) {
        revive();

        x = (p % Dungeon.level.width()) * DungeonTilemap.SIZE;
        y = (p / Dungeon.level.width()) * DungeonTilemap.SIZE;

        origin.set( width / 2, height / 2 );

        time = TIME_TO_FADE;
    }

    @Override
    public void update() {
        super.update();

        if ((time -= Game.elapsed) <= 0) {
            kill();
        } else {
            float p = time / TIME_TO_FADE;

//            if( Random.Int( 3 ) == 0 ){
//                scale.y *= -1;
//            }
//
//            if( Random.Int( 3 ) == 0 ){
//                scale.x *= -1;
//            }

            alpha( p );
        }
    }
}
