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

package com.shatteredpixel.yasd.actors.blobs;

import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.Actor;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.buffs.Buff;
import com.shatteredpixel.yasd.actors.buffs.Burning;
import com.shatteredpixel.yasd.effects.BlobEmitter;
import com.shatteredpixel.yasd.effects.particles.FlameParticle;
import com.shatteredpixel.yasd.effects.particles.SmokeParticle;
import com.shatteredpixel.yasd.items.Heap;
import com.shatteredpixel.yasd.items.Item;
import com.shatteredpixel.yasd.items.armor.ClothArmor;
import com.shatteredpixel.yasd.items.armor.LightArmor;
import com.shatteredpixel.yasd.items.food.Food;
import com.shatteredpixel.yasd.items.scrolls.Scroll;
import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.plants.Plant;
import com.shatteredpixel.yasd.scenes.GameScene;
import com.shatteredpixel.yasd.sprites.ItemSprite;

public class Fire extends Blob {

	@Override
	protected void evolve() {

		boolean[] flamable = Dungeon.level.flamable;
		int cell;
		int fire;
		
		Freezing freeze = (Freezing)Dungeon.level.blobs.get( Freezing.class );

		boolean observe = false;

		for (int i = area.left-1; i <= area.right; i++) {
			for (int j = area.top-1; j <= area.bottom; j++) {
				cell = i + j*Dungeon.level.width();
				if (cur[cell] > 0) {
					
					if (freeze != null && freeze.volume > 0 && freeze.cur[cell] > 0){
						freeze.clear(cell);
						off[cell] = cur[cell] = 0;
						continue;
					}

					burn( cell );

					fire = cur[cell] - 1;
					if (fire <= 0 && flamable[cell]) {

						Dungeon.level.destroy( cell );

						observe = true;
						GameScene.updateMap( cell );

					}

				} else if (freeze == null || freeze.volume <= 0 || freeze.cur[cell] <= 0) {

					if (flamable[cell]
							&& (cur[cell-1] > 0
							|| cur[cell+1] > 0
							|| cur[cell-Dungeon.level.width()] > 0
							|| cur[cell+Dungeon.level.width()] > 0)) {
						fire = 4;
						burn( cell );
						area.union(i, j);
					} else {
						fire = 0;
					}

				} else {
					fire = 0;
				}

				volume += (off[cell] = fire);
			}
		}

		if (observe) {
			Dungeon.observe();
		}
	}
	
	public static void burn( int pos ) {
		Char ch = Actor.findChar( pos );
		if (ch != null && !ch.isImmune(Fire.class)) {
			Buff.affect( ch, Burning.class ).reignite( ch );
		}
		
		Heap heap = Dungeon.level.heaps.get( pos );
		if (heap != null) {
			heap.burn();
		}

		Plant plant = Dungeon.level.plants.get( pos );
		if (plant != null){
			plant.wither();
		}
	}

	public static boolean canBurn(Item i) {
		if (i instanceof Scroll ||
				i instanceof Food || //All food can burn
				(i instanceof ClothArmor || i instanceof LightArmor & i.level() < 1) || //Can burn unupgraded cloth or leather armour too ;)
				i instanceof Plant.Seed){
			return true;
		}
		return false;
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		emitter.pour( FlameParticle.FACTORY, 0.03f );
	}
	
	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}
}
