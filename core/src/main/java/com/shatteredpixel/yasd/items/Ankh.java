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

package com.shatteredpixel.yasd.items;

//import com.shatteredpixel.yasd.Assets;
import com.shatteredpixel.yasd.Assets;
import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.Holiday;
import com.shatteredpixel.yasd.Statistics;
import com.shatteredpixel.yasd.actors.Actor;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.buffs.Buff;
import com.shatteredpixel.yasd.actors.buffs.Paralysis;
import com.shatteredpixel.yasd.actors.hero.Hero;
//import com.shatteredpixel.yasd.effects.CellEmitter;
//import com.shatteredpixel.yasd.effects.Speck;
//import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.effects.CellEmitter;
import com.shatteredpixel.yasd.effects.Flare;
import com.shatteredpixel.yasd.effects.Speck;
import com.shatteredpixel.yasd.items.artifacts.DriedRose;
import com.shatteredpixel.yasd.items.potions.PotionOfHealing;
import com.shatteredpixel.yasd.items.wands.DamageWand;
import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.sprites.ItemSprite.Glowing;
import com.shatteredpixel.yasd.sprites.ItemSpriteSheet;
//import com.shatteredpixel.yasd.utils.GLog;
//import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.yasd.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class Ankh extends Item {

	//public static final String AC_BLESS = "BLESS";

	{
		image = ItemSpriteSheet.ANKH;

		//bones = true;
	}

	//private Boolean blessed = false;
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions(hero);
		DewVial vial = hero.belongings.getItem(DewVial.class);
		/*if (vial != null && vial.isFull() && !blessed)
			actions.add( AC_BLESS );*/
		return actions;
	}

	@Override
	public void execute( final Hero hero, String action ) {

		super.execute( hero, action );

		/*if (action.equals( AC_BLESS )) {

			DewVial vial = hero.belongings.getItem(DewVial.class);
			if (vial != null){
				blessed = true;
				vial.empty();
				GLog.p( Messages.get(this, "bless") );
				hero.spend( 1f );
				hero.busy();


				Sample.INSTANCE.play( Assets.SND_DRINK );
				CellEmitter.get(hero.pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
				hero.sprite.operate( hero.pos );
			}
		}*/
	}

	/*@Override
	public String desc() {
		if (blessed)
			return Messages.get(this, "desc_blessed");
		else
			return super.desc();
	}*/

	/*public Boolean isBlessed(){
		return blessed;
	}*/

	private static final Glowing WHITE = new Glowing( 0xFFFFCC );

	@Override
	public Glowing glowing() {
		return WHITE;
	}

	public void revive(Char toRevive) {
		revive(toRevive, this);
	}

	public static void revive(Char toRevive, Ankh ankh) {
		toRevive.HP = toRevive.HT;

		//ensures that you'll get to act first in almost any case, to prevent reviving and then instantly dieing again.
		PotionOfHealing.cure(toRevive);
		Buff.detach(toRevive, Paralysis.class);
		toRevive.spend(-toRevive.cooldown());
		if (Dungeon.hero.fieldOfView[toRevive.pos]) {
			new Flare(8, 32).color(0xFFFF66, true).show(toRevive.sprite, 2f);
			CellEmitter.get(toRevive.pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
		}

		if (toRevive.hasBelongings() && ankh != null) {
			ankh.detach(toRevive.belongings.backpack);
			toRevive.belongings.uncurseEquipped();
		}

		if (toRevive == Dungeon.hero) {
			Sample.INSTANCE.play(Assets.SND_TELEPORT);
			GLog.w(Messages.get(Ankh.class, "revive"));
			Statistics.ankhsUsed++;
		}

		for (Char ch : Actor.chars()){
			if (ch instanceof DriedRose.GhostHero){
				((DriedRose.GhostHero) ch).sayAnhk();
				return;
			}
		}
	}

	private static final String BLESSED = "blessed";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		//bundle.put( BLESSED, blessed );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		//blessed	= bundle.getBoolean( BLESSED );
	}
	
	@Override
	public int price() {
		return 100 * quantity;
	}
}
