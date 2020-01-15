package com.shatteredpixel.yasd.items.alcohol;

import com.shatteredpixel.yasd.sprites.ItemSpriteSheet;

public class Beer extends Alcohol {
    {
        image = ItemSpriteSheet.BEER;
        MoraleGain = 5f;
        drunkTurns = 100f;
    }

    @Override
    public int price() {
        return 30*quantity;
    }
}
