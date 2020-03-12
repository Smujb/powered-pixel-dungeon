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

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.Bones;
import com.shatteredpixel.yasd.general.Challenges;
import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.GameSettings;
import com.shatteredpixel.yasd.general.GamesInProgress;
import com.shatteredpixel.yasd.general.MainGame;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.blobs.Alchemy;
import com.shatteredpixel.yasd.general.actors.buffs.Amok;
import com.shatteredpixel.yasd.general.actors.buffs.Awareness;
import com.shatteredpixel.yasd.general.actors.buffs.Barkskin;
import com.shatteredpixel.yasd.general.actors.buffs.Berserk;
import com.shatteredpixel.yasd.general.actors.buffs.Bleeding;
import com.shatteredpixel.yasd.general.actors.buffs.Bless;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Combo;
import com.shatteredpixel.yasd.general.actors.buffs.Drunk;
import com.shatteredpixel.yasd.general.actors.buffs.FlavourBuff;
import com.shatteredpixel.yasd.general.actors.buffs.Foresight;
import com.shatteredpixel.yasd.general.actors.buffs.Hunger;
import com.shatteredpixel.yasd.general.actors.buffs.Invisibility;
import com.shatteredpixel.yasd.general.actors.buffs.LimitedAir;
import com.shatteredpixel.yasd.general.actors.buffs.MindVision;
import com.shatteredpixel.yasd.general.actors.buffs.Momentum;
import com.shatteredpixel.yasd.general.actors.buffs.Paralysis;
import com.shatteredpixel.yasd.general.actors.buffs.Regeneration;
import com.shatteredpixel.yasd.general.actors.buffs.SnipersMark;
import com.shatteredpixel.yasd.general.actors.buffs.Vertigo;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.effects.CheckedCell;
import com.shatteredpixel.yasd.general.items.Amulet;
import com.shatteredpixel.yasd.general.items.Ankh;
import com.shatteredpixel.yasd.general.items.Dewdrop;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.items.Heap.Type;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.KindOfWeapon;
import com.shatteredpixel.yasd.general.items.armor.glyphs.Viscosity;
import com.shatteredpixel.yasd.general.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.yasd.general.items.artifacts.DriedRose;
import com.shatteredpixel.yasd.general.items.artifacts.EtherealChains;
import com.shatteredpixel.yasd.general.items.artifacts.HornOfPlenty;
import com.shatteredpixel.yasd.general.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.yasd.general.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.yasd.general.items.keys.CrystalKey;
import com.shatteredpixel.yasd.general.items.keys.GoldenKey;
import com.shatteredpixel.yasd.general.items.keys.IronKey;
import com.shatteredpixel.yasd.general.items.keys.Key;
import com.shatteredpixel.yasd.general.items.keys.SkeletonKey;
import com.shatteredpixel.yasd.general.items.potions.Potion;
import com.shatteredpixel.yasd.general.items.potions.PotionOfExperience;
import com.shatteredpixel.yasd.general.items.potions.PotionOfStrength;
import com.shatteredpixel.yasd.general.items.potions.elixirs.ElixirOfMight;
import com.shatteredpixel.yasd.general.items.rings.RingOfElements;
import com.shatteredpixel.yasd.general.items.rings.RingOfPerception;
import com.shatteredpixel.yasd.general.items.rings.RingOfFocus;
import com.shatteredpixel.yasd.general.items.rings.RingOfEvasion;
import com.shatteredpixel.yasd.general.items.rings.RingOfPower;
import com.shatteredpixel.yasd.general.items.scrolls.Scroll;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.yasd.general.items.wands.WandOfWarding;
import com.shatteredpixel.yasd.general.items.weapon.SpiritBow;
import com.shatteredpixel.yasd.general.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.yasd.general.journal.Notes;
import com.shatteredpixel.yasd.general.levels.Terrain;
import com.shatteredpixel.yasd.general.levels.features.Chasm;
import com.shatteredpixel.yasd.general.levels.traps.Trap;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.plants.Swiftthistle;
import com.shatteredpixel.yasd.general.scenes.AlchemyScene;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.scenes.InterlevelScene;
import com.shatteredpixel.yasd.general.scenes.SurfaceScene;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.ui.AttackIndicator;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;
import com.shatteredpixel.yasd.general.ui.QuickSlotButton;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.shatteredpixel.yasd.general.windows.WndHero;
import com.shatteredpixel.yasd.general.windows.WndMessage;
import com.shatteredpixel.yasd.general.windows.WndTradeItem;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;

public class Hero extends Char {

	{
		actPriority = HERO_PRIO;
		
		alignment = Alignment.ALLY;

		attackSkill = 10;
		defenseSkill = 4;
		immunities.add(Amok.class);
	}

	public final float MAX_MORALE = 10f;
	public float morale = MAX_MORALE;

	public static final int STARTING_STR = 10;

	private int Power      = 1;
	private int Focus      = 1;
	private int Perception = 1;
	private int Evasion    = 1;
	public  int DistributionPoints = 0;

	private static final float TIME_TO_REST		    = 1f;
	private static final float TIME_TO_SEARCH	    = 2f;
	private static final float HUNGER_FOR_SEARCH	= 6f;
	
	public HeroClass heroClass = HeroClass.ROGUE;
	public HeroSubClass subClass = HeroSubClass.NONE;


	public boolean ready = false;
	private boolean damageInterrupt = true;
	public HeroAction curAction = null;
	public HeroAction lastAction = null;

	private Char enemy;
	
	public boolean resting = false;

	
	public float awareness;
	
	public int lvl = 1;
	public int exp = 0;
	
	public int HTBoost = 0;
	
	private ArrayList<Mob> visibleEnemies;

	//This list is maintained so that some logic checks can be skipped
	// for enemies we know we aren't seeing normally, resulting in better performance

	public class Morale{}

	public ArrayList<Mob> mindVisionEnemies = new ArrayList<>();
	
	public Hero() {
		super();
		name = Messages.get(this, "name");
		belongings = new Belongings(this);
		
		HP = HT = 20;
		STR = STARTING_STR;
		
		visibleEnemies = new ArrayList<>();
	}

