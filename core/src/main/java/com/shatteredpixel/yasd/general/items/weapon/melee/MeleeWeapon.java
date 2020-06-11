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

package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.MainGame;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MeleeWeapon extends Weapon {
	{
		image = ItemSpriteSheet.SWORD;
	}

	protected String desc = null;

	public int tier = 1;

	public float defenseMultiplier = 0f;

	public float degradeFactor = 1f;

	@Override
	public boolean canDegrade() {
		return Constants.DEGRADATION;
	}

	@Override
	public int min(float lvl) {
		return  Math.round(tier +  //base
				lvl);    //level scaling
	}

	@Override
	public int max(float lvl) {
		int max = max((int) lvl, tier, getDamageMultiplier(curUser));
		if (max < min(lvl)) {
			max = min(lvl);
		}
		return max;
	}

	public static int max(int lvl, int tier, float damageMultiplier) {
		return (int) ((5*(tier+1) +    //base
				lvl*(tier*2))*damageMultiplier);   //level scaling
	}

	public int STRReq(int lvl){
		lvl = Math.max(0, lvl);
		//Str req is 7 + tier * 3, so 10 for T1, 13 for T2, 16 for T3, etc, and is decreased by 1 per upgrade.
		return  (7 + Math.round(tier * 3)) - lvl;
	}

	@Override
	public int defenseFactor(Char owner) {
		return (int) ((max(level(), tier, 1f)/2f)*defenseMultiplier);
	}

	@Override
	public String desc() {
		return desc == null ? "" : desc;
	}

	@Override
	public int damageRoll(Char owner) {

		int damage = augment.damageFactor(super.damageRoll( owner ));

		if (owner instanceof Hero & owner.STR() < Integer.MAX_VALUE) {
			int exStr = owner.STR() - STRReq();
			if (exStr > 0) {
				damage += Random.IntRange( 0, exStr );
			}
		}
		if (properties.contains(Property.SURPRISE_ATTK_BENEFIT)) {
			Char enemy = null;
			float bonus = 0;
			if (curUser instanceof Hero) {
				enemy = ((Hero) curUser).enemy();
			} else if (curUser instanceof Mob) {
				enemy = ((Mob) curUser).getEnemy();
			}
			if (enemy != null) {
				bonus = 1f-enemy.noticeChance(curUser, true);
			}
			if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(curUser) && curUser.canSurpriseAttack()) {
				damage *= (2 + bonus);
				if (damage < max()) {
					damage = max();
				}
			}
		}
		return damage;
	}

	private float getDamageMultiplier(Char owner) {
		float multiplier = 1f;
		multiplier *= DLY;
		multiplier *= degradeFactor;
		multiplier *= 1/ACC;
		multiplier *= 1/ (1 + defenseMultiplier);
		multiplier *= 3/(RCH+2f);
		if (properties.contains(Property.DUAL_HANDED)) {
			multiplier *= 1.2f;
		}
		if (breaksArmor(owner)) {
			multiplier *= 0.8f;
		}
		if (properties.contains(Property.CANT_SURPRISE_ATTK)) {
			multiplier *= 1.3;
		} else if (properties.contains(Property.SURPRISE_ATTK_BENEFIT)) {
			multiplier *= 0.6f;
		}
		return multiplier;
	}

	private static float randomStat() {
		int num = Random.Int(5, 20);
		return num/10f;
	}

	@Override
	public Item random() {
		Item item = super.random();
		return ((MeleeWeapon)item).initStats();
	}

	public void setName(String name) {
		this.name = name;
	}

	private void resetStats() {
		DLY = 1f;
		ACC = 1f;
		degradeFactor = 1f;
		defenseMultiplier = 0f;
		RCH = 1;
		properties = new ArrayList<>();
	}

	//Generates stats for the weapon.
	public MeleeWeapon initStats() {
		resetStats();
		if (Random.Int(5) == 0) {
			DLY = randomStat();
		}
		if (Random.Int(5) == 0) {
			ACC = randomStat();
		}
		if (Random.Int(5) == 0) {
			degradeFactor = randomStat();
		}
		if (Random.Int(5) == 0) {
			defenseMultiplier = Random.Float();
		}
		if (Random.Int(5) == 0) {
			RCH = Random.Int(1, 3);
		}
		for (Property property : Property.values()) {
			if (Random.Int(5) == 0 && property.canApply(this)) {
				properties.add(property);
			}
		}

		return matchProfile();
	}

	@Contract(" -> this")
	public MeleeWeapon matchProfile() {
		//Weapons that are only very slightly different from the basic weapon get it's image and description.
		float closestMatch = 1.1f;
		WeaponProfile closestMatchProfile = WeaponProfile.NONE;
		//Shuffle list first in case two are tied for first place, to give all an equal chance. Randomness is fine as the image variable is stored in bundles, so it won't change for an individual weapon.
		ArrayList<WeaponProfile> profiles = new ArrayList<>(Arrays.asList(WeaponProfile.values()));
		Collections.shuffle(profiles);
		for (WeaponProfile profile : profiles) {
			float importance = profile.match(this);
			if (importance > closestMatch) {
				closestMatch = importance;
				closestMatchProfile = profile;
			}
		}
		closestMatchProfile.copy(this);
		return this;
	}

	public int defaultSTRReq() {
		return Math.max(super.STRReq(),10);
	}

	@Override
	public int STRReq() {
		if (isEquipped(Dungeon.hero)) {
			return Dungeon.hero.belongings.getWeaponSTRReq();
		} else {
			return defaultSTRReq();
		}
	}

	@Override
	public void use(float amount, boolean override) {
		super.use(amount*DLY*degradeFactor, override);
	}

	@Override
	public String info() {

		String info = desc();

		if (levelKnown) {
			info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_known", tier, augment.damageFactor(min()), augment.damageFactor(max()), STRReq());
			if (STRReq() > Dungeon.hero.STR()) {
				info += " " + Messages.get(Weapon.class, "too_heavy");
			} else if (Dungeon.hero.STR() > STRReq()){
				info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
			}
		} else {
			info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_unknown", tier, min(0), max(0), STRReq(0));
			if (STRReq(0) > Dungeon.hero.STR()) {
				info += " " + Messages.get(MeleeWeapon.class, "probably_too_heavy");
			}
		}

		if (DLY != 1f | ACC != 1f | RCH != 1 | degradeFactor != 1 | !properties.isEmpty() | defenseFactor(curUser) > 0 || breaksArmor(curUser)) {

			info += "\n";

			if (DLY > 1f) {
				info += "\n" + Messages.get(MeleeWeapon.class, "delay_increase", Math.round((DLY-1f)*100));
			} else if (DLY < 1f) {
				info += "\n" + Messages.get(MeleeWeapon.class, "delay_decrease", Math.round((1f-DLY)*100));
			}

			if (degradeFactor > 1f) {
				info += "\n" + Messages.get(MeleeWeapon.class, "degrade_increase", Math.round((degradeFactor-1f)*100));
			} else if (degradeFactor < 1f) {
				info += "\n" + Messages.get(MeleeWeapon.class, "degrade_decrease", Math.round((1f-degradeFactor)*100));
			}

			if (ACC > 1f) {
				info += "\n" + Messages.get(MeleeWeapon.class, "acc_increase", Math.round((ACC-1f)*100));
			} else if (ACC < 1f) {
				info += "\n" + Messages.get(MeleeWeapon.class, "acc_decrease", Math.round((1f-ACC)*100));
			}

			if (RCH > 1) {
				info += "\n" + Messages.get(MeleeWeapon.class, "reach_increase", RCH - 1);
			}

			if (properties.contains(Property.DUAL_HANDED)) {
				info += "\n" + Messages.get(MeleeWeapon.class, "dual_wield_penalty");
			}

			if (breaksArmor(curUser)) {
				info += "\n" + Messages.get(MeleeWeapon.class, "breaks_armour");
			}

			if (properties.contains(Property.CANT_SURPRISE_ATTK)) {
				info += "\n" + Messages.get(MeleeWeapon.class, "cant_surprise_attk");
			}

			if (defenseFactor(curUser) > 0) {
				info += "\n" + Messages.get(MeleeWeapon.class, "blocks", 0,  defenseFactor(Dungeon.hero));
			}

			if (properties.contains(Property.SURPRISE_ATTK_BENEFIT)) {
				info += "\n" + Messages.get(MeleeWeapon.class, "sneak_benefit");
			}
		}

		switch (augment) {
			case SPEED:
				info += "\n\n" + Messages.get(Weapon.class, "faster");
				break;
			case DAMAGE:
				info += "\n\n" + Messages.get(Weapon.class, "stronger");
				break;
			case NONE:
		}

		if (enchantment != null && cursedKnown) {
			info += "\n\n" + Messages.get(Weapon.class, "enchanted", enchantment.name());
			info += " " + Messages.get(enchantment, "desc");
		}

		if (cursed && isEquipped( Dungeon.hero )) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
		} else if (cursedKnown && cursed) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed");
		} else if (!isIdentified() && cursedKnown){
			info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
		}
		
		return info;
	}
	
	@Override
	public int price() {
		int price = 20 * tier;
		if (hasGoodEnchant()) {
			price *= 1.5;
		}
		if (cursedKnown && (cursed || hasCurseEnchant())) {
			price /= 2;
		}
		if (levelKnown && level() > 0) {
			price *= (level() + 1);
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}

	public MeleeWeapon setTier(int tier) {
		this.tier = tier;
		updateTier();
		return this;
	}

	public MeleeWeapon upgradeTier(int tier) {
		this.tier += tier;
		updateTier();
		return this;
	}

	public MeleeWeapon degradeTier(int tier) {
		this.tier -= tier;
		updateTier();
		return this;
	}

	private void updateTier() {

	}

	private static final String TIER = "tier";
	private static final String DELAY = "delay";
	private static final String DEGRADE = "degrade-factor";
	private static final String ACCURACY = "accuracy";
	private static final String DEFENSEFACTOR = "defense-factor";
	private static final String REACH = "reach";
	private static final String PROPERTIES = "props";
	private static final String PROPERTIES_AMT = "num-props";
	private static final String IMG = "image";
	private static final String NAME = "name";
	private static final String DESC = "desc";

	@Override
	public void storeInBundle(  Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(TIER, tier);
		bundle.put(DELAY, DLY);
		bundle.put(DEGRADE, degradeFactor);
		bundle.put(ACCURACY, ACC);
		bundle.put(DEFENSEFACTOR, defenseMultiplier);
		bundle.put(REACH, RCH);
		bundle.put(PROPERTIES_AMT, properties.size());
		bundle.put(NAME, name);
		bundle.put(IMG, image);
		bundle.put(DESC, desc);
		for (int i = 0; i < properties.size(); i++) {
			bundle.put(PROPERTIES+i, properties.get(i));
		}
	}

	@Override
	public void restoreFromBundle(  Bundle bundle) {
		super.restoreFromBundle(bundle);
		tier = bundle.getInt(TIER);
		if (Dungeon.version > MainGame.v0_3_6) {
			DLY = bundle.getFloat(DELAY);
			degradeFactor = bundle.getFloat(DEGRADE);
			ACC = bundle.getFloat(ACCURACY);
			defenseMultiplier = bundle.getFloat(DEFENSEFACTOR);
			RCH = bundle.getInt(REACH);
			desc = bundle.getString(DESC);
			name = bundle.getString(NAME);
			image = bundle.getInt(IMG);
			int numProps = bundle.getInt(PROPERTIES_AMT);
			for (int i = 0; i < numProps; i++) {
				properties.add(bundle.getEnum(PROPERTIES+i, Property.class));
			}
		} else {
			desc = super.desc();
			name = Messages.get(this, "name");
			properties = new ArrayList<>();
		}
	}
}
