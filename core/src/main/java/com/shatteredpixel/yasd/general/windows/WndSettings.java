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

package com.shatteredpixel.yasd.general.windows;

import com.shatteredpixel.yasd.ModHandler;
import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.GameSettings;
import com.shatteredpixel.yasd.general.MainGame;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.ui.CheckBox;
import com.shatteredpixel.yasd.general.ui.OptionSlider;
import com.shatteredpixel.yasd.general.ui.RedButton;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
import com.shatteredpixel.yasd.general.ui.Toolbar;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.DeviceCompat;

public class WndSettings extends WndTabbed {

	private static final int WIDTH		    = 112;
	private static final int HEIGHT         = 150;
	private static final int SLIDER_HEIGHT	= 24;
	private static final int BTN_HEIGHT	    = 18;
	private static final int GAP_TINY 		= 2;
	private static final int GAP_SML 		= 6;
	private static final int GAP_LRG 		= 18;

	private DisplayTab display;
	private UITab ui;
	private AudioTab audio;

	private static int last_index = 0;

	public WndSettings() {
		super();

		display = new DisplayTab();
		add( display );

		ui = new UITab();
		add( ui );

		audio = new AudioTab();
		add( audio );

		add( new LabeledTab(Messages.get(this, "display")){
			@Override
			protected void select(boolean value) {
				super.select(value);
				display.visible = display.active = value;
				if (value) last_index = 0;
			}
		});

		add( new LabeledTab(Messages.get(this, "ui")){
			@Override
			protected void select(boolean value) {
				super.select(value);
				ui.visible = ui.active = value;
				if (value) last_index = 1;
			}
		});

		add( new LabeledTab(Messages.get(this, "audio")){
			@Override
			protected void select(boolean value) {
				super.select(value);
				audio.visible = audio.active = value;
				if (value) last_index = 2;
			}
		});

		resize(WIDTH, HEIGHT);

		layoutTabs();

		select(last_index);

	}

	private class DisplayTab extends Group {