	public int levelToScaleFactor() {
		return lvl;
	}

	@Override
	public void updateHT(boolean boostHP) {
		HT = 20 + 5*(lvl-1) + HTBoost;
		super.updateHT(boostHP);
	}

	public void moraleCheck() {
		if (morale > MAX_MORALE*0.67) {
			return;
		} else if (morale > MAX_MORALE*0.33) {
			int choice = Random.Int(5) + 1;
			String messageTitle = "low_morale_" + choice;
			GLog.w(Messages.get(Hero.class, messageTitle));

		} else {
			int choice = Random.Int(5) + 1;
			String messageTitle = "very_low_morale_" + choice;
			GLog.w(Messages.get(Hero.class, messageTitle));

		}
	}

	public void loseMorale(float Amount) {
		loseMorale(Amount, true);
	}

	public void loseMorale(float Amount, boolean say) {
		if (!Constants.MORALE) {
			return;
		}
		float difficultyMultiplier;
		switch (Dungeon.difficulty) {
			case 1:// -33% Morale loss in Easy
				difficultyMultiplier = 0.67f;
				break;
			case 2:
			default://Normal Morale loss in Medium
				difficultyMultiplier = 1f;
				break;
			case 3://+50% Morale loss in Hard
				difficultyMultiplier = 1.5f;
				break;

		}
		Amount *= difficultyMultiplier;
		morale -= Amount;
		morale = Math.max(morale, 0);
		if (buff(Drunk.class) == null) {//Can't lose Morale when drunk
			if (this.sprite != null) {
				this.sprite.showStatus(CharSprite.NEGATIVE, Messages.get(Morale.class, "loss"));
			}
			if (say) {
				moraleCheck();
			}
			if (morale == 0f & isAlive()) {
				Buff.affect(this, Bleeding.class).set(Math.max(1, this.HP / 6));
				morale += Random.Float() + 2;
			}
		}
	}

	public void gainMorale(float Amount) {
		if (!Constants.MORALE) {
			return;
		}
		morale += Amount;
		morale = Math.min(morale, MAX_MORALE);
		if (this.sprite != null) {
			this.sprite.showStatus(CharSprite.NEUTRAL, Messages.get(Morale.class, "gain"));
		}
	}

	public int getPower() {
		return Power + RingOfPower.powerBonus(this);
	}

	public int getPerception() {
		return Perception + RingOfPerception.expertiseBonus(this);
	}

	public int getFocus() {
		return Focus + RingOfFocus.focusBonus(this);
	}

	public int getEvasion() {
		return Evasion + RingOfEvasion.luckBonus(this);
	}

	public void setPower(int power) {
		Power = power;
		Dungeon.hero.STR();
		Badges.validateStrengthAttained();
	}

	public void increasePower() {
		setPower(Power+1);
	}

	public void setPerception(int perception) {
		Perception = perception;
	}

	public void increasePerception() {
		setPerception(Perception +1);
	}

	public void setFocus(int focus) {
		Focus = focus;
	}

	public void increaseFocus() {
		setFocus(Focus+1);
	}

	public void setEvasion(int evasion) {
		Evasion = evasion;
	}

	public void increaseEvasion() {
		setEvasion(Evasion +1);
	}

	@Override
	public float resist(Class effect) {
		return super.resist(effect);
	}

	private static final String ATTACK		= "attackSkill";
	private static final String DEFENSE		= "evasion";
	private static final String STRENGTH	= "STR";
	private static final String LEVEL		= "lvl";
	private static final String EXPERIENCE	= "exp";
	private static final String HTBOOST     = "htboost";
	private static final String MORALE      = "morale";
	private static final String POWER       = "power";
	private static final String FOCUS       = "focus";
	private static final String PERCEPTION  = "expertise";
	private static final String EVASION     = "combatskill";
	private static final String DISTRIBUTIONPOINTS  = "distribution-points";
	
	@Override
	public void storeInBundle( Bundle bundle ) {

		super.storeInBundle( bundle );
		
		heroClass.storeInBundle( bundle );
		subClass.storeInBundle( bundle );
		
		bundle.put( ATTACK, attackSkill );
		bundle.put( DEFENSE, defenseSkill );
		
		bundle.put( STRENGTH, STR );
		
		bundle.put( LEVEL, lvl );
		bundle.put( EXPERIENCE, exp );
		
		bundle.put( HTBOOST, HTBoost );

		//Morale
		bundle.put( MORALE, morale );


		//Hero stats
		bundle.put( POWER, Power );
		bundle.put( FOCUS, Focus );
		bundle.put( PERCEPTION, Perception );
		bundle.put( EVASION, Evasion );
		bundle.put( DISTRIBUTIONPOINTS, DistributionPoints );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		
		heroClass = HeroClass.restoreInBundle( bundle );
		subClass = HeroSubClass.restoreInBundle( bundle );
		
		attackSkill = bundle.getInt( ATTACK );
		defenseSkill = bundle.getInt( DEFENSE );
		
		STR = bundle.getInt( STRENGTH );
		
		lvl = bundle.getInt( LEVEL );
		exp = bundle.getInt( EXPERIENCE );
		
		HTBoost = bundle.getInt(HTBOOST);

		//Morale
		morale = bundle.getFloat(MORALE);

		//Hero stats
		Power = bundle.getInt( POWER );
		Focus = bundle.getInt( FOCUS );
		Perception = bundle.getInt( PERCEPTION );
		Evasion = bundle.getInt( EVASION );
		DistributionPoints = bundle.getInt( DISTRIBUTIONPOINTS );
	}


	@Override
	public int STR() {
		STR = 10 + Power/2;
		return super.STR();
	}

	@Override
	public float stealth() {
		int stealth = 9 + Evasion;
		if (buff(Drunk.class) != null) {
			stealth /= 2;
		}
		return stealth;
	}

	@Override
	public float perception() {
		return 4 + Perception;
	}

