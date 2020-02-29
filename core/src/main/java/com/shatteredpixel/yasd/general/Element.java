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

package com.shatteredpixel.yasd.general;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Burning;
import com.shatteredpixel.yasd.general.actors.buffs.Ooze;
import com.shatteredpixel.yasd.general.actors.buffs.Weakness;
import com.shatteredpixel.yasd.general.effects.Beam;
import com.shatteredpixel.yasd.general.effects.Lightning;
import com.shatteredpixel.yasd.general.effects.MagicMissile;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.items.weapon.missiles.ThrowingKnife;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.MissileSprite;
import com.shatteredpixel.yasd.general.tiles.DungeonTilemap;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public enum Element {
	/*
	The purpose of this file is to make it easier to add types of damage to the game. Eventually this will replace the "src" parameter of damage().
	 */
	PHYSICAL( false ),
	RANGED( false ),
	NONE( true ),
	MAGICAL( true ),
	DESTRUCTION( true ),
	NATURAL( true ),
	FIRE( true),
	WATER( true ),
	EARTH( false ),
	GRASS( false ),
	AIR( true ),
	ACID( true ),
	ELECTRIC( true ),
	CONFUSION( true ),
	VENOM( true ),
	HOLY( true ),
	DRAIN( true ),
	DARK( true );


	public int attackProc(int damage, Char attacker, Char defender) {
		switch (this) {
			case PHYSICAL:
				break;
			case FIRE:
				Buff.affect(defender, Burning.class).reignite(defender);
				break;
			case ACID:
				Buff.affect(defender, Ooze.class).set(20f);
				break;
			case DARK:
				Buff.affect(defender, Weakness.class, Weakness.DURATION);
				break;
			case DRAIN:
				if ( attacker.isAlive() && Dungeon.level.adjacent(attacker.pos, defender.pos)) {

					int healed = damage/3;

					if (healed > 0) {

						attacker.HP += Math.min(attacker.missingHP(), healed);

						if( attacker.sprite.visible ) {
							attacker.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
						}
					}
				}
				break;
			case ELECTRIC:
				if (Dungeon.level.liquid()[defender.pos] && !defender.flying) {
					damage *= 1.5f;
				}
				break;
		}

		if (attacker.alignment == Char.Alignment.ENEMY) {
			switch (Dungeon.difficulty) {
				case 1://Easy = -25% damage
					damage *= 0.75f;
					break;
				case 2: default://Medium = normal damage
					break;
				case 3://Hard = +25% damage
					damage *= 1.25f;
					break;
			}
		}

		if (Dungeon.hero.fieldOfView[defender.pos] || Dungeon.hero.fieldOfView[attacker.pos]) {
			Sample.INSTANCE.play(Assets.SND_HIT, 1, 1, Random.Float(0.8f, 1.25f));
		}
		return damage;
	}

	public int defenseProc(int damage, Char attacker, Char defender) {
		//damage = defender.defenseProc(attacker, damage, Element.PHYSICAL);
		return damage;
	}

	public int affectDamage(Char ch, int damage) {
		damage = Math.max(damage - ch.drRoll(this), 0);
		return damage;
	}

	public void FX(Char ch, int cell, Callback attack) {

		switch (this) {
			case MAGICAL:
			case NONE:
			case PHYSICAL:
				attack.call();
				break;
			case RANGED:
				((MissileSprite)ch.sprite.parent.recycle( MissileSprite.class )).
						reset( ch.pos, cell, new ThrowingKnife(), attack );
				break;
			case DESTRUCTION:
				ch.sprite.parent.add(
						new Beam.DeathRay(ch.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(cell)));
				attack.call();
				break;
			case NATURAL:
				break;
			case FIRE:
				MagicMissile.boltFromChar( ch.sprite.parent,
						MagicMissile.FIRE,
						ch.sprite,
						cell,
						attack);
				break;
			case WATER:
				MagicMissile.boltFromChar( ch.sprite.parent,
						MagicMissile.WATER_CONE,
						ch.sprite,
						cell,
						attack);
				break;
			case EARTH:
				MagicMissile.boltFromChar( ch.sprite.parent,
						MagicMissile.EARTH,
						ch.sprite,
						cell,
						attack);
				break;
			case GRASS:
				MagicMissile.boltFromChar( ch.sprite.parent,
						MagicMissile.FOLIAGE_CONE,
						ch.sprite,
						cell,
						attack);
				break;
			case CONFUSION:
				MagicMissile.boltFromChar( ch.sprite.parent,
						MagicMissile.RAINBOW,
						ch.sprite,
						cell,
						attack);
				break;
			case VENOM:
				MagicMissile.boltFromChar( ch.sprite.parent,
						MagicMissile.POISON,
						ch.sprite,
						cell,
						attack);
				break;
			case AIR: case HOLY:
				ch.sprite.parent.add(
						new Beam.LightRay(ch.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(cell)));
				attack.call();
				break;
			case ACID:
				MagicMissile.boltFromChar( ch.sprite.parent,
						MagicMissile.CORROSION,
						ch.sprite,
						cell,
						attack);
				break;
			case ELECTRIC:
				ch.sprite.parent.add(
						new Lightning(ch.pos, DungeonTilemap.raisedTileCenterToWorld(cell), null));//No callback as damaging after lightning anim finishes looks messy
				attack.call();
				break;
			case DARK: case DRAIN:
				MagicMissile.boltFromChar( ch.sprite.parent,
						MagicMissile.SHADOW,
						ch.sprite,
						cell,
						attack);
				break;
		}
	}

	Element(boolean magical) {
		this.magical = magical;
	}

	private boolean magical;

	public boolean isMagical() {
		return magical;
	}

	public String label() {
		return Messages.get(this, this.name());
	}
}
