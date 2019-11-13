package com.shatteredpixel.yasd.items.alcohol;

import com.shatteredpixel.yasd.sprites.ItemSpriteSheet;

public class Beer extends Alcohol {
    {
        image = ItemSpriteSheet.BEER;
        MoraleGain = 5f;
        MoraleMultiplier = 1.5f;
    }

    @Override
    public int price() {
        return 30*quantity;
    }
}
