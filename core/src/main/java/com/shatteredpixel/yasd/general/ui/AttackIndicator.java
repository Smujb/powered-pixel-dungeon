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

package com.shatteredpixel.yasd.general.ui;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.PPDAction;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.input.GameAction;
import com.watabou.noosa.Game;
import com.watabou.noosa.Visual;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class AttackIndicator extends Tag {
	
	private static final float ENABLED	= 1.0f;
	private static final float DISABLED	= 0.3f;

	private static float delay;
	
	private static AttackIndicator instance;
	
	private Visual sprite = null;

	private Mob lastTarget;
	private ArrayList<Mob> candidates = new ArrayList<>();
	
	public AttackIndicator() {
		super( DangerIndicator.COLOR );
		
		instance = this;
		lastTarget = null;
		
		setSize( 24, 24 );
		visible( false );
		enable( false );
	}

	@Override
	public GameAction keyAction() {
		return PPDAction.TAG_ATTACK;
	}


	@Override
	protected void createChildren() {
		super.createChildren();
	}
	
	@Override
	protected synchronized void layout() {
		super.layout();
		
		if (sprite != null) {
			sprite.x = x + (width - sprite.width()) / 2 + 1;
			sprite.y = y + (height - sprite.height()) / 2;
			PixelScene.align(sprite);
		}
	}
	
	@Override
	public synchronized void update() {
		super.update();

		if (!bg.visible){
			enable(false);
			if (delay > 0f) delay -= Game.elapsed;
			if (delay <= 0f) active = false;
		} else {
			delay = 0.75f;
			active = true;
		
			if (Dungeon.hero.isAlive()) {

				enable(Dungeon.hero.ready);

			} else {
				visible( false );
				enable( false );
			}
		}
	}
	
	private synchronized void checkEnemies() {

		candidates.clear();
		int v = Dungeon.hero.visibleEnemies();
		for (int i=0; i < v; i++) {
			Mob mob = Dungeon.hero.visibleEnemy( i );
			if ( Dungeon.hero.canAttack( mob) ) {
				candidates.add( mob );
			}
		}
		
		if (!candidates.contains( lastTarget )) {
			if (candidates.isEmpty()) {
				lastTarget = null;
			} else {
				active = true;
				lastTarget = Random.element( candidates );
				flash();
			}
		} else {
			if (!bg.visible) {
				active = true;
				flash();
			}
		}

		updateImage();
		visible( v > 0 );
		enable( lastTarget != null );
	}
	
	private synchronized void updateImage() {
		
		if (sprite != null) {
			sprite.killAndErase();
			sprite = null;
		}

		if (lastTarget == null) {
			int img;
			if (Dungeon.hero.belongings.getWeapons().size() > 0) {
				img = Dungeon.hero.belongings.getCurrentWeapon().image;
			} else {
				img = ItemSpriteSheet.WEAPON_HOLDER;
			}
			sprite = new ItemSprite(img);
		} else {
			sprite = Reflection.newInstance(lastTarget.spriteClass);
			active = true;
			((CharSprite)sprite).linkVisuals(lastTarget);
			((CharSprite)sprite).idle();
			((CharSprite)sprite).paused = true;
		}
		add(sprite);
		layout();
	}
	
	private boolean enabled = true;
	private synchronized void enable( boolean value ) {
		enabled = value;
		if (sprite != null) {
			sprite.alpha( value ? ENABLED : DISABLED );
		}
	}
	
	private synchronized void visible( boolean value ) {
		bg.visible = value;
		if (sprite != null) {
			sprite.visible = value;
		}
	}
	
	@Override
	protected void onClick() {
		if (Dungeon.hero.canAttack(lastTarget)) {
			if (Dungeon.hero.handle( lastTarget.pos )) {
				Dungeon.hero.next();
			}
		} else {
			Dungeon.hero.belongings.doSwitchToNextWeapon();
		}
	}
	
	public static void target( Char target ) {
		synchronized (instance) {
			instance.lastTarget = (Mob) target;
			instance.updateImage();

			TargetHealthIndicator.instance.target(target);
		}
	}
	
	public static void updateState() {
		if (instance != null) {
			instance.checkEnemies();
		}
	}
}
