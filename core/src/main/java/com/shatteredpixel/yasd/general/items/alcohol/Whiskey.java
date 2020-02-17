package com.shatteredpixel.yasd.general.items.alcohol;

import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class Whiskey extends Alcohol {
    {
        image = ItemSpriteSheet.WHISKEY;
        MoraleGain = 7.5f;
        drunkTurns = 200f;
    }

    @Override
    public int price() {
        return 50*quantity;
    }
}
