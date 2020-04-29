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
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.blobs.ToxicGas;
import com.shatteredpixel.yasd.general.actors.buffs.Amok;
import com.shatteredpixel.yasd.general.actors.buffs.Burning;
import com.shatteredpixel.yasd.general.actors.buffs.Charm;
import com.shatteredpixel.yasd.general.actors.buffs.Light;
import com.shatteredpixel.yasd.general.actors.buffs.LockedFloor;
import com.shatteredpixel.yasd.general.actors.buffs.Sleep;
import com.shatteredpixel.yasd.general.actors.buffs.Terror;
import com.shatteredpixel.yasd.general.actors.buffs.Vertigo;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.Pushing;
import com.shatteredpixel.yasd.general.effects.particles.ShadowParticle;
import com.shatteredpixel.yasd.general.items.artifacts.DriedRose;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.yasd.general.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Grim;
import com.shatteredpixel.yasd.general.levels.traps.GrimTrap;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.sprites.LarvaSprite;
import com.shatteredpixel.yasd.general.sprites.YogSprite;
import com.shatteredpixel.yasd.general.ui.BossHealthBar;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

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
	private Class[] toSummon = new Class[3];
	{
		toSummon[0] = (Random.Int(2) == 0 ? YogFist.Burning.class : YogFist.Soiled.class);
		toSummon[1] = (Random.Int(2) == 0 ? YogFist.Rotting.class : YogFist.Rusted.class);
		toSummon[2] = (Random.Int(2) == 0 ? YogFist.Bright.class  : YogFist.Dark.class);
		Random.shuffle(toSummon);
	}
	@Override
	public void damage( int dmg, DamageSrc src ) {

		if (findFist() != null){
			sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "immune"));
			return;
		}

		super.damage( dmg, src );

		if (phase < 3 && HP <= HT * (3 - phase)/4f){
			Dungeon.level.viewDistance--;
			if (Dungeon.hero.buff(Light.class) == null){
				Dungeon.hero.viewDistance = Dungeon.level.viewDistance;
			}
			Dungeon.observe();
			GLog.n(Messages.get(this, "darkness"));
			sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "immune"));

			YogFist fist = (YogFist) Reflection.newInstance(toSummon[phase]);
			fist.pos = Dungeon.level.getExitPos();

			//TODO change based on what fist is summoned?
			CellEmitter.get(Dungeon.level.getExitPos()-1).burst(ShadowParticle.UP, 25);
			CellEmitter.get(Dungeon.level.getExitPos()).burst(ShadowParticle.UP, 100);
			CellEmitter.get(Dungeon.level.getExitPos()+1).burst(ShadowParticle.UP, 25);

			int targetPos = Dungeon.level.getExitPos() + Dungeon.level.width();
			if (Actor.findChar(targetPos) == null){
				fist.pos = targetPos;
			} else if (Actor.findChar(targetPos-1) == null){
				fist.pos = targetPos-1;
			} else if (Actor.findChar(targetPos+1) == null){
				fist.pos = targetPos+1;
			}

			GameScene.add(fist, 1);
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
		super.notice();
		if (!BossHealthBar.isAssigned()) {
			BossHealthBar.assignBoss(this);
			yell(Messages.get(this, "notice"));
			for (Char ch : Actor.chars()){
				if (ch instanceof DriedRose.GhostHero){
					GLog.n("\n");
					((DriedRose.GhostHero) ch).sayBoss();
				}
			}
		}
	}

	{
		immunities.add( Grim.class );
		immunities.add( GrimTrap.class );
		immunities.add( Terror.class );
		immunities.add( Amok.class );
		immunities.add( Charm.class );
		immunities.add( Sleep.class );
		immunities.add( Burning.class );
		immunities.add( ToxicGas.class );
		immunities.add( ScrollOfRetribution.class );
		immunities.add( ScrollOfPsionicBlast.class );
		immunities.add( Vertigo.class );
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		BossHealthBar.assignBoss(this);
	}

	public static class Larva extends Mob {

		{
			spriteClass = LarvaSprite.class;

			healthFactor = 0.33f;
			damageFactor = 1.1f;

			EXP = 5;
			maxLvl = -2;

			state = HUNTING;

			properties.add(Property.DEMONIC);
		}

	}

}