	public static void preview(GamesInProgress.Info info, Bundle bundle ) {
		info.level = bundle.getInt( LEVEL );
		info.str = bundle.getInt( STRENGTH );
		info.exp = bundle.getInt( EXPERIENCE );
		info.hp = bundle.getInt( Char.TAG_HP );
		info.ht = bundle.getInt( Char.TAG_HT );
		info.shld = bundle.getInt( Char.TAG_SHLD );
		info.heroClass = HeroClass.restoreInBundle( bundle );
		info.subClass = HeroSubClass.restoreInBundle( bundle );
		Belongings.preview( info, bundle );
	}
	
	public String className() {
		return subClass == null || subClass == HeroSubClass.NONE ? heroClass.title() : subClass.title();
	}

	public String givenName(){
		return name.equals(Messages.get(this, "name")) ? className() : name;
	}
	
	public void live() {
		Buff.affect( this, Regeneration.class );
		Buff.affect( this, Hunger.class );
	}
	
	public int tier() {
		return belongings.getArmors().size() == 0 ? 0 : belongings.getArmors().get(0).appearance();
	}

	@Override
	public boolean shoot(Char enemy, MissileWeapon wep) {
		boolean hit = super.shoot(enemy,wep);
		if (subClass == HeroSubClass.GLADIATOR){
			if (hit) {
				Buff.affect( this, Combo.class ).hit( enemy );
			} else {
				Combo combo = buff(Combo.class);
				if (combo != null) combo.miss( enemy );
			}
		}
		return hit;
	}

	/*@Override
	public int elementalDefenseProc(Char enemy, int damage) {
		damage *= RingOfElements.resist(this);
		damageMorale(damage- elementalDRRoll());
		return super.elementalDefenseProc(enemy, damage);
	}*/

	@Override
	public int attackSkill( Char target ) {
		attackSkill = 9 + Perception;
		float moraleMultiplier = (float) ((morale - MAX_MORALE) * 0.04);
		return (int) (super.attackSkill(target)*(1+moraleMultiplier));
	}

	@Override
	public int defenseSkill( Char enemy ) {
		defenseSkill = 3 + Evasion;
		float moraleMultiplier = (float) ((morale - MAX_MORALE) * 0.04);
		//GLog.w(String.valueOf(evasion));
		return (int) (super.defenseSkill(enemy)*(1+moraleMultiplier));
	}

	@Override
	public void spend( float time ) {
		justMoved = false;
		TimekeepersHourglass.timeFreeze freeze = buff(TimekeepersHourglass.timeFreeze.class);
		if (freeze != null) {
			freeze.processTime(time);
			return;
		}
		
		Swiftthistle.TimeBubble bubble = buff(Swiftthistle.TimeBubble.class);
		if (bubble != null){
			bubble.processTime(time);
			return;
		}
		
		super.spend(time);
	}

	@Override
	public void spendAndNext(float time) {
		busy();
		super.spendAndNext(time);
	}

	@Override
	public boolean act() {
		//Manually call this as it's called in Char.act which is overridden.
		LimitedAir.updateBuff(this);

		//calls to dungeon.observe will also update hero's local FOV.
		fieldOfView = Dungeon.level.heroFOV;
		
		if (!ready) {
			//do a full observe (including fog update) if not resting.
			if (!resting || buff(MindVision.class) != null || buff(Awareness.class) != null) {
				Dungeon.observe();
			} else {
				//otherwise just directly re-calculate FOV
				Dungeon.level.updateFieldOfView(this, fieldOfView);
			}
		}
		
		checkVisibleMobs();
		if (buff(FlavourBuff.class) != null) {
			BuffIndicator.refreshHero();
		}
		
		if (paralysed > 0) {
			
			curAction = null;
			
			spendAndNext( TICK );
			return false;
		}
		
		boolean actResult;
		if (curAction == null) {
			
			if (resting) {
				spend( TIME_TO_REST );
				next();
			} else {
				ready();
			}
			
			actResult = false;
			
		} else {
			
			resting = false;
			
			ready = false;
			
			if (curAction instanceof HeroAction.Move) {
				actResult = actMove( (HeroAction.Move)curAction );
				
			} else if (curAction instanceof HeroAction.Interact) {
				actResult = actInteract( (HeroAction.Interact)curAction );
				
			} else if (curAction instanceof HeroAction.Buy) {
				actResult = actBuy( (HeroAction.Buy)curAction );
				
			}else if (curAction instanceof HeroAction.PickUp) {
				actResult = actPickUp( (HeroAction.PickUp)curAction );
				
			} else if (curAction instanceof HeroAction.OpenChest) {
				actResult = actOpenChest( (HeroAction.OpenChest)curAction );
				
			} else if (curAction instanceof HeroAction.Unlock) {
				actResult = actUnlock((HeroAction.Unlock) curAction);
				
			} else if (curAction instanceof HeroAction.Descend) {
				actResult = actDescend( (HeroAction.Descend)curAction );
				
			} else if (curAction instanceof HeroAction.Ascend) {
				actResult = actAscend( (HeroAction.Ascend)curAction );
				
			} else if (curAction instanceof HeroAction.Attack) {
				actResult = actAttack( (HeroAction.Attack)curAction );
				
			} else if (curAction instanceof HeroAction.Alchemy) {
				actResult = actAlchemy( (HeroAction.Alchemy)curAction );
				
			} else {
				actResult = false;
			}
		}
		
		if( subClass == HeroSubClass.WARDEN && Dungeon.level.map[pos] == Terrain.FURROWED_GRASS){
			Buff.affect(this, Barkskin.class).set( lvl + 5, 1 );
		}
		
		return actResult;
	}
	@Override
	public void busy() {
		ready = false;
	}
	
	private void ready() {
		if (sprite.looping()) sprite.idle();
		curAction = null;
		damageInterrupt = true;
		ready = true;

		AttackIndicator.updateState();
		
		GameScene.ready();
	}
	
	public void interrupt() {
		if (isAlive() && curAction != null &&
			((curAction instanceof HeroAction.Move && curAction.dst != pos) ||
			(curAction instanceof HeroAction.Ascend || curAction instanceof HeroAction.Descend))) {
			lastAction = curAction;
		}
		curAction = null;
	}
	
