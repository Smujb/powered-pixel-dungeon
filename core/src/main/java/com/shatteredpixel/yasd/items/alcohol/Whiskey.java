package com.shatteredpixel.yasd.items.alcohol;

import com.shatteredpixel.yasd.sprites.ItemSpriteSheet;

public class Whiskey extends Alcohol {
    {
        image = ItemSpriteSheet.WHISKEY;
        MoraleGain = 7.5f;
        MoraleMultiplier = 2f;
    }

    @Override
    public int price() {
        return 500;
    }
}
