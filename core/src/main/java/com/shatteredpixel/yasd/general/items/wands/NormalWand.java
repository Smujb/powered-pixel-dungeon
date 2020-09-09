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

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.blobs.Blob;
import com.shatteredpixel.yasd.general.actors.blobs.ConfusionGas;
import com.shatteredpixel.yasd.general.actors.blobs.CorrosiveGas;
import com.shatteredpixel.yasd.general.actors.blobs.Electricity;
import com.shatteredpixel.yasd.general.actors.blobs.Fire;
import com.shatteredpixel.yasd.general.actors.blobs.Freezing;
import com.shatteredpixel.yasd.general.actors.blobs.Miasma;
import com.shatteredpixel.yasd.general.actors.blobs.ParalyticGas;
import com.shatteredpixel.yasd.general.actors.blobs.Regrowth;
import com.shatteredpixel.yasd.general.actors.blobs.StormCloud;
import com.shatteredpixel.yasd.general.actors.blobs.ToxicGas;
import com.shatteredpixel.yasd.general.actors.buffs.Corrosion;
import com.shatteredpixel.yasd.general.effects.MagicMissile;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.yasd.general.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.general.levels.terrain.KindOfTerrain;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.levels.traps.GrippingTrap;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

public abstract class NormalWand extends DamageWand {
	private static final int BASE_INITIAL_CHARGES = 3;
	private static final int BASE_CHARGES_PER_CAST = 1;

	private int initialCharges;
	private int chargesPerCast;

