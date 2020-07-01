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

package com.shatteredpixel.yasd.general.windows;

import com.shatteredpixel.yasd.general.Chrome;
import com.shatteredpixel.yasd.general.MainGame;
import com.shatteredpixel.yasd.general.YASDSettings;
import com.shatteredpixel.yasd.general.messages.Languages;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.ui.RedButton;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
import com.shatteredpixel.yasd.general.ui.Window;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.utils.DeviceCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class WndLangs extends Window {

	private int WIDTH_P = 120;
	private int WIDTH_L = 171;

	private int MIN_HEIGHT = 110;

	private int BTN_WIDTH = 50;
	private int BTN_HEIGHT = 12;

	public WndLangs(){
		super();

		final ArrayList<Languages> langs = new ArrayList<>(Arrays.asList(Languages.values()));

		Languages nativeLang = Languages.matchLocale(Locale.getDefault());
		langs.remove(nativeLang);
		//move the native language to the top.
		langs.add(0, nativeLang);

		final Languages currLang = Messages.lang();

		//language buttons layout
		int y = 0;
		for (int i = 0; i < langs.size(); i++){
			final int langIndex = i;
			RedButton btn = new RedButton(Messages.titleCase(langs.get(i).nativeName())){
				@Override
				protected void onClick() {
					super.onClick();
					Messages.setup(langs.get(langIndex));
					MainGame.seamlessResetScene(new Game.SceneChangeCallback() {
						@Override
						public void beforeCreate() {
							YASDSettings.language(langs.get(langIndex));
							Game.platform.resetGenerators();
						}
						@Override
						public void afterCreate() {
							//do nothing
						}
					});
				}
			};
			if (currLang == langs.get(i)){
				btn.textColor(TITLE_COLOR);
			} else {
				switch (langs.get(i).status()) {
					case INCOMPLETE:
					case UNFINISHED:
						btn.textColor(0x888888);
						break;
					case UNREVIEWED:
						btn.textColor(0xBBBBBB);
						break;
				}
			}
			btn.setSize(BTN_WIDTH, BTN_HEIGHT);
			if (PixelScene.landscape() && i % 2 == 1){
				btn.setPos(BTN_WIDTH+1, y-(BTN_HEIGHT + 1));
			} else {
				btn.setPos(0, y);
				y += BTN_HEIGHT;
				if (PixelScene.landscape()) y++;
			}
			if (langs.get(i).status() != Languages.Status.UNFINISHED || DeviceCompat.isDebug()) {
				add(btn);
			}
		}
		y = Math.max(MIN_HEIGHT, y);
		resize(PixelScene.landscape() ? WIDTH_L : WIDTH_P, y);

		int textLeft = width - 65;
		int textWidth = width - textLeft;

		ColorBlock separator = new ColorBlock(1, y, 0xFF000000);
		separator.x = textLeft - 2.5f;
		add(separator);

		//language info layout.
		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.titleCase(currLang.nativeName()) , 9 );
		title.setPos( textLeft + (textWidth - title.width())/2f, 2 );
		title.hardlight(TITLE_COLOR);
		PixelScene.align(title);
		add(title);

		if (currLang == Languages.ENGLISH){

			RenderedTextBlock info = PixelScene.renderTextBlock(6);
			info.text("This is the source language, written by the developer.", width - textLeft);
			info.setPos(textLeft, title.bottom() + 4);
			add(info);

		} else {

			RenderedTextBlock info = PixelScene.renderTextBlock(6);
			switch (currLang.status()) {
				case REVIEWED:
					info.text(Messages.get(this, "completed"), width - textLeft);
					break;
				case UNREVIEWED:
					info.text(Messages.get(this, "unreviewed"), width - textLeft);
					break;
				case INCOMPLETE:
					info.text(Messages.get(this, "unfinished"), width - textLeft);
					break;
			}
			info.setPos(textLeft, title.bottom() + 4);
			add(info);

			RedButton creditsBtn = new RedButton(Messages.titleCase(Messages.get(this, "credits"))){
				@Override
				protected void onClick() {
					super.onClick();
					String creds = "";
					String creds2 = "";
					String[] reviewers = currLang.reviewers();
					String[] translators = currLang.translators();
					
					boolean wide = false;
					if (PixelScene.landscape() && (reviewers.length + translators.length) > 10){
						wide = true;
					}
					
					int i;
					if (reviewers.length > 0){
						creds += Messages.titleCase(Messages.get(WndLangs.class, "reviewers")) + "\n";
						creds2 += "";
						for ( i = 0; i < reviewers.length; i++){
							if (wide && i % 2 == 1){
								creds2 += "-" + reviewers[i] + "\n";
							} else {
								creds += "-" + reviewers[i] + "\n";
							}
						}
						creds += "\n";
						creds2 += "\n";
						if (i % 2 == 1) creds2 += "\n";
					}

					if (reviewers.length > 0 || translators.length > 0){
						creds += Messages.titleCase(Messages.get(WndLangs.class, "translators")) + "\n";
						creds2 += "\n";
						//reviewers are also translators
						for ( i = 0; i < reviewers.length; i++){
							if (wide && i % 2 == 1){
								creds2 += "-" + reviewers[i] + "\n";
							} else {
								creds += "-" + reviewers[i] + "\n";
							}
						}
						for (int j = 0; j < translators.length; j++){
							if (wide && (j + i) % 2 == 1){
								creds2 += "-" + translators[j] + "\n";
							} else {
								creds += "-" + translators[j] + "\n";
							}
						}
					}
					
					creds = creds.substring(0, creds.length()-1);

					Window credits = new Window( 0, 0, 0, Chrome.get(Chrome.Type.TOAST) );
					
					int w = wide? 135 : 65;

					RenderedTextBlock title = PixelScene.renderTextBlock(6);
					title.text(Messages.titleCase(Messages.get(WndLangs.class, "credits")) , w);
					title.hardlight(SHPX_COLOR);
					title.setPos((w - title.width())/2, 0);
					credits.add(title);

					RenderedTextBlock text = PixelScene.renderTextBlock(5);
					text.setHightlighting(false);
					text.text(creds, 65);
					text.setPos(0, title.bottom() + 2);
					credits.add(text);
					
					if (wide){
						RenderedTextBlock rightColumn = PixelScene.renderTextBlock(5);
						rightColumn.setHightlighting(false);
						rightColumn.text(creds2, 65);
						rightColumn.setPos(70, title.bottom() + 8.5f);
						credits.add(rightColumn);
					}

					credits.resize(w, (int)text.bottom());
					parent.add(credits);
				}
			};
			creditsBtn.setSize(creditsBtn.reqWidth() + 2, 16);
			creditsBtn.setPos(textLeft + (textWidth - creditsBtn.width()) / 2f, y - 18);
			add(creditsBtn);

			RenderedTextBlock transifex_text = PixelScene.renderTextBlock(6);
			transifex_text.text(Messages.get(this, "transifex"), width - textLeft);
			transifex_text.setPos(textLeft, creditsBtn.top() - 2 - transifex_text.height());
			add(transifex_text);

		}

	}
	
	@Override
	public void hide() {
		super.hide();
		//resets generators because there's no need to retain chars for languages not selected
		MainGame.seamlessResetScene(new Game.SceneChangeCallback() {
			@Override
			public void beforeCreate() {
				Game.platform.resetGenerators();
			}
			@Override
			public void afterCreate() {
				//do nothing
			}
		});
	}
}
