/*
 *
 *  * Pixel Dungeon
 *  * Copyright (C) 2012-2015 Oleg Dolya
 *  *
 *  * Shattered Pixel Dungeon
 *  * Copyright (C) 2014-2019 Evan Debenham
 *  *
 *  * Powered Pixel Dungeon
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

package com.shatteredpixel.yasd.general.items.artifacts;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.PPDGame;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.blobs.CorrosiveGas;
import com.shatteredpixel.yasd.general.actors.blobs.ToxicGas;
import com.shatteredpixel.yasd.general.actors.buffs.Burning;
import com.shatteredpixel.yasd.general.actors.buffs.Corruption;
import com.shatteredpixel.yasd.general.actors.buffs.LockedFloor;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.actors.mobs.Wraith;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.Ghost;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.NPC;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.effects.particles.ShaftParticle;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.armor.Armor;
import com.shatteredpixel.yasd.general.items.armor.glyphs.Brimstone;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.yasd.general.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.CellSelector;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.sprites.GhostSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.ui.BossHealthBar;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
import com.shatteredpixel.yasd.general.ui.Window;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.shatteredpixel.yasd.general.windows.IconTitle;
import com.shatteredpixel.yasd.general.windows.WndBag;
import com.shatteredpixel.yasd.general.windows.WndBlacksmith;
import com.shatteredpixel.yasd.general.windows.WndUseItem;
import com.shatteredpixel.yasd.general.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DriedRose extends Artifact {

	{
		image = ItemSpriteSheet.ARTIFACT_ROSE1;

		levelCap = 10;

		charge = 100;
		chargeCap = 100;

		defaultAction = AC_SUMMON;
	}

	private boolean talkedTo = false;
	private boolean firstSummon = false;
	
	private GhostHero ghost = null;
	private int ghostID = 0;
	
	private MeleeWeapon weapon = null;
	private Armor armor = null;

	public int droppedPetals = 0;

	public static final String AC_SUMMON = "SUMMON";
	public static final String AC_DIRECT = "DIRECT";
	public static final String AC_OUTFIT = "OUTFIT";

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (!Ghost.Quest.completed()){
			actions.remove(AC_EQUIP);
			return actions;
		}
		if (isEquipped( hero ) && charge == chargeCap && !cursed && ghostID == 0) {
			actions.add(AC_SUMMON);
		}
		if (ghostID != 0){
			actions.add(AC_DIRECT);
		}
		if (isIdentified() && !cursed){
			actions.add(AC_OUTFIT);
		}
		
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {

		super.execute(hero, action);

		if (action.equals(AC_SUMMON)) {

			if (!Ghost.Quest.completed())   GameScene.show(new WndUseItem(null, this));
			else if (ghost != null)         GLog.i( Messages.get(this, "spawned") );
			else if (!isEquipped( hero ))   GLog.i( Messages.get(Artifact.class, "need_to_equip") );
			else if (charge != chargeCap)   GLog.i( Messages.get(this, "no_charge") );
			else if (cursed)                GLog.i( Messages.get(this, "cursed") );
			else {
				ArrayList<Integer> spawnPoints = new ArrayList<>();
				for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
					int p = hero.pos + PathFinder.NEIGHBOURS8[i];
					if (Actor.findChar(p) == null && (Dungeon.level.passable(p) || Dungeon.level.avoid(p))) {
						spawnPoints.add(p);
					}
				}

				if (spawnPoints.size() > 0) {
					ghost = new GhostHero( this );
					ghostID = ghost.id();
					ghost.pos = Random.element(spawnPoints);

					GameScene.add(ghost, 1f);
					Dungeon.level.occupyCell(ghost);
					
					CellEmitter.get(ghost.pos).start( ShaftParticle.FACTORY, 0.3f, 4 );
					CellEmitter.get(ghost.pos).start( Speck.factory(Speck.LIGHT), 0.2f, 3 );

					hero.spend(1f);
					hero.busy();
					hero.sprite.operate(hero.pos);

					if (!firstSummon) {
						ghost.yell( Messages.get(GhostHero.class, "hello", Dungeon.hero.name()) );
						Sample.INSTANCE.play( Assets.Sounds.GHOST );
						firstSummon = true;
						
					} else {
						if (BossHealthBar.isAssigned()) {
							ghost.sayBoss();
						} else {
							ghost.sayAppeared();
						}
					}
					
					charge = 0;
					partialCharge = 0;
					updateQuickslot();

				} else
					GLog.i( Messages.get(this, "no_space") );
			}

		} else if (action.equals(AC_DIRECT)){
			if (ghost == null && ghostID != 0){
				Actor a = Actor.findById(ghostID);
				if (a != null){
					ghost = (GhostHero)a;
				} else {
					ghostID = 0;
				}
			}
			if (ghost != null) GameScene.selectCell(ghostDirector);
			
		} else if (action.equals(AC_OUTFIT)){
			GameScene.show( new WndGhostHero(this) );
		}
	}
	
	public int ghostStrength(){
		return 13 + level()/2;
	}

	@Override
	public String desc() {
		if (!Ghost.Quest.completed() && !isIdentified()){
			return Messages.get(this, "desc_no_quest");
		}
		
		String desc = super.desc();

		if (isEquipped( Dungeon.hero )){
			if (!cursed){

				if (level() < levelCap)
					desc+= "\n\n" + Messages.get(this, "desc_hint");

			} else
				desc += "\n\n" + Messages.get(this, "desc_cursed");
		}


		desc += "\n";

		if (weapon != null){
			desc += "\n" + Messages.get(this, "desc_weapon", weapon.toString());
		}

		if (armor != null){
			desc += "\n" + Messages.get(this, "desc_armor", armor.toString());
		}


		return desc;
	}
	
	@Override
	public String status() {
		if (ghost == null && ghostID != 0){
			try {
				Actor a = Actor.findById(ghostID);
				if (a != null) {
					ghost = (GhostHero) a;
				} else {
					ghostID = 0;
				}
			} catch ( ClassCastException e ){
				PPDGame.reportException(e);
				ghostID = 0;
			}
		}
		if (ghost == null){
			return super.status();
		} else {
			return (int)((ghost.HP+partialCharge)*100) / ghost.HT + "%";
		}
	}
	
	@Override
	protected ArtifactBuff passiveBuff() {
		return new roseRecharge();
	}
	
	@Override
	public void charge(Hero target) {
		if (ghost == null){
			if (charge < chargeCap) {
				charge += 4;
				if (charge >= chargeCap) {
					charge = chargeCap;
					partialCharge = 0;
					GLog.p(Messages.get(DriedRose.class, "charged"));
				}
				updateQuickslot();
			}
		} else {
			ghost.HP = Math.min( ghost.HT, ghost.HP + 1 + level()/3);
			updateQuickslot();
		}
	}
	
	@Override
	public Item upgrade() {
		if (level() >= 9)
			image = ItemSpriteSheet.ARTIFACT_ROSE3;
		else if (level() >= 4)
			image = ItemSpriteSheet.ARTIFACT_ROSE2;

		//For upgrade transferring via well of transmutation
		droppedPetals = Math.max( level(), droppedPetals );
		
		if (ghost != null){
			ghost.updateRose();
		}

		return super.upgrade();
	}
	
	public Weapon ghostWeapon(){
		return weapon;
	}
	
	public Armor ghostArmor(){
		return armor;
	}

	private static final String TALKEDTO =      "talkedto";
	private static final String FIRSTSUMMON =   "firstsummon";
	private static final String GHOSTID =       "ghostID";
	private static final String PETALS =        "petals";
	
	private static final String WEAPON =        "getWeapons";
	private static final String ARMOR =         "getArmors";

	@Override
	public void storeInBundle(  Bundle bundle ) {
		super.storeInBundle(bundle);

		bundle.put( TALKEDTO, talkedTo );
		bundle.put( FIRSTSUMMON, firstSummon );
		bundle.put( GHOSTID, ghostID );
		bundle.put( PETALS, droppedPetals );
		
		if (weapon != null) bundle.put( WEAPON, weapon );
		if (armor != null)  bundle.put( ARMOR, armor );
	}

	@Override
	public void restoreFromBundle(  Bundle bundle ) {
		super.restoreFromBundle(bundle);

		talkedTo = bundle.getBoolean( TALKEDTO );
		firstSummon = bundle.getBoolean( FIRSTSUMMON );
		ghostID = bundle.getInt( GHOSTID );
		droppedPetals = bundle.getInt( PETALS );
		
		if (ghostID != 0) defaultAction = AC_DIRECT;
		
		if (bundle.contains(WEAPON)) weapon = (MeleeWeapon)bundle.get( WEAPON );
		if (bundle.contains(ARMOR))  armor = (Armor)bundle.get( ARMOR );
	}

	public class roseRecharge extends ArtifactBuff {

		@Override
		public boolean act() {
			
			spend( TICK );
			
			if (ghost == null && ghostID != 0){
				Actor a = Actor.findById(ghostID);
				if (a != null){
					ghost = (GhostHero)a;
				} else {
					ghostID = 0;
				}
			}
			
			//rose does not charge while ghost hero is alive
			if (ghost != null){
				defaultAction = AC_DIRECT;
				
				//heals to full over 1000 turns
				if (ghost.HP < ghost.HT) {
					partialCharge += ghost.HT / 1000f;
					updateQuickslot();
					
					if (partialCharge > 1) {
						ghost.HP++;
						partialCharge--;
					}
				} else {
					partialCharge = 0;
				}
				
				return true;
			} else {
				defaultAction = AC_SUMMON;
			}
			
			LockedFloor lock = target.buff(LockedFloor.class);
			if (charge < chargeCap && !cursed && (lock == null || lock.regenOn())) {
				partialCharge += 1/5f; //500 turns to a full charge
				if (partialCharge > 1){
					charge++;
					partialCharge--;
					if (charge == chargeCap){
						partialCharge = 0f;
						GLog.p( Messages.get(DriedRose.class, "charged") );
					}
				}
			} else if (cursed && Random.Int(100) == 0) {

				ArrayList<Integer> spawnPoints = new ArrayList<>();

				for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
					int p = target.pos + PathFinder.NEIGHBOURS8[i];
					if (Actor.findChar(p) == null && (Dungeon.level.passable(p) || Dungeon.level.avoid(p))) {
						spawnPoints.add(p);
					}
				}

				if (spawnPoints.size() > 0) {
					Wraith.spawnAt(Random.element(spawnPoints));
					Sample.INSTANCE.play(Assets.Sounds.CURSED);
				}

			}

			updateQuickslot();

			return true;
		}
	}
	
	public CellSelector.Listener ghostDirector = new CellSelector.Listener(){
		
		@Override
		public void onSelect(Integer cell) {
			if (cell == null) return;
			
			Sample.INSTANCE.play( Assets.Sounds.GHOST );
			
			if (!Dungeon.level.heroFOV[cell]
					|| Actor.findChar(cell) == null
					|| (Actor.findChar(cell) != Dungeon.hero && Actor.findChar(cell).alignment != Char.Alignment.ENEMY)){
				ghost.yell(Messages.get(ghost, "directed_position_" + Random.IntRange(1, 5)));
				ghost.aggro(null);
				ghost.state = ghost.WANDERING;
				ghost.defendingPos = cell;
				ghost.movingToDefendPos = true;
				return;
			}
			
			if (ghost.fieldOfView == null || ghost.fieldOfView.length != Dungeon.level.length()){
				ghost.fieldOfView = new boolean[Dungeon.level.length()];
			}
			Dungeon.level.updateFieldOfView( ghost, ghost.fieldOfView );
			
			if (Actor.findChar(cell) == Dungeon.hero){
				ghost.yell(Messages.get(ghost, "directed_follow_" + Random.IntRange(1, 5)));
				ghost.aggro(null);
				ghost.state = ghost.WANDERING;
				ghost.defendingPos = -1;
				ghost.movingToDefendPos = false;
				
			} else if (Actor.findChar(cell).alignment == Char.Alignment.ENEMY){
				ghost.yell(Messages.get(ghost, "directed_attack_" + Random.IntRange(1, 5)));
				ghost.aggro(Actor.findChar(cell));
				ghost.setTarget(cell);
				ghost.movingToDefendPos = false;
				
			}
		}
		
		@Override
		public String prompt() {
			return  "\"" + Messages.get(GhostHero.class, "direct_prompt") + "\"";
		}
	};

	public static class Petal extends Item {

		{
			stackable = true;
			dropsDownHeap = true;
			
			image = ItemSpriteSheet.PETAL;
		}

		@Override
		public boolean doPickUp(Char ch) {
			DriedRose rose = ch.belongings.getItem( DriedRose.class );

			if (rose == null){
				GLog.w( Messages.get(this, "no_rose") );
				return false;
			} if ( rose.level() >= rose.levelCap ){
				GLog.i( Messages.get(this, "no_room") );
				ch.spendAndNext(TIME_TO_PICK_UP);
				return true;
			} else {

				rose.upgrade();
				if (rose.level() == rose.levelCap) {
					GLog.p( Messages.get(this, "maxlevel") );
				} else
					GLog.i( Messages.get(this, "levelup") );

				Sample.INSTANCE.play( Assets.Sounds.DEWDROP );
				ch.spendAndNext(TIME_TO_PICK_UP);
				return true;

			}
		}

	}

	public static class GhostHero extends NPC {

		{
			spriteClass = GhostSprite.class;

			flying = true;

			alignment = Alignment.ALLY;
			intelligentAlly = true;
			WANDERING = new Wandering();
			
			state = HUNTING;
			
			//before other mobs
			actPriority = MOB_PRIO + 1;
			
			properties.add(Property.UNDEAD);
		}
		
		private DriedRose rose = null;
		
		public GhostHero(){
			super();
		}

		public GhostHero(DriedRose rose){
			super();
			this.rose = rose;
			updateRose();
			HP = HT;
		}
		
		private void updateRose(){
			if (rose == null) {
				rose = Dungeon.hero.belongings.getItem(DriedRose.class);
			}
			
			//same dodge as the hero
			defenseSkill = (Dungeon.hero.lvl+4);
			if (rose == null) return;
			HT = 20 + 8*rose.level();
		}
		
		private int defendingPos = -1;
		private boolean movingToDefendPos = false;
		
		public void clearDefensingPos(){
			defendingPos = -1;
			movingToDefendPos = false;
		}

		@Override
		protected boolean act() {
			updateRose();
			if (rose == null || !rose.isEquipped(Dungeon.hero)){
				damage(1, new Char.DamageSrc(Element.META).ignoreDefense());

			}
			
			if (!isAlive())
				return true;
			if (!Dungeon.hero.isAlive()){
				sayHeroKilled();
				sprite.die();
				destroy();
				return true;
			}
			return super.act();
		}
		
		@Override
		protected Char chooseEnemy() {
			Char enemy = super.chooseEnemy();
			
			int targetPos = defendingPos != -1 ? defendingPos : Dungeon.hero.pos;
			
			//will never attack something far from their target
			if (enemy != null
					&& Dungeon.level.mobs.contains(enemy)
					&& (Dungeon.level.distance(enemy.pos, targetPos) <= 8)){
				return enemy;
			}
			
			return null;
		}

		@Override
		public int attackSkill(Char target) {
			
			//same accuracy as the hero.
			int acc = Dungeon.hero.lvl + 9;
			
			if (rose != null && rose.weapon != null){
				acc *= rose.weapon.accuracyFactor(this);
			}
			
			return acc;
		}
		
		@Override
        public float attackDelay() {
			float delay = super.attackDelay();
			if (rose != null && rose.weapon != null){
				delay *= rose.weapon.speedFactor(this);
			}
			return delay;
		}
		
		@Override
        public boolean canAttack(@NotNull Char enemy) {
			return super.canAttack(enemy) || (rose != null && rose.weapon != null && rose.weapon.canReach(this, enemy.pos));
		}
		
		@Override
		public int damageRoll() {
			int dmg = 0;
			if (rose != null && rose.weapon != null){
				dmg += rose.weapon.damageRoll(this);
			} else {
				dmg += Random.NormalIntRange(0, 5);
			}
			dmg = affectDamageRoll(dmg);
			
			return dmg;
		}
		
		@Override
		public int attackProc(Char enemy, int damage) {
			damage = super.attackProc(enemy, damage);
			if (rose != null && rose.weapon != null) {
				rose.weapon.use();
				damage = rose.weapon.proc( this, enemy, damage );
			}
			return damage;
		}
		
		@Override
		public int defenseProc(Char enemy, int damage, DamageSrc src) {
			if (rose != null && rose.armor != null) {
				rose.armor.use();
				return rose.armor.proc( enemy, this, damage );
			} else {
				return super.defenseProc(enemy, damage, src);
			}
		}
		
		@Override
		public void damage(int dmg,  DamageSrc src) {
			super.damage( dmg, src);
			Item.updateQuickslot();
		}
		
		@Override
		public float speed() {
			float speed = super.speed();
			
			//if (rose != null && rose.armor != null){
		//		speed = rose.armor.speedFactor(this, speed);
		//	}
			
			return speed;
		}
		
		@Override
		public int defenseSkill(Char enemy) {
			int defense = super.defenseSkill(enemy);

		//	if (defense != 0 && rose != null && rose.armor != null ){
		//		defense = Math.round(rose.armor.evasionFactor( this, defense ));
		//	}
			
			return defense;
		}
		
		@Override
		public float sneakSkill(Char enemy) {
			float stealth = super.sneakSkill(enemy);
			
		//	if (rose != null && rose.armor != null){
		//		stealth = rose.armor.stealthFactor(this, stealth);
		//	}
			
			return stealth;
		}

		@Override
		public int defense() {

			return rose.armor.defense();
		}
		
		private void setTarget(int cell) {
			target = cell;
		}

		@Override
		public boolean isImmune(Class effect) {
			if (effect == Burning.class
					&& rose != null
					&& rose.armor != null
					&& rose.armor.hasGlyph(Brimstone.class, this)){
				return true;
			}
			return super.isImmune(effect);
		}

		@Override
		public boolean interact(Char c) {
			updateRose();
			if (c == Dungeon.hero && rose != null && !rose.talkedTo){
				rose.talkedTo = true;
				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						GameScene.show(new WndQuest(GhostHero.this, Messages.get(GhostHero.this, "introduce") ));
					}
				});
				return false;
			} else {
				return super.interact(c);
			}
		}

		@Override
		public void die( DamageSrc cause) {
			sayDefeated();
			super.die(cause);
		}

		@Override
		public void destroy() {
			updateRose();
			if (rose != null) {
				rose.ghost = null;
				rose.charge = 0;
				rose.partialCharge = 0;
				rose.ghostID = -1;
				rose.defaultAction = AC_SUMMON;
			}
			super.destroy();
		}
		
		public void sayAppeared(){
			int depth = (Dungeon.depth - 1) / 5;
			
			//only some lines are said on the first floor of a depth
			int variant = Dungeon.depth % Constants.CHAPTER_LENGTH == 1 ? Random.IntRange(1, 3) : Random.IntRange(1, 6);
			
			switch(depth){
				case 0:
					yell( Messages.get( this, "dialogue_sewers_" + variant ));
					break;
				case 1:
					yell( Messages.get( this, "dialogue_prison_" + variant ));
					break;
				case 2:
					yell( Messages.get( this, "dialogue_caves_" + variant ));
					break;
				case 3:
					yell( Messages.get( this, "dialogue_city_" + variant ));
					break;
				case 4: default:
					yell( Messages.get( this, "dialogue_halls_" + variant ));
					break;
			}
			if (PPDGame.scene() instanceof GameScene) {
				Sample.INSTANCE.play( Assets.Sounds.GHOST );
			}
		}
		
		public void sayBoss(){
			int depth = (Dungeon.depth - 1) / Constants.CHAPTER_LENGTH;
			
			switch(depth){
				case 0:
					yell( Messages.get( this, "seen_goo_" + Random.IntRange(1, 3) ));
					break;
				case 1:
					yell( Messages.get( this, "seen_tengu_" + Random.IntRange(1, 3) ));
					break;
				case 2:
					yell( Messages.get( this, "seen_dm300_" + Random.IntRange(1, 3) ));
					break;
				case 3:
					yell( Messages.get( this, "seen_king_" + Random.IntRange(1, 3) ));
					break;
				case 4: default:
					yell( Messages.get( this, "seen_yog_" + Random.IntRange(1, 3) ));
					break;
			}
			Sample.INSTANCE.play( Assets.Sounds.GHOST );
		}
		
		public void sayDefeated(){
			if (BossHealthBar.isAssigned()){
				yell( Messages.get( this, "defeated_by_boss_" + Random.IntRange(1, 3) ));
			} else {
				yell( Messages.get( this, "defeated_by_enemy_" + Random.IntRange(1, 3) ));
			}
			Sample.INSTANCE.play( Assets.Sounds.GHOST );
		}
		
		public void sayHeroKilled(){
			if (Dungeon.bossLevel()){
				yell( Messages.get( this, "hero_killed_boss_" + Random.IntRange(1, 3) ));
			} else {
				yell( Messages.get( this, "hero_killed_" + Random.IntRange(1, 3) ));
			}
			Sample.INSTANCE.play( Assets.Sounds.GHOST );
		}
		
		public void sayAnhk(){
			yell( Messages.get( this, "blessed_ankh_" + Random.IntRange(1, 3) ));
			Sample.INSTANCE.play( Assets.Sounds.GHOST );
		}
		
		private static final String DEFEND_POS = "defend_pos";
		private static final String MOVING_TO_DEFEND = "moving_to_defend";
		
		@Override
		public void storeInBundle( Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(DEFEND_POS, defendingPos);
			bundle.put(MOVING_TO_DEFEND, movingToDefendPos);
		}
		
		@Override
		public void restoreFromBundle( Bundle bundle) {
			super.restoreFromBundle(bundle);
			if (bundle.contains(DEFEND_POS)) defendingPos = bundle.getInt(DEFEND_POS);
			movingToDefendPos = bundle.getBoolean(MOVING_TO_DEFEND);
		}
		
		{
			immunities.add( ToxicGas.class );
			immunities.add( CorrosiveGas.class );
			immunities.add( Burning.class );
			immunities.add( ScrollOfRetribution.class );
			immunities.add( ScrollOfPsionicBlast.class );
			immunities.add( Corruption.class );
		}
		
		private class Wandering extends Mob.Wandering {
			
			@Override
			public boolean act( boolean enemyInFOV, boolean justAlerted ) {
				if ( enemyInFOV && !movingToDefendPos ) {
					
					enemySeen = true;
					
					notice();
					alerted = true;
					state = HUNTING;
					target = enemy.pos;
					
				} else {
					
					enemySeen = false;
					
					int oldPos = pos;
					target = defendingPos != -1 ? defendingPos : Dungeon.hero.pos;
					//always move towards the hero when wandering
					if (getCloser( target )) {
						//moves 2 tiles at a time when returning to the hero
						if (defendingPos == -1 && !Dungeon.level.adjacent(target, pos)){
							getCloser( target );
						}
						spend( 1 / speed() );
						if (pos == defendingPos) movingToDefendPos = false;
						return moveSprite( oldPos, pos );
					} else {
						spend( TICK );
					}
					
				}
				return true;
			}
			
		}

	}
	
	private static class WndGhostHero extends Window{
		
		private static final int BTN_SIZE	= 32;
		private static final float GAP		= 2;
		private static final float BTN_GAP	= 12;
		private static final int WIDTH		= 116;
		
		private WndBlacksmith.ItemButton btnWeapon;
		private WndBlacksmith.ItemButton btnArmor;
		
		WndGhostHero(final DriedRose rose){
			
			IconTitle titlebar = new IconTitle();
			titlebar.icon( new ItemSprite(rose) );
			titlebar.label( Messages.get(this, "title") );
			titlebar.setRect( 0, 0, WIDTH, 0 );
			add( titlebar );
			
			RenderedTextBlock message =
					PixelScene.renderTextBlock(Messages.get(this, "desc", rose.ghostStrength()), 6);
			message.maxWidth( WIDTH );
			message.setPos(0, titlebar.bottom() + GAP);
			add( message );
			
			btnWeapon = new WndBlacksmith.ItemButton(){
				@Override
				protected void onClick() {
					if (rose.weapon != null){
						item(new WndBag.Placeholder(ItemSpriteSheet.WEAPON_HOLDER));
						if (!rose.weapon.doPickUp(Dungeon.hero)){
							Dungeon.level.drop( rose.weapon, Dungeon.hero.pos);
						}
						rose.weapon = null;
					} else {
						GameScene.selectItem(new WndBag.Listener() {
							@Override
							public void onSelect(Item item) {
								if (!(item instanceof MeleeWeapon)) {
									//do nothing, should only happen when window is cancelled
								} else if (item.unique) {
									GLog.w( Messages.get(WndGhostHero.class, "cant_unique"));
									hide();
								} else if (!item.isIdentified()) {
									GLog.w( Messages.get(WndGhostHero.class, "cant_unidentified"));
									hide();
								} else if (item.cursed) {
									GLog.w( Messages.get(WndGhostHero.class, "cant_cursed"));
									hide();
								} else if (((MeleeWeapon)item).STRReq() > rose.ghostStrength()) {
									GLog.w( Messages.get(WndGhostHero.class, "cant_strength"));
									hide();
								} else {
									if (item.isEquipped(Dungeon.hero)){
										((MeleeWeapon) item).doUnequip(Dungeon.hero, false, false);
									} else {
										item.detach(Dungeon.hero.belongings.backpack);
									}
									rose.weapon = (MeleeWeapon) item;
									item(rose.weapon);
								}
								
							}
						}, WndBag.Mode.WEAPON, Messages.get(WndGhostHero.class, "weapon_prompt"));
					}
				}
			};
			btnWeapon.setRect( (WIDTH - BTN_GAP) / 2 - BTN_SIZE, message.top() + message.height() + GAP, BTN_SIZE, BTN_SIZE );
			if (rose.weapon != null) {
				btnWeapon.item(rose.weapon);
			} else {
				btnWeapon.item(new WndBag.Placeholder(ItemSpriteSheet.WEAPON_HOLDER));
			}
			add( btnWeapon );
			
			btnArmor = new WndBlacksmith.ItemButton(){
				@Override
				protected void onClick() {
					if (rose.armor != null){
						item(new WndBag.Placeholder(ItemSpriteSheet.ARMOR_HOLDER));
						if (!rose.armor.doPickUp(Dungeon.hero)){
							Dungeon.level.drop( rose.armor, Dungeon.hero.pos);
						}
						rose.armor = null;
					} else {
						GameScene.selectItem(new WndBag.Listener() {
							@Override
							public void onSelect(Item item) {
								if (!(item instanceof Armor)) {
									//do nothing, should only happen when window is cancelled
								} else if (item.unique || ((Armor) item).checkSeal() != null) {
									GLog.w( Messages.get(WndGhostHero.class, "cant_unique"));
									hide();
								} else if (!item.isIdentified()) {
									GLog.w( Messages.get(WndGhostHero.class, "cant_unidentified"));
									hide();
								} else if (item.cursed) {
									GLog.w( Messages.get(WndGhostHero.class, "cant_cursed"));
									hide();
								} else if (((Armor)item).STRReq() > rose.ghostStrength()) {
									GLog.w( Messages.get(WndGhostHero.class, "cant_strength"));
									hide();
								} else {
									if (item.isEquipped(Dungeon.hero)){
										((Armor) item).doUnequip(Dungeon.hero, false, false);
									} else {
										item.detach(Dungeon.hero.belongings.backpack);
									}
									rose.armor = (Armor) item;
									item(rose.armor);
								}
								
							}
						}, WndBag.Mode.ARMOR, Messages.get(WndGhostHero.class, "armor_prompt"));
					}
				}
			};
			btnArmor.setRect( btnWeapon.right() + BTN_GAP, btnWeapon.top(), BTN_SIZE, BTN_SIZE );
			if (rose.armor != null) {
				btnArmor.item(rose.armor);
			} else {
				btnArmor.item(new WndBag.Placeholder(ItemSpriteSheet.ARMOR_HOLDER));
			}
			add( btnArmor );
			
			resize(WIDTH, (int)(btnArmor.bottom() + GAP));
		}
	
	}
}
