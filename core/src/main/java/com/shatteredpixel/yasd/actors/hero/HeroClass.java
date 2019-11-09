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

package com.shatteredpixel.yasd.actors.hero;

import com.shatteredpixel.yasd.Assets;
import com.shatteredpixel.yasd.Badges;
import com.shatteredpixel.yasd.Challenges;
import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.items.BrokenSeal;
import com.shatteredpixel.yasd.items.Generator;
import com.shatteredpixel.yasd.items.Item;
import com.shatteredpixel.yasd.items.TomeOfMastery;
import com.shatteredpixel.yasd.items.alcohol.Beer;
import com.shatteredpixel.yasd.items.alcohol.Whiskey;
import com.shatteredpixel.yasd.items.armor.ClothArmor;
import com.shatteredpixel.yasd.items.armor.HuntressArmor;
import com.shatteredpixel.yasd.items.armor.MageArmor;
import com.shatteredpixel.yasd.items.armor.RogueArmor;
import com.shatteredpixel.yasd.items.armor.WarriorArmor;
import com.shatteredpixel.yasd.items.artifacts.CloakOfShadows;
import com.shatteredpixel.yasd.items.bags.PotionBandolier;
import com.shatteredpixel.yasd.items.bags.ScrollHolder;
import com.shatteredpixel.yasd.items.bags.VelvetPouch;
import com.shatteredpixel.yasd.items.food.Food;
import com.shatteredpixel.yasd.items.food.SmallRation;
import com.shatteredpixel.yasd.items.potions.PotionOfHealing;
import com.shatteredpixel.yasd.items.potions.PotionOfInvisibility;
import com.shatteredpixel.yasd.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.yasd.items.potions.PotionOfMindVision;
import com.shatteredpixel.yasd.items.potions.PotionOfStrength;
import com.shatteredpixel.yasd.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.yasd.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.yasd.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.yasd.items.scrolls.ScrollOfRage;
import com.shatteredpixel.yasd.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.yasd.items.wands.WandOfLifeDrain;
import com.shatteredpixel.yasd.items.wands.WandOfMagicMissile;
import com.shatteredpixel.yasd.items.weapon.SpiritBow;
import com.shatteredpixel.yasd.items.weapon.melee.Crossbow;
import com.shatteredpixel.yasd.items.weapon.melee.Dagger;
import com.shatteredpixel.yasd.items.weapon.melee.Gloves;
import com.shatteredpixel.yasd.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.items.weapon.melee.Spear;
import com.shatteredpixel.yasd.items.weapon.melee.WornShortsword;
import com.shatteredpixel.yasd.items.weapon.missiles.ThrowingKnife;
import com.shatteredpixel.yasd.items.weapon.missiles.ThrowingStone;
import com.shatteredpixel.yasd.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.yasd.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.DeviceCompat;

public enum HeroClass {

	WARRIOR( "warrior", HeroSubClass.BERSERKER, HeroSubClass.GLADIATOR ),
	MAGE( "mage", HeroSubClass.BATTLEMAGE, HeroSubClass.WARLOCK ),
	ROGUE( "rogue", HeroSubClass.ASSASSIN, HeroSubClass.FREERUNNER ),
	HUNTRESS( "huntress", HeroSubClass.SNIPER, HeroSubClass.WARDEN );

	private String title;
	private HeroSubClass[] subClasses;

	HeroClass( String title, HeroSubClass...subClasses ) {
		this.title = title;
		this.subClasses = subClasses;
	}

	public void initHero( Hero hero ) {

		hero.heroClass = this;

		initCommon( hero );

		switch (this) {
			case WARRIOR:
				initWarrior( hero );
				break;

			case MAGE:
				initMage( hero );
				break;

			case ROGUE:
				initRogue( hero );
				break;

			case HUNTRESS:
				initHuntress( hero );
				break;
		}
		
	}

	private static void initCommon( Hero hero ) {
		Item i = new ClothArmor().identify();
		if (!Challenges.isItemBlocked(i))

		i = new Food();
		if (!Challenges.isItemBlocked(i)) i.collect();

		if (Dungeon.isChallenged(Challenges.NO_FOOD)){
			new SmallRation().collect();
		}
		
		new ScrollOfIdentify().identify().collect();

		new Beer().collect();
		//new WandOfLifeDrain().identify().collect();
		//new TomeOfMastery().collect();
		/*new WornShortsword().identify().collect();

		new Spear().identify().collect();
		new Crossbow().identify().collect();
		new Dart().quantity(5).collect();
		Generator.random(Generator.Category.ARTIFACT).identify().collect();
		new PotionOfStrength().quantity(10).identify().collect();*/
	}

	public Badges.Badge masteryBadge() {
		switch (this) {
			case WARRIOR:
				return Badges.Badge.MASTERY_WARRIOR;
			case MAGE:
				return Badges.Badge.MASTERY_MAGE;
			case ROGUE:
				return Badges.Badge.MASTERY_ROGUE;
			case HUNTRESS:
				return Badges.Badge.MASTERY_HUNTRESS;
		}
		return null;
	}

