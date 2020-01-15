package com.shatteredpixel.yasd.items.armor;

import com.shatteredpixel.yasd.sprites.ItemSpriteSheet;

public class DiscArmor extends Armor{
    {
        image = ItemSpriteSheet.ARMOR_DISC;
        DRfactor = 0.7f;
        EVA = 1.35f;
    }
    public DiscArmor() {
        super(4);
    }

    @Override
    public int appearance() {
        return 4;
    }
}
