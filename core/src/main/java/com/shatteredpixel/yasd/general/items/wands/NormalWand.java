/*
 *
 *   Pixel Dungeon
 *   Copyright (C) 2012-2015 Oleg Dolya
 *
 *   Shattered Pixel Dungeon
 *   Copyright (C) 2014-2019 Evan Debenham
 *
 *   Yet Another Shattered Dungeon
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
import com.shatteredpixel.yasd.general.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class NormalWand extends DamageWand {

	public NormalWand() {
		initStats();
	}

	@Override
	public int image() {
		switch (element) {
			case PHYSICAL:
				return ItemSpriteSheet.THROWING_KNIFE;
			case MAGICAL:
				return ItemSpriteSheet.WAND_MAGIC_MISSILE;
			case EARTH:
				return ItemSpriteSheet.WAND_LIVING_EARTH;
			case GRASS:
				return ItemSpriteSheet.WAND_REGROWTH;
			case STONE:
				return ItemSpriteSheet.WAND_STENCH;
			case SHARP:
				return ItemSpriteSheet.SHURIKEN;
			case FIRE:
				return ItemSpriteSheet.WAND_FIREBOLT;
			case DESTRUCTION:
				return ItemSpriteSheet.WAND_DISINTEGRATION;
			case ACID:
				return ItemSpriteSheet.WAND_ACID;
			case DRAIN:
				return ItemSpriteSheet.WAND_LIFE_DRAIN;
			case WATER:
				return ItemSpriteSheet.WAND_FLOW;
			case COLD:
				return ItemSpriteSheet.WAND_FROST;
			case TOXIC:
				return ItemSpriteSheet.WAND_THORNVINES;
			case CONFUSION:
				return ItemSpriteSheet.WAND_CONFUSION;
			case AIR:
				return ItemSpriteSheet.WAND_AIR;
			case SHOCK:
				return ItemSpriteSheet.WAND_LIGHTNING;
			case LIGHT:
				return ItemSpriteSheet.WAND_PRISMATIC_LIGHT;
			case SPIRIT:
				return ItemSpriteSheet.WAND_DAMNATION;
		}
		return super.image();
	}

	private Wand initStats() {
		do {
			element = Random.element(Element.values());
		} while (element == Element.META);
		collisionProperties = Ballistica.WONT_STOP;
		if (Random.Int(3) != 0) {
			collisionProperties = collisionProperties | Ballistica.STOP_CHARS;
		}

		if (Random.Int(3) != 0) {
			collisionProperties = collisionProperties | Ballistica.STOP_TARGET;
		}

		if (Random.Int(3) != 0) {
			collisionProperties = collisionProperties | Ballistica.STOP_TERRAIN;
		}
		return this;
	}

	@Override
	public void onZap(Ballistica bolt) {
		Char ch = Actor.findChar(bolt.collisionPos);
		if (ch != null) {

			processSoulMark(ch, chargesPerCast());
			hit(ch);

			ch.sprite.burst(0xFFFFFFFF, level() / 2 + 2);

		} else {
			Dungeon.level.pressCell(bolt.collisionPos);
		}
	}


	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		element.attackProc(damage, attacker, defender);
	}

	@Override
	public float min(float lvl) {
		return 3 + lvl;
	}

	@Override
	public float max(float lvl) {
		return 9 + 4 * lvl;
	}
}
