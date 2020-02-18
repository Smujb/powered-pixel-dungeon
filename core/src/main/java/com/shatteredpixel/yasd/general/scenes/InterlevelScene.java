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

package com.shatteredpixel.yasd.general.scenes;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.GamesInProgress;
import com.shatteredpixel.yasd.general.MainGame;
import com.shatteredpixel.yasd.general.Statistics;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.levels.DeadEndLevel;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.features.Chasm;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.ui.GameLog;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
import com.shatteredpixel.yasd.general.windows.WndError;
import com.shatteredpixel.yasd.general.windows.WndStory;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Blending;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.NoosaScriptNoLighting;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.noosa.audio.Sample;

import java.io.FileNotFoundException;
import java.io.IOException;

public class InterlevelScene extends PixelScene {

	//slow fade on entering a new region
	private static final float SLOW_FADE = 1f; //.33 in, 1.33 steady, .33 out, 2 seconds total
	//norm fade when loading, falling, returning, or descending to a new floor
	private static final float NORM_FADE = 0.67f; //.33 in, .67 steady, .33 out, 1.33 seconds total
	//fast fade when ascending, or descending to a floor you've been on
	private static final float FAST_FADE = 0.50f; //.33 in, .33 steady, .33 out, 1 second total
	
	private static float fadeTime;
	
	public enum Mode {
		DESCEND, ASCEND, CONTINUE, RESURRECT, RETURN, FALL, RESET, NONE, INIT, PATH1, PATH2, PATH3
	}
	private static Mode mode;

	private static int depth;
	private static int path;
	private static String msg;

	private static int returnPos;
	
	public static boolean noStory = false;

	public static boolean fallIntoPit;
	
	private enum Phase {
		FADE_IN, STATIC, FADE_OUT
	}
	private Phase phase;
	private float timeLeft;
	
	private RenderedTextBlock message;
	
	private static Thread thread;
	private static Exception error = null;
	private float waitingTime;

	public static Level level;
	
	@Override
	public void create() {
		super.create();
		
		String loadingAsset;
		final float scrollSpeed;
		fadeTime = NORM_FADE;
		level = new DeadEndLevel();
		if (thread == null) {
			thread = new Thread() {
				@Override
				public void run() {

					try {

						if (Dungeon.hero != null){
							Dungeon.hero.spendToWhole();
						}
						Actor.fixTime();

						if (mode == Mode.CONTINUE) {
							level = restore();
						} else {
							if (Dungeon.hero != null) {
								level = switchDepth(depth, path, mode);
							} else {
								level = switchDepth(1, 0, Mode.DESCEND);
							}
						}

						if (Dungeon.bossLevel()) {
							Sample.INSTANCE.load(Assets.SND_BOSS);
						}

					} catch (Exception e) {

						error = e;
						level = null;

					}

					if (phase == Phase.STATIC && error == null) {
						fadeOut();
					}
				}
			};
			thread.start();
		}

		int loadingDepth;
		if (mode == Mode.CONTINUE) {
			loadingDepth = GamesInProgress.check(GamesInProgress.curSlot).depth;
		} else {
			loadingDepth = Dungeon.depth;
		}
		switch (mode) {
			default:
				scrollSpeed = 0;
				break;
			case CONTINUE:
				scrollSpeed = 5;
				break;
			case DESCEND:
				if (Dungeon.hero == null) {
					fadeTime = SLOW_FADE;
				} else {
					if (!(Statistics.deepestFloor < loadingDepth)) {
						fadeTime = FAST_FADE;
					} else if (loadingDepth == 6 || loadingDepth == 11
							|| loadingDepth == 16 || loadingDepth == 22) {
						fadeTime = SLOW_FADE;
					}
				}
				scrollSpeed = 5;
				break;
			case FALL:
				scrollSpeed = 50;
				break;
			case ASCEND:
				fadeTime = FAST_FADE;
				scrollSpeed = -5;
				break;
			case RETURN:
				scrollSpeed = depth > Dungeon.depth ? 15 : -15;
				break;
		}

		Level level = null;
		try {
			level = Constants.LEVEL_TYPES.get(Dungeon.path).get(loadingDepth).newInstance();
		} catch (Exception ignored) {}

		if (level == null || level.loadImg() == null) {
			loadingAsset = Assets.SHADOW;
		} else {
			loadingAsset = level.loadImg();
		}
		
		SkinnedBlock bg = new SkinnedBlock(Camera.main.width, Camera.main.height, loadingAsset ){
			@Override
			protected NoosaScript script() {
				return NoosaScriptNoLighting.get();
			}
			
			@Override
			public void draw() {
				Blending.disable();
				super.draw();
				Blending.enable();
			}
			
			@Override
			public void update() {
				super.update();
				offset(0, Game.elapsed * scrollSpeed);
			}
		};
		bg.scale(4, 4);
		add(bg);
		
		Image im = new Image(TextureCache.createGradient(0xAA000000, 0xBB000000, 0xCC000000, 0xDD000000, 0xFF000000)){
			@Override
			public void update() {
				super.update();
				if (phase == Phase.FADE_IN)         aa = Math.max( 0, (timeLeft - (fadeTime - 0.667f)));
				else if (phase == Phase.FADE_OUT)   aa = Math.max( 0, (0.667f - timeLeft));
				else                                aa = 0;
			}
		};
		im.angle = 90;
		im.x = Camera.main.width;
		im.scale.x = Camera.main.height/5f;
		im.scale.y = Camera.main.width;
		add(im);

		String text = msg;
		
		message = PixelScene.renderTextBlock( text, 9 );
		message.setPos(
				(Camera.main.width - message.width()) / 2,
				(Camera.main.height - message.height()) / 2
		);
		align(message);
		add( message );
		
		phase = Phase.FADE_IN;
		timeLeft = fadeTime;
		

		waitingTime = 0f;
	}
	