	public void resume() {
		curAction = lastAction;
		lastAction = null;
		damageInterrupt = false;
		next();
	}
	
	//FIXME this is a fairly crude way to track this, really it would be nice to have a short
	//history of hero actions
	public boolean justMoved = false;
	
	private boolean actMove( HeroAction.Move action ) {

		if (getCloser( action.dst )) {
			justMoved = true;
			return true;

		} else {
			ready();
			return false;
		}
	}
	
	private boolean actInteract( HeroAction.Interact action ) {
		
		Char ch = action.ch;

		if (ch.canInteract(this)) {
			
			ready();
			sprite.turnTo( pos, ch.pos );
			return ch.interact();
			
		} else {
			
			if (fieldOfView[ch.pos] && getCloser( ch.pos )) {

				return true;

			} else {
				ready();
				return false;
			}
			
		}
	}
	
	private boolean actBuy( HeroAction.Buy action ) {
		int dst = action.dst;
		if (pos == dst || Dungeon.level.adjacent( pos, dst )) {

			ready();
			
			Heap heap = Dungeon.level.heaps.get( dst );
			if (heap != null && heap.type == Type.FOR_SALE && heap.size() == 1) {
				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						GameScene.show( new WndTradeItem( heap, true ) );
					}
				});
			}

			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actAlchemy( HeroAction.Alchemy action ) {
		int dst = action.dst;
		if (Dungeon.level.distance(dst, pos) <= 1) {

			ready();
			
			AlchemistsToolkit.kitEnergy kit = buff(AlchemistsToolkit.kitEnergy.class);
			if (kit != null && kit.isCursed()){
				GLog.w( Messages.get(AlchemistsToolkit.class, "cursed"));
				return false;
			}
			
			Alchemy alch = (Alchemy) Dungeon.level.blobs.get(Alchemy.class);
			//TODO logic for a well having dried up?
			if (alch != null) {
				Alchemy.alchPos = dst;
				AlchemyScene.setProvider( alch );
			}
			MainGame.switchScene(AlchemyScene.class);
			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actPickUp( HeroAction.PickUp action ) {
		int dst = action.dst;
		if (pos == dst) {
			
			Heap heap = Dungeon.level.heaps.get( pos );
			if (heap != null) {
				Item item = heap.peek();
				if (item.doPickUp( this )) {
					heap.pickUp();

					if (item instanceof Dewdrop
							|| item instanceof TimekeepersHourglass.sandBag
							|| item instanceof DriedRose.Petal
							|| item instanceof Key) {
						//Do Nothing
					} else {

						boolean important =
								(item instanceof ScrollOfUpgrade && ((Scroll)item).isKnown()) ||
								(item instanceof PotionOfStrength && ((Potion)item).isKnown());
						if (important) {
							GLog.p( Messages.get(this, "you_now_have", item.name()) );
						} else {
							GLog.i( Messages.get(this, "you_now_have", item.name()) );
						}
					}
					
					curAction = null;
				} else {
					heap.sprite.drop();
					ready();
				}
			} else {
				ready();
			}

			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}
	
	private boolean actOpenChest( HeroAction.OpenChest action ) {
		int dst = action.dst;
		if (Dungeon.level.adjacent( pos, dst ) || pos == dst) {
			
			Heap heap = Dungeon.level.heaps.get( dst );
			if (heap != null && (heap.type != Type.HEAP && heap.type != Type.FOR_SALE)) {
				
				if ((heap.type == Type.LOCKED_CHEST && Notes.keyCount(new GoldenKey(Dungeon.xPos, Dungeon.yPos, Dungeon.zPos)) < 1)
					|| (heap.type == Type.CRYSTAL_CHEST && Notes.keyCount(new CrystalKey(Dungeon.xPos, Dungeon.yPos, Dungeon.zPos)) < 1)){

						GLog.w( Messages.get(this, "locked_chest") );
						ready();
						return false;

				}
				
				switch (heap.type) {
				case TOMB:
					Sample.INSTANCE.play( Assets.SND_TOMB );
					Camera.main.shake( 1, 0.5f );
					break;
				case SKELETON:
				case REMAINS:
					break;
				default:
					Sample.INSTANCE.play( Assets.SND_UNLOCK );
				}
				
				sprite.operate( dst );
				
			} else {
				ready();
			}

			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}
	
	private boolean actUnlock( HeroAction.Unlock action ) {
		int doorCell = action.dst;
		if (Dungeon.level.adjacent( pos, doorCell )) {
			
			boolean hasKey = false;
			Terrain door = Dungeon.level.map[doorCell];
			
			if (door == Terrain.LOCKED_DOOR
					&& Notes.keyCount(new IronKey(Dungeon.xPos, Dungeon.yPos, Dungeon.zPos)) > 0) {
				
				hasKey = true;
				
			} else if (door == Terrain.LOCKED_EXIT
					&& Notes.keyCount(new SkeletonKey(Dungeon.xPos, Dungeon.yPos, Dungeon.zPos)) > 0) {

				hasKey = true;
				
			}
			
			if (hasKey) {
				
				sprite.operate( doorCell );
				
				Sample.INSTANCE.play( Assets.SND_UNLOCK );
				
			} else {
				GLog.w( Messages.get(this, "locked_door") );
				ready();
			}

			return false;

		} else if (getCloser( doorCell )) {

			return true;

		} else {
			ready();
			return false;
		}
	}
	
	private boolean actDescend( HeroAction.Descend action ) {
		int stairs = action.dst;
		if (pos == stairs) {
			
			curAction = null;

			Buff buff = buff(TimekeepersHourglass.timeFreeze.class);
			if (buff != null) buff.detach();
			buff = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
			if (buff != null) buff.detach();
			
			InterlevelScene.descend();

			return false;

		} else if (getCloser( stairs )) {

			return true;

		} else {
			ready();
			return false;
		}
	}
	
	private boolean actAscend( HeroAction.Ascend action ) {
		int stairs = action.dst;
		if (pos == stairs) {
			
			if (Dungeon.yPos == 1) {
				
				if (belongings.getItem( Amulet.class ) == null) {
					Game.runOnRenderThread(new Callback() {
						@Override
						public void call() {
							GameScene.show( new WndMessage( Messages.get(Hero.this, "leave") ) );
						}
					});
					ready();
				} else {
					Badges.silentValidateHappyEnd();
					Dungeon.win( Amulet.class );
					Dungeon.deleteGame( GamesInProgress.curSlot, true );
					Game.switchScene( SurfaceScene.class );
				}
				
			} else {
				
				curAction = null;

				Buff buff = buff(TimekeepersHourglass.timeFreeze.class);
				if (buff != null) buff.detach();
				buff = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
				if (buff != null) buff.detach();

				InterlevelScene.ascend();
			}

			return false;

		} else if (getCloser( stairs )) {

			return true;

		} else {
			ready();
			return false;
		}
	}
	
	private boolean actAttack( HeroAction.Attack action ) {

		enemy = action.target;

		if (enemy.isAlive() && canAttack( enemy ) && !isCharmedBy( enemy )) {
			
			sprite.attack( enemy.pos );

			return false;

		} else {

			if (fieldOfView[enemy.pos] && getCloser( enemy.pos )) {

				return true;

			} else {
				ready();
				return false;
			}

		}
	}



	public Char enemy(){
		return enemy;
	}
	
	public void rest( boolean fullRest ) {
		spendAndNext( TIME_TO_REST );
		if (!fullRest) {
			sprite.showStatus( CharSprite.DEFAULT, Messages.get(this, "wait") );
		}
		resting = fullRest;
	}
	
	@Override
	public int attackProc( final Char enemy, int damage ) {
		KindOfWeapon wep = belongings.getCurrentWeapon();
		damage = super.attackProc(enemy,damage);
		switch (subClass) {
		case SNIPER:
			if (wep instanceof MissileWeapon && !(wep instanceof SpiritBow.SpiritArrow)) {
				Actor.add(new Actor() {
					
					{
						actPriority = VFX_PRIO;
					}
					
					@Override
					protected boolean act() {
						if (enemy.isAlive()) {
							Buff.prolong(Hero.this, SnipersMark.class, 2f).object = enemy.id();
						}
			 			Actor.remove(this);
						return true;
					}
				});
			}
			break;
		default:
		}

		
		return damage;
	}
	
	@Override
	public int defenseProc(Char enemy, int damage, Element element) {

		if (enemy.elementalType().isMagical()) {
			damage *= RingOfElements.resist(enemy);
		}
		
		if (damage > 0 && subClass == HeroSubClass.BERSERKER){
			Berserk berserk = Buff.affect(this, Berserk.class);
			berserk.damage(damage);
		}

		damage = super.defenseProc(enemy,damage, element);

		damageMorale(damage-drRoll(element));
		
		return damage;
	}

	private void damageMorale(int dmg) {
		float shake;
		if (dmg <= 0) {
			return;
		}
		int effectiveHP = Math.max(HT/2, HP);
		shake = ((float) dmg / (float) effectiveHP) * 4f;

		if (shake > 0.5f) {
			Camera.main.shake(GameMath.gate(1, shake, 5), Math.min(shake/2f,1f));
			if (GameSettings.vibrate()) {
				MainGame.vibrate(Math.min(500,(int) (shake * 50)));
			}
			if (shake > 1f) {
				loseMorale(shake*0.5f);
			} else {
				loseMorale(shake*0.5f,false);
			}
		}
	}
	
	@Override
	public void damage(int dmg, Object src, Element element, boolean ignoresDefense) {
		if (!(src instanceof Hunger || src instanceof Viscosity.DeferedDamage) && damageInterrupt) {
			interrupt();
			resting = false;
		}

		super.damage( dmg, src, element, ignoresDefense);
	}
	
	public void checkVisibleMobs() {
		ArrayList<Mob> visible = new ArrayList<>();

		boolean newMob = false;

		Mob target = null;
		for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])) {
			if (fieldOfView[ m.pos ] && m.alignment == Alignment.ENEMY) {
				visible.add(m);
				if (!visibleEnemies.contains( m )) {
					newMob = true;
				}

				if (!mindVisionEnemies.contains(m) && QuickSlotButton.autoAim(m) != -1){
					if (target == null){
						target = m;
					} else if (distance(target) > distance(m)) {
						target = m;
					}
				}
			}
		}

		Char lastTarget = QuickSlotButton.lastTarget;
		if (target != null && (lastTarget == null ||
							!lastTarget.isAlive() ||
							!fieldOfView[lastTarget.pos]) ||
							(lastTarget instanceof WandOfWarding.Ward && mindVisionEnemies.contains(lastTarget))){
			QuickSlotButton.target(target);
		}
		
		if (newMob) {
			interrupt();
			resting = false;
		}

		visibleEnemies = visible;
	}
	
	public int visibleEnemies() {
		return visibleEnemies.size();
	}
	
	public Mob visibleEnemy( int index ) {
		return visibleEnemies.get(index % visibleEnemies.size());
	}
	
	private boolean walkingToVisibleTrapInFog = false;
	
	protected boolean getCloser( final int target ) {

		if (target == pos)
			return false;

		if (rooted) {
			Camera.main.shake( 1, 1f );
			return false;
		}
		
		int step = -1;
		
		if (Dungeon.level.adjacent( pos, target )) {

			path = null;

			if (Actor.findChar( target ) == null) {
				if (Dungeon.level.map[target].pit && !flying && !Dungeon.level.map[target].solid) {
					if (!Chasm.jumpConfirmed){
						Chasm.heroJump(this);
						interrupt();
					} else {
						Chasm.heroFall(target);
					}
					return false;
				}
				if (Dungeon.level.map[target].passable || Dungeon.level.map[target].avoid) {
					step = target;
				}
				if (walkingToVisibleTrapInFog
						&& Dungeon.level.traps.get(target) != null
						&& Dungeon.level.traps.get(target).visible){
					return false;
				}
			}
			
		} else {

			boolean newPath = false;
			if (path == null || path.isEmpty() || !Dungeon.level.adjacent(pos, path.getFirst()))
				newPath = true;
			else if (path.getLast() != target)
				newPath = true;
			else {
				//looks ahead for xPos validity, up to length-1 or 2.
				//Note that this is shorter than for mobs, so that mobs usually yield to the hero
				int lookAhead = (int) GameMath.gate(0, path.size()-1, 2);
				for (int i = 0; i < lookAhead; i++){
					int cell = path.get(i);
					if (!Dungeon.level.passable()[cell] || (fieldOfView[cell] && Actor.findChar(cell) != null)) {
						newPath = true;
						break;
					}
				}
			}

			if (newPath) {

				int len = Dungeon.level.length();
				boolean[] p = Dungeon.level.passable();
				boolean[] v = Dungeon.level.visited;
				boolean[] m = Dungeon.level.mapped;
				boolean[] passable = new boolean[len];
				for (int i = 0; i < len; i++) {
					passable[i] = p[i] && (v[i] || m[i]);
				}

				path = Dungeon.findPath(this, pos, target, passable, fieldOfView);
			}

			if (path == null) return false;
			step = path.removeFirst();

		}

		if (step != -1) {
			
			float speed = speed();
			if (Dungeon.isChallenged(Challenges.COLLAPSING_FLOOR) & !(Dungeon.level.map[pos] == Terrain.EXIT || Dungeon.level.map[pos] == Terrain.DOOR || Dungeon.level.map[pos] == Terrain.ENTRANCE|| Dungeon.level.map[pos] == Terrain.OPEN_DOOR) & !Dungeon.bossLevel()) {
				if (MainGame.scene() instanceof GameScene) {
					if (!isFlying()) {
						Dungeon.level.set(pos, Terrain.CHASM);
					}
					GameScene.updateMap(pos);
					if (Dungeon.level.heroFOV[pos]) Dungeon.observe();
				}
			}

			sprite.move(pos, step);
			move(step);

			spend( 1 / speed );
			
			search(false);
			
			if (subClass == HeroSubClass.FREERUNNER){
				Buff.affect(this, Momentum.class).gainStack();
			}

			//FIXME this is a fairly sloppy fix for a crash involving pitfall traps.
			//really there should be a way for traps to specify whether action should continue or
			//not when they are pressed.
			return InterlevelScene.mode() != InterlevelScene.Mode.FALL;

		} else {

			return false;
			
		}

	}
	
	public boolean handle( int cell ) {
		
		if (cell == -1) {
			return false;
		}
		
		Char ch;
		Heap heap;
		
		if (Dungeon.level.map[cell] == Terrain.ALCHEMY && cell != pos) {
			
			curAction = new HeroAction.Alchemy( cell );
			
		} else if (fieldOfView[cell] && (ch = Actor.findChar( cell )) instanceof Mob) {

			if (ch.alignment != Alignment.ENEMY && ch.buff(Amok.class) == null) {
				curAction = new HeroAction.Interact( ch );
			} else {
				curAction = new HeroAction.Attack( ch );
			}

		} else if ((heap = Dungeon.level.heaps.get( cell )) != null
				//moving to an item doesn't auto-pickup when enemies are near...
				&& (visibleEnemies.size() == 0 || cell == pos ||
				//...but only for standard heaps, chests and similar open as normal.
				(heap.type != Type.HEAP && heap.type != Type.FOR_SALE))) {

			switch (heap.type) {
			case HEAP:
				curAction = new HeroAction.PickUp( cell );
				break;
			case FOR_SALE:
				curAction = heap.size() == 1 && heap.peek().price() > 0 ?
					new HeroAction.Buy( cell ) :
					new HeroAction.PickUp( cell );
				break;
			default:
				curAction = new HeroAction.OpenChest( cell );
			}
			
		} else if (Dungeon.level.map[cell] == Terrain.LOCKED_DOOR || Dungeon.level.map[cell] == Terrain.LOCKED_EXIT) {
			
			curAction = new HeroAction.Unlock( cell );
			
		} else if ((cell == Dungeon.level.exit || Dungeon.level.map[cell] == Terrain.EXIT || Dungeon.level.map[cell] == Terrain.UNLOCKED_EXIT)
				&&  Dungeon.level.hasExit) {
			
			curAction = new HeroAction.Descend( cell );
			
		} else if (cell == Dungeon.level.entrance || Dungeon.level.map[cell] == Terrain.ENTRANCE
				&& Dungeon.level.hasEntrance) {
			
			curAction = new HeroAction.Ascend( cell );
			
		} else  {

			walkingToVisibleTrapInFog = !Dungeon.level.visited[cell] && !Dungeon.level.mapped[cell]
					&& Dungeon.level.traps.get(cell) != null && Dungeon.level.traps.get(cell).visible;
			
			curAction = new HeroAction.Move( cell );
			lastAction = null;
			
		}

		return true;
	}
	
	public void earnExp( int exp, Class source ) {
		
		this.exp += exp;
		float percent = exp/(float)maxExp();

		EtherealChains.chainsRecharge chains = buff(EtherealChains.chainsRecharge.class);
		if (chains != null) chains.gainExp(percent);

		HornOfPlenty.hornRecharge horn = buff(HornOfPlenty.hornRecharge.class);
		if (horn != null) horn.gainCharge(percent);
		
		AlchemistsToolkit.kitEnergy kit = buff(AlchemistsToolkit.kitEnergy.class);
		if (kit != null) kit.gainCharge(percent);
		
		Berserk berserk = buff(Berserk.class);
		if (berserk != null) berserk.recover(percent);
		
		if (source != PotionOfExperience.class) {
			for (Item i : belongings) {
				i.onHeroGainExp(percent, this);
			}
		}
		
		boolean levelUp = false;
		while (this.exp >= maxExp()) {
			this.exp -= maxExp();
			if (lvl < Constants.HERO_EXP_CAP) {
				lvl++;
				levelUp = true;
				
				if (buff(ElixirOfMight.HTBoost.class) != null){
					buff(ElixirOfMight.HTBoost.class).onLevelUp();
				}
				
				updateHT( true );

			} else {
				Buff.prolong(this, Bless.class, 30f);
				this.exp = 0;

				GLog.p( Messages.get(this, "level_cap"));
				Sample.INSTANCE.play( Assets.SND_LEVELUP );
			}
			
		}
		
		if (levelUp) {
			
			if (sprite != null) {
				GLog.p( Messages.get(this, "new_level"), lvl );
				sprite.showStatus( CharSprite.POSITIVE, Messages.get(Hero.class, "level_up") );
				Sample.INSTANCE.play( Assets.SND_LEVELUP );
				float missingMoralePercent = (float) (1f - (morale/MAX_MORALE)*0.1);
				gainMorale(missingMoralePercent*0.5f);//Gains more Morale on level up when on low Morale (up to 1)
			}

			DistributionPoints += 3;
			MainGame.runOnRenderThread(new Callback() {
				@Override
				public void call() {
					WndHero window = new WndHero();
					window.switchToAbilities();
					GameScene.show(window);
				}
			});
			Item.updateQuickslot();
			
			Badges.validateLevelReached();
		}
	}
	
	public int maxExp() {
		return maxExp( lvl );
	}
	
	public static int maxExp( int lvl ){
		return 5 + lvl * 5;
	}
	
	public boolean isStarving() {
		return Buff.affect(this, Hunger.class).isStarving();
	}
	
	@Override
	public void add( Buff buff ) {

		if (buff(TimekeepersHourglass.timeStasis.class) != null)
			return;

		super.add( buff );

		if (sprite != null) {
			String msg = buff.heroMessage();
			if (msg != null){
				GLog.w(msg);
			}

			if (buff instanceof Paralysis || buff instanceof Vertigo) {
				interrupt();
			}

		}
		
		BuffIndicator.refreshHero();
	}
	
	@Override
	public void remove( Buff buff ) {
		super.remove( buff );

		BuffIndicator.refreshHero();
	}
	
	@Override
	public void die( Object cause  ) {
		
		curAction = null;

		Ankh ankh = null;

		//look for ankhs in player inventory, prioritize ones which are blessed.
		for (Item item : belongings){
			if (item instanceof Ankh) {
				if (ankh == null) {
					ankh = (Ankh) item;
				}
			}
		}

		if (ankh != null) {
			ankh.revive(this);
			return;
		}
		
		Actor.fixTime();
		super.die( cause );
		reallyDie( cause );
		/*
		if (ankh == null) {
			
			reallyDie( cause );
			
		} else {
			
			Dungeon.deleteGame( GamesInProgress.curSlot, false );
			final Ankh finalAnkh = ankh;
			Game.runOnRenderThread(new Callback() {
				@Override
				public void call() {
					GameScene.show( new WndResurrect( finalAnkh, cause ) );
				}
			});
			
		}*/
	}
	
	public static void reallyDie( Object cause ) {
		
		int length = Dungeon.level.length();
		Terrain[] map = Dungeon.level.map;
		boolean[] visited = Dungeon.level.visited;
		boolean[] discoverable = Dungeon.level.discoverable;
		
		for (int i=0; i < length; i++) {
			
			Terrain terr = map[i];
			
			if (discoverable[i]) {
				
				visited[i] = true;
				if (terr.secret) {
					Dungeon.level.discover( i );
				}
			}
		}
		
		Bones.leave();
		
		Dungeon.observe();
		GameScene.updateFog();
				
		Dungeon.hero.belongings.identify();

		int pos = Dungeon.hero.pos;

		ArrayList<Integer> passable = new ArrayList<>();
		for (Integer ofs : PathFinder.NEIGHBOURS8) {
			int cell = pos + ofs;
			if ((Dungeon.level.passable()[cell] || Dungeon.level.avoid()[cell]) && Dungeon.level.heaps.get( cell ) == null) {
				passable.add( cell );
			}
		}
		Collections.shuffle( passable );

		ArrayList<Item> items = new ArrayList<>(Dungeon.hero.belongings.backpack.items);
		for (Integer cell : passable) {
			if (items.isEmpty()) {
				break;
			}

			Item item = Random.element( items );
			Dungeon.level.drop( item, cell ).sprite.drop( pos );
			items.remove( item );
		}

		GameScene.gameOver();
		
		if (cause instanceof Hero.Doom) {
			((Hero.Doom)cause).onDeath();
		}
		
		Dungeon.deleteGame( GamesInProgress.curSlot, true );
	}

	//effectively cache this buff to prevent having to call buff(Berserk.class) a bunch.
	//This is relevant because we call isAlive during drawing, which has both performance
	//and concurrent modification implications if that method calls buff(Berserk.class)
	private Berserk berserk;

	@Override
	public boolean isAlive() {
		
		if (HP <= 0){
			if (berserk == null) berserk = buff(Berserk.class);
			return berserk != null && berserk.berserking();
		} else {
			berserk = null;
			return super.isAlive();
		}
	}

	@Override
	public void move( int step ) {
		super.move( step );
		
		if (!flying) {
			if (Dungeon.level.liquid()[pos]) {
				Sample.INSTANCE.play( Assets.SND_WATER, 1, 1, Random.Float( 0.8f, 1.25f ) );
			} else {
				Sample.INSTANCE.play( Assets.SND_STEP );
			}
		}
	}
	
	@Override
	public void onAttackComplete() {
		
		AttackIndicator.target(enemy);
		
		boolean hit = attack( enemy );

		if (subClass == HeroSubClass.GLADIATOR){
			if (hit) {
				Buff.affect( this, Combo.class ).hit( enemy );
			} else {
				Combo combo = buff(Combo.class);
				if (combo != null) combo.miss( enemy );
			}
		}
		
		Invisibility.dispel();
		spend( attackDelay() );

		curAction = null;

		super.onAttackComplete();
	}
	
	@Override
	public void onOperateComplete() {
		
		if (curAction instanceof HeroAction.Unlock) {

			int doorCell = ((HeroAction.Unlock)curAction).dst;
			Terrain door = Dungeon.level.map[doorCell];
			
			if (Dungeon.level.distance(pos, doorCell) <= 1) {
				boolean hasKey = true;
				if (door == Terrain.LOCKED_DOOR) {
					hasKey = Notes.remove(new IronKey(Dungeon.xPos, Dungeon.yPos, Dungeon.zPos));
					if (hasKey) Dungeon.level.set(doorCell, Terrain.DOOR);
				} else {
					hasKey = Notes.remove(new SkeletonKey(Dungeon.xPos, Dungeon.yPos, Dungeon.zPos));
					if (hasKey) Dungeon.level.set(doorCell, Terrain.UNLOCKED_EXIT);
				}
				
				if (hasKey) {
					GameScene.updateKeyDisplay();
					Dungeon.level.set(doorCell, door == Terrain.LOCKED_DOOR ? Terrain.DOOR : Terrain.UNLOCKED_EXIT);
					GameScene.updateMap(doorCell);
					spend(Key.TIME_TO_UNLOCK);
				}
			}
			
		} else if (curAction instanceof HeroAction.OpenChest) {
			
			Heap heap = Dungeon.level.heaps.get( ((HeroAction.OpenChest)curAction).dst );
			
			if (Dungeon.level.distance(pos, heap.pos) <= 1){
				boolean hasKey = true;
				if (heap.type == Type.SKELETON || heap.type == Type.REMAINS) {
					Sample.INSTANCE.play( Assets.SND_BONES );
				} else if (heap.type == Type.LOCKED_CHEST){
					hasKey = Notes.remove(new GoldenKey(Dungeon.xPos, Dungeon.yPos, Dungeon.zPos));
				} else if (heap.type == Type.CRYSTAL_CHEST){
					hasKey = Notes.remove(new CrystalKey(Dungeon.xPos, Dungeon.yPos, Dungeon.zPos));
				}
				
				if (hasKey) {
					GameScene.updateKeyDisplay();
					heap.open(this);
					spend(Key.TIME_TO_UNLOCK);
				}
			}
			
		}
		curAction = null;

		super.onOperateComplete();
	}

	public boolean search( boolean intentional ) {
		
		if (!isAlive()) return false;
		
		boolean smthFound = false;

		int distance = heroClass == HeroClass.ROGUE ? 2 : 1;
		
		boolean foresight = buff(Foresight.class) != null;
		
		if (foresight) distance++;
		
		int cx = pos % Dungeon.level.width();
		int cy = pos / Dungeon.level.width();
		int ax = cx - distance;
		if (ax < 0) {
			ax = 0;
		}
		int bx = cx + distance;
		if (bx >= Dungeon.level.width()) {
			bx = Dungeon.level.width() - 1;
		}
		int ay = cy - distance;
		if (ay < 0) {
			ay = 0;
		}
		int by = cy + distance;
		if (by >= Dungeon.level.height()) {
			by = Dungeon.level.height() - 1;
		}

		TalismanOfForesight.Foresight talisman = buff( TalismanOfForesight.Foresight.class );
		boolean cursed = talisman != null && talisman.isCursed();
		
		for (int y = ay; y <= by; y++) {
			for (int x = ax, p = ax + y * Dungeon.level.width(); x <= bx; x++, p++) {
				
				if (fieldOfView[p] && p != pos) {
					
					if (intentional) {
						sprite.parent.addToBack( new CheckedCell( p ) );
					}
					
					if (Dungeon.level.map[p].secret){
						
						Trap trap = Dungeon.level.traps.get( p );
						if (trap != null && !trap.canBeSearched){
							continue;
						}
						
						float chance;
						//intentional searches always succeed
						if (intentional){
							chance = 1f;
						
						//unintentional searches always fail with a cursed talisman
						} else if (cursed) {
							chance = 0f;
							
						//..and always succeed when affected by foresight buff
						} else if (foresight){
							chance = 1f;
							
						//unintentional trap detection scales from 40% at floor 0 to 30% at floor 25
						} else if (Dungeon.level.map[p] == Terrain.SECRET_TRAP) {
							chance = 0.4f - (Dungeon.yPos / 250f);
							
						//unintentional door detection scales from 20% at floor 0 to 0% at floor 20
						} else {
							chance = 0.2f - (Dungeon.yPos / 100f);
						}
						
						if (Random.Float() < chance) {
						
							Terrain oldValue = Dungeon.level.map[p];
							
							GameScene.discoverTile( p, oldValue );
							
							Dungeon.level.discover( p );
							
							ScrollOfMagicMapping.discover( p );
							
							smthFound = true;
	
							if (talisman != null && !talisman.isCursed())
								talisman.charge();
						}
					}
				}
			}
		}

		
		if (intentional) {
			sprite.showStatus( CharSprite.DEFAULT, Messages.get(this, "search") );
			sprite.operate( pos );
			if (!Dungeon.level.locked) {
				if (cursed) {
					GLog.n(Messages.get(this, "search_distracted"));
					Buff.affect(this, Hunger.class).reduceHunger(TIME_TO_SEARCH - (2 * HUNGER_FOR_SEARCH));
				} else {
					Buff.affect(this, Hunger.class).reduceHunger(TIME_TO_SEARCH - HUNGER_FOR_SEARCH);
				}
			}
			spendAndNext(TIME_TO_SEARCH);
			
		}
		
		if (smthFound) {
			GLog.w( Messages.get(this, "noticed_smth") );
			Sample.INSTANCE.play( Assets.SND_SECRET );
			interrupt();
		}
		
		return smthFound;
	}
	
	public void resurrect( int resetLevel ) {
		
		HP = HT;
		Dungeon.gold = 0;
		exp = 0;
		
		belongings.resurrect( resetLevel );

		live();
	}

	@Override
	public void next() {
		if (isAlive())
			super.next();
	}

	public interface Doom {
		void onDeath();
	}
}
