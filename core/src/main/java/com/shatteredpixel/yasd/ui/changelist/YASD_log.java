/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.yasd.ui.changelist;

import com.shatteredpixel.yasd.Assets;
import com.shatteredpixel.yasd.Badges;
import com.shatteredpixel.yasd.Challenges;
import com.shatteredpixel.yasd.effects.BadgeBanner;
import com.shatteredpixel.yasd.items.Ankh;
import com.shatteredpixel.yasd.items.Honeypot;
import com.shatteredpixel.yasd.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.yasd.items.artifacts.DriedRose;
import com.shatteredpixel.yasd.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.yasd.items.food.Blandfruit;
import com.shatteredpixel.yasd.items.potions.PotionOfHealing;
import com.shatteredpixel.yasd.items.quest.CorpseDust;
import com.shatteredpixel.yasd.items.quest.Embers;
import com.shatteredpixel.yasd.items.rings.RingOfWealth;
import com.shatteredpixel.yasd.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.yasd.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.yasd.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.yasd.items.scrolls.ScrollOfTerror;
import com.shatteredpixel.yasd.items.wands.WandOfLivingEarth;
import com.shatteredpixel.yasd.items.wands.WandOfRegrowth;
import com.shatteredpixel.yasd.items.wands.WandOfTransfusion;
import com.shatteredpixel.yasd.items.wands.WandOfWarding;
import com.shatteredpixel.yasd.items.weapon.melee.Gauntlet;
import com.shatteredpixel.yasd.items.weapon.missiles.Shuriken;
import com.shatteredpixel.yasd.items.weapon.missiles.Tomahawk;
import com.shatteredpixel.yasd.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.scenes.ChangesScene;
import com.shatteredpixel.yasd.sprites.AlbinoSprite;
import com.shatteredpixel.yasd.sprites.CharSprite;
import com.shatteredpixel.yasd.sprites.ItemSprite;
import com.shatteredpixel.yasd.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.sprites.SwarmSprite;
import com.shatteredpixel.yasd.ui.BuffIndicator;
import com.shatteredpixel.yasd.ui.Icons;
import com.shatteredpixel.yasd.ui.Window;
import com.watabou.noosa.Image;

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
		
		changes.addButton( new ChangeButton(Icons.get(Icons.INFO), "Developer Commentary",
				"_-_ Released August 12th, 2019\n" +
						"_-_ First release\n" +
						"_-_ 25 ays after Shattered Pixel Dungeon v0.7.4" +
						"\n" +
						"Dev commentary will be added here in the future."));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.CHALLENGE_ON), "Challenges",
				"_-_ Added 11 new challenges\n" +
						"_-_ Challenges are unlocked by default"));
		changes.addButton(new ChangeButton(new Image(Assets.HUNTRESS, 0, 90, 12, 15),"Huntress","All hero classes are unlocked by default"));
		
		changes = new ChangeInfo("Challenges",false,null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);
		
		changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_OFF),Messages.get(Challenges.class,"amnesia"),Messages.get(Challenges.class,"amnesia_desc")));
	}
	
	
}
