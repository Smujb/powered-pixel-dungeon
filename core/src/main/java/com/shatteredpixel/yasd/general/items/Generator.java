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

package com.shatteredpixel.yasd.general.items;

import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.items.alcohol.Alcohol;
import com.shatteredpixel.yasd.general.items.alcohol.Beer;
import com.shatteredpixel.yasd.general.items.alcohol.Whiskey;
import com.shatteredpixel.yasd.general.items.armor.Armor;
import com.shatteredpixel.yasd.general.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.yasd.general.items.artifacts.Artifact;
import com.shatteredpixel.yasd.general.items.artifacts.CapeOfThorns;
import com.shatteredpixel.yasd.general.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.yasd.general.items.artifacts.CloakOfShadows;
import com.shatteredpixel.yasd.general.items.artifacts.DriedRose;
import com.shatteredpixel.yasd.general.items.artifacts.EtherealChains;
import com.shatteredpixel.yasd.general.items.artifacts.HornOfPlenty;
import com.shatteredpixel.yasd.general.items.artifacts.LloydsBeacon;
import com.shatteredpixel.yasd.general.items.artifacts.MasterThievesArmband;
import com.shatteredpixel.yasd.general.items.artifacts.SandalsOfNature;
import com.shatteredpixel.yasd.general.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.yasd.general.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.yasd.general.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.yasd.general.items.bags.Bag;
import com.shatteredpixel.yasd.general.items.food.Food;
import com.shatteredpixel.yasd.general.items.food.MysteryMeat;
import com.shatteredpixel.yasd.general.items.food.Pasty;
import com.shatteredpixel.yasd.general.items.potions.Potion;
import com.shatteredpixel.yasd.general.items.potions.PotionOfExperience;
import com.shatteredpixel.yasd.general.items.potions.PotionOfFrost;
import com.shatteredpixel.yasd.general.items.potions.PotionOfHaste;
import com.shatteredpixel.yasd.general.items.potions.PotionOfHealing;
import com.shatteredpixel.yasd.general.items.potions.PotionOfInvisibility;
import com.shatteredpixel.yasd.general.items.potions.PotionOfLevitation;
import com.shatteredpixel.yasd.general.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.yasd.general.items.potions.PotionOfMindVision;
import com.shatteredpixel.yasd.general.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.yasd.general.items.potions.PotionOfPurity;
import com.shatteredpixel.yasd.general.items.potions.PotionOfForbiddenKnowledge;
import com.shatteredpixel.yasd.general.items.potions.PotionOfToxicGas;
import com.shatteredpixel.yasd.general.items.rings.Ring;
import com.shatteredpixel.yasd.general.items.rings.RingOfElements;
import com.shatteredpixel.yasd.general.items.rings.RingOfEvasion;
import com.shatteredpixel.yasd.general.items.rings.RingOfFocus;
import com.shatteredpixel.yasd.general.items.rings.RingOfFuror;
import com.shatteredpixel.yasd.general.items.rings.RingOfHaste;
import com.shatteredpixel.yasd.general.items.rings.RingOfPerception;
import com.shatteredpixel.yasd.general.items.rings.RingOfPower;
import com.shatteredpixel.yasd.general.items.rings.RingOfSharpshooting;
import com.shatteredpixel.yasd.general.items.rings.RingOfTenacity;
import com.shatteredpixel.yasd.general.items.rings.RingOfWealth;
import com.shatteredpixel.yasd.general.items.scrolls.Scroll;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRage;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfTerror;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.yasd.general.items.stones.Runestone;
import com.shatteredpixel.yasd.general.items.stones.StoneOfAffection;
import com.shatteredpixel.yasd.general.items.stones.StoneOfAugmentation;
import com.shatteredpixel.yasd.general.items.stones.StoneOfBlast;
import com.shatteredpixel.yasd.general.items.stones.StoneOfBlink;
import com.shatteredpixel.yasd.general.items.stones.StoneOfClairvoyance;
import com.shatteredpixel.yasd.general.items.stones.StoneOfDeepenedSleep;
import com.shatteredpixel.yasd.general.items.stones.StoneOfDisarming;
import com.shatteredpixel.yasd.general.items.stones.StoneOfEnchantment;
import com.shatteredpixel.yasd.general.items.stones.StoneOfFlock;
import com.shatteredpixel.yasd.general.items.stones.StoneOfIntuition;
import com.shatteredpixel.yasd.general.items.stones.StoneOfRepair;
import com.shatteredpixel.yasd.general.items.stones.StoneOfShock;
import com.shatteredpixel.yasd.general.items.wands.NormalWand;
import com.shatteredpixel.yasd.general.items.wands.Wand;
import com.shatteredpixel.yasd.general.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.yasd.general.items.weapon.missiles.Bolas;
import com.shatteredpixel.yasd.general.items.weapon.missiles.FishingSpear;
import com.shatteredpixel.yasd.general.items.weapon.missiles.ForceCube;
import com.shatteredpixel.yasd.general.items.weapon.missiles.HeavyBoomerang;
import com.shatteredpixel.yasd.general.items.weapon.missiles.Javelin;
import com.shatteredpixel.yasd.general.items.weapon.missiles.Kunai;
import com.shatteredpixel.yasd.general.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.yasd.general.items.weapon.missiles.Shuriken;
import com.shatteredpixel.yasd.general.items.weapon.missiles.ThrowingClub;
import com.shatteredpixel.yasd.general.items.weapon.missiles.ThrowingHammer;
import com.shatteredpixel.yasd.general.items.weapon.missiles.ThrowingKnife;
import com.shatteredpixel.yasd.general.items.weapon.missiles.ThrowingSpear;
import com.shatteredpixel.yasd.general.items.weapon.missiles.ThrowingStone;
import com.shatteredpixel.yasd.general.items.weapon.missiles.Tomahawk;
import com.shatteredpixel.yasd.general.items.weapon.missiles.Trident;
import com.shatteredpixel.yasd.general.plants.Blindweed;
import com.shatteredpixel.yasd.general.plants.Dreamfoil;
import com.shatteredpixel.yasd.general.plants.Earthroot;
import com.shatteredpixel.yasd.general.plants.Fadeleaf;
import com.shatteredpixel.yasd.general.plants.Firebloom;
import com.shatteredpixel.yasd.general.plants.Icecap;
import com.shatteredpixel.yasd.general.plants.Plant;
import com.shatteredpixel.yasd.general.plants.Rotberry;
import com.shatteredpixel.yasd.general.plants.Sorrowmoss;
import com.shatteredpixel.yasd.general.plants.Starflower;
import com.shatteredpixel.yasd.general.plants.Stormvine;
import com.shatteredpixel.yasd.general.plants.Sungrass;
import com.shatteredpixel.yasd.general.plants.Swiftthistle;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Generator {

	public enum Category {
		WEAPON(4, MeleeWeapon.class),

		ARMOR(4, Armor.class),

		MISSILE ( 4,    MissileWeapon.class ),
		MIS_T1(0, MissileWeapon.class),
		MIS_T2(0, MissileWeapon.class),
		MIS_T3(0, MissileWeapon.class),
		MIS_T4(0, MissileWeapon.class),
		MIS_T5(0, MissileWeapon.class),

		WAND(3, Wand.class),
		RING(2, Ring.class),
		ARTIFACT(3, Artifact.class),

		FOOD(0, Food.class),

		ALCOHOL(0, Alcohol.class),

		POTION	( 16,   Potion.class ),
		SEED	( 2,    Plant.Seed.class ),

		SCROLL	( 16,   Scroll.class ),
		STONE(2, Runestone.class),

		GOLD	( 20,   Gold.class );

		public Class<?>[] classes;

		//some item types use a deck-based system, where the probs decrement as items are picked
		// until they are all 0, and then they reset. Those generator classes should define
		// defaultProbs. If defaultProbs is null then a deck system isn't used.
		//Artifacts in particular don't reset, no duplicates!
		public float[] probs;
		public float[] defaultProbs = null;

		public float prob;
		public Class<? extends Item> superClass;

		private Category( float prob, Class<? extends Item> superClass ) {
			this.prob = prob;
			this.superClass = superClass;
		}

		public static int order( Item item ) {
			for (int i=0; i < values().length; i++) {
				if (values()[i].superClass.isInstance( item )) {
					return i;
				}
			}

			return item instanceof Bag ? Integer.MAX_VALUE : Integer.MAX_VALUE - 1;
		}

		static {
			GOLD.classes = new Class<?>[]{
					Gold.class};
			GOLD.probs = new float[]{1};

			ALCOHOL.classes = new Class<?>[]{
					Whiskey.class,
					Beer.class
			};
			ALCOHOL.probs = new float[]{1, 2};

			POTION.classes = new Class<?>[]{
					PotionOfForbiddenKnowledge.class,
					PotionOfHealing.class,
					PotionOfMindVision.class,
					PotionOfFrost.class,
					PotionOfLiquidFlame.class,
					PotionOfToxicGas.class,
					PotionOfHaste.class,
					PotionOfInvisibility.class,
					PotionOfLevitation.class,
					PotionOfParalyticGas.class,
					PotionOfPurity.class,
					PotionOfExperience.class};
			POTION.defaultProbs = new float[]{1, 6, 4, 3, 3, 3, 2, 2, 2, 2, 2, 1};
			POTION.probs = POTION.defaultProbs.clone();

			SEED.classes = new Class<?>[]{
					Rotberry.Seed.class, //quest item
					Sungrass.Seed.class,
					Fadeleaf.Seed.class,
					Icecap.Seed.class,
					Firebloom.Seed.class,
					Sorrowmoss.Seed.class,
					Stormvine.Seed.class,
					Sungrass.Seed.class,
					Swiftthistle.Seed.class,
					Blindweed.Seed.class,
					Stormvine.Seed.class,
					Earthroot.Seed.class,
					Dreamfoil.Seed.class,
					Starflower.Seed.class};
			SEED.defaultProbs = new float[]{ 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 2 };
			SEED.probs = SEED.defaultProbs.clone();

			SCROLL.classes = new Class<?>[]{
					ScrollOfUpgrade.class, //3 drop every chapter, see Dungeon.souNeeded()
					ScrollOfIdentify.class,
					ScrollOfRemoveCurse.class,
					ScrollOfMirrorImage.class,
					ScrollOfRecharging.class,
					ScrollOfTeleportation.class,
					ScrollOfLullaby.class,
					ScrollOfMagicMapping.class,
					ScrollOfRage.class,
					ScrollOfRetribution.class,
					ScrollOfTerror.class,
					ScrollOfTransmutation.class
			};
			SCROLL.defaultProbs = new float[]{0, 6, 4, 3, 3, 3, 2, 2, 2, 2, 2, 1};
			SCROLL.probs = SCROLL.defaultProbs.clone();

			STONE.classes = new Class<?>[]{
					StoneOfEnchantment.class,   //1 drops per chapter, can rarely find more
					StoneOfIntuition.class,     //1 additional stone is also dropped on floors 1-3
					StoneOfDisarming.class,
					StoneOfFlock.class,
					StoneOfShock.class,
					StoneOfBlink.class,
					StoneOfDeepenedSleep.class,
					StoneOfClairvoyance.class,
					StoneOfRepair.class,
					StoneOfBlast.class,
					StoneOfAffection.class,
					StoneOfAugmentation.class  //1 is sold in each shop
			};
			STONE.defaultProbs = new float[]{ 0, 5, 5, 5, 5, 5, 5, 5, 0, 5, 5, 0 };
			STONE.probs = STONE.defaultProbs.clone();

			//see generator.randomWeapon
			WEAPON.classes = new Class<?>[]{};
			WEAPON.probs = new float[]{};

			//see Generator.randomArmor
			ARMOR.classes = new Class<?>[]{};
			ARMOR.probs = new float[]{};

			//see Generator.randomMissile
			MISSILE.classes = new Class<?>[]{};
			MISSILE.probs = new float[]{};

			//see Generator.randomWand
			WAND.classes = new Class<?>[]{};
			WAND.probs = new float[]{};

			MIS_T1.classes = new Class<?>[]{
					ThrowingStone.class,
					ThrowingKnife.class
			};
			MIS_T1.probs = new float[]{6, 5};

			MIS_T2.classes = new Class<?>[]{
					FishingSpear.class,
					ThrowingClub.class,
					Shuriken.class
			};
			MIS_T2.probs = new float[]{6, 5, 4};

			MIS_T3.classes = new Class<?>[]{
					ThrowingSpear.class,
					Kunai.class,
					Bolas.class
			};
			MIS_T3.probs = new float[]{6, 5, 4};

			MIS_T4.classes = new Class<?>[]{
					Javelin.class,
					Tomahawk.class,
					HeavyBoomerang.class
			};
			MIS_T4.probs = new float[]{6, 5, 4};

			MIS_T5.classes = new Class<?>[]{
					Trident.class,
					ThrowingHammer.class,
					ForceCube.class
			};
			MIS_T5.probs = new float[]{6, 5, 4};

			FOOD.classes = new Class<?>[]{
					Food.class,
					Pasty.class,
					MysteryMeat.class};
			FOOD.probs = new float[]{4, 1, 0};

			RING.classes = new Class<?>[]{
					RingOfPerception.class,
					//_Unused.class,
					RingOfElements.class,
					RingOfEvasion.class,
					RingOfFuror.class,
					RingOfHaste.class,
					RingOfFocus.class,
					RingOfPower.class,
					RingOfSharpshooting.class,
					RingOfTenacity.class,
					RingOfWealth.class};
			RING.probs = new float[]{1, /*1,*/ 1, 1, 1, 1, 1, 1, 1, 1, 1};

			ARTIFACT.classes = new Class<?>[]{
					CapeOfThorns.class,
					ChaliceOfBlood.class,
					CloakOfShadows.class,
					HornOfPlenty.class,
					MasterThievesArmband.class,
					SandalsOfNature.class,
					TalismanOfForesight.class,
					TimekeepersHourglass.class,
					UnstableSpellbook.class,
					AlchemistsToolkit.class,
					DriedRose.class,
					LloydsBeacon.class,
					EtherealChains.class
			};
			ARTIFACT.defaultProbs = new float[]{0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1};
			ARTIFACT.probs = ARTIFACT.defaultProbs.clone();
		}
	}

	private static final String CATEGORY_PROBS = "_probs";

	private static final float[][] floorSetTierProbs = new float[][] {
			{0, 70, 20,  8,  2},
			{0, 25, 50, 20,  5},
			{0,  0, 40, 50, 10},
			{0,  0, 20, 40, 40},
			{0,  0,  0, 20, 80}
	};

	private static HashMap<Category,Float> categoryProbs = new LinkedHashMap<>();

	public static void reset() {
		for (Category cat : Category.values()) {
			categoryProbs.put( cat, cat.prob );
			if (cat.defaultProbs != null) cat.probs = cat.defaultProbs.clone();
		}
	}

	public static void reset(Category cat){
		cat.probs = cat.defaultProbs.clone();
	}

	public static Item random() {
		Category cat = Random.chances( categoryProbs );
		do {
			reset();
			cat = Random.chances( categoryProbs );
		} while (cat == null);
		categoryProbs.put( cat, categoryProbs.get( cat ) - 1);
		return random( cat );
	}

	public static Item random( Category cat ) {
		if (cat == null) return random();
		switch (cat) {
			case WAND:
				return randomWand();
			case ARMOR:
				return randomArmor();
			case WEAPON:
				return randomWeapon();
			case MISSILE:
				return randomMissile();
			case ARTIFACT:
				Item item = randomArtifact();
				//if we're out of artifacts, return a ring instead.
				return item != null ? item : random(Category.RING);
			default:
				if (cat.probs == null) {
					cat.probs = cat.defaultProbs;
				}
				int i = Random.chances(cat.probs);
				if (i == -1) {
					reset(cat);
					i = Random.chances(cat.probs);
				}
				if (cat.defaultProbs != null) cat.probs[i]--;
				return ((Item) Reflection.newInstance(cat.classes[i])).random();
		}
	}

	//overrides any deck systems and always uses default probs
	public static Item randomUsingDefaults( Category cat ){
		if (cat.defaultProbs == null) {
			return random(cat); //currently covers weapons/armor/missiles
		} else {
			return ((Item) Reflection.newInstance(cat.classes[Random.chances(cat.defaultProbs)])).random();
		}
	}

	public static Item random( Class<? extends Item> cl ) {
		return Reflection.newInstance(cl).random();
	}

	@NotNull
	public static Armor randomArmor(){
		return randomArmor(Dungeon.getScaleFactor() / Constants.CHAPTER_LENGTH);
	}

	@NotNull
	public static Armor randomArmor(int floorSet) {

		floorSet += (Random.chances(new float[]{1, 3, 3, 2, 1})) - 1;
		int tier = (int) GameMath.gate(1, floorSet, Constants.MAXIMUM_TIER);

		//Armor a = (Armor)Reflection.newInstance(Category.ARMOR.classes[Random.chances(Category.ARMOR.probs)]);
		Armor a = new Armor();
		a.random();
		a.setTier(tier);
		if (a.tier == 1) {
			switch (Random.Int(3)) {
				case 0:
					a.setTier(2);
					break;
				case 2:
					a.cursed = false;
					a.inscribe();
					break;
				case 3:
					a.upgrade();
					break;
			}
		}
		return a;
	}


	public static Wand randomWand() {
		return (Wand) NormalWand.createRandom().random();
	}

	public static MeleeWeapon randomWeapon(){
		return randomWeapon(Dungeon.getScaleFactor() / Constants.CHAPTER_LENGTH);
	}

	public static MeleeWeapon randomWeapon(int floorSet) {
		MeleeWeapon w = new MeleeWeapon();

		floorSet += (Random.chances(new float[]{1, 3, 3, 2, 1})) - 1;
		int tier = (int) GameMath.gate(1, floorSet, Constants.MAXIMUM_TIER);
		//w = (MeleeWeapon) Reflection.newInstance(Category.WEAPON.classes[Random.chances( Category.WEAPON.probs )]);
		w.random();
		w.setTier(tier);
		if (w.tier == 1) {
			switch (Random.Int(3)) {
				case 0:
					w.setTier(2);
					break;
				case 2:
					w.cursed = false;
					w.enchant();
					break;
				case 3:
					w.upgrade();
					break;
			}
		}
		w.initStats();
		return w;
	}

	public static final Category[] misTiers = new Category[]{
			Category.MIS_T1,
			Category.MIS_T2,
			Category.MIS_T3,
			Category.MIS_T4,
			Category.MIS_T5
	};

	public static MissileWeapon randomMissile(){
		return randomMissile(Dungeon.depth / 5);
	}

	public static MissileWeapon randomMissile(int floorSet) {

		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);

		Category c = misTiers[Random.chances(floorSetTierProbs[floorSet])];
		MissileWeapon w = (MissileWeapon)Reflection.newInstance(c.classes[Random.chances(c.probs)]);
		w.random();
		return w;
	}

	//enforces uniqueness of artifacts throughout a run.
	public static Artifact randomArtifact() {

		Category cat = Category.ARTIFACT;
		int i = Random.chances( cat.probs );

		//if no artifacts are left, return null
		if (i == -1){
			return null;
		}

		cat.probs[i]--;
		return (Artifact) Reflection.newInstance((Class<? extends Artifact>) cat.classes[i]).random();

	}

	public static boolean removeArtifact(Class<?extends Artifact> artifact) {
		Category cat = Category.ARTIFACT;
		for (int i = 0; i < cat.classes.length; i++){
			if (cat.classes[i].equals(artifact)) {
				cat.probs[i] = 0;
				return true;
			}
		}
		return false;
	}

	private static final String GENERAL_PROBS = "general_probs";

	public static void storeInBundle(Bundle bundle) {
		Float[] genProbs = categoryProbs.values().toArray(new Float[0]);
		float[] storeProbs = new float[genProbs.length];
		for (int i = 0; i < storeProbs.length; i++){
			storeProbs[i] = genProbs[i];
		}
		bundle.put( GENERAL_PROBS, storeProbs);

		for (Category cat : Category.values()){
			if (cat.defaultProbs == null) continue;
			boolean needsStore = false;
			for (int i = 0; i < cat.probs.length; i++){
				if (cat.probs[i] != cat.defaultProbs[i]){
					needsStore = true;
					break;
				}
			}

			if (needsStore){
				bundle.put(cat.name().toLowerCase() + CATEGORY_PROBS, cat.probs);
			}
		}
	}

	public static void restoreFromBundle(Bundle bundle) {
		reset();

		if (bundle.contains(GENERAL_PROBS)){
			float[] probs = bundle.getFloatArray(GENERAL_PROBS);
			for (int i = 0; i < probs.length; i++){
				categoryProbs.put(Category.values()[i], probs[i]);
			}
		}

		for (Category cat : Category.values()){
			if (bundle.contains(cat.name().toLowerCase() + CATEGORY_PROBS)){
				float[] probs = bundle.getFloatArray(cat.name().toLowerCase() + CATEGORY_PROBS);
				if (cat.defaultProbs != null && probs.length == cat.defaultProbs.length){
					cat.probs = probs;
				}
			}
		}

		//pre-0.8.1
		if (bundle.contains("spawned_artifacts")) {
			for (Class<? extends Artifact> artifact : bundle.getClassArray("spawned_artifacts")) {
				Category cat = Category.ARTIFACT;
				for (int i = 0; i < cat.classes.length; i++) {
					if (cat.classes[i].equals(artifact)) {
						cat.probs[i] = 0;
					}
				}
			}
		}

	}
}
