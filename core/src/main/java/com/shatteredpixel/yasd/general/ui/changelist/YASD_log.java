/*
 *
 *  * Pixel Dungeon
 *  * Copyright (C) 2012-2015 Oleg Dolya
 *  *
 *  * Shattered Pixel Dungeon
 *  * Copyright (C) 2014-2019 Evan Debenham
 *  *
 *  * Yet Another Shattered Dungeon
 *  * Copyright (C) 2014-2020 Samuel Braithwaite
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.general.ui.changelist;

import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.ChangesScene;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.ui.Icons;
import com.shatteredpixel.yasd.general.ui.Window;

import java.util.ArrayList;

public class YASD_log {
	
	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
		add_v0_1_0_Changes(changeInfos);
	}

	public static void add_v0_1_0_Changes(ArrayList<ChangeInfo> changeInfos){
		ChangeInfo changes = new ChangeInfo( "0.1.0 - release", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"),false,null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);
		
		changes.addButton(new ChangeButton(Icons.get(Icons.COMPASS), "Equipment System Reworked", "The equipment system has been completely overhauled:\n" +
				"_-_ The hero now has 5 equip slots, and Wands must be equipped\n" +
				"_-_ Each slot can hold a weapon, armour, a wand, a ring or an artifact\n" +
				"_-_ When using multiple weapons, you will attack with one after the other, and strength requirements will be increased\n" +
				"_-_ When using multiple armours, all defense rolls will be added together and all glyphs will proc from left to right, and strength requirements will be increased."));

		changes.addButton(new ChangeButton(Icons.get(Icons.WARNING), "Upgrade Limits", "All items are capped at +3"));
		
		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"),false,null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.DEPTH), "Morale System", "A Darkest PD style \"Morale\" system has been implemented:\n" +
				"_-_ Taking large amounts of damage, starving and some other things will reduce hero's Morale\n" +
				"_-_ Low Morale will cause your accuracy, evasion and Wand charge speed to be reduced\n" +
				"_-_ You can increase Morale a little by eating or leveling up, and a lot through Scrolls of Lullaby and Beer or Whiskey\n" +
				"_-_ Be careful though, as Beer and Whiskey will also permanently increase the rate at which the hero loses morale"));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_LIFE_DRAIN, null), "Wand of Life Drain", "Wand of Life Drain from YAPD has been added:\n" +
				"_-_ Functionality is basically the same as YAPD\n" +
				"_-_ Consumes all charges, heavy damage, heals the hero if the enemy is not undead"));
	}
	
	
}
