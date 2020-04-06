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
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.FileUtils;

import org.jetbrains.annotations.Contract;

import java.io.IOException;

public class LevelHandler {

	private static int returnPos;
	private static boolean fallIntoPit;
	public static int depth;
	public static String key;

	public static String filename(String key, int slot) {
		return GamesInProgress.slotFolder(slot) + "/" + key + ".dat";
	}

	public static Level getLevel(String key) {
		return getLevel(key, GamesInProgress.curSlot);
	}


	public static Level getLevel(String key, int save) {

		try {
			Bundle bundle = FileUtils.bundleFromFile(filename(key, save));
			return (Level) bundle.get(Dungeon.LEVEL);
		} catch (IOException e) {
			return null;
		}
	}

	public enum Mode {
		DESCEND, ASCEND, CONTINUE, RESURRECT, RETURN, FALL, RESET, NONE, INIT
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
						switchDepth(depth, mode, key );
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

	@Contract(pure = true)
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
				scrollSpeed = depth > Dungeon.depth ? 15 : -15;
				break;
		}
		return scrollSpeed;
	}

	@Contract(pure = true)
	public static Mode mode() {
		return mode;
	}

	public static void descend() {
		move(Dungeon.keyForDepth(Dungeon.depth +1), Messages.get(Mode.class, Mode.DESCEND.name()), Mode.DESCEND, Dungeon.depth + 1);
	}

	public static void ascend() {
		move(Dungeon.keyForDepth(Dungeon.depth -1), Messages.get(Mode.class, Mode.ASCEND.name()), Mode.ASCEND, Dungeon.depth - 1);
	}

	public static void fall(boolean fallIntoPit, boolean bossLevel) {
		LevelHandler.fallIntoPit = fallIntoPit;
		if (bossLevel) {//If falling from a boss level, the hero will fall back onto the same floor and it will reset.
			Dungeon.level.reset();
			move(Dungeon.keyForDepth(), Messages.get(Mode.class, Mode.FALL.name()), Mode.FALL, Dungeon.depth);
		} else {
			move(Dungeon.keyForDepth(Dungeon.depth +1), Messages.get(Mode.class, Mode.FALL.name()), Mode.FALL, Dungeon.depth + 1);
		}
	}

	public static void reset() {
		move(Dungeon.keyForDepth(), Messages.get(Mode.class, Mode.RESET.name()), Mode.RESET, Dungeon.depth);
	}

	public static void resurrect() {
		move(Dungeon.keyForDepth(), Messages.get(Mode.class, Mode.RESURRECT.name()), Mode.RESURRECT, Dungeon.depth);
	}

	public static void returnTo(int depth, int pos) {
		move(Dungeon.keyForDepth(depth), Messages.get(Mode.class, Mode.RETURN.name()), Mode.RETURN, depth);
		returnPos = pos;
	}

	public static void dive(int pos) {
		Dungeon.underwater = !Dungeon.underwater;
		move(Dungeon.keyForDepth(), Messages.get(Mode.class, Mode.RETURN.name()), Mode.RETURN, Dungeon.depth);
		returnPos = pos;
	}

	public static void doRestore() {
		mode = Mode.CONTINUE;
		TextScene.init(Messages.get(Mode.class, Mode.CONTINUE.name()), Messages.get(LevelHandler.class, "continue"), Dungeon.newLevel( Dungeon.keyForDepth(), false).loadImg(), getSpeed(), 0.67f, new Callback() {
			@Override
			public void call() {
				MainGame.switchScene(GameScene.class);
			}
		}, getThread(), GameSettings.fastInterlevelScene());
	}

	public static void doInit() {
		mode = Mode.DESCEND;
		depth = 1;
		TextScene.init(Messages.get(Mode.class, Mode.DESCEND.name()), Messages.get(LevelHandler.class, "continue"), Dungeon.newLevel( Dungeon.keyForDepth(), false).loadImg(), getSpeed(), 0.67f, new Callback() {
			@Override
			public void call() {
				MainGame.switchScene(GameScene.class);
			}
		}, new Thread() {
			@Override
			public void run() {
				try {
					initGame();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}, GameSettings.fastInterlevelScene());
	}

	public static void move(String key, String msg, Mode mode, int yPos) {
		LevelHandler.depth = yPos;
		LevelHandler.mode = mode;
		LevelHandler.key = key;
		TextScene.init(msg, Messages.get(LevelHandler.class, "continue"), Dungeon.newLevel( Dungeon.keyForDepth(), false).loadImg(), getSpeed(), 0.67f, new Callback() {
			@Override
			public void call() {
				MainGame.switchScene(GameScene.class);
			}
		}, getThread(), GameSettings.fastInterlevelScene());
	}

	public static void resetMode() {
		mode = Mode.NONE;
	}

	private static void initGame() throws IOException {
		Mob.clearHeldAllies();
		Dungeon.level = null;
		Dungeon.hero = null;
		Dungeon.init();
		GameLog.wipe();
		LevelHandler.key = Dungeon.keyForDepth();
		switchDepth(1, Mode.DESCEND, Dungeon.keyForDepth());
	}

	private static void switchDepth(int yPos, final Mode mode, String key) throws IOException {
		if (Dungeon.hero != null && Dungeon.level != null) {
			Mob.holdMobs( Dungeon.level );
			Dungeon.saveAll();
		}
		Dungeon.depth = yPos;
		if (mode.equals(Mode.RESURRECT) & Dungeon.hero != null & Dungeon.level != null) {
			if (Dungeon.level.locked) {
				Dungeon.hero.resurrect( Dungeon.key);
			} else {
				Dungeon.hero.resurrect( null );
				Dungeon.resetLevel();
				return;
			}
		}


		Level level = null;
		Dungeon.key = key;
		if (mode != Mode.RESET) {
			level = getLevel(key);
		}
		if (level == null) {

			Dungeon.level = null;
			Actor.clear();

			if (yPos > Statistics.deepestFloor) {
				Statistics.deepestFloor = yPos;

				Statistics.completedWithNoKilling = Statistics.qualifiedForNoKilling;
			}

			level = Dungeon.newLevel( LevelHandler.key, true );

			Statistics.qualifiedForNoKilling = !Dungeon.bossLevel();
			if (level instanceof DeadEndLevel) {
				Statistics.deepestFloor--;
			}

		}

		switch (mode) {
			default:
				Dungeon.switchLevel(level, level.getEntrancePos());
				break;
			case FALL:
				Buff.affect( Dungeon.hero, Chasm.Falling.class );
				Dungeon.switchLevel(level, level.fallCell(fallIntoPit));
				break;
			case ASCEND:
				Dungeon.switchLevel(level, level.getExit().getPos(level));
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
		Dungeon.level = null;
		Actor.clear();
		if (Dungeon.depth == -1) {
			Dungeon.depth = Statistics.deepestFloor;
			Dungeon.switchLevel( getLevel(Dungeon.key), -1 );
		} else {
			level = getLevel( Dungeon.key, GamesInProgress.curSlot );
			Dungeon.switchLevel( level, Dungeon.hero.pos );
		}
	}
}
