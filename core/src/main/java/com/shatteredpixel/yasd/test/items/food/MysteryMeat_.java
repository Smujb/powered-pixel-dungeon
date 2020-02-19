package com.shatteredpixel.yasd.test.items.food;

import com.shatteredpixel.yasd.ModHandler;
import com.shatteredpixel.yasd.general.items.food.MysteryMeat;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class MysteryMeat_ extends MysteryMeat {
	{
		image = ModHandler.newObject(ItemSpriteSheet.class).RATION;
	}
}
