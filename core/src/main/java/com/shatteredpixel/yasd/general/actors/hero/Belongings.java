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

package com.shatteredpixel.yasd.general.actors.hero;

import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.GamesInProgress;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Berserk;
import com.shatteredpixel.yasd.general.actors.buffs.Burning;
import com.shatteredpixel.yasd.general.actors.buffs.Fury;
import com.shatteredpixel.yasd.general.actors.buffs.Invisibility;
import com.shatteredpixel.yasd.general.actors.buffs.Momentum;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.items.EquipableItem;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.KindOfWeapon;
import com.shatteredpixel.yasd.general.items.KindofMisc;
import com.shatteredpixel.yasd.general.items.armor.Armor;
import com.shatteredpixel.yasd.general.items.armor.RogueArmor;
import com.shatteredpixel.yasd.general.items.armor.curses.Bulk;
import com.shatteredpixel.yasd.general.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.yasd.general.items.armor.glyphs.Brimstone;
import com.shatteredpixel.yasd.general.items.armor.glyphs.Flow;
import com.shatteredpixel.yasd.general.items.armor.glyphs.Obfuscation;
import com.shatteredpixel.yasd.general.items.armor.glyphs.Stone;
import com.shatteredpixel.yasd.general.items.armor.glyphs.Swiftness;
import com.shatteredpixel.yasd.general.items.artifacts.CapeOfThorns;
import com.shatteredpixel.yasd.general.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.yasd.general.items.bags.Bag;
import com.shatteredpixel.yasd.general.items.keys.Key;
import com.shatteredpixel.yasd.general.items.rings.RingOfFuror;
import com.shatteredpixel.yasd.general.items.rings.RingOfHaste;
import com.shatteredpixel.yasd.general.items.rings.RingOfTenacity;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.yasd.general.items.wands.Wand;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.yasd.general.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.yasd.general.levels.Terrain;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Iterator;

public class Belongings implements Iterable<Item> {

	public static final int BACKPACK_SIZE	= 20;
	
	private Char owner;
	
	public Bag backpack;
	public int currentWeapon = 0;
	public KindofMisc[] miscs = new KindofMisc[Constants.MISC_SLOTS];

	public Belongings( Char owner ) {
		this.owner = owner;

		backpack = new Bag() {{
			name = Messages.get(Bag.class, "name");
			size = BACKPACK_SIZE;
		}};
		backpack.owner = owner;
	}

	//##############################################################################################
	//########################## Stuff for handling chars with belongings ##########################
	//##############################################################################################

	private ArrayList<Integer> availibleSlots(KindofMisc misc) {
		ArrayList<Integer> slots = new ArrayList<>();
		slots.add(0);
		slots.add(1);
		slots.add(2);
		slots.add(3);
		slots.add(4);
		return slots;
	}

	public boolean canEquip(KindofMisc misc, int slot) {//Use for setting specific properties for each slot.
		return miscs[slot] == null && availibleSlots(misc).contains(slot);
	}

