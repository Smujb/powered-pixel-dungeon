/*
 *
 *   Pixel Dungeon
 *   Copyright (C) 2012-2015 Oleg Dolya
 *
 *   Shattered Pixel Dungeon
 *   Copyright (C) 2014-2019 Evan Debenham
 *
 *   Powered Pixel Dungeon
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

package com.shatteredpixel.yasd.general.actors.mobs.npcs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.LotusSprite;
import com.watabou.utils.Bundle;

public class Lotus extends NPC {

	{
		alignment = Alignment.ALLY;
		properties.add(Property.IMMOVABLE);

		spriteClass = LotusSprite.class;

		viewDistance = 1;
	}

	@Override
	protected void onCreate() {
		super.onCreate();
		HP = HT = level*4;
	}

	public boolean inRange(int pos){
		return Dungeon.level.trueDistance(this.pos, pos) <= level;
	}

	public float seedPreservation(){
		return 0.25f + 0.05f*level;
	}

	@Override
	public boolean canInteract(Char c) {
		return false;
	}

	@Override
	protected boolean act() {
		super.act();
		throwItem();

		if (--HP <= 0){
			destroy();
			sprite.die();
		}

		spend(TICK);
		return true;
	}

	@Override
	public void damage( int dmg, DamageSrc src ) {
	}

	@Override
	public void add( Buff buff ) {
	}

	@Override
	public void destroy() {
		super.destroy();
		Dungeon.observe();
		GameScene.updateFog(pos, viewDistance+1);
	}

	@Override
	public boolean isInvulnerable(Class effect) {
		return true;
	}

	@Override
	public String description() {
		return Messages.get(this, "desc", level, (int)(seedPreservation()*100), (int)(seedPreservation()*100) );
	}

	private static final String WAND_LVL = "wand_lvl";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(WAND_LVL, level);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		level = bundle.getInt(WAND_LVL);
	}
}
