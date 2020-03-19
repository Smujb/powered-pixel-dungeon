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

package com.shatteredpixel.yasd.general.scenes;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.MainGame;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
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
import com.watabou.utils.Callback;

public class TextScene extends PixelScene {

	//TODO: This copy pastes from Inter Level Scene. Hopefully I can make it use this.

	private enum Phase {
		FADE_IN, STATIC, FADE_OUT
	}

	private Phase phase;
	private float timeLeft;
	private float fadeTime;
	private float waitingTime;
	private RenderedTextBlock message;

	//slow fade on entering a new region
	private static final float SLOW_FADE = 1f; //.33 in, 1.33 steady, .33 out, 2 seconds total
	//norm fade when loading, falling, returning, or descending to a new floor
	private static final float NORM_FADE = 0.67f; //.33 in, .67 steady, .33 out, 1.33 seconds total
	//fast fade when ascending, or descending to a floor you've been on
	private static final float FAST_FADE = 0.50f; //.33 in, .33 steady, .33 out, 1 second total

	private static String text = "TEST";
	private static Thread thread = null;
	private static String bgTex;
	private static float scrollSpeed = 0;
	private static Callback onFinish = null;

	@Override
	public void create() {
		super.create();

		if (bgTex == null) {
			bgTex = Dungeon.level.loadImg();
		}

		if (thread != null) {
			thread.run();
		}

		if (onFinish == null) {
			onFinish = new Callback() {
				@Override
				public void call() {
					MainGame.switchScene(GameScene.class);
				}
			};
		}

		SkinnedBlock bg = new SkinnedBlock(Camera.main.width, Camera.main.height, bgTex ){
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
				else if (phase ==  Phase.FADE_OUT)   aa = Math.max( 0, (0.667f - timeLeft));
				else                                aa = 0;
			}
		};
		im.angle = 90;
		im.x = Camera.main.width;
		im.scale.x = Camera.main.height/5f;
		im.scale.y = Camera.main.width;
		add(im);

		message = PixelScene.renderTextBlock( text, 9 );
		message.setPos(
				(Camera.main.width - message.width()) / 2,
				(Camera.main.height - message.height()) / 2
		);
		message.maxWidth(Camera.main.width);
		align( message );
		add( message );

		phase = Phase.FADE_IN;

		fadeTime = NORM_FADE;
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

				message.alpha(1 - p);
				if ((timeLeft -= Game.elapsed) <= 0) {
					if ((thread == null || !thread.isAlive()) || waitingTime > 5f) {
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
				message.alpha(p);

				if ((timeLeft -= Game.elapsed) <= 0) {
					if (onFinish != null) {
						onFinish.call();
					}
					thread = null;
				}
				break;

		}
	}
	/*
	The Dungeon lies right beneath the City, its upper levels actually constitute the City's sewer system.
	As dark energy has crept up from below the usually harmless sewer creatures have become more and more dangerous. The city sends guard patrols down here to try and maintain safety for those above, but they are slowly failing.
	This place is dangerous, but at least the evil magic at work here is weak.
	 */

	public static void init(String text, Thread thread, String bgTex, float scrollSpeed, Callback onFinish) {
		TextScene.text = text;
		TextScene.thread = thread;
		TextScene.bgTex = bgTex;
		TextScene.scrollSpeed = scrollSpeed;
		TextScene.onFinish = onFinish;
		MainGame.switchScene(TextScene.class);
	}

	private void fadeOut() {
		phase = Phase.FADE_OUT;
		timeLeft = fadeTime;
	}

	@Override
	protected void onBackPressed() {
		if ((thread == null || !thread.isAlive() || waitingTime > 5f) && phase != Phase.FADE_OUT) {
			fadeOut();
		}
	}
}