	@Override
	public void update() {
		super.update();

		waitingTime += Game.elapsed;
		
		float p = timeLeft / fadeTime;
		
		switch (phase) {

		case FADE_IN:

			message.alpha( 1 - p );
			if ((timeLeft -= Game.elapsed) <= 0) {
				if (!thread.isAlive() && error == null) {
					message.text(Messages.get(this, "tap"));
					message.setPos((Camera.main.width - message.width()) / 2, (Camera.main.height - message.height()) / 2);
					PointerArea hotArea = new PointerArea(0, 0, Camera.main.width, Camera.main.height) {
						@Override
						protected void onClick(PointerEvent event) {
							if (phase != Phase.FADE_OUT) {
								fadeOut();
								destroy();
							}
						}
					};
					add(hotArea);
				} else {
					phase = Phase.STATIC;
				}
			}
			break;
			
		case FADE_OUT:
			message.alpha( p );
			
			if ((timeLeft -= Game.elapsed) <= 0) {
				Game.switchScene( GameScene.class );
				thread = null;
				error = null;
			}
			break;
			
		case STATIC:
			if (error != null) {
				String errorMsg;
				if (error instanceof FileNotFoundException)     errorMsg = Messages.get(this, "file_not_found");
				else if (error instanceof IOException)          errorMsg = Messages.get(this, "io_error");
				else if (error.getMessage() != null &&
						error.getMessage().equals("old save")) errorMsg = Messages.get(this, "io_error");

				else throw new RuntimeException("fatal error occured while moving between floors. " +
							"Seed:" + Dungeon.seed + " depth:" + Dungeon.depth, error);

				add( new WndError( errorMsg ) {
					public void onBackPressed() {
						super.onBackPressed();
						Game.switchScene( StartScene.class );
					}
				} );
				thread = null;
				error = null;
			} else if (thread != null && (int)waitingTime == 10){
				waitingTime = 11f;
				String s = "";
				for (StackTraceElement t : thread.getStackTrace()){
					s += "\n";
					s += t.toString();
				}
				MainGame.reportException(
						new RuntimeException("waited more than 10 seconds on levelgen. " +
								"Seed:" + Dungeon.seed + " depth:" + Dungeon.depth + " trace:" +
								s)
				);
			}
			break;
		}
	}

