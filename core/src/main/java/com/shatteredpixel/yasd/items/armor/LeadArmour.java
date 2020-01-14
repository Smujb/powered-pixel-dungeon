package com.shatteredpixel.yasd.items.armor;

import com.shatteredpixel.yasd.sprites.ItemSpriteSheet;

public class LeadArmour extends Armor {
    {
        image = ItemSpriteSheet.ARMOR_BANDED;

        EVA = 0.6f;
        magicalDRFactor = 0.5f;
        DRfactor = 1.2f;
        speedFactor = 2/3f;
    }
    public LeadArmour() {
        super(5);
    }

    @Override
    public int appearance() {
        return 5;
    }
}
