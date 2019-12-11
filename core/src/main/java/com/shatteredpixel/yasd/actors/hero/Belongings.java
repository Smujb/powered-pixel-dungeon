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

import com.shatteredpixel.yasd.Badges;
import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.GamesInProgress;
import com.shatteredpixel.yasd.actors.Actor;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.buffs.Momentum;
import com.shatteredpixel.yasd.items.EquipableItem;
import com.shatteredpixel.yasd.items.Item;
import com.shatteredpixel.yasd.items.KindOfWeapon;
import com.shatteredpixel.yasd.items.KindofMisc;
import com.shatteredpixel.yasd.items.armor.Armor;
import com.shatteredpixel.yasd.items.armor.ClothArmor;
import com.shatteredpixel.yasd.items.armor.HuntressArmor;
import com.shatteredpixel.yasd.items.armor.MageArmor;
import com.shatteredpixel.yasd.items.armor.RogueArmor;
import com.shatteredpixel.yasd.items.armor.WarriorArmor;
import com.shatteredpixel.yasd.items.armor.curses.Bulk;
import com.shatteredpixel.yasd.items.armor.glyphs.Flow;
import com.shatteredpixel.yasd.items.armor.glyphs.Obfuscation;
import com.shatteredpixel.yasd.items.armor.glyphs.Stone;
import com.shatteredpixel.yasd.items.armor.glyphs.Swiftness;
import com.shatteredpixel.yasd.items.bags.Bag;
import com.shatteredpixel.yasd.items.keys.Key;
import com.shatteredpixel.yasd.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.yasd.items.wands.Wand;
import com.shatteredpixel.yasd.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.yasd.items.weapon.melee.WornShortsword;
import com.shatteredpixel.yasd.levels.Terrain;
import com.shatteredpixel.yasd.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Iterator;

public class Belongings implements Iterable<Item> {

	public static final int BACKPACK_SIZE	= 20;
	
	private Char owner;
	
	public Bag backpack;

	public ArrayList<Armor> getArmors() {
		ArrayList<Armor> armors = new ArrayList<>();

		if (miscs[0] instanceof Armor) {
			armors.add((Armor) miscs[0]);
		}

		if (miscs[1] instanceof Armor) {
			armors.add((Armor) miscs[1]);
		}

		if (miscs[2] instanceof Armor) {
			armors.add((Armor) miscs[2]);
		}

		if (miscs[3] instanceof Armor) {
			armors.add((Armor) miscs[3]);
		}

		if (miscs[4] instanceof Armor) {
			armors.add((Armor) miscs[4]);
		}
		if (armors.isEmpty()) {
			armors.add(new ClothArmor());
		}
		return armors;

	}

	public ArrayList<KindOfWeapon> getWeapons() {
		ArrayList<KindOfWeapon> weapons = new ArrayList<>();

		if (miscs[0] instanceof MeleeWeapon) {
			weapons.add((KindOfWeapon) miscs[0]);
		}

		if (miscs[1] instanceof MeleeWeapon) {
			weapons.add((KindOfWeapon) miscs[1]);
		}

		if (miscs[2] instanceof MeleeWeapon) {
			weapons.add((KindOfWeapon) miscs[2]);
		}

		if (miscs[3] instanceof MeleeWeapon) {
			weapons.add((KindOfWeapon) miscs[3]);
		}

		if (miscs[4] instanceof MeleeWeapon) {
			weapons.add((KindOfWeapon) miscs[4]);
		}
		if (weapons.isEmpty()) {
			weapons.add(new WornShortsword());
		}
		return weapons;

	}

	public ArrayList<Item> getEquippedItemsOFType( Class type ) {//Find equipped items of a certain kind
		ArrayList<Item> items = new ArrayList<>();

		for (int i = 0; i < miscs.length; i++) {
			if (type.isInstance( miscs[i])) {
				items.add(miscs[i]);
			}
		}

		/*if (type.isInstance( miscs[0] )) {
			items.add(miscs[0]);
		}
		if (miscs[1].getClass() == type) {
			items.add(miscs[1]);
		}
		if (miscs[2].getClass() == type) {
			items.add(miscs[2]);
		}
		if (miscs[3].getClass() == type) {
			items.add(miscs[3]);
		}
		if (miscs[4].getClass() == type) {
			items.add(miscs[4]);
		}*/
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

	public KindofMisc[] miscs = new KindofMisc[5];


	public Belongings( Char owner ) {
		this.owner = owner;
		
		backpack = new Bag() {{
			name = Messages.get(Bag.class, "name");
			size = BACKPACK_SIZE;
		}};
		backpack.owner = owner;
	}

	public float EvasionFactor(float evasion) {
		ArrayList<Armor> Armors = getArmors();
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
			if (CurArmour instanceof HuntressArmor || CurArmour instanceof MageArmor || CurArmour instanceof RogueArmor) {
				evasion*=1.25;
			}
			if (CurArmour instanceof WarriorArmor) {
				evasion*=0.75;
			}

		}
		return evasion;
	}

