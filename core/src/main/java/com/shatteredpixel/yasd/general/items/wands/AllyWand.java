/*
 *
 *   Pixel Dungeon
 *   Copyright (C) 2012-2015 Oleg Dolya
 *
 *   Shattered Pixel Dungeon
 *   Copyright (C) 2014-2019 Evan Debenham
 *
 *   Powered Pixel Dungeon
 *   Copyright (C) 2014-2020 Samuel Braithwaite
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.general.items.wands;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Amok;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.StatueSprite;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

public class AllyWand extends NormalWand {

	@Override
	protected float getDamageMultiplier() {
		return super.getDamageMultiplier()/2f;
	}

	@Override
	public void onZap(Ballistica bolt) {
		Char ch = Actor.findChar(bolt.collisionPos);

		Sentry sent = null;
		for (Mob m : Dungeon.level.mobs){
			if (m instanceof Sentry && ((Sentry) m).linkable(this)){
				sent = (Sentry) m;
				break;
			}
		}

		//shooting at the guardian
		if (sent != null && sent == ch){
			sent.sprite.centerEmitter().burst(element.particleType(), 8 + level() / 2);
			sent.link(this);
			processSoulMark(sent, chargesPerCast());
			sent.heal(damageRoll(), true);

			//shooting the guardian at a location
		} else if ( sent == null ){

			//create a new guardian
			sent = new Sentry();
			sent.link(this);

			//if the collision pos is occupied (likely will be), then spawn the guardian in the
			//adjacent cell which is closes to the user of the wand.
			if (ch != null){

				ch.sprite.centerEmitter().burst(element.particleType(), 5 + level()/2);

				processSoulMark(ch, chargesPerCast());
				hit(ch);

				int closest = -1;
				boolean[] passable = Dungeon.level.passable();

				for (int n : PathFinder.NEIGHBOURS9) {
					int c = bolt.collisionPos + n;
					if (passable[c] && Actor.findChar( c ) == null
							&& (closest == -1 || (Dungeon.level.trueDistance(c, curUser.pos) < (Dungeon.level.trueDistance(closest, curUser.pos))))) {
						closest = c;
					}
				}

				if (closest == -1){
					curUser.sprite.centerEmitter().burst(element.particleType(), 8 + level()/2);
					return; //do not spawn guardian or detach buff
				} else {
					sent.pos = closest;
					GameScene.add(sent, 1);
					Dungeon.level.occupyCell(sent);
				}

				if (ch.alignment == Char.Alignment.ENEMY || ch.buff(Amok.class) != null) {
					sent.aggro(ch);
				}

			} else {
				sent.pos = bolt.collisionPos;
				GameScene.add(sent, 1);
				Dungeon.level.occupyCell(sent);
			}

			sent.sprite.centerEmitter().burst(element.particleType(), 8 + level()/2);

			//shooting at a location/enemy with no guardian being shot
		} else {

			if (ch != null) {

				ch.sprite.centerEmitter().burst(element.particleType(), 5 + level() / 2);

				processSoulMark(ch, chargesPerCast());
				hit(ch);

				sent.sprite.centerEmitter().burst(element.particleType(), 8 + level() / 2);
				sent.link(this);
				sent.heal(damageRoll()/2, true);
				if (ch.alignment == Char.Alignment.ENEMY || ch.buff(Amok.class) != null) {
					sent.aggro(ch);
				}

			} else {
				Dungeon.level.pressCell(bolt.collisionPos);
			}
		}

	}

	public static class Sentry extends Mob {

		private int wandLvl = 0;
		private Element element = Element.MAGICAL;
		private float dmgFactor = 1f;
		private Emitter emitter = null;

		{
			spriteClass = StatueSprite.class;

			state = HUNTING;

			WANDERING = new Following();
		}

		@Override
		public int attackProc(Char enemy, int damage) {
			sprite.emitter().burst(element.particleType(), level/3 + 1);
			return super.attackProc(enemy, damage);
		}

		public void link(@NotNull AllyWand wand) {
			level = wandLvl = Math.round(wand.actualLevel());
			updateHT(false);
			HP = Math.max(HP, HT/5);
			element = wand.element;
			alignment = wand.curUser.alignment;
			dmgFactor = wand.getDamageMultiplier();
			setupEmitter();
		}

		public boolean linkable(@NotNull AllyWand wand) {
			return alignment == wand.curUser.alignment && element == wand.element;
		}

		private void setupEmitter() {
			Emitter.Factory factory = element.particleType();
			if (emitter == null && sprite != null && factory != null) {
				emitter = sprite.emitter();
				emitter.pour(factory, 0.1f);
			}
		}

		@Override
		protected boolean act() {
			setupEmitter();
			return super.act();
		}

		@Override
		public void die(DamageSrc cause) {
			super.die(cause);
			if (emitter != null) {
				emitter.kill();
			}
			emitter = null;
		}

		@Override
		public int damageRoll() {
			return Random.NormalIntRange((int) NormalWand.realMin(wandLvl, 1), (int) NormalWand.realMax(wandLvl, 1, dmgFactor));
		}

		@Override
		public Element elementalType() {
			return element;
		}

		private static final String WAND_LEVEL = "wand-level";
		private static final String DMG_FACTOR = "dmg-factor";
		private static final String ELEMENT = "element";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(WAND_LEVEL, wandLvl);
			bundle.put(DMG_FACTOR, dmgFactor);
			bundle.put(ELEMENT, element);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			wandLvl = bundle.getInt(WAND_LEVEL);
			dmgFactor = bundle.getFloat(DMG_FACTOR);
			element = bundle.getEnum(ELEMENT, Element.class);
		}
	}
}