	private static void initWarrior( Hero hero ) {
		(hero.belongings.miscs[0] = new WornShortsword()).identify();
		(hero.belongings.miscs[1] = new WarriorArmor()).identify();
		ThrowingStone stones = new ThrowingStone();
		stones.quantity(3).collect();
		Dungeon.quickslot.setSlot(0, stones);

		new BrokenSeal().collect();
		
		new PotionBandolier().collect();
		Dungeon.LimitedDrops.POTION_BANDOLIER.drop();
		
		new PotionOfHealing().identify();
		new ScrollOfRage().identify();
	}

	private static void initMage( Hero hero ) {
		MagesStaff staff;
		
		staff = new MagesStaff(new WandOfMagicMissile());

		(hero.belongings.miscs[0] = staff).identify();
		(hero.belongings.miscs[1] = new MageArmor()).identify();
		hero.belongings.getWeapons().get(0).activate(hero);

		Dungeon.quickslot.setSlot(0, staff);

		new ScrollHolder().collect();
		Dungeon.LimitedDrops.SCROLL_HOLDER.drop();
		
		new ScrollOfUpgrade().identify();
		new PotionOfLiquidFlame().identify();
	}

	private static void initRogue( Hero hero ) {
		(hero.belongings.miscs[0] = new Dagger()).identify();
		(hero.belongings.miscs[1] = new RogueArmor()).identify();

		CloakOfShadows cloak = new CloakOfShadows();
		(hero.belongings.miscs[2] = cloak).identify();
		hero.belongings.miscs[2].activate( hero );

		ThrowingKnife knives = new ThrowingKnife();
		knives.quantity(3).collect();

		Dungeon.quickslot.setSlot(0, cloak);
		Dungeon.quickslot.setSlot(1, knives);

		new VelvetPouch().collect();
		Dungeon.LimitedDrops.VELVET_POUCH.drop();
		
		new ScrollOfMagicMapping().identify();
		new PotionOfInvisibility().identify();
	}

	private static void initHuntress( Hero hero ) {

		(hero.belongings.miscs[0] = new Gloves()).identify();
		(hero.belongings.miscs[1] = new HuntressArmor()).identify();
		SpiritBow bow = new SpiritBow();
		bow.identify().collect();

		Dungeon.quickslot.setSlot(0, bow);

		new VelvetPouch().collect();
		Dungeon.LimitedDrops.VELVET_POUCH.drop();
		
		new PotionOfMindVision().identify();
		new ScrollOfLullaby().identify();
	}
	
	public String title() {
		return Messages.get(HeroClass.class, title);
	}
	
	public HeroSubClass[] subClasses() {
		return subClasses;
	}
	
	public String spritesheet() {
		switch (this) {
			case WARRIOR: default:
				return Assets.WARRIOR;
			case MAGE:
				return Assets.MAGE;
			case ROGUE:
				return Assets.ROGUE;
			case HUNTRESS:
				return Assets.HUNTRESS;
		}
	}
	
	public String[] perks() {
		switch (this) {
			case WARRIOR: default:
				return new String[]{
						Messages.get(HeroClass.class, "warrior_perk1"),
						Messages.get(HeroClass.class, "warrior_perk2"),
						Messages.get(HeroClass.class, "warrior_perk3"),
						Messages.get(HeroClass.class, "warrior_perk4"),
						Messages.get(HeroClass.class, "warrior_perk5"),
				};
			case MAGE:
				return new String[]{
						Messages.get(HeroClass.class, "mage_perk1"),
						Messages.get(HeroClass.class, "mage_perk2"),
						Messages.get(HeroClass.class, "mage_perk3"),
						Messages.get(HeroClass.class, "mage_perk4"),
						Messages.get(HeroClass.class, "mage_perk5"),
				};
			case ROGUE:
				return new String[]{
						Messages.get(HeroClass.class, "rogue_perk1"),
						Messages.get(HeroClass.class, "rogue_perk2"),
						Messages.get(HeroClass.class, "rogue_perk3"),
						Messages.get(HeroClass.class, "rogue_perk4"),
						Messages.get(HeroClass.class, "rogue_perk5"),
				};
			case HUNTRESS:
				return new String[]{
						Messages.get(HeroClass.class, "huntress_perk1"),
						Messages.get(HeroClass.class, "huntress_perk2"),
						Messages.get(HeroClass.class, "huntress_perk3"),
						Messages.get(HeroClass.class, "huntress_perk4"),
						Messages.get(HeroClass.class, "huntress_perk5"),
				};
		}
	}
	
	public boolean isUnlocked(){
		//always unlock on debug builds
		if (DeviceCompat.isDebug()) return true;
		
		switch (this){
			case WARRIOR: default:
				return true;
			case MAGE:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_MAGE);
			case ROGUE:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_ROGUE);
			case HUNTRESS:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_HUNTRESS);
		}
	}
	
	public String unlockMsg() {
		switch (this){
			case WARRIOR: default:
				return "";
			case MAGE:
				return Messages.get(HeroClass.class, "mage_unlock");
			case ROGUE:
				return Messages.get(HeroClass.class, "rogue_unlock");
			case HUNTRESS:
				return Messages.get(HeroClass.class, "huntress_unlock");
		}
	}

	private static final String CLASS	= "class";
	
	public void storeInBundle( Bundle bundle ) {
		bundle.put( CLASS, toString() );
	}
	
	public static HeroClass restoreInBundle( Bundle bundle ) {
		String value = bundle.getString( CLASS );
		return value.length() > 0 ? valueOf( value ) : ROGUE;
	}
}
