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
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Barrier;
import com.shatteredpixel.yasd.general.actors.buffs.Bleeding;
import com.shatteredpixel.yasd.general.actors.buffs.Bless;
import com.shatteredpixel.yasd.general.actors.buffs.Blindness;
import com.shatteredpixel.yasd.general.actors.buffs.BlobImmunity;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Burning;
import com.shatteredpixel.yasd.general.actors.buffs.Corrosion;
import com.shatteredpixel.yasd.general.actors.buffs.Drowsy;
import com.shatteredpixel.yasd.general.actors.buffs.FireImbue;
import com.shatteredpixel.yasd.general.actors.buffs.Frost;
import com.shatteredpixel.yasd.general.actors.buffs.FrostImbue;
import com.shatteredpixel.yasd.general.actors.buffs.Haste;
import com.shatteredpixel.yasd.general.actors.buffs.Healing;
import com.shatteredpixel.yasd.general.actors.buffs.Hex;
import com.shatteredpixel.yasd.general.actors.buffs.Levitation;
import com.shatteredpixel.yasd.general.actors.buffs.Light;
import com.shatteredpixel.yasd.general.actors.buffs.MagicCharge;
import com.shatteredpixel.yasd.general.actors.buffs.Paralysis;
import com.shatteredpixel.yasd.general.actors.buffs.Poison;
import com.shatteredpixel.yasd.general.actors.buffs.Roots;
import com.shatteredpixel.yasd.general.actors.buffs.Shadows;
import com.shatteredpixel.yasd.general.actors.buffs.Slow;
import com.shatteredpixel.yasd.general.actors.buffs.ToxicImbue;
import com.shatteredpixel.yasd.general.actors.buffs.Vertigo;
import com.shatteredpixel.yasd.general.actors.buffs.Vulnerable;
import com.shatteredpixel.yasd.general.actors.buffs.Weakness;
import com.shatteredpixel.yasd.general.actors.buffs.Wet;
import com.shatteredpixel.yasd.general.items.armor.glyphs.Viscosity;
import com.shatteredpixel.yasd.general.items.potions.PotionOfHealing;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.plants.Earthroot;
import com.shatteredpixel.yasd.general.plants.Sungrass;
import com.watabou.utils.Random;

public class SupportWand extends NormalWand {

	@Override
	protected float getDamageMultiplier() {
		return super.getDamageMultiplier()/2f;
	}

	@Override
	public void onZap(Ballistica bolt) {
		Char ch = Actor.findChar(bolt.collisionPos);
		if (ch != null && ch != curUser) {

			processSoulMark(ch, chargesPerCast());
			hit(ch);
			ch.sprite.burst(0xFFFFFFFF, level() / 2 + 2);


		} else {
			Dungeon.level.pressCell(bolt.collisionPos);
		}
	}


	@Override
	public int hit(Char enemy, int damage) {
		if (enemy.alignment == Char.Alignment.NEUTRAL) return 0;
		boolean attack = enemy.alignment != curUser.alignment;
		damage = proc(enemy, attack, damage);
		if (attack) {
			Char.DamageSrc src = new Char.DamageSrc(element, this);
			damage = enemy.defenseProc(curUser, damage, src);
			enemy.damage(damage, src);
		} else {
			if (damage > 0) {
				enemy.heal(damage, true);
			}
		}
		return damage;
	}

	private int proc(Char receiver, boolean attack, int power) {
		switch (element) {
			case PHYSICAL:
				if (attack) {
					Buff.affect(receiver, Viscosity.DeferedDamage.class).prolong(receiver.HT/5);
				} else {
					Buff.affect(receiver, Healing.class).setHeal(receiver.HT/2, 0.2f, 0);
				}
				break;
			case MAGICAL:
				if (attack) {
					//Refresh, but don't extend the buff.
					Buff.affect(curUser, MagicCharge.class, MagicCharge.DURATION).setLevel(level());
				} else {
					Buff.affect(receiver, Sungrass.Health.class).boost(receiver.HT);
				}
				break;
			case EARTH:
				if (attack) {
					Buff.affect(receiver, Slow.class, Slow.DURATION);
				} else {
					Buff.affect(receiver, Earthroot.Armor.class).level(receiver.HT/2);
				}
				break;
			case GRASS:
				if (attack) {
					Buff.affect(receiver, Roots.class, Slow.DURATION*3);
				} else {
					Buff.affect(receiver, Bless.class, Bless.DURATION);
				}
				break;
			case STONE:
				if (attack) {
					if (Random.Int(2) == 0) {
						Buff.affect(receiver, Paralysis.class, Paralysis.DURATION);
					}
				} else {
					PotionOfHealing.cure(receiver);
				}
				break;
			case SHARP:
				if (attack) {
					Buff.affect(receiver, Bleeding.class).set(power * 2);
				} else {
					return power * 2;
				}
				break;
			case FIRE:
				if (attack) {
					Buff.affect(receiver, Burning.class).reignite(receiver, Burning.DURATION * 3);
				} else {
					Buff.affect(receiver, FireImbue.class).set(FireImbue.DURATION/2);
				}
				break;
			case DESTRUCTION:
				if (attack) {
					Buff.affect(receiver, Vulnerable.class, Vulnerable.DURATION);
				} else {
					Buff.affect(receiver, BlobImmunity.class, BlobImmunity.DURATION);
				}
				break;
			case ACID:
				if (attack) {
					Buff.affect(receiver, Corrosion.class).set(3f, Corrosion.defaultStrength(Dungeon.getScaleFactor()));
				} else {
					Buff.affect(receiver, BlobImmunity.class, BlobImmunity.DURATION);
				}
				break;
			case DRAIN:
				if (attack) {
					curUser.heal(power);
				} else {
					Buff.affect(curUser, Barrier.class).setShield(power);
					Buff.affect(receiver, Barrier.class).setShield(power);
				}
				break;
			case WATER:
				if (attack) {
					Buff.affect(receiver, Wet.class, Wet.DURATION * 10);
				} else {
					Buff.affect(receiver, Haste.class, Haste.DURATION);
				}
				break;
			case COLD:
				if (attack) {
					Buff.affect(receiver, Frost.class, 6f);
				} else {
					Buff.affect(receiver, FrostImbue.class, FrostImbue.DURATION);
				}
				break;
			case TOXIC:
				if (attack) {
					Buff.affect(receiver, Poison.class).set(4 + Dungeon.getScaleFactor()/2f);
				} else {
					Buff.affect(receiver, ToxicImbue.class).set(ToxicImbue.DURATION);
				}
				break;
			case CONFUSION:
				if (attack) {
					Buff.affect(receiver, Vertigo.class, Vertigo.DURATION * 3);
				} else {
					Buff.affect(receiver, Drowsy.class);
				}
			case AIR:
				if (attack) {
					Buff.affect(receiver, Hex.class, Hex.DURATION);
				} else {
					Buff.affect(receiver, Levitation.class, Levitation.DURATION);
				}
				break;
			case SHOCK:
				if (Dungeon.level.liquid(receiver.pos)) {
					power *= 3;
				}
				break;
			case LIGHT:
				if (attack) {
					Buff.affect(receiver, Blindness.class, Vertigo.DURATION * 3);
				} else {
					Buff.affect(curUser, Light.class, Light.DURATION/2f);
				}
				break;
			case SPIRIT:
				if (attack) {
					Buff.affect(receiver, Weakness.class, Weakness.DURATION);
				} else {
					Buff.affect(receiver, Shadows.class, Shadows.DURATION);
				}
				break;
		}
		return power;
	}
}