	public float SpeedFactor(float speed) {
		ArrayList<Armor> Armors = getArmors();
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
			} else if (CurArmour.hasGlyph(Flow.class, owner) && Dungeon.level.water[owner.pos]){
				speed *= 2f;
			}

			if (CurArmour.hasGlyph(Bulk.class, owner) &&
					(Dungeon.level.map[owner.pos] == Terrain.DOOR
							|| Dungeon.level.map[owner.pos] == Terrain.OPEN_DOOR )) {
				speed /= 3f;
			}
			if (CurArmour instanceof HuntressArmor) {
				speed*=1.25;
			}

		}
		return speed;
	}

	public float StealthFactor(float stealth) {
		ArrayList<Armor> Armors = getArmors();
		for (int i=0; i < Armors.size(); i++) {
			Armor CurArmour = Armors.get(i);
			//stealth *= CurArmour.stealthMultiplier(ownerID);

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
			if (CurArmour instanceof WarriorArmor) {
				stealth*=0.75;
			}
		}
		return stealth;
	}
	private static final String ARMOR		= "getArmors";
	private static final String MISC1       = "misc1";
	private static final String MISC2       = "misc2";
	private static final String MISC3       = "misc3";
	private static final String MISC4       = "misc4";
	private static final String MISC5       = "misc5";


	public void storeInBundle( Bundle bundle ) {
		
		backpack.storeInBundle( bundle );
		bundle.put( MISC1, miscs[0]);
		bundle.put( MISC2, miscs[1]);
		bundle.put( MISC3, miscs[2]);
		bundle.put( MISC4, miscs[3]);
		bundle.put( MISC5, miscs[4]);
		bundle.put( ARMOR, getArmors().get(0));//Used for previewing games.
	}
	
	public void restoreFromBundle( Bundle bundle ) {
		
		backpack.clear();
		backpack.restoreFromBundle( bundle );
		
		miscs[0] = (KindofMisc)bundle.get(MISC1);
		if (miscs[0] != null) {
			miscs[0].activate( owner );
		}
		
		miscs[1] = (KindofMisc)bundle.get(MISC2);
		if (miscs[1] != null) {
			miscs[1].activate( owner );
		}

		miscs[2] = (KindofMisc)bundle.get(MISC3);
		if (miscs[2] != null) {
			miscs[2].activate( owner );
		}

		miscs[3] = (KindofMisc)bundle.get(MISC4);
		if (miscs[3] != null) {
			miscs[3].activate( owner );
		}

		miscs[4] = (KindofMisc)bundle.get(MISC5);
		if (miscs[4] != null) {
			miscs[4].activate( owner );
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
		if (miscs[0] != null) {
			miscs[0].identify();
			Badges.validateItemLevelAquired(miscs[0]);
		}
		if (miscs[1] != null) {
			miscs[1].identify();
			Badges.validateItemLevelAquired(miscs[1]);
		}

		if (miscs[2] != null) {
			miscs[2].identify();
			Badges.validateItemLevelAquired(miscs[2]);
		}

		if (miscs[3] != null) {
			miscs[3].identify();
			Badges.validateItemLevelAquired(miscs[3]);
		}

		if (miscs[4] != null) {
			miscs[4].identify();
			Badges.validateItemLevelAquired(miscs[4]);
		}
		for (Item item : backpack) {
			if (item instanceof EquipableItem || item instanceof Wand) {
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
				if (((Key)item).depth == depth) {
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

		
		if (miscs[0] != null) {
			miscs[0].cursed = false;
			miscs[0].activate( owner );
		}
		if (miscs[1] != null) {
			miscs[1].cursed = false;
			miscs[1].activate( owner );
		}

		if (miscs[2] != null) {
			miscs[2].cursed = false;
			miscs[2].activate( owner );
		}
		if (miscs[3] != null) {
			miscs[3].cursed = false;
			miscs[3].activate( owner );
		}

		if (miscs[4] != null) {
			miscs[4].cursed = false;
			miscs[4].activate( owner );
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
