package com.shatteredpixel.yasd.items.armor;

import com.shatteredpixel.yasd.sprites.ItemSpriteSheet;

public class HideArmor extends Armor {
    {
        image = ItemSpriteSheet.ARMOR_HIDE;
        DRfactor = 0.7f;
        EVA = 1.25f;
    }
    public HideArmor() {
        super(2);
    }

    @Override
    public int appearance() {
        return 2;
    }
}
