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

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.artifacts.Artifact;
import com.shatteredpixel.yasd.general.items.rings.Ring;
import com.shatteredpixel.yasd.general.items.wands.Wand;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.MimicSprite;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class CrystalMimic extends Mimic {

	{
		spriteClass = MimicSprite.Crystal.class;
	}

	@Override
	public String name() {
		if (alignment == Alignment.NEUTRAL){
			return Messages.get(Heap.class, "crystal_chest");
		} else {
			return super.name();
		}
	}

	@Override
	public String description() {
		if (alignment == Alignment.NEUTRAL){
			//TODO variable based on contents
			for (Item i : items){
				if (i instanceof Artifact){
					return Messages.get(Heap.class, "crystal_chest_desc", Messages.get(Heap.class, "artifact"));
				} else if (i instanceof Ring){
					return Messages.get(Heap.class, "crystal_chest_desc", Messages.get(Heap.class, "ring"));
				} else if (i instanceof Wand){
					return Messages.get(Heap.class, "crystal_chest_desc", Messages.get(Heap.class, "wand"));
				}
			}
			return Messages.get(Heap.class, "locked_chest_desc");
		} else {
			return super.description();
		}
	}

	public void stopHiding(){
		state = HUNTING;
		if (Dungeon.level.heroFOV[pos] && Actor.chars().contains(this)) {
			enemy = Dungeon.hero;
			target = Dungeon.hero.pos;
			enemySeen = true;
			GLog.w(Messages.get(this, "reveal") );
			CellEmitter.get(pos).burst(Speck.factory(Speck.STAR), 10);
			Sample.INSTANCE.play(Assets.SND_MIMIC, 1, 1.25f);
		}
	}

	//TODO different AI

}
