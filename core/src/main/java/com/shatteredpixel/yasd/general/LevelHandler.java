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

import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.levels.DeadEndLevel;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.features.Chasm;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.scenes.TextScene;
import com.shatteredpixel.yasd.general.ui.GameLog;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.FileUtils;

import java.io.IOException;

public class LevelHandler {

	private static int returnPos;
	public static int yPos;
	public static int xPos;
	public static int zPos;
	public static boolean fallIntoPit;

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

	public enum Mode {
		DESCEND, ASCEND, CONTINUE, RESURRECT, RETURN, FALL, RESET, NONE, INIT, PATH1, PATH2, PATH3
	}
	private static Mode mode;
	
	private static Thread getThread() {
		return new Thread() {
			@Override
			public void run() {

				try {

					if (Dungeon.hero != null){
						Dungeon.hero.spendToWhole();
					}
					Actor.fixTime();

					if (mode == Mode.CONTINUE) {
						restore();
					} else {
						if (Dungeon.hero != null) {
							switchDepth(xPos,yPos, zPos, mode);
						} else {
							initGame();
						}
					}

					if (Dungeon.bossLevel()) {
						Sample.INSTANCE.load(Assets.SND_BOSS);
					}

				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
	}

	public static float getSpeed() {
		float scrollSpeed;
		switch (mode) {
			default:
				scrollSpeed = 0;
				break;
			case CONTINUE:
			case DESCEND:
				scrollSpeed = 5;
				break;
			case FALL:
				scrollSpeed = 50;
				break;
			case ASCEND:
				scrollSpeed = -5;
				break;
			case RETURN:
				scrollSpeed = yPos > Dungeon.yPos ? 15 : -15;
				break;
		}
		return scrollSpeed;
	}

	public static Mode mode() {
		return mode;
	}

	public static void descend() {
		move(Dungeon.xPos, Dungeon.yPos + 1, Dungeon.zPos, Messages.get(Mode.class, Mode.DESCEND.name()), Mode.DESCEND);
	}

	public static void ascend() {
		move(Dungeon.xPos, Dungeon.yPos - 1, Dungeon.zPos, Messages.get(Mode.class, Mode.ASCEND.name()), Mode.ASCEND);
	}

	public static void fall() {
		move(Dungeon.xPos, Dungeon.yPos + 1, Dungeon.zPos, Messages.get(Mode.class, Mode.FALL.name()), Mode.FALL);
	}

	public static void reset() {
		move(Dungeon.xPos, Dungeon.yPos, Dungeon.zPos, Messages.get(Mode.class, Mode.RESET.name()), Mode.RESET);
	}

	public static void resurrect() {
		move(Dungeon.xPos, Dungeon.yPos, Dungeon.zPos, Messages.get(Mode.class, Mode.RESURRECT.name()), Mode.RESURRECT);
	}

	public static void returnTo(int depth, int pos) {
		move(Dungeon.xPos, depth, 0, Messages.get(Mode.class, Mode.RETURN.name()), Mode.RETURN);
		returnPos = pos;
	}

	public static void dive(int pos) {
		int zpos = 0;
		if (Dungeon.zPos < 1) {
			zpos = 1;
		}
		move(Dungeon.xPos, Dungeon.yPos, zpos, Messages.get(Mode.class, Mode.RETURN.name()), Mode.RETURN);
		returnPos = pos;
	}

	public static void doRestore() {
		mode = Mode.CONTINUE;
		Messages.get(Mode.class, Mode.CONTINUE.name());
		xPos = Dungeon.xPos;
		yPos = Dungeon.yPos;
		zPos = Dungeon.zPos;
		TextScene.init(Messages.get(Mode.class, Mode.CONTINUE.name()), getThread(), Dungeon.newLevel(xPos, yPos, zPos, false).loadImg(), getSpeed(), new Callback() {
			@Override
			public void call() {
				MainGame.switchScene(GameScene.class);
			}
		}, 0.67f);
	}

	public static void move(int xPos, int yPos, int zPos, String msg, Mode mode) {
		LevelHandler.xPos = xPos;
		LevelHandler.yPos = yPos;
		LevelHandler.zPos = zPos;
		LevelHandler.mode = mode;
		TextScene.init(msg, getThread(), Dungeon.newLevel(xPos, yPos, zPos, false).loadImg(), getSpeed(), new Callback() {
			@Override
			public void call() {
				MainGame.switchScene(GameScene.class);
			}
		}, 0.67f);
	}

	public static void resetMode() {
		mode = Mode.NONE;
	}

	private static void initGame() throws IOException {
		Mob.clearHeldAllies();
		Dungeon.init();
		GameLog.wipe();
		switchDepth(0, 1, 0, Mode.DESCEND);
	}

	private static void switchDepth(int xPos, int yPos, int zPos, final Mode mode) throws IOException {
		if (Dungeon.hero != null && Dungeon.level != null) {
			Mob.holdMobs( Dungeon.level );
			Dungeon.saveAll();
		}
		Dungeon.yPos = yPos;
		Dungeon.xPos = xPos;
		Dungeon.zPos = zPos;
		if (mode.equals(Mode.RESURRECT) & Dungeon.hero != null & Dungeon.level != null) {
			if (Dungeon.level.locked) {
				Dungeon.hero.resurrect( Dungeon.yPos );
			} else {
				Dungeon.hero.resurrect( -1 );
				Dungeon.resetLevel();
				return;
			}
		}
		Level level;
		if (Dungeon.depthLoaded(xPos, yPos, zPos) & mode != Mode.RESET) {

			level = Dungeon.loadLevel(GamesInProgress.curSlot);

		} else  {

			Dungeon.level = null;
			Actor.clear();

			if (yPos > Statistics.deepestFloor) {
				Statistics.deepestFloor = yPos;

				Statistics.completedWithNoKilling = Statistics.qualifiedForNoKilling;
			}

			level = Dungeon.newLevel(xPos, yPos, zPos, true);

			Dungeon.setLoaded(xPos, yPos, zPos);
			Statistics.qualifiedForNoKilling = !Dungeon.bossLevel();
			if (level instanceof DeadEndLevel) {
				Statistics.deepestFloor--;
			}

		}

		switch (mode) {
			default:
				Dungeon.switchLevel(level, level.entrance);
				break;
			case FALL:
				Buff.affect( Dungeon.hero, Chasm.Falling.class );
				Dungeon.switchLevel(level, level.fallCell(fallIntoPit));
				break;
			case ASCEND:
				Dungeon.switchLevel(level, level.exit);
				break;
			case RETURN:
				Dungeon.switchLevel(level, returnPos);
				break;
		}
	}

	private static void restore() throws IOException {

		Mob.clearHeldAllies();

		GameLog.wipe();
		Level level;
		Dungeon.loadGame( GamesInProgress.curSlot );
		if (Dungeon.yPos == -1) {
			Dungeon.yPos = Statistics.deepestFloor;
			Dungeon.switchLevel( level = Dungeon.loadLevel( GamesInProgress.curSlot ), -1 );
		} else {
			level = Dungeon.loadLevel( GamesInProgress.curSlot );
			Dungeon.switchLevel( level, Dungeon.hero.pos );
		}
	}
}
