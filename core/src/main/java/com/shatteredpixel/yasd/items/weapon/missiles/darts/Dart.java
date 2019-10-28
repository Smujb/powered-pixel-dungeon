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

package com.shatteredpixel.yasd.items.weapon.missiles.darts;

import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.buffs.MagicImmune;
import com.shatteredpixel.yasd.actors.hero.Hero;
import com.shatteredpixel.yasd.items.Item;
import com.shatteredpixel.yasd.items.weapon.melee.Crossbow;
import com.shatteredpixel.yasd.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.plants.Plant;
import com.shatteredpixel.yasd.scenes.GameScene;
import com.shatteredpixel.yasd.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.windows.WndBag;
import com.shatteredpixel.yasd.windows.WndOptions;

import java.util.ArrayList;

public class Dart extends MissileWeapon {

	{
		image = ItemSpriteSheet.DART;

		tier = 1;

		//infinite, even with penalties
		baseUses = 1000;
	}
	public int CurrentCrossbow = 0;

	protected static final String AC_TIP = "TIP";
	
	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_TIP );
		return actions;
	}
	@Override
	public void execute(Hero hero, String action) {
		if (action.equals(AC_TIP)){
			GameScene.selectItem(itemSelector, WndBag.Mode.SEED, "select a seed");
		}
		
		super.execute(hero, action);
	}
	
	@Override
	public int min(int lvl) {
		if (bow != null){
			return  4 +                 //4 base
					bow.level() + lvl;  //+1 per level or bow level
		} else {
			return  1 +     //1 base, down from 2
					lvl;    //scaling unchanged
		}
	}

	@Override
	public int max(int lvl) {
		if (bow != null){
			return  12 +                    //12 base
					3*bow.level() + 2*lvl;  //+3 per bow level, +2 per level (default scaling +2)
		} else {
			return  2 +     //2 base, down from 5
					2*lvl;  //scaling unchanged
		}
	}
	
	private static Crossbow bow;
	
	private ArrayList<Item> getCrossbows(){
		return Dungeon.hero.belongings.getEquippedItemsOFType( Crossbow.class );
	}

	private Crossbow getCrossbow() {
		resetCrossbow();
		return ((Crossbow)getCrossbows().get(CurrentCrossbow));
	}

	private void resetCrossbow() {
		if (CurrentCrossbow > this.getCrossbows().size() - 1) {
			CurrentCrossbow = 0;
		}
	}

	private int numberOfBows() {
		return getCrossbows().size();
	}
	
	@Override
	public boolean hasEnchant(Class<? extends Enchantment> type, Char owner) {
		if (bow != null && bow.hasEnchant(type, owner)){
			return true;
		} else {
			return super.hasEnchant(type, owner);
		}
	}
	
	@Override
	public int proc(Char attacker, Char defender, int damage) {
		if (bow != null && bow.enchantment != null && attacker.buff(MagicImmune.class) == null){
			level(bow.level());
			damage = bow.enchantment.proc(this, attacker, defender, damage);
			level(0);
		}
		return super.proc(attacker, defender, damage);
	}

	@Override
	public float castDelay(Char user, int dst) {
		return super.castDelay(user, dst)/(float) numberOfBows();
	}

	@Override
	protected void onThrow(int cell) {
		CurrentCrossbow += 1;
		bow = getCrossbow();
		super.onThrow(cell);
	}
	
	@Override
	public String info() {
		bow = getCrossbow();
		return super.info();
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public int price() {
		return super.price()/2; //half normal value
	}
	
	private final WndBag.Listener itemSelector = new WndBag.Listener() {
		
		@Override
		public void onSelect(final Item item) {
			
			if (item == null) return;
			
			final int maxToTip = Math.min(curItem.quantity(), item.quantity()*2);
			final int maxSeedsToUse = (maxToTip+1)/2;
			
			final int singleSeedDarts;
			
			final String[] options;
			
			if (curItem.quantity() == 1){
				singleSeedDarts = 1;
				options = new String[]{
						Messages.get(Dart.class, "tip_one"),
						Messages.get(Dart.class, "tip_cancel")};
			} else {
				singleSeedDarts = 2;
				if (maxToTip <= 2){
					options = new String[]{
							Messages.get(Dart.class, "tip_two"),
							Messages.get(Dart.class, "tip_cancel")};
				} else {
					options = new String[]{
							Messages.get(Dart.class, "tip_all", maxToTip, maxSeedsToUse),
							Messages.get(Dart.class, "tip_two"),
							Messages.get(Dart.class, "tip_cancel")};
				}
			}
			
			TippedDart tipResult = TippedDart.getTipped((Plant.Seed) item, 1);
			
			GameScene.show(new WndOptions(Messages.get(Dart.class, "tip_title"),
					Messages.get(Dart.class, "tip_desc", tipResult.name()) + "\n\n" + tipResult.desc(),
					options){
				
				@Override
				protected void onSelect(int index) {
					super.onSelect(index);
					
					if (index == 0 && options.length == 3){
						if (item.quantity() <= maxSeedsToUse){
							item.detachAll( curUser.belongings.backpack );
						} else {
							item.quantity(item.quantity() - maxSeedsToUse);
						}
						
						if (maxToTip < curItem.quantity()){
							curItem.quantity(curItem.quantity() - maxToTip);
						} else {
							curItem.detachAll(curUser.belongings.backpack);
						}
						
						TippedDart newDart = TippedDart.getTipped((Plant.Seed) item, maxToTip);
						if (!newDart.collect()) Dungeon.level.drop(newDart, curUser.pos).sprite.drop();
						
						curUser.spend( 1f );
						curUser.busy();
						curUser.sprite.operate(curUser.pos);
						
					} else if ((index == 1 && options.length == 3) || (index == 0 && options.length == 2)){
						item.detach( curUser.belongings.backpack );
						
						if (curItem.quantity() <= singleSeedDarts){
							curItem.detachAll( curUser.belongings.backpack );
						} else {
							curItem.quantity(curItem.quantity() - singleSeedDarts);
						}
						
						TippedDart newDart = TippedDart.getTipped((Plant.Seed) item, singleSeedDarts);
						if (!newDart.collect()) Dungeon.level.drop(newDart, curUser.pos).sprite.drop();
						
						curUser.spend( 1f );
						curUser.busy();
						curUser.sprite.operate(curUser.pos);
					}
				}
			});
			
		}
		
	};
}