		public DisplayTab() {
			super();

			OptionSlider scale = new OptionSlider(Messages.get(this, "scale"),
					(int)Math.ceil(2* Game.density)+ "X",
					PixelScene.maxDefaultZoom + "X",
					(int)Math.ceil(2* Game.density),
					PixelScene.maxDefaultZoom ) {
				@Override
				protected void onChange() {
					if (getSelectedValue() != GameSettings.scale()) {
						GameSettings.scale(getSelectedValue());
						MainGame.seamlessResetScene();
					}
				}
			};
			if ((int)Math.ceil(2* Game.density) < PixelScene.maxDefaultZoom) {
				scale.setSelectedValue(PixelScene.defaultZoom);
				scale.setRect(0, 0, WIDTH, SLIDER_HEIGHT);
				add(scale);
			}

			CheckBox chkSaver = new CheckBox( Messages.get(this, "saver") ) {
				@Override
				protected void onClick() {
					super.onClick();
					if (checked()) {
						checked(!checked());
						MainGame.scene().add(new WndOptions(
								Messages.get(DisplayTab.class, "saver"),
								Messages.get(DisplayTab.class, "saver_desc"),
								Messages.get(DisplayTab.class, "okay"),
								Messages.get(DisplayTab.class, "cancel")) {
							@Override
							protected void onSelect(int index) {
								if (index == 0) {
									checked(!checked());
									GameSettings.powerSaver(checked());
								}
							}
						});
					} else {
						GameSettings.powerSaver(checked());
					}
				}
			};
			if (PixelScene.maxScreenZoom >= 2) {
				chkSaver.setRect(0, scale.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
				chkSaver.checked(GameSettings.powerSaver());
				add(chkSaver);
			}

			RedButton btnOrientation = new RedButton( GameSettings.landscape() ?
					Messages.get(this, "portrait")
					: Messages.get(this, "landscape") ) {
				@Override
				protected void onClick() {
					GameSettings.landscape(!GameSettings.landscape());
				}
			};
			btnOrientation.setRect(0, chkSaver.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			add( btnOrientation );


			OptionSlider brightness = new OptionSlider(Messages.get(this, "brightness"),
					Messages.get(this, "dark"), Messages.get(this, "bright"), -2, 2) {
				@Override
				protected void onChange() {
					GameSettings.brightness(getSelectedValue());
				}
			};
			brightness.setSelectedValue(GameSettings.brightness());
			brightness.setRect(0, btnOrientation.bottom() + GAP_LRG, WIDTH, SLIDER_HEIGHT);
			add(brightness);

			OptionSlider tileGrid = new OptionSlider(Messages.get(this, "visual_grid"),
					Messages.get(this, "off"), Messages.get(this, "high"), -1, 3) {
				@Override
				protected void onChange() {
					GameSettings.visualGrid(getSelectedValue());
				}
			};
			tileGrid.setSelectedValue(GameSettings.visualGrid());
			tileGrid.setRect(0, brightness.bottom() + GAP_TINY, WIDTH, SLIDER_HEIGHT);
			add(tileGrid);

			RedButton btnMod = new RedButton( Messages.get(this, "mods") ) {
				@Override
				protected void onClick() {
					MainGame.scene().addToFront(new WndOptions(Messages.get(DisplayTab.class, "mods_title"),
							Messages.get(DisplayTab.class, "mods_text", ModHandler.getName()),
							Messages.titleCase(ModHandler.NONE.displayName()),
							Messages.titleCase(ModHandler.YASD.displayName()),
							Messages.titleCase(ModHandler.CURSED.displayName()),
							Messages.titleCase(ModHandler.TEST.displayName())) {
						@Override
						protected void onSelect(int index) {
							if (index == ModHandler.YASD.getValue()) {
								GameSettings.mod(ModHandler.YASD.getValue());
							} else if (index == ModHandler.TEST.getValue()) {
								GameSettings.mod(ModHandler.TEST.getValue());
							} else if (index == ModHandler.CURSED.getValue()) {
								GameSettings.mod(ModHandler.CURSED.getValue());
							} else if (index == ModHandler.NONE.getValue()) {
								GameSettings.mod(ModHandler.NONE.getValue());
							}

							/*try {
								Dungeon.saveAll();
							} catch (Exception e) {
								MainGame.reportException(e);
							}
							Game.instance.finish();*/
						}
					});
				}
			};
			btnMod.setRect(0, tileGrid.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			add( btnMod );

		}
	}

	private class UITab extends Group {

		public UITab(){
			super();

			RenderedTextBlock barDesc = PixelScene.renderTextBlock(Messages.get(this, "mode"), 9);
			barDesc.setPos((WIDTH-barDesc.width())/2f, GAP_TINY);
			PixelScene.align(barDesc);
			add(barDesc);

			RedButton btnSplit = new RedButton(Messages.get(this, "split")){
				@Override
				protected void onClick() {
					GameSettings.toolbarMode(Toolbar.Mode.SPLIT.name());
					Toolbar.updateLayout();
				}
			};
			btnSplit.setRect( 0, barDesc.bottom() + GAP_TINY, 36, 16);
			add(btnSplit);

			RedButton btnGrouped = new RedButton(Messages.get(this, "group")){
				@Override
				protected void onClick() {
					GameSettings.toolbarMode(Toolbar.Mode.GROUP.name());
					Toolbar.updateLayout();
				}
			};
			btnGrouped.setRect( btnSplit.right()+GAP_TINY, btnSplit.top(), 36, 16);
			add(btnGrouped);

			RedButton btnCentered = new RedButton(Messages.get(this, "center")){
				@Override
				protected void onClick() {
					GameSettings.toolbarMode(Toolbar.Mode.CENTER.name());
					Toolbar.updateLayout();
				}
			};
			btnCentered.setRect(btnGrouped.right()+GAP_TINY, btnSplit.top(), 36, 16);
			add(btnCentered);

			CheckBox chkFlipToolbar = new CheckBox(Messages.get(this, "flip_toolbar")){
				@Override
				protected void onClick() {
					super.onClick();
					GameSettings.flipToolbar(checked());
					Toolbar.updateLayout();
				}
			};
			chkFlipToolbar.setRect(0, btnGrouped.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			chkFlipToolbar.checked(GameSettings.flipToolbar());
			add(chkFlipToolbar);

			final CheckBox chkFlipTags = new CheckBox(Messages.get(this, "flip_indicators")){
				@Override
				protected void onClick() {
					super.onClick();
					GameSettings.flipTags(checked());
					GameScene.layoutTags();
				}
			};
			chkFlipTags.setRect(0, chkFlipToolbar.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			chkFlipTags.checked(GameSettings.flipTags());
			add(chkFlipTags);

			OptionSlider slots = new OptionSlider(Messages.get(this, "quickslots"), "0", "4", 0, 4) {
				@Override
				protected void onChange() {
					GameSettings.quickSlots(getSelectedValue());
					Toolbar.updateLayout();
				}
			};
			slots.setSelectedValue(GameSettings.quickSlots());
			slots.setRect(0, chkFlipTags.bottom() + GAP_TINY, WIDTH, SLIDER_HEIGHT);
			add(slots);

			CheckBox chkImmersive = new CheckBox( Messages.get(this, "nav_bar") ) {
				@Override
				protected void onClick() {
					super.onClick();
					GameSettings.fullscreen(checked());
				}
			};
			chkImmersive.setRect( 0, slots.bottom() + GAP_SML, WIDTH, BTN_HEIGHT );
			chkImmersive.checked(GameSettings.fullscreen());
			chkImmersive.enable(DeviceCompat.supportsFullScreen());
			add(chkImmersive);

			CheckBox chkFont = new CheckBox(Messages.get(this, "system_font")){
				@Override
				protected void onClick() {
					super.onClick();
					MainGame.seamlessResetScene(new Game.SceneChangeCallback() {
						@Override
						public void beforeCreate() {
							GameSettings.systemFont(checked());
						}

						@Override
						public void afterCreate() {
							//do nothing
						}
					});
				}
			};
			chkFont.setRect(0, chkImmersive.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			chkFont.checked(GameSettings.systemFont());
			add(chkFont);
		}

	}

	private class AudioTab extends Group {

		public AudioTab() {
			OptionSlider musicVol = new OptionSlider(Messages.get(this, "music_vol"), "0", "10", 0, 10) {
				@Override
				protected void onChange() {
					GameSettings.musicVol(getSelectedValue());
				}
			};
			musicVol.setSelectedValue(GameSettings.musicVol());
			musicVol.setRect(0, 0, WIDTH, SLIDER_HEIGHT);
			add(musicVol);

			CheckBox musicMute = new CheckBox(Messages.get(this, "music_mute")){
				@Override
				protected void onClick() {
					super.onClick();
					GameSettings.music(!checked());
				}
			};
			musicMute.setRect(0, musicVol.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			musicMute.checked(!GameSettings.music());
			add(musicMute);


			OptionSlider SFXVol = new OptionSlider(Messages.get(this, "sfx_vol"), "0", "10", 0, 10) {
				@Override
				protected void onChange() {
					GameSettings.SFXVol(getSelectedValue());
				}
			};
			SFXVol.setSelectedValue(GameSettings.SFXVol());
			SFXVol.setRect(0, musicMute.bottom() + GAP_LRG, WIDTH, SLIDER_HEIGHT);
			add(SFXVol);

			CheckBox btnSound = new CheckBox( Messages.get(this, "sfx_mute") ) {
				@Override
				protected void onClick() {
					super.onClick();
					GameSettings.soundFx(!checked());
					Sample.INSTANCE.play( Assets.SND_CLICK );
				}
			};
			btnSound.setRect(0, SFXVol.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			btnSound.checked(!GameSettings.soundFx());
			add( btnSound );

			CheckBox btnVibrate = new CheckBox( Messages.get(this, "vibrate") ) {
				@Override
				protected void onClick() {
					super.onClick();
					GameSettings.vibrate(checked());
					Sample.INSTANCE.play( Assets.SND_CLICK );
				}
			};
			btnVibrate.setRect(0, btnSound.bottom() + GAP_LRG, WIDTH, BTN_HEIGHT);
			btnVibrate.checked(GameSettings.vibrate());
			add( btnVibrate );

			resize( WIDTH, (int)btnVibrate.bottom());
		}

	}
}
