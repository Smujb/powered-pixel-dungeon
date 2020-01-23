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

package com.shatteredpixel.yasd;

import com.shatteredpixel.yasd.items.weapon.melee.Fist;
import com.shatteredpixel.yasd.scenes.PixelScene;
import com.shatteredpixel.yasd.scenes.WelcomeScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PlatformSupport;

public class YASD extends Game {
	
	//variable constants for specific older versions of shattered, used for data conversion
	//versions older than v0.6.5c are no longer supported, and data from them is ignored
	public static final int v0_6_5c = 264;
	
	public static final int v0_7_0c = 311;
	public static final int v0_7_1d = 323;
	public static final int v0_7_2d = 340;
	public static final int v0_7_3b = 349;
	public static final int v0_7_4c = 362;
	public static final int v0_7_5  = 371;
	public static final int v0_2_0  = 400;
	public static final int v0_2_1  = 401;
	
	public YASD(PlatformSupport platform ) {
		super( sceneClass == null ? WelcomeScene.class : sceneClass, platform );
		
		//v0.7.0
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.items.bombs.Bomb.class,
				"com.shatteredpixel.yasd.items.Bomb" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.items.scrolls.ScrollOfRetribution.class,
				"com.shatteredpixel.yasd.items.scrolls.ScrollOfPsionicBlast" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.items.potions.elixirs.ElixirOfMight.class,
				"com.shatteredpixel.yasd.items.potions.PotionOfMight" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.items.spells.MagicalInfusion.class,
				"com.shatteredpixel.yasd.items.scrolls.ScrollOfMagicalInfusion" );
		
		//v0.7.1
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.items.weapon.SpiritBow.class,
				"com.shatteredpixel.yasd.items.getWeapons.missiles.Boomerang" );
		
		com.watabou.utils.Bundle.addAlias(
				Fist.class,
				"com.shatteredpixel.yasd.items.getWeapons.melee.Knuckles" );
		
		//v0.7.2
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.items.stones.StoneOfDisarming.class,
				"com.shatteredpixel.yasd.items.stones.StoneOfDetectCurse" );
		
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.items.weapon.enchantments.Elastic.class,
				"com.shatteredpixel.yasd.items.getWeapons.curses.Elastic" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.items.weapon.enchantments.Elastic.class,
				"com.shatteredpixel.yasd.items.getWeapons.enchantments.Dazzling" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.items.weapon.enchantments.Elastic.class,
				"com.shatteredpixel.yasd.items.getWeapons.enchantments.Eldritch" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.items.weapon.enchantments.Grim.class,
				"com.shatteredpixel.yasd.items.getWeapons.enchantments.Stunning" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.items.weapon.enchantments.Chilling.class,
				"com.shatteredpixel.yasd.items.getWeapons.enchantments.Venomous" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.items.weapon.enchantments.Kinetic.class,
				"com.shatteredpixel.yasd.items.getWeapons.enchantments.Vorpal" );
		
		//v0.7.3
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.items.weapon.enchantments.Kinetic.class,
				"com.shatteredpixel.yasd.items.getWeapons.enchantments.Precise" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.items.weapon.enchantments.Kinetic.class,
				"com.shatteredpixel.yasd.items.getWeapons.enchantments.Swift" );
		
		//v0.7.5
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.levels.rooms.sewerboss.SewerBossEntranceRoom.class,
				"com.shatteredpixel.yasd.levels.rooms.standard.SewerBossEntranceRoom" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.levels.OldPrisonBossLevel.class,
				"com.shatteredpixel.yasd.levels.PrisonBossLevel" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.actors.mobs.OldTengu.class,
				"com.shatteredpixel.yasd.actors.mobs.Tengu" );
	}
	
	@Override
	public void create() {
		super.create();

		updateSystemUI();
		
		Music.INSTANCE.enable( YASDSettings.music() );
		Music.INSTANCE.volume( YASDSettings.musicVol()/10f );
		Sample.INSTANCE.enable( YASDSettings.soundFx() );
		Sample.INSTANCE.volume( YASDSettings.SFXVol()/10f );

		Sample.INSTANCE.load(
				Assets.SND_CLICK,
				Assets.SND_BADGE,
				Assets.SND_GOLD,

				Assets.SND_STEP,
				Assets.SND_WATER,
				Assets.SND_OPEN,
				Assets.SND_UNLOCK,
				Assets.SND_ITEM,
				Assets.SND_DEWDROP,
				Assets.SND_HIT,
				Assets.SND_MISS,

				Assets.SND_DESCEND,
				Assets.SND_EAT,
				Assets.SND_READ,
				Assets.SND_LULLABY,
				Assets.SND_DRINK,
				Assets.SND_SHATTER,
				Assets.SND_ZAP,
				Assets.SND_LIGHTNING,
				Assets.SND_LEVELUP,
				Assets.SND_DEATH,
				Assets.SND_CHALLENGE,
				Assets.SND_CURSED,
				Assets.SND_EVOKE,
				Assets.SND_TRAP,
				Assets.SND_TOMB,
				Assets.SND_ALERT,
				Assets.SND_MELD,
				Assets.SND_BOSS,
				Assets.SND_BLAST,
				Assets.SND_PLANT,
				Assets.SND_RAY,
				Assets.SND_BEACON,
				Assets.SND_TELEPORT,
				Assets.SND_CHARMS,
				Assets.SND_MASTERY,
				Assets.SND_PUFF,
				Assets.SND_ROCKS,
				Assets.SND_BURNING,
				Assets.SND_FALLING,
				Assets.SND_GHOST,
				Assets.SND_SECRET,
				Assets.SND_BONES,
				Assets.SND_BEE,
				Assets.SND_DEGRADE,
				Assets.SND_MIMIC );

		
	}

	public static void switchNoFade(Class<? extends PixelScene> c){
		switchNoFade(c, null);
	}

	public static void switchNoFade(Class<? extends PixelScene> c, SceneChangeCallback callback) {
		PixelScene.noFade = true;
		switchScene( c, callback );
	}
	
	public static void seamlessResetScene(SceneChangeCallback callback) {
		if (scene() instanceof PixelScene){
			((PixelScene) scene()).saveWindows();
			switchNoFade((Class<? extends PixelScene>) sceneClass, callback );
		} else {
			resetScene();
		}
	}
	
	public static void seamlessResetScene(){
		seamlessResetScene(null);
	}
	
	@Override
	protected void switchScene() {
		super.switchScene();
		if (scene instanceof PixelScene){
			((PixelScene) scene).restoreWindows();
		}
	}
	
	@Override
	public void resize( int width, int height ) {
		
		if (scene instanceof PixelScene &&
				(height != Game.height || width != Game.width)) {
			((PixelScene) scene).saveWindows();
		}

		super.resize( width, height );

		updateDisplaySize();

	}

	public void updateDisplaySize(){
		platform.updateDisplaySize();
	}

	public static void updateSystemUI() {
		platform.updateSystemUI();
	}
}