	public boolean canEquip(KindofMisc misc) {//Use for setting specific properties for each slot.
		for (int i = 0; i < miscs.length; i++) {
			if (canEquip(misc, i)) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<Armor> getArmors() {
		ArrayList<Armor> armors = new ArrayList<>();
		for (KindofMisc misc : miscs) {
			if (misc instanceof Armor) {
				armors.add((Armor) misc);
			}
		}
		return armors;

	}

	public ArrayList<KindOfWeapon> getWeapons() {
		ArrayList<KindOfWeapon> weapons = new ArrayList<>();
		for (KindofMisc misc : miscs) {
			if (misc instanceof MeleeWeapon) {
				weapons.add((KindOfWeapon) misc);
			}
		}
		return weapons;

	}

	public ArrayList<KindofMisc> getEquippedItemsOFType( Class type ) {//Find equipped items of a certain kind
		ArrayList<KindofMisc> items = new ArrayList<>();
		for (KindofMisc misc : miscs) {
			if (type.isInstance(misc)) {
				items.add(misc);
			}
		}
		return items;
	}

	public int getWeaponSTRReq() {
		ArrayList<KindOfWeapon> weapons = getWeapons();
		int TotalRequirement = 8;
		int IndividualRequirement;
		for (int i=0; i < weapons.size(); i++) {
			if (weapons.get(i) instanceof MeleeWeapon) {
				if (weapons.size() > 1 & ((MeleeWeapon) weapons.get(i)).dualWieldpenalty) {//If the weapon has a dual wield penalty and it is being used with another weapon, increase total requirement.
					TotalRequirement += 2;
				}
				IndividualRequirement = ((MeleeWeapon)weapons.get(i)).defaultSTRReq();
				IndividualRequirement -= 8;
				TotalRequirement += IndividualRequirement;
			}

		}
		return TotalRequirement;
	}

	public boolean shoot(Char enemy, MissileWeapon wep) {

		//temporarily set the hero's weapon to the missile weapon being used
		KindofMisc equipped = miscs[0];
		miscs[0] = wep;
		boolean hit = owner.attack(enemy);
		Invisibility.dispel();
		miscs[0] = equipped;

		return hit;
	}

	public int numberOfWeapons() {
		return getWeapons().size();
	}

	public void resetWeapon() {
		if (currentWeapon > (numberOfWeapons() - 1)) {
			currentWeapon = 0;
		}
	}

	public void nextWeapon() {
		currentWeapon += 1;
		resetWeapon();
	}

	public KindOfWeapon getCurrentWeapon() {
	    if (getWeapons().size() == 0) {
	        return null;
        }
		resetWeapon();
		if (miscs[0] instanceof MissileWeapon) {
			return ((MissileWeapon) miscs[0]);
		}
		return getWeapons().get(currentWeapon);
	}

	public float accuracyFactor(float accuracy, Char target) {
		KindOfWeapon wep = getCurrentWeapon();
		//accuracy *= RingOfPerception.accuracyMultiplier(owner);
		if (wep instanceof MissileWeapon) {
			if (Dungeon.level.adjacent(owner.pos, target.pos)) {
				accuracy *= 0.5f;
			} else {
				accuracy *= 1.5f;
			}
		}

		if (wep != null) {
			accuracy *= wep.accuracyFactor(owner);
		}
		return accuracy;
	}

	public int magicalDR() {
		int dr = 0;
		ArrayList<Armor> Armors = getArmors();
		int degradeAmount = new Item().defaultDegradeAmount();
		for (int i = 0; i < Armors.size(); i++) {
			Armors.get(i).use(degradeAmount / Armors.size());
			int armDr = Armors.get(i).magicalDRRoll();
			if (owner.STR() < Armors.get(i).STRReq()) {
				armDr -= 2 * (Armors.get(i).STRReq() - owner.STR());
			}
			if (armDr > 0) dr += armDr;
			if (Armors.get(i) != null && Armors.get(i).hasGlyph(AntiMagic.class, owner)) {
				dr += AntiMagic.drRoll(Armors.get(i).level());
			}
		}
		return dr;
	}

	public int drRoll() {
		int dr = 0;
		ArrayList<Armor> Armors = getArmors();
		if (Armors.size() > 0) {
			Armors.get(0).use();
			for (int i = 0; i < Armors.size(); i++) {
				int armDr = Armors.get(i).DRRoll();
				if (owner.STR() < Armors.get(i).STRReq()) {
					armDr -= 2 * (Armors.get(i).STRReq() - owner.STR());
				}
				if (armDr > 0) dr += armDr;
			}
		}

		ArrayList<KindOfWeapon> Weapons = getWeapons();
		if (Weapons.size() > 0) {
			for (int i = 0; i < Weapons.size(); i++) {
				int wepDr = Random.NormalIntRange(0, Weapons.get(i).defenseFactor(owner));
				if (Weapons.get(i) instanceof MeleeWeapon & owner.STR() < ((MeleeWeapon) Weapons.get(i)).STRReq()) {
					wepDr -= 2 * (((MeleeWeapon) Weapons.get(i)).STRReq()) - owner.STR();
				}
				if (wepDr > 0) dr += wepDr;
			}
		}
		return dr;
	}

	public int damageRoll() {
		int dmg;
		KindOfWeapon wep = getCurrentWeapon();
		if (wep != null) {
			if (wep instanceof MeleeWeapon) {
				wep.use();
			}
			dmg = wep.damageRoll(owner);
			//if (!(wep instanceof MissileWeapon)) dmg += RingOfForce.armedDamageBonus(owner);
		} else {
			int level = 0;
			if (owner instanceof Mob) {
				level = ((Mob) owner).EXP;
			} else if (owner instanceof Hero) {
				level = ((Hero)owner).lvl/2;
			}
			dmg = Random.Int(1, level);
			//dmg = RingOfForce.damageRoll(owner);
		}
		if (dmg < 0) dmg = 0;

		Berserk berserk = owner.buff(Berserk.class);
		if (berserk != null) dmg = berserk.damageFactor(dmg);

		return owner.buff(Fury.class) != null ? (int) (dmg * 1.5f) : dmg;
	}

	public int getArmorSTRReq() {
		ArrayList<Armor> armors = getArmors();
		int TotalRequirement = 8;
		int IndividualRequirement;
		for (int i=0; i < armors.size(); i++) {
			IndividualRequirement = armors.get(i).defaultSTRReq();
			IndividualRequirement -= 8;
			TotalRequirement += IndividualRequirement;


		}
		return TotalRequirement;
	}

	public int defenseProc(Char enemy, int damage) {
		ArrayList<Armor> Armors = getArmors();//Proc all armours 1 by 1
		for (int i=0; i < Armors.size(); i++) {
			damage = Armors.get(i).proc(enemy, owner, damage);
		}
		return damage;
	}

	public int attackProc(Char enemy, int damage) {
		KindOfWeapon wep = getCurrentWeapon();

		if (wep != null) damage = wep.proc(owner, enemy, damage);
		return damage;
	}

	public int magicalAttackProc(Char enemy, int damage) {
		return damage;
	}

	public int magicalDefenseProc(Char enemy, int damage) {
		ArrayList<Armor> Armors = getArmors();//Proc all armours 1 by 1
		for (int i=0; i < Armors.size(); i++) {
			damage = Armors.get(i).magicalProc(enemy, owner, damage);
		}
		return damage;
	}

	public boolean canSurpriseAttack() {
		KindOfWeapon curWep = getCurrentWeapon();
		if (!(curWep instanceof Weapon)) return true;
		if (owner.STR() < ((Weapon) curWep).STRReq()) return false;
		return curWep.canSurpriseAttack;
	}

	public int affectDamage(int damage, Object src) {
		if (owner.buff(TimekeepersHourglass.timeStasis.class) != null) {
			return 0;
		}
		CapeOfThorns.Thorns thorns = owner.buff(CapeOfThorns.Thorns.class);
		if (thorns != null) {
			damage = thorns.proc(damage, (src instanceof Char ? (Char) src : null), owner);
		}

		damage = (int) Math.ceil(damage * RingOfTenacity.damageMultiplier(owner));

		return damage;
	}

	public float attackDelay() {
		float multiplier = 1f;
		multiplier /= numberOfWeapons();
		if (numberOfWeapons() > 0) {
			return getCurrentWeapon().speedFactor(owner) * multiplier;//Two weapons = 1/2 attack speed
		} else {
			//Normally putting furor speed on unarmed attacks would be unnecessary
			//But there's going to be that one guy who gets a furor+force ring combo
			//This is for that one guy, you shall get your fists of fury!
			return RingOfFuror.attackDelayMultiplier(owner);
		}
	}


	public float EvasionFactor(float evasion) {
		ArrayList<Armor> Armors = getArmors();
		//evasion *= _Unused.evasionMultiplier(owner);
		for (int i=0; i < Armors.size(); i++) {
			Armor CurArmour = Armors.get(i);
			//evasion *= CurArmour.evasionMultiplier(ownerID);
			if (CurArmour.hasGlyph(Stone.class, owner) && !((Stone) CurArmour.glyph).testingEvasion()) {
				return 0;
			}
			int aEnc = CurArmour.STRReq() -  owner.STR();
			if (aEnc > 0) evasion /= Math.pow(1.5, aEnc);

			Momentum momentum = owner.buff(Momentum.class);
			if (momentum != null) {
				evasion += momentum.evasionBonus(Math.max(0, -aEnc));

			}
			evasion += CurArmour.augment.evasionFactor(CurArmour.level());

			evasion *= CurArmour.EVA;

		}
		return evasion;
	}

	public float SpeedFactor(float speed) {
		ArrayList<Armor> Armors = getArmors();
		speed *= RingOfHaste.speedMultiplier(owner);
		for (int i=0; i < Armors.size(); i++) {
			Armor CurArmour = Armors.get(i);
			//speed *= CurArmour.speedMultiplier(ownerID);
			int aEnc = CurArmour.STRReq() - owner.STR();
			if (aEnc > 0) speed /= Math.pow(1.2, aEnc);

			if (CurArmour.hasGlyph(Swiftness.class, owner)) {
				boolean enemyNear = false;
				for (Char ch : Actor.chars()){
					if (Dungeon.level.adjacent(ch.pos, owner.pos) && owner.alignment != ch.alignment){
						enemyNear = true;
						break;
					}
				}
				if (!enemyNear) speed *= (1.2f + 0.04f * CurArmour.level());
			} else if (CurArmour.hasGlyph(Flow.class, owner) && Dungeon.level.liquid(owner.pos)){
				speed *= 2f;
			}

			if (CurArmour.hasGlyph(Bulk.class, owner) &&
					(Dungeon.level.map[owner.pos] == Terrain.DOOR
							|| Dungeon.level.map[owner.pos] == Terrain.OPEN_DOOR )) {
				speed /= 3f;
			}
			speed *= CurArmour.speedFactor;

		}
		return speed;
	}

	public float StealthFactor(float stealth) {
		ArrayList<Armor> Armors = getArmors();
		for (int i=0; i < Armors.size(); i++) {
			Armor CurArmour = Armors.get(i);
			//sneakSkill *= CurArmour.stealthMultiplier(ownerID);

			if (CurArmour.hasGlyph(Obfuscation.class, owner)){
				stealth += 1 + CurArmour.level()/3f;
			}
			int penalty = CurArmour.tier;
			if (CurArmour instanceof RogueArmor) {
				stealth += CurArmour.level() + 1;
			} else {
				if (owner instanceof Hero) {
					if (((Hero)owner).heroClass == HeroClass.ROGUE) {
						penalty /= 2;
					}
				}
				stealth -= penalty;
			}
			stealth *= CurArmour.STE;
		}
		return stealth;
	}

	public boolean isImmune(Class effect) {
		//if *any* armour has Brimstone Glyph
		ArrayList<Armor> Armors = getArmors();
		for (int i=0; i < Armors.size(); i++) {
			if (effect == Burning.class
					&& Armors.get(i) != null
					&& Armors.get(i).hasGlyph(Brimstone.class, owner)) {
				return true;
			}
		}
		return false;
	}

	//##############################################################################################
	//####################### End of stuff for handling chars with belongings ######################
	//##############################################################################################

	private static final String ARMOR		= "getArmors";
	private static final String MISC        = "misc";


	public void storeInBundle( Bundle bundle ) {
		
		backpack.storeInBundle( bundle );
		for (int i = 0; i < Constants.MISC_SLOTS; i++) {//Store all miscs
			bundle.put( MISC + i, miscs[i]);
		}
		if (getArmors().size() > 0) {
			bundle.put(ARMOR, getArmors().get(0));//Used for previewing games.
		}
	}
	
	public void restoreFromBundle( Bundle bundle ) {
		
		backpack.clear();
		backpack.restoreFromBundle( bundle );
		for (int i = 0; i < Constants.MISC_SLOTS; i++) {//Restore all miscs
			miscs[i] = (KindofMisc)bundle.get(MISC + i);
			if (miscs[i] != null) {
				miscs[i].activate( owner );
			}
		}
	}
	
	public static void preview( GamesInProgress.Info info, Bundle bundle ) {
		if (bundle.contains( ARMOR )){
			info.armorTier = ((Armor)bundle.get( ARMOR )).tier;
		} else {
			info.armorTier = 0;
		}
	}
	
	@SuppressWarnings("unchecked")
	public<T extends Item> T getItem( Class<T> itemClass ) {

		for (Item item : this) {
			if (itemClass.isInstance( item )) {
				return (T)item;
			}
		}
		
		return null;
	}
	
	public boolean contains( Item contains ){
		
		for (Item item : this) {
			if (contains == item ) {
				return true;
			}
		}
		
		return false;
	}
	
	public Item getSimilar( Item similar ){
		
		for (Item item : this) {
			if (similar != item && similar.isSimilar(item)) {
				return item;
			}
		}
		
		return null;
	}
	
	public ArrayList<Item> getAllSimilar( Item similar ){
		ArrayList<Item> result = new ArrayList<>();
		
		for (Item item : this) {
			if (item != similar && similar.isSimilar(item)) {
				result.add(item);
			}
		}
		
		return result;
	}
	
	public void identify() {
		for (Item item : this) {
			item.identify();
		}
	}
	
	public void observe() {
		for (int i = 0; i < Constants.MISC_SLOTS; i++) {//Restore all miscs
			if (miscs[i] != null) {
				miscs[i].identify();
				Badges.validateItemLevelAquired(miscs[i]);
			}
		}

		for (Item item : backpack) {
			if (item instanceof EquipableItem) {
				item.cursedKnown = true;
			}
		}
	}
	
	public void uncurseEquipped() {
		ScrollOfRemoveCurse.uncurse( owner, miscs[0], miscs[1], miscs[2], miscs[3], miscs[4]);
	}
	
	public Item randomUnequipped() {
		return Random.element( backpack.items );
	}
	
	public void resurrect( int depth ) {

		for (Item item : backpack.items.toArray( new Item[0])) {
			if (item instanceof Key) {
				if (((Key)item).yPos == depth) {
					item.detachAll( backpack );
				}
			} else if (item.unique) {
				item.detachAll(backpack);
				//you keep the bag itself, not its contents.
				if (item instanceof Bag){
					((Bag)item).resurrect();
				}
				item.collect();
			} else if (!item.isEquipped( owner )) {
				item.detachAll( backpack );
			}
		}

		for (int i = 0; i < Constants.MISC_SLOTS; i++) {//Restore all miscs
			if (miscs[i] != null) {
				miscs[i].cursed = false;
				miscs[i].activate( owner );
			}
		}
	}
	
	public int charge( float charge ) {
		
		int count = 0;
		
		for (Wand.Charger charger : owner.buffs(Wand.Charger.class)){
			charger.gainCharge(charge);
			count++;
		}
		
		return count;
	}

	@Override
	public Iterator<Item> iterator() {
		return new ItemIterator();
	}
	
	private class ItemIterator implements Iterator<Item> {

		private int index = 0;
		
		private Iterator<Item> backpackIterator = backpack.iterator();
		
		private Item[] equipped = { miscs[0], miscs[1],  miscs[2], miscs[3], miscs[4]};
		private int backpackIndex = equipped.length;
		
		@Override
		public boolean hasNext() {
			
			for (int i=index; i < backpackIndex; i++) {
				if (equipped[i] != null) {
					return true;
				}
			}
			
			return backpackIterator.hasNext();
		}

		@Override
		public Item next() {
			
			while (index < backpackIndex) {
				Item item = equipped[index++];
				if (item != null) {
					return item;
				}
			}
			
			return backpackIterator.next();
		}

		@Override
		public void remove() {
			switch (index) {
				case 0:
					equipped[0] = miscs[0] = null;
					break;
				case 1:
					equipped[1] = miscs[1] = null;
					break;
				case 2:
					equipped[2] = miscs[2] = null;
					break;
				case 3:
					equipped[3] = miscs[3] = null;
					break;
				case 4:
					equipped[4] = miscs[4] = null;
					break;
				case 5:
					equipped[5] = miscs[5] = null;
					break;
				default:
					backpackIterator.remove();
			}
		}
	}
}
