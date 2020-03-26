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
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

public class MeleeWeapon extends Weapon {
	
	public int tier;

	public float damageMultiplier = 1f;
	public float defenseFactorMultiplier = 0f;

	public boolean dualWieldpenalty = false;
	public boolean sneakBenefit = false;

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
		return (int) ((5*(tier+1) +    //base
				lvl*(tier*2))*damageMultiplier);   //level scaling
	}

	public int STRReq(int lvl){
		lvl = Math.max(0, lvl);
		//strength req decreases at +1,+3,+6,+10,etc.
		return  (7 + Math.round(tier * 3)) - lvl;
	}
	
	@Override
	public int damageRoll(Char owner) {
		if (sneakBenefit) {
			Char enemy = null;
			if (owner instanceof Hero) {
				enemy = ((Hero) owner).enemy();
			} else if (owner instanceof Mob){
				enemy = ((Mob)owner).getEnemy();
			}
			if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(owner)) {
				//deals 50% toward max to max on surprise (plus more if the hero's sneakSkill is higher), instead of min to max.
				float multiplier = 0.5f + (1f - enemy.noticeChance(owner)) * 0.5f;
				int diff = max() - min();
				int damage = augment.damageFactor(Random.NormalIntRange(
						min() + Math.round(diff*multiplier),
						max()));
				int exStr = owner.STR() - STRReq();
				if (exStr > 0) {
					damage += Random.IntRange(0, exStr);
				}
				return damage;
			}
		}
		int damage = augment.damageFactor(super.damageRoll( owner ));

		if (owner instanceof Hero & owner.STR() != Integer.MAX_VALUE) {
			int exStr = owner.STR() - STRReq();
			if (exStr > 0) {
				damage += Random.IntRange( 0, exStr );
			}
		}
		
		return damage;
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
		super.use(amount*DLY, override);
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

		//String statsInfo = statsInfo();
		//if (!statsInfo.equals("")) info += "\n\n" + statsInfo;
		if (DLY != 1f | ACC != 1f | RCH != 1 | dualWieldpenalty | breaksArmor(Dungeon.hero) | !canSurpriseAttack | defenseFactor(Dungeon.hero) > 0 | sneakBenefit) {

			info += "\n";

			if (DLY > 1f) {
				info += "\n" + Messages.get(MeleeWeapon.class, "delay_increase", Math.round((DLY-1f)*100));
			} else if (DLY < 1f) {
				info += "\n" + Messages.get(MeleeWeapon.class, "delay_decrease", Math.round((1f-DLY)*100));
			}

			if (ACC > 1f) {
				info += "\n" + Messages.get(MeleeWeapon.class, "acc_increase", Math.round((ACC-1f)*100));
			} else if (ACC < 1f) {
				info += "\n" + Messages.get(MeleeWeapon.class, "acc_decrease", Math.round((1f-ACC)*100));
			}

			if (RCH > 1f) {
				info += "\n" + Messages.get(MeleeWeapon.class, "reach_increase", RCH - 1);
			}

			if (dualWieldpenalty) {
				info += "\n" + Messages.get(MeleeWeapon.class, "dual_wield_penalty");
			}

			if (breaksArmor(Dungeon.hero)) {
				info += "\n" + Messages.get(MeleeWeapon.class, "breaks_armour");
			}

			if (!canSurpriseAttack) {
				info += "\n" + Messages.get(MeleeWeapon.class, "cant_surprise_attk");
			}

			if (defenseFactor(Dungeon.hero) > 0) {
				info += "\n" + Messages.get(MeleeWeapon.class, "blocks", 0,  defenseFactor(Dungeon.hero));
			}

			if (sneakBenefit) {
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

		if (enchantment != null && (cursedKnown || !enchantment.curse())){
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
	
	/*public String statsInfo(){
		return Messages.get(this, "stats_desc");
	}*/
	
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

	public static String TIER = "tier";

	@Override
	public void storeInBundle(@NotNull Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(TIER, tier);
	}

	@Override
	public void restoreFromBundle(@NotNull Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (Dungeon.version >= MainGame.v0_2_0) {//Support older saves
			tier = bundle.getInt(TIER);
		}
	}
}
