package com.shatteredpixel.yasd.general.effects;

import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.watabou.noosa.Gizmo;

public class YellowBlock extends Gizmo {
    private CharSprite target;

    public YellowBlock( CharSprite target ) {
        super();

        this.target = target;
    }

    @Override
    public void update() {
        super.update();

        target.tint(1,1,0,0.5f);

    }

    public void lighten() {

        target.resetColor();
        killAndErase();

    }

    public static YellowBlock darken( CharSprite sprite ) {

        YellowBlock YellowBlock = new YellowBlock( sprite );
        if (sprite.parent != null)
            sprite.parent.add( YellowBlock );

        return YellowBlock;
    }
}
