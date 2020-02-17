package com.shatteredpixel.yasd.general.effects;

import com.shatteredpixel.yasd.general.sprites.CharSprite;

public class RedShieldHalo extends ShieldHalo {
    public RedShieldHalo(CharSprite sprite) {
        super(sprite);
        hardlight(0xFF0000);
    }
}
