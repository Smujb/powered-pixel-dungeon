/*
 *
 *   Pixel Dungeon
 *   Copyright (C) 2012-2015 Oleg Dolya
 *
 *   Shattered Pixel Dungeon
 *   Copyright (C) 2014-2019 Evan Debenham
 *
 *   Yet Another Shattered Dungeon
 *   Copyright (C) 2014-2020 Samuel Braithwaite
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Statistics;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Amok;
import com.shatteredpixel.yasd.general.actors.buffs.Charm;
import com.shatteredpixel.yasd.general.actors.buffs.Cripple;
import com.shatteredpixel.yasd.general.actors.buffs.Light;
import com.shatteredpixel.yasd.general.actors.buffs.LockedFloor;
import com.shatteredpixel.yasd.general.actors.buffs.Roots;
import com.shatteredpixel.yasd.general.actors.buffs.Sleep;
import com.shatteredpixel.yasd.general.actors.buffs.Terror;
import com.shatteredpixel.yasd.general.actors.buffs.Vertigo;
import com.shatteredpixel.yasd.general.effects.Beam;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.Pushing;
import com.shatteredpixel.yasd.general.effects.TargetedCell;
import com.shatteredpixel.yasd.general.effects.particles.PurpleParticle;
import com.shatteredpixel.yasd.general.effects.particles.ShadowParticle;
import com.shatteredpixel.yasd.general.items.artifacts.DriedRose;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.sprites.LarvaSprite;
import com.shatteredpixel.yasd.general.sprites.YogSprite;
import com.shatteredpixel.yasd.general.tiles.DungeonTilemap;
import com.shatteredpixel.yasd.general.ui.BossHealthBar;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class YogDzewa extends Mob {

	{
		spriteClass = YogSprite.class;

		healthFactor = 4f;

		EXP = 50;

		state = PASSIVE;

		properties.add(Property.BOSS);
		properties.add(Property.IMMOVABLE);
		properties.add(Property.DEMONIC);
	}

	private int phase = 0;
	private float abilityCooldown;
	private static final int MIN_ABILITY_CD = 10;
	private static final int MAX_ABILITY_CD = 15;

	private float summonCooldown;
	private static final int MIN_SUMMON_CD = 10;
	private static final int MAX_SUMMON_CD = 15;

	private ArrayList<Class> fistSummons = new ArrayList<>();
	{
		fistSummons.add(Random.Int(2) == 0 ? YogFist.Burning.class : YogFist.Soiled.class);
		fistSummons.add(Random.Int(2) == 0 ? YogFist.Rotting.class : YogFist.Rusted.class);
		fistSummons.add(Random.Int(2) == 0 ? YogFist.Bright.class : YogFist.Dark.class);
		Random.shuffle(fistSummons);
	}

	private static final int SUMMON_DECK_SIZE = 4;
	private ArrayList<Class> regularSummons = new ArrayList<>();

	{
		for (int i = 0; i < SUMMON_DECK_SIZE; i++){
			if (i >= Statistics.spawnersAlive){
				regularSummons.add(Larva.class);
			} else {
				regularSummons.add(YogRipper.class);
			}
		}
		Random.shuffle(regularSummons);
	}

	private ArrayList<Integer> targetedCells = new ArrayList<>();

	@Override
	protected boolean act() {
		if (phase == 0 && Dungeon.hero.viewDistance >= Dungeon.level.distance(pos, Dungeon.hero.pos)){
			Dungeon.observe();
			if (Dungeon.level.heroFOV[pos]) {
				notice();
			}
		}

		if (phase == 4 && findFist() == null){
			yell(Messages.get(this, "hope"));
			summonCooldown = -15; //summon a burst of minions!
			phase = 5;
		}

		if (phase == 0){
			spend(TICK);
			return true;
		} else {

			boolean terrainAffected = false;
			HashSet<Char> affected = new HashSet<>();
			for (int i : targetedCells){
				Ballistica b = new Ballistica(pos, i, Ballistica.WONT_STOP);
				//shoot beams
				sprite.parent.add(new Beam.DeathRay(sprite.center(), DungeonTilemap.raisedTileCenterToWorld(b.collisionPos)));
				for (int p : b.path){
					Char ch = Actor.findChar(p);
					if (ch != null && ch.alignment != alignment){
						affected.add(ch);
					}
					if (Dungeon.level.flammable(p)){
						Dungeon.level.destroy( p );
						GameScene.updateMap( p );
						terrainAffected = true;
					}
				}
			}
			if (terrainAffected){
				Dungeon.observe();
			}
			for (Char ch : affected){
				ch.damage(damageRoll(), this);

				if (Dungeon.level.heroFOV[pos]) {
					ch.sprite.flash();
					CellEmitter.center( pos ).burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
				}
				if (!ch.isAlive() && ch == Dungeon.hero) {
					Dungeon.fail( getClass() );
					GLog.n( Messages.get(Char.class, "kill", name()) );
				}
			}
			targetedCells.clear();

			if (abilityCooldown <= 0){

				int beams = 1 + (HT - HP)/400;
				HashSet<Integer> affectedCells = new HashSet<>();
				for (int i = 0; i < beams; i++){

					int targetPos = Dungeon.hero.pos;
					if (i != 0){
						do {
							targetPos = Dungeon.hero.pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
						} while (Dungeon.level.trueDistance(pos, Dungeon.hero.pos)
								> Dungeon.level.trueDistance(pos, targetPos));
					}
					targetedCells.add(targetPos);
					Ballistica b = new Ballistica(pos, targetPos, Ballistica.WONT_STOP);
					affectedCells.addAll(b.path);
				}

				//remove one beam if multiple shots would cause every cell next to the hero to be targeted
				boolean allAdjTargeted = true;
				for (int i : PathFinder.NEIGHBOURS9){
					if (!affectedCells.contains(Dungeon.hero.pos + i) && Dungeon.level.passable(Dungeon.hero.pos + i)){
						allAdjTargeted = false;
						break;
					}
				}
				if (allAdjTargeted){
					targetedCells.remove(targetedCells.size()-1);
				}
				for (int i : targetedCells){
					Ballistica b = new Ballistica(pos, i, Ballistica.WONT_STOP);
					for (int p : b.path){
						sprite.parent.add(new TargetedCell(p, 0xFF0000));
						affectedCells.add(p);
					}
				}

				//wait extra time to let a crippled/rooted hero evade
				if (Dungeon.hero.buff(Cripple.class) != null){
					spend(TICK);
				} else if (Dungeon.hero.buff(Roots.class) != null){
					spend(Dungeon.hero.buff(Roots.class).cooldown());
				}

				Dungeon.hero.interrupt();

				abilityCooldown += Random.NormalFloat(MIN_ABILITY_CD, MAX_ABILITY_CD);
				abilityCooldown -= phase;

			}

			while (summonCooldown <= 0){

				Class<?extends Mob> cls = regularSummons.remove(0);
				Mob summon = Reflection.newInstance(cls);
				regularSummons.add(cls);

				int spawnPos = -1;
				for (int i : PathFinder.NEIGHBOURS8){
					if (Actor.findChar(pos+i) == null){
						if (spawnPos == -1 || Dungeon.level.trueDistance(Dungeon.hero.pos, spawnPos) > Dungeon.level.trueDistance(Dungeon.hero.pos, pos+i)){
							spawnPos = pos + i;
						}
					}
				}

				if (spawnPos != -1) {
					summon.pos = spawnPos;
					GameScene.add( summon );
					Actor.addDelayed( new Pushing( summon, pos, summon.pos ), -1 );

					summonCooldown += Random.NormalFloat(MIN_SUMMON_CD, MAX_SUMMON_CD);
					summonCooldown -= phase;
					if (findFist() != null){
						summonCooldown += MIN_SUMMON_CD - phase;
					}
				}
			}

		}

		if (summonCooldown > 0) summonCooldown--;
		if (abilityCooldown > 0) abilityCooldown--;

		//extra fast abilities and summons at the final 100 HP
		if (phase == 5 && abilityCooldown > 2){
			abilityCooldown = 2;
		}
		if (phase == 5 && summonCooldown > 3){
			summonCooldown = 3;
		}

		spend(TICK);
		return true;
	}

	@Override
	public boolean isAlive() {
		return super.isAlive() || phase != 5;
	}
	@Override
	public void damage( int dmg, DamageSrc src ) {

		if (phase == 0 || findFist() != null){
			sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "immune"));
			return;
		}

		int preHP = HP;
		super.damage( dmg, src );

		int dmgTaken = preHP - HP;

		abilityCooldown -= dmgTaken/10f;
		summonCooldown -= dmgTaken/10f;

		if (phase < 4 && HP <= HT - (HT/3)*phase){
			HP = HT - (HT/3)*phase;

			Dungeon.level.viewDistance--;
			if (Dungeon.hero.buff(Light.class) == null){
				Dungeon.hero.viewDistance = Dungeon.level.viewDistance;
			}
			Dungeon.observe();
			GLog.n(Messages.get(this, "darkness"));
			sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "immune"));

			YogFist fist = (YogFist) Mob.create(fistSummons.remove(0), level);
			fist.pos = Dungeon.level.getExitPos();

			CellEmitter.get(Dungeon.level.getExitPos()-1).burst(ShadowParticle.UP, 25);
			CellEmitter.get(Dungeon.level.getExitPos()).burst(ShadowParticle.UP, 100);
			CellEmitter.get(Dungeon.level.getExitPos()+1).burst(ShadowParticle.UP, 25);

			if (abilityCooldown < 5) abilityCooldown = 5;
			if (summonCooldown < 5) summonCooldown = 5;

			int targetPos = Dungeon.level.getExitPos() + Dungeon.level.width();
			if (Actor.findChar(targetPos) == null){
				fist.pos = targetPos;
			} else if (Actor.findChar(targetPos-1) == null){
				fist.pos = targetPos-1;
			} else if (Actor.findChar(targetPos+1) == null){
				fist.pos = targetPos+1;
			}

			GameScene.add(fist, 3);
			Actor.addDelayed( new Pushing( fist, Dungeon.level.getExitPos(), fist.pos ), -1 );
			phase++;
		}

		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if (lock != null) lock.addTime(dmg);

	}

	private YogFist findFist(){
		for ( Char c : Actor.chars() ){
			if (c instanceof YogFist){
				return (YogFist) c;
			}
		}
		return null;
	}

	@Override
	public void beckon( int cell ) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void die( DamageSrc cause ) {

		for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
			if (mob instanceof Larva || mob instanceof RipperDemon) {
				mob.die( cause );
			}
		}

		Dungeon.level.viewDistance = 4;
		if (Dungeon.hero.buff(Light.class) == null){
			Dungeon.hero.viewDistance = Dungeon.level.viewDistance;
		}
		GameScene.bossSlain();
		Dungeon.level.unseal();
		super.die( cause );

		yell( Messages.get(this, "defeated") );
	}

	@Override
	public void notice() {
		if (!BossHealthBar.isAssigned()) {
			BossHealthBar.assignBoss(this);
			yell(Messages.get(this, "notice"));
			for (Char ch : Actor.chars()){
				if (ch instanceof DriedRose.GhostHero){
					GLog.n("\n");
					((DriedRose.GhostHero) ch).sayBoss();
				}
			}
			if (phase == 0) {
				phase = 1;
				summonCooldown = Random.NormalFloat(MIN_SUMMON_CD, MAX_SUMMON_CD);
				abilityCooldown = Random.NormalFloat(MIN_ABILITY_CD, MAX_ABILITY_CD);
			}
		}
	}

	@Override
	public String description() {
		String desc = super.description();

		if (Statistics.spawnersAlive > 0){
			desc += "\n\n" + Messages.get(this, "desc_spawners");
		}
		return desc;
	}

	{
		immunities.add( Terror.class );
		immunities.add( Amok.class );
		immunities.add( Charm.class );
		immunities.add( Sleep.class );
		immunities.add( Vertigo.class );
	}

	private static final String PHASE = "phase";

	private static final String ABILITY_CD = "ability_cd";
	private static final String SUMMON_CD = "summon_cd";

	private static final String FIST_SUMMONS = "fist_summons";
	private static final String REGULAR_SUMMONS = "regular_summons";

	private static final String TARGETED_CELLS = "targeted_cells";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(PHASE, phase);

		bundle.put(ABILITY_CD, abilityCooldown);
		bundle.put(SUMMON_CD, summonCooldown);

		bundle.put(FIST_SUMMONS, fistSummons.toArray(new Class[0]));
		bundle.put(REGULAR_SUMMONS, regularSummons.toArray(new Class[0]));

		int[] bundleArr = new int[targetedCells.size()];
		for (int i = 0; i < targetedCells.size(); i++){
			bundleArr[i] = targetedCells.get(i);
		}
		bundle.put(TARGETED_CELLS, bundleArr);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		BossHealthBar.assignBoss(this);
		phase = bundle.getInt(PHASE);
		if (phase != 0) BossHealthBar.assignBoss(this);

		abilityCooldown = bundle.getFloat(ABILITY_CD);
		summonCooldown = bundle.getFloat(SUMMON_CD);

		fistSummons.clear();
		Collections.addAll(fistSummons, bundle.getClassArray(FIST_SUMMONS));
		regularSummons.clear();
		Collections.addAll(regularSummons, bundle.getClassArray(REGULAR_SUMMONS));

		for (int i : bundle.getIntArray(TARGETED_CELLS)){
			targetedCells.add(i);
		}
	}

	public static class Larva extends Mob {

		{
			spriteClass = LarvaSprite.class;

			healthFactor = 0.33f;
			damageFactor = 1.1f;
			viewDistance = Light.DISTANCE;

			EXP = 5;
			maxLvl = -2;

			state = HUNTING;

			properties.add(Property.DEMONIC);
		}

	}

	//used so death to yog's ripper demons have their own rankings description and are more aggro
	public static class YogRipper extends RipperDemon {
		{
			state = HUNTING;
		}
	}

}