	public static Mode mode() {
		return mode;
	}

	public static void descend() {
		move(Dungeon.depth + 1, Dungeon.path, Messages.get(Mode.class, Mode.DESCEND.name()), Mode.DESCEND);
	}

	public static void ascend() {
		move(Dungeon.depth - 1, Dungeon.path, Messages.get(Mode.class, Mode.ASCEND.name()), Mode.ASCEND);
	}

	public static void fall() {
		move(Dungeon.depth + 1, Dungeon.path, Messages.get(Mode.class, Mode.FALL.name()), Mode.FALL);
	}

	public static void reset() {
		move(Dungeon.depth, Dungeon.path, Messages.get(Mode.class, Mode.RESET.name()), Mode.RESET);
	}

	public static void resurrect() {
		move(Dungeon.depth, Dungeon.path, Messages.get(Mode.class, Mode.RESURRECT.name()), Mode.RESURRECT);
	}

	public static void returnTo(int depth, int pos) {
		move(depth, Dungeon.path, Messages.get(Mode.class, Mode.RETURN.name()), Mode.RETURN);
		returnPos = pos;
	}

	public static void doRestore() {
		mode = Mode.CONTINUE;
		msg = Messages.get(Mode.class, Mode.CONTINUE.name());
		MainGame.switchScene(InterlevelScene.class);
	}

	public static void move(int depthToAccess, int path, String msg, Mode mode) {
		InterlevelScene.depth = depthToAccess;
		InterlevelScene.path = path;
		InterlevelScene.msg = msg;
		InterlevelScene.mode = mode;
		MainGame.switchScene(InterlevelScene.class);
	}

	public static void resetMode() {
		InterlevelScene.mode = Mode.NONE;
	}

	private static Level switchDepth(int depthToAccess, int path, final Mode mode) throws IOException {
		if (Dungeon.hero == null) {
			Mob.clearHeldAllies();
			Dungeon.init();
			if (noStory) {
				Dungeon.chapters.add( WndStory.ID_SEWERS );
				noStory = false;
			}
			GameLog.wipe();
		} else {
			Mob.holdAllies( Dungeon.level );
			Dungeon.saveAll();
		}
		Dungeon.depth = depthToAccess;
		Dungeon.path = path;
		if (mode.equals(Mode.RESURRECT)) {
			if (Dungeon.level.locked) {
				Dungeon.hero.resurrect( Dungeon.depth );
			} else {
				Dungeon.hero.resurrect( -1 );
				Dungeon.resetLevel();
				return Dungeon.level;
			}
		}
		Level level;
		if (Dungeon.depthLoaded(path, depthToAccess) & mode != Mode.RESET) {

			level = Dungeon.loadLevel(GamesInProgress.curSlot);

		} else  {

			level = Dungeon.newLevel(Dungeon.depth);

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
		return level;
	}

	private Level restore() throws IOException {

		Mob.clearHeldAllies();

		GameLog.wipe();
		Level level;
		Dungeon.loadGame( GamesInProgress.curSlot );
		if (Dungeon.depth == -1) {
			Dungeon.depth = Statistics.deepestFloor;
			Dungeon.switchLevel( level = Dungeon.loadLevel( GamesInProgress.curSlot ), -1 );
		} else {
			level = Dungeon.loadLevel( GamesInProgress.curSlot );
			Dungeon.switchLevel( level, Dungeon.hero.pos );
		}
		return level;
	}

	public void fadeOut() {
		phase = Phase.FADE_OUT;
		timeLeft = fadeTime;
	}

	@Override
	protected void onBackPressed() {
		if (thread != null && !thread.isAlive() && phase != Phase.FADE_OUT) {
			fadeOut();
		}
	}
}
