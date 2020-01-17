package com.shatteredpixel.yasd.effects;

import com.shatteredpixel.yasd.sprites.CharSprite;
import com.watabou.noosa.Gizmo;

public class RedBlock extends Gizmo {
    private CharSprite target;

    public RedBlock( CharSprite target ) {
        super();

        this.target = target;
    }

    @Override
    public void update() {
        super.update();

        target.tint(1,0,0,0.5f);

    }

    public void lighten() {

        target.resetColor();
        killAndErase();

    }

    public static RedBlock darken( CharSprite sprite ) {

        RedBlock RedBlock = new RedBlock( sprite );
        if (sprite.parent != null)
            sprite.parent.add( RedBlock );

        return RedBlock;
    }
}
