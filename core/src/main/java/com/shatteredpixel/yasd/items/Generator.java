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

import com.shatteredpixel.yasd.Constants;
import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.items.alcohol.Alcohol;
import com.shatteredpixel.yasd.items.alcohol.Beer;
import com.shatteredpixel.yasd.items.alcohol.Whiskey;
import com.shatteredpixel.yasd.items.armor.Armor;
import com.shatteredpixel.yasd.items.armor.ClothArmor;
import com.shatteredpixel.yasd.items.armor.DiscArmor;
import com.shatteredpixel.yasd.items.armor.HideArmor;
import com.shatteredpixel.yasd.items.armor.HuntressArmor;
import com.shatteredpixel.yasd.items.armor.LeadArmor;
import com.shatteredpixel.yasd.items.armor.LeatherArmor;
import com.shatteredpixel.yasd.items.armor.MageArmor;
import com.shatteredpixel.yasd.items.armor.MailArmor;
import com.shatteredpixel.yasd.items.armor.PlateArmor;
import com.shatteredpixel.yasd.items.armor.RogueArmor;
import com.shatteredpixel.yasd.items.armor.ScaleArmor;
import com.shatteredpixel.yasd.items.armor.WarriorArmor;
import com.shatteredpixel.yasd.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.yasd.items.artifacts.Artifact;
import com.shatteredpixel.yasd.items.artifacts.CapeOfThorns;
import com.shatteredpixel.yasd.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.yasd.items.artifacts.CloakOfShadows;
import com.shatteredpixel.yasd.items.artifacts.DriedRose;
import com.shatteredpixel.yasd.items.artifacts.EtherealChains;
import com.shatteredpixel.yasd.items.artifacts.HornOfPlenty;
import com.shatteredpixel.yasd.items.artifacts.LloydsBeacon;
import com.shatteredpixel.yasd.items.artifacts.MasterThievesArmband;
import com.shatteredpixel.yasd.items.artifacts.SandalsOfNature;
import com.shatteredpixel.yasd.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.yasd.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.yasd.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.yasd.items.bags.Bag;
import com.shatteredpixel.yasd.items.food.Food;
import com.shatteredpixel.yasd.items.food.MysteryMeat;
import com.shatteredpixel.yasd.items.food.Pasty;
import com.shatteredpixel.yasd.items.potions.Potion;
import com.shatteredpixel.yasd.items.potions.PotionOfExperience;
import com.shatteredpixel.yasd.items.potions.PotionOfFrost;
import com.shatteredpixel.yasd.items.potions.PotionOfHaste;
import com.shatteredpixel.yasd.items.potions.PotionOfHealing;
import com.shatteredpixel.yasd.items.potions.PotionOfInvisibility;
import com.shatteredpixel.yasd.items.potions.PotionOfLevitation;
import com.shatteredpixel.yasd.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.yasd.items.potions.PotionOfMindVision;
import com.shatteredpixel.yasd.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.yasd.items.potions.PotionOfPurity;
import com.shatteredpixel.yasd.items.potions.PotionOfStrength;
import com.shatteredpixel.yasd.items.potions.PotionOfToxicGas;
import com.shatteredpixel.yasd.items.rings.Ring;
import com.shatteredpixel.yasd.items.rings.RingOfAccuracy;
import com.shatteredpixel.yasd.items.rings.RingOfElements;
import com.shatteredpixel.yasd.items.rings.RingOfEnergy;
import com.shatteredpixel.yasd.items.rings.RingOfEvasion;
import com.shatteredpixel.yasd.items.rings.RingOfForce;
import com.shatteredpixel.yasd.items.rings.RingOfFuror;
import com.shatteredpixel.yasd.items.rings.RingOfHaste;
import com.shatteredpixel.yasd.items.rings.RingOfMight;
import com.shatteredpixel.yasd.items.rings.RingOfSharpshooting;
import com.shatteredpixel.yasd.items.rings.RingOfTenacity;
import com.shatteredpixel.yasd.items.rings.RingOfWealth;
import com.shatteredpixel.yasd.items.scrolls.Scroll;
import com.shatteredpixel.yasd.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.yasd.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.yasd.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.yasd.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.yasd.items.scrolls.ScrollOfRage;
import com.shatteredpixel.yasd.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.yasd.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.yasd.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.yasd.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.yasd.items.scrolls.ScrollOfTerror;
import com.shatteredpixel.yasd.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.yasd.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.yasd.items.stones.Runestone;
import com.shatteredpixel.yasd.items.stones.StoneOfAffection;
import com.shatteredpixel.yasd.items.stones.StoneOfRepair;
import com.shatteredpixel.yasd.items.stones.StoneOfAugmentation;
import com.shatteredpixel.yasd.items.stones.StoneOfBlast;
import com.shatteredpixel.yasd.items.stones.StoneOfBlink;
import com.shatteredpixel.yasd.items.stones.StoneOfClairvoyance;
import com.shatteredpixel.yasd.items.stones.StoneOfDeepenedSleep;
import com.shatteredpixel.yasd.items.stones.StoneOfDisarming;
import com.shatteredpixel.yasd.items.stones.StoneOfEnchantment;
import com.shatteredpixel.yasd.items.stones.StoneOfFlock;
import com.shatteredpixel.yasd.items.stones.StoneOfIntuition;
import com.shatteredpixel.yasd.items.stones.StoneOfShock;
import com.shatteredpixel.yasd.items.wands.Wand;
import com.shatteredpixel.yasd.items.wands.WandOfAcid;
import com.shatteredpixel.yasd.items.wands.WandOfBlastWave;
import com.shatteredpixel.yasd.items.wands.WandOfCorrosion;
import com.shatteredpixel.yasd.items.wands.WandOfCorruption;
import com.shatteredpixel.yasd.items.wands.WandOfDamnation;
import com.shatteredpixel.yasd.items.wands.WandOfDarkness;
import com.shatteredpixel.yasd.items.wands.WandOfDisintegration;
import com.shatteredpixel.yasd.items.wands.WandOfFireblast;
import com.shatteredpixel.yasd.items.wands.WandOfFlow;
import com.shatteredpixel.yasd.items.wands.WandOfFrost;
import com.shatteredpixel.yasd.items.wands.WandOfLifeDrain;
import com.shatteredpixel.yasd.items.wands.WandOfLightning;
import com.shatteredpixel.yasd.items.wands.WandOfLivingEarth;
import com.shatteredpixel.yasd.items.wands.WandOfMagicMissile;
import com.shatteredpixel.yasd.items.wands.WandOfPlasmaBolt;
import com.shatteredpixel.yasd.items.wands.WandOfPrismaticLight;
import com.shatteredpixel.yasd.items.wands.WandOfRegrowth;
import com.shatteredpixel.yasd.items.wands.WandOfThornvines;
import com.shatteredpixel.yasd.items.wands.WandOfTransfusion;
import com.shatteredpixel.yasd.items.wands.WandOfWarding;
import com.shatteredpixel.yasd.items.weapon.melee.AssassinsBlade;
import com.shatteredpixel.yasd.items.weapon.melee.BattleAxe;
import com.shatteredpixel.yasd.items.weapon.melee.Crossbow;
import com.shatteredpixel.yasd.items.weapon.melee.Dagger;
import com.shatteredpixel.yasd.items.weapon.melee.Dirk;
import com.shatteredpixel.yasd.items.weapon.melee.Flail;
import com.shatteredpixel.yasd.items.weapon.melee.Gauntlet;
import com.shatteredpixel.yasd.items.weapon.melee.Glaive;
import com.shatteredpixel.yasd.items.weapon.melee.Gloves;
import com.shatteredpixel.yasd.items.weapon.melee.Greataxe;
import com.shatteredpixel.yasd.items.weapon.melee.Greatshield;
import com.shatteredpixel.yasd.items.weapon.melee.Greatsword;
import com.shatteredpixel.yasd.items.weapon.melee.HandAxe;
import com.shatteredpixel.yasd.items.weapon.melee.Longsword;
import com.shatteredpixel.yasd.items.weapon.melee.Mace;
import com.shatteredpixel.yasd.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.yasd.items.weapon.melee.Quarterstaff;
import com.shatteredpixel.yasd.items.weapon.melee.RoundShield;
import com.shatteredpixel.yasd.items.weapon.melee.RunicBlade;
import com.shatteredpixel.yasd.items.weapon.melee.Sai;
import com.shatteredpixel.yasd.items.weapon.melee.Scimitar;
import com.shatteredpixel.yasd.items.weapon.melee.Shortsword;
import com.shatteredpixel.yasd.items.weapon.melee.Spear;
import com.shatteredpixel.yasd.items.weapon.melee.Sword;
import com.shatteredpixel.yasd.items.weapon.melee.WarHammer;
import com.shatteredpixel.yasd.items.weapon.melee.Whip;
import com.shatteredpixel.yasd.items.weapon.melee.WornShortsword;
import com.shatteredpixel.yasd.items.weapon.missiles.HeavyBoomerang;
import com.shatteredpixel.yasd.items.weapon.missiles.Bolas;
import com.shatteredpixel.yasd.items.weapon.missiles.FishingSpear;
import com.shatteredpixel.yasd.items.weapon.missiles.ForceCube;
import com.shatteredpixel.yasd.items.weapon.missiles.Javelin;
import com.shatteredpixel.yasd.items.weapon.missiles.Kunai;
import com.shatteredpixel.yasd.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.yasd.items.weapon.missiles.Shuriken;
import com.shatteredpixel.yasd.items.weapon.missiles.ThrowingClub;
import com.shatteredpixel.yasd.items.weapon.missiles.ThrowingHammer;
import com.shatteredpixel.yasd.items.weapon.missiles.ThrowingKnife;
import com.shatteredpixel.yasd.items.weapon.missiles.ThrowingSpear;
import com.shatteredpixel.yasd.items.weapon.missiles.ThrowingStone;
import com.shatteredpixel.yasd.items.weapon.missiles.Tomahawk;
import com.shatteredpixel.yasd.items.weapon.missiles.Trident;
import com.shatteredpixel.yasd.plants.Blindweed;
import com.shatteredpixel.yasd.plants.Dreamfoil;
import com.shatteredpixel.yasd.plants.Earthroot;
import com.shatteredpixel.yasd.plants.Fadeleaf;
import com.shatteredpixel.yasd.plants.Firebloom;
import com.shatteredpixel.yasd.plants.Icecap;
import com.shatteredpixel.yasd.plants.Plant;
import com.shatteredpixel.yasd.plants.Rotberry;
import com.shatteredpixel.yasd.plants.Sorrowmoss;
import com.shatteredpixel.yasd.plants.Starflower;
import com.shatteredpixel.yasd.plants.Stormvine;
import com.shatteredpixel.yasd.plants.Sungrass;
import com.shatteredpixel.yasd.plants.Swiftthistle;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Generator {

	public enum Category {
		WEAPON	( 6,    MeleeWeapon.class),
		WEP_T1	( 0,    MeleeWeapon.class),
		WEP_T2	( 0,    MeleeWeapon.class),
		WEP_T3	( 0,    MeleeWeapon.class),
		WEP_T4	( 0,    MeleeWeapon.class),
		WEP_T5	( 0,    MeleeWeapon.class),
		
		ARMOR	( 4,    Armor.class ),
		ARM_T1  (0,     Armor.class ),
		ARM_T2  (0,     Armor.class ),
		ARM_T3  (0,     Armor.class ),
		ARM_T4  (0,     Armor.class ),
		ARM_T5  (0,     Armor.class ),
		
		MISSILE ( 3,    MissileWeapon.class ),
		MIS_T1  ( 0,    MissileWeapon.class ),
		MIS_T2  ( 0,    MissileWeapon.class ),
		MIS_T3  ( 0,    MissileWeapon.class ),
		MIS_T4  ( 0,    MissileWeapon.class ),
		MIS_T5  ( 0,    MissileWeapon.class ),
		
		WAND	( 3,    Wand.class ),
		RING	( 1,    Ring.class ),
		ARTIFACT( 1,    Artifact.class),
		
		FOOD	( 0,    Food.class ),

		ALCOHOL ( 0,    Alcohol.class),

		POTION	( 20,   Potion.class ),
		SEED	( 0,    Plant.Seed.class ), //dropped by grass
		
		SCROLL	( 20,   Scroll.class ),
		STONE   ( 2,    Runestone.class),
		
		GOLD	( 18,   Gold.class );
		
		public Class<?>[] classes;
		public float[] probs;
		
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
		
		private static final float[] INITIAL_ARTIFACT_PROBS = new float[]{ 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1};
		
		static {
			GOLD.classes = new Class<?>[]{
					Gold.class };
			GOLD.probs = new float[]{ 1 };

			ALCOHOL.classes = new Class<?>[]{
					Whiskey.class,
					Beer.class
			};
			ALCOHOL.probs = new float[]{ 1, 2 };
			
			POTION.classes = new Class<?>[]{
					PotionOfStrength.class, //2 drop every chapter, see Dungeon.posNeeded()
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
			POTION.probs = new float[]{ 0, 6, 4, 3, 3, 3, 2, 2, 2, 2, 2, 1 };
			
			SEED.classes = new Class<?>[]{
					Rotberry.Seed.class, //quest item
					Blindweed.Seed.class,
					Dreamfoil.Seed.class,
					Earthroot.Seed.class,
					Fadeleaf.Seed.class,
					Firebloom.Seed.class,
					Icecap.Seed.class,
					Sorrowmoss.Seed.class,
					Stormvine.Seed.class,
					Sungrass.Seed.class,
					Swiftthistle.Seed.class,
					Starflower.Seed.class};
			SEED.probs = new float[]{ 0, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 1 };
			
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
			SCROLL.probs = new float[]{ 0, 6, 4, 3, 3, 3, 2, 2, 2, 2, 2, 1 };
			
			STONE.classes = new Class<?>[]{
					StoneOfEnchantment.class,   //1 drops per chapter, can rarely find more
					StoneOfAugmentation.class,  //1 is sold in each shop, can rarely find more
					StoneOfIntuition.class,     //1 additional stone is also dropped on floors 1-3
					StoneOfRepair.class,		//2-3 are sold in each shop, can rarely find more
					StoneOfAffection.class,
					StoneOfBlast.class,
					StoneOfBlink.class,
					StoneOfClairvoyance.class,
					StoneOfDeepenedSleep.class,
					StoneOfDisarming.class,
					StoneOfFlock.class,
					StoneOfShock.class
			};
			STONE.probs = new float[]{ 1, 1, 10, 2, 10, 10, 10, 10, 10, 10, 10, 10 };

			WAND.classes = new Class<?>[]{
					WandOfMagicMissile.class,
					WandOfLightning.class,
					WandOfDisintegration.class,
					WandOfFireblast.class,
					WandOfCorrosion.class,
					WandOfBlastWave.class,
					WandOfLivingEarth.class,
					WandOfFrost.class,
					WandOfPrismaticLight.class,
					WandOfWarding.class,
					WandOfTransfusion.class,
					WandOfCorruption.class,
					WandOfRegrowth.class,
					WandOfLifeDrain.class,
					WandOfAcid.class,
					WandOfDamnation.class,
					WandOfThornvines.class,
					WandOfPlasmaBolt.class,
					WandOfFlow.class,
					WandOfDarkness.class};
			WAND.probs = new float[]{ 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 3, 3, 4, 3, 3 };
			
			//see generator.randomWeapon
			WEAPON.classes = new Class<?>[]{};
			WEAPON.probs = new float[]{};
			
			WEP_T1.classes = new Class<?>[]{
					WornShortsword.class,
					Gloves.class,
					Dagger.class,
					MagesStaff.class
			};
			WEP_T1.probs = new float[]{ 1, 1, 1, 0 };
			
			WEP_T2.classes = new Class<?>[]{
					Shortsword.class,
					HandAxe.class,
					Spear.class,
					Quarterstaff.class,
					Dirk.class
			};
			WEP_T2.probs = new float[]{ 6, 5, 5, 4, 4 };
			
			WEP_T3.classes = new Class<?>[]{
					Sword.class,
					Mace.class,
					Scimitar.class,
					RoundShield.class,
					Sai.class,
					Whip.class
			};
			WEP_T3.probs = new float[]{ 6, 5, 5, 4, 4, 4 };
			
			WEP_T4.classes = new Class<?>[]{
					Longsword.class,
					BattleAxe.class,
					Flail.class,
					RunicBlade.class,
					AssassinsBlade.class,
					Crossbow.class
			};
			WEP_T4.probs = new float[]{ 6, 5, 5, 4, 4, 4 };
			
			WEP_T5.classes = new Class<?>[]{
					Greatsword.class,
					WarHammer.class,
					Glaive.class,
					Greataxe.class,
					Greatshield.class,
					Gauntlet.class
			};
			WEP_T5.probs = new float[]{ 6, 5, 5, 4, 4, 4 };
			
			//see Generator.randomArmor
			ARMOR.classes = new Class<?>[]{
					ClothArmor.class,
					LeatherArmor.class,
					MailArmor.class,
					ScaleArmor.class,
					PlateArmor.class };
			ARMOR.probs = new float[]{ 0, 0, 0, 0, 0 };

			ARM_T1.classes = new Class<?>[]{
					ClothArmor.class,
					WarriorArmor.class,
					HuntressArmor.class,
					RogueArmor.class,
					MageArmor.class
			};
			ARM_T1.probs = new float[]{ 1, 1, 1, 1, 1 };

			ARM_T2.classes = new Class<?>[]{
					LeatherArmor.class,
					HideArmor.class
			};

			ARM_T2.probs = new float[]{ 1, 1 };

			ARM_T3.classes = new Class<?>[]{
					MailArmor.class
			};

			ARM_T3.probs = new float[]{ 1 };

			ARM_T4.classes = new Class<?>[]{
					ScaleArmor.class,
					DiscArmor.class
			};

			ARM_T4.probs = new float[]{ 1, 1 };

			ARM_T5.classes = new Class<?>[]{
					PlateArmor.class,
					LeadArmor.class
			};
			ARM_T5.probs = new float[]{ 3, 2 };
			
			//see Generator.randomMissile
			MISSILE.classes = new Class<?>[]{};
			MISSILE.probs = new float[]{};
			
			MIS_T1.classes = new Class<?>[]{
					ThrowingStone.class,
					ThrowingKnife.class
			};
			MIS_T1.probs = new float[]{ 6, 5 };
			
			MIS_T2.classes = new Class<?>[]{
					FishingSpear.class,
					ThrowingClub.class,
					Shuriken.class
			};
			MIS_T2.probs = new float[]{ 6, 5, 4 };
			
			MIS_T3.classes = new Class<?>[]{
					ThrowingSpear.class,
					Kunai.class,
					Bolas.class
			};
			MIS_T3.probs = new float[]{ 6, 5, 4 };
			
			MIS_T4.classes = new Class<?>[]{
					Javelin.class,
					Tomahawk.class,
					HeavyBoomerang.class
			};
			MIS_T4.probs = new float[]{ 6, 5, 4 };
			
			MIS_T5.classes = new Class<?>[]{
					Trident.class,
					ThrowingHammer.class,
					ForceCube.class
			};
			MIS_T5.probs = new float[]{ 6, 5, 4 };
			
			FOOD.classes = new Class<?>[]{
					Food.class,
					Pasty.class,
					MysteryMeat.class };
			FOOD.probs = new float[]{ 4, 1, 0 };
			
			RING.classes = new Class<?>[]{
					RingOfAccuracy.class,
					RingOfEvasion.class,
					RingOfElements.class,
					RingOfForce.class,
					RingOfFuror.class,
					RingOfHaste.class,
					RingOfEnergy.class,
					RingOfMight.class,
					RingOfSharpshooting.class,
					RingOfTenacity.class,
					RingOfWealth.class};
			RING.probs = new float[]{ 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1 };
			
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
			ARTIFACT.probs = INITIAL_ARTIFACT_PROBS.clone();
		}
	}

	private static final float[][] floorSetTierProbs = new float[][] {
			{0, 70, 20,  8,  2},
			{0, 25, 50, 20,  5},
			{0, 10, 40, 40, 10},
			{0,  5, 20, 50, 25},
			{0,  2,  8, 20, 70}
	};
	
	private static HashMap<Category,Float> categoryProbs = new LinkedHashMap<>();
	
	public static void reset() {
		for (Category cat : Category.values()) {
			categoryProbs.put( cat, cat.prob );
		}
	}
	
	public static Item random() {
		Category cat = Random.chances( categoryProbs );
		if (cat == null){
			reset();
			cat = Random.chances( categoryProbs );
		}
		categoryProbs.put( cat, categoryProbs.get( cat ) - 1);
		return random( cat );
	}
	
	public static Item random( Category cat ) {
		switch (cat) {
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
			return ((Item) Reflection.newInstance(cat.classes[Random.chances( cat.probs )])).random();
		}
	}
	
	public static Item random( Class<? extends Item> cl ) {
		return Reflection.newInstance(cl).random();
	}

	public static Armor randomArmor(){
		return randomArmor(Dungeon.depth / Constants.CHAPTER_LENGTH);
	}
	
	public static Armor randomArmor(int floorSet) {

		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);

		int tier = Random.chances(floorSetTierProbs[floorSet]);

		Category c = armorTiers[tier];
		
		Armor a = (Armor)Reflection.newInstance(c.classes[Random.chances(c.probs)]);
		a.random();
		return a;
	}

	public static final Category[] armorTiers = new Category[]{
			Category.ARM_T1,
			Category.ARM_T2,
			Category.ARM_T3,
			Category.ARM_T4,
			Category.ARM_T5
	};

	public static final Category[] wepTiers = new Category[]{
			Category.WEP_T1,
			Category.WEP_T2,
			Category.WEP_T3,
			Category.WEP_T4,
			Category.WEP_T5
	};

	public static MeleeWeapon randomWeapon(){
		return randomWeapon(Dungeon.depth / Constants.CHAPTER_LENGTH);
	}
	
	public static MeleeWeapon randomWeapon(int floorSet) {

		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);
		
		Category c = wepTiers[Random.chances(floorSetTierProbs[floorSet])];
		MeleeWeapon w = (MeleeWeapon)Reflection.newInstance(c.classes[Random.chances(c.probs)]);
		w.random();
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
		return randomMissile(Dungeon.depth / Constants.CHAPTER_LENGTH);
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
		
		Class<?extends Artifact> art = (Class<? extends Artifact>) cat.classes[i];

		if (removeArtifact(art)) {
			Artifact artifact = Reflection.newInstance(art);
			artifact.random();
			return artifact;
		} else {
			return null;
		}
	}

	public static boolean removeArtifact(Class<?extends Artifact> artifact) {
		if (spawnedArtifacts.contains(artifact))
			return false;

		Category cat = Category.ARTIFACT;
		for (int i = 0; i < cat.classes.length; i++)
			if (cat.classes[i].equals(artifact)) {
				if (cat.probs[i] == 1){
					cat.probs[i] = 0;
					spawnedArtifacts.add(artifact);
					return true;
				} else
					return false;
			}

		return false;
	}

	//resets artifact probabilities, for new dungeons
	public static void initArtifacts() {
		Category.ARTIFACT.probs = Category.INITIAL_ARTIFACT_PROBS.clone();
		spawnedArtifacts = new ArrayList<>();
	}

	private static ArrayList<Class<?extends Artifact>> spawnedArtifacts = new ArrayList<>();
	
	private static final String GENERAL_PROBS = "general_probs";
	private static final String SPAWNED_ARTIFACTS = "spawned_artifacts";
	
	public static void storeInBundle(Bundle bundle) {
		Float[] genProbs = categoryProbs.values().toArray(new Float[0]);
		float[] storeProbs = new float[genProbs.length];
		for (int i = 0; i < storeProbs.length; i++){
			storeProbs[i] = genProbs[i];
		}
		bundle.put( GENERAL_PROBS, storeProbs);
		
		bundle.put( SPAWNED_ARTIFACTS, spawnedArtifacts.toArray(new Class[0]));
	}

	public static void restoreFromBundle(Bundle bundle) {
		if (bundle.contains(GENERAL_PROBS)){
			float[] probs = bundle.getFloatArray(GENERAL_PROBS);
			for (int i = 0; i < probs.length; i++){
				categoryProbs.put(Category.values()[i], probs[i]);
			}
		} else {
			reset();
		}
		
		initArtifacts();
		
		for ( Class<?extends Artifact> artifact : bundle.getClassArray(SPAWNED_ARTIFACTS) ){
			removeArtifact(artifact);
		}
		
	}
}