	private static final String ELEMENT = "element";
	private static final String INITIAL_CHARGES = "initial-charges";
	private static final String CHARGES_PER_CAST = "charges-per-cast";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(ELEMENT, element);
		bundle.put(INITIAL_CHARGES, initialCharges);
		bundle.put(CHARGES_PER_CAST, chargesPerCast);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		element = bundle.getEnum(ELEMENT, Element.class);
		initialCharges = bundle.getInt(INITIAL_CHARGES);
		chargesPerCast = bundle.getInt(CHARGES_PER_CAST);
		updateLevel();
	}

	public void resetStats() {
		initialCharges = curCharges = maxCharges = BASE_INITIAL_CHARGES;
		chargesPerCast = BASE_CHARGES_PER_CAST;
	}

	@Override
	public int image() {
		switch (element) {
			case PHYSICAL:
				return ItemSpriteSheet.WAND_PLASMA;
			case MAGICAL:
				return ItemSpriteSheet.WAND_MAGIC_MISSILE;
			case EARTH:
				return ItemSpriteSheet.WAND_LIVING_EARTH;
			case GRASS:
				return ItemSpriteSheet.WAND_REGROWTH;
			case STONE:
				return ItemSpriteSheet.WAND_STENCH;
			case FIRE:
				return ItemSpriteSheet.WAND_FIREBOLT;
			case DESTRUCTION:
				return ItemSpriteSheet.WAND_DISINTEGRATION;
			case ACID:
				return ItemSpriteSheet.WAND_ACID;
			case SHARP:
				return ItemSpriteSheet.WAND_LIFE_DRAIN;
			case DRAIN:
				return ItemSpriteSheet.WAND_TRANSFUSION;
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

	@Override
	public String name() {
		return getName() == null ? name(false) : getName();
	}
	public String name(boolean staff) {
		String base = staff ? Messages.get(MagesStaff.class, "name_wand") : Messages.get(Wand.class, "name");
		return Messages.format(base, Messages.get(NormalWand.class, element.name() + "_name", Messages.get(this, "name")));
	}

	@Override
	public String desc() {
		return Messages.get(NormalWand.class, element.name() + "_desc");
	}

	@Override
	protected int initialCharges() {
		return initialCharges;
	}

	@NotNull
	public static NormalWand createRandom() {
		NormalWand wand;
		switch (Random.Int(6)) {
			default:
				wand = new BoltWand();
				break;
			case 2: case 3:
				wand = new AOEWand();
				break;
			case 4:
				wand = new SupportWand();
				break;
			case 5:
				wand = new AllyWand();
				break;

		}
		return wand.initStats();
	}

	protected NormalWand initStats() {
		resetStats();
		do {
			element = Random.element(Element.values());
		} while (element == Element.META);
		if (Random.Int(3) == 0) {
			chargesPerCast += Random.Int(3);
		}
		if (Random.Int(3) == 0) {
			initialCharges += Random.Int(3);
		}
		return this;
	}

	protected float getDamageMultiplier() {
		float multiplier = 1f;
		switch (element) {
			case PHYSICAL:
				multiplier *= 1.4f;
				break;
			case MAGICAL:
				multiplier *= 1.2f;
				break;
		}
		if (initialCharges > 2) {
			multiplier *= Math.pow(0.95, initialCharges -2);
		}
		return multiplier;
	}

	private static boolean override(Element element, int cell) {
		if (element == Element.COLD || element == Element.WATER || element == Element.SHOCK) {
			return Dungeon.level.liquid(cell);
		} else if (element == Element.FIRE || element == Element.DESTRUCTION) {
			return Dungeon.level.flammable(cell);
		}
		return false;
	}

	void affectCell(int userPos, int cell) {
		//only affect cells directly near caster if they are flammable
		//TODO Air, Sharp and Drain
		final int defaultAmt = 5 + chargesPerCast()*2;
		if (!(cell == userPos ||Dungeon.level.adjacent(userPos, cell))
				|| override(element, cell)) {
			switch (element) {
				case EARTH:
					break;
				case GRASS:
					GameScene.add(Blob.seed(cell, defaultAmt, Regrowth.class));
					break;
				case STONE:
					GameScene.add(Blob.seed(cell, defaultAmt, ParalyticGas.class));
					break;
				case SHARP:
					new GrippingTrap().set(cell).activate();
					break;
				case FIRE:
					GameScene.add(Blob.seed(cell, defaultAmt/2, Fire.class));
					break;
				case DESTRUCTION:
					if (Dungeon.level.terrainIsOneOf(cell, Terrain.HIGH_GRASS, Terrain.FURROWED_GRASS, Terrain.GRASS, Terrain.EMPTY_DECO, Terrain.EMPTY, Terrain.EMPTY_SP)) {
						Dungeon.level.destroy(cell);
					}
					break;
				case ACID:
					GameScene.add(Blob.seed(cell, defaultAmt, CorrosiveGas.class).setStrength(Corrosion.defaultStrength(Dungeon.getScaleFactor())));
					break;
				case DRAIN:
					break;
				case WATER:
					GameScene.add(Blob.seed(cell, defaultAmt, StormCloud.class));
					break;
				case COLD:
					GameScene.add(Blob.seed(cell, defaultAmt, Freezing.class));
					break;
				case TOXIC:
					GameScene.add(Blob.seed(cell, defaultAmt, ToxicGas.class));
					break;
				case CONFUSION:
					GameScene.add(Blob.seed(cell, defaultAmt, ConfusionGas.class));
					break;
				case AIR:
					break;
				case SHOCK:
					if (Dungeon.level.liquid(cell)) {
						GameScene.add(Blob.seed(cell, defaultAmt, Electricity.class));
					}
					break;
				case LIGHT:
					KindOfTerrain terr = Dungeon.level.getTerrain(cell);
					if (Dungeon.level.secret(cell)) {

						Dungeon.level.discover( cell );

						GameScene.discoverTile( cell, terr );
						ScrollOfMagicMapping.discover(cell);
					}
					break;
				case SPIRIT:
					GameScene.add(Blob.seed(cell, defaultAmt, Miasma.class));
					break;
			}
			GameScene.updateMap(cell);
		}
	}

	@Override
	protected int chargesPerCast() {
		return Math.max(1, Math.min(chargesPerCast, curCharges));
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		element.attackProc(damage, attacker, defender);
	}

	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		fx(bolt, callback, curUser, element);
	}

	protected static void fx(Ballistica bolt, Callback callback, Char ch, Element element) {
		if (element == Element.PHYSICAL) {
			MagicMissile m = MagicMissile.boltFromChar(ch.sprite.parent,
					MagicMissile.PLASMA_BOLT,
					ch.sprite,
					bolt.collisionPos,
					callback);
			Sample.INSTANCE.play(Assets.Sounds.ZAP);
			int dist = bolt.dist;
			if (dist > 5){
				m.setSpeed(dist*25);
			}
		} else {
			element.FX(ch, bolt.collisionPos, callback);
		}
	}

	protected static float realMin(float lvl, int chargesPerCast) {
		return (3 + lvl) * chargesPerCast;
	}

	protected static float realMax(float lvl, int chargesPerCast, float damageMultiplier) {
		return (9 + 4 * lvl) * chargesPerCast * damageMultiplier;
	}

	@Override
	public float min(float lvl) {
		return realMin(lvl, chargesPerCast());
	}

	@Override
	public float max(float lvl) {
		return realMax(lvl, chargesPerCast(), getDamageMultiplier());
	}

}
