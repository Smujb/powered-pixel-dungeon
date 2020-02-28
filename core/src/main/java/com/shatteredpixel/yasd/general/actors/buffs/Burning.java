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

package com.shatteredpixel.yasd.general.actors.buffs;

import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.blobs.Blob;
import com.shatteredpixel.yasd.general.actors.blobs.Fire;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Thief;
import com.shatteredpixel.yasd.general.effects.particles.ElmoParticle;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.food.ChargrilledMeat;
import com.shatteredpixel.yasd.general.items.food.FrozenCarpaccio;
import com.shatteredpixel.yasd.general.items.food.MysteryMeat;
import com.shatteredpixel.yasd.general.items.scrolls.Scroll;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Burning extends Buff implements Hero.Doom {
	
	private static final float DURATION = 8f;
	
	private float left;
	
	//for tracking burning of hero items
	private int burnIncrement = 0;
	
	private static final String LEFT	= "left";
	private static final String BURN	= "burnIncrement";

	{
		type = buffType.NEGATIVE;
		announced = true;
	}
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEFT, left );
		bundle.put( BURN, burnIncrement );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		left = bundle.getFloat( LEFT );
		burnIncrement = bundle.getInt( BURN );
	}

	@Override
	public boolean act() {
		
		if (target.isAlive() && !target.isImmune(getClass())) {
			
			int damage = (int) ( Random.NormalIntRange( 1, 3 + Dungeon.getScaleFactor() ) * 1.5f );
			Buff.detach( target, Chill.class);

			if (target instanceof Hero) {
				
				Hero hero = (Hero)target;

				hero.damage( damage, this, Element.FIRE );
				burnIncrement++;

				//at 4+ turns, there is a (turns-3)/3 chance an item burns
				if (Random.Int(3) < (burnIncrement - 3)){
					burnIncrement = 0;

					ArrayList<Item> burnable = new ArrayList<>();
					//does not reach inside of containers
					for (Item i : hero.belongings.backpack.items){
						if (Fire.canBurn(i)) {
							burnable.add(i);
						}
					}

					if (!burnable.isEmpty()){
						Item toBurn = Random.element(burnable).detach(hero.belongings.backpack);
						GLog.w( Messages.get(this, "burnsup", Messages.capitalize(toBurn.toString())) );
						if (toBurn instanceof MysteryMeat){
							ChargrilledMeat steak = new ChargrilledMeat();
							if (!steak.collect( hero.belongings.backpack )) {
								Dungeon.level.drop( steak, hero.pos ).sprite.drop();
							}
						} else if (toBurn instanceof FrozenCarpaccio) {//Frozen Carpaccio turns back into mystery meat ;)
							MysteryMeat meat = new MysteryMeat();
							if (!meat.collect( hero.belongings.backpack )) {
								Dungeon.level.drop( meat, hero.pos ).sprite.drop();
							}
						}
						Heap.burnFX( hero.pos );
					}
				}
				
			} else {
				target.damage( damage, this, Element.FIRE );
			}

			if (target instanceof Thief) {

				Item item = ((Thief) target).item;

				if (item instanceof Scroll) {
					target.sprite.emitter().burst( ElmoParticle.FACTORY, 6 );
					((Thief)target).item = null;
				} else if (item instanceof MysteryMeat) {
					target.sprite.emitter().burst( ElmoParticle.FACTORY, 6 );
					((Thief)target).item = new ChargrilledMeat();
				}

			}

		} else {

			detach();
		}
		
		if (Dungeon.level.flammable()[target.pos] && Blob.volumeAt(target.pos, Fire.class) == 0) {
			GameScene.add( Blob.seed( target.pos, 4, Fire.class ) );
		}
		
		spend( TICK );
		left -= TICK;
		
		if (left <= 0 ||
			(Dungeon.level.liquid()[target.pos] && !target.flying)) {
			
			detach();
		}
		
		return true;
	}
	
	public void reignite( Char ch ) {
		reignite( ch, DURATION );
	}
	
	public void reignite( Char ch, float duration ) {
		left = duration;
	}
	
	@Override
	public int icon() {
		return BuffIndicator.FIRE;
	}

	@Override
	public void fx(boolean on) {
		if (on) target.sprite.add(CharSprite.State.BURNING);
		else target.sprite.remove(CharSprite.State.BURNING);
	}

	@Override
	public String heroMessage() {
		return Messages.get(this, "heromsg");
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns(left));
	}

	@Override
	public void onDeath() {
		
		Badges.validateDeathFromFire();
		
		Dungeon.fail( getClass() );
		GLog.n( Messages.get(this, "ondeath") );
	}
}
