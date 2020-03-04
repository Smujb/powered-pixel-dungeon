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

package com.shatteredpixel.yasd.general;

import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import java.io.IOException;

public class LevelHandler {

	public static Level getLevel() {
		return getLevel(GamesInProgress.curSlot);
	}

	public static Level getLevel(int save) {
		return getLevel(Dungeon.xPos, Dungeon.yPos, Dungeon.zPos, save);
	}

	public static Level getLevel(int x, int y, int z, int save) {

		Bundle bundle;
		long start = System.currentTimeMillis();
		try {
			bundle = FileUtils.bundleFromFile(GamesInProgress.depthFile(save, x, y, z));
		} catch (IOException e) {
			MainGame.reportException(e);
			throw new RuntimeException(e.fillInStackTrace());
		}
		Level level = (Level) bundle.get(Dungeon.LEVEL);
		long taken = System.currentTimeMillis() - start;
		GLog.w("Loaded level in " + taken + "ms\n");
		return level;

	}
}
