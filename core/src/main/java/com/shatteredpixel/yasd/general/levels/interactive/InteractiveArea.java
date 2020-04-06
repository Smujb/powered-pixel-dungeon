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

package com.shatteredpixel.yasd.general.levels.interactive;

import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import org.jetbrains.annotations.NotNull;

public abstract class InteractiveArea implements Bundlable {
	//TODO: Maybe InteractiveArea should be a list of InteractiveCell s, rather than being a superclass?
	protected int x;
	protected int y;
	protected int width;
	protected int height;

	public abstract void interact(Hero hero);

	public InteractiveArea setPos(int x, int y, int  width, int height) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		GLog.i("created area with x, y, width, height: " + x +"," + y + "," + width + "," + height);
		return this;
	}

	public boolean posInside(@NotNull Level level, int pos) {
		int[] coords = level.getXY(pos);
		int posX = coords[0];
		int posY = coords[1];
		return posX >= x && posX < x + width
				&& posY >= y && posY < y + height;
	}

	private static final String X = "x";
	private static final String Y = "y";
	private static final String WIDTH = "width";
	private static final String HEIGHT = "height";

	@Override
	public void storeInBundle(Bundle bundle) {
		bundle.put(X, x);
		bundle.put(Y, y);
		bundle.put(WIDTH, width);
		bundle.put(HEIGHT, height);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		x = bundle.getInt(X);
		y = bundle.getInt(Y);
		width = bundle.getInt(WIDTH);
		height = bundle.getInt(HEIGHT);
	}
}