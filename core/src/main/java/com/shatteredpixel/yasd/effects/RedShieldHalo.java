package com.shatteredpixel.yasd.effects;

import com.shatteredpixel.yasd.sprites.CharSprite;

public class RedShieldHalo extends ShieldHalo {
    public RedShieldHalo(CharSprite sprite) {
        super(sprite);
        hardlight(0xFF0000);
    }
}
