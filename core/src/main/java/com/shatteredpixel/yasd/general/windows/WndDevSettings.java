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

package com.shatteredpixel.yasd.general.windows;

import com.shatteredpixel.yasd.general.PPDSettings;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.ui.CheckBox;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
import com.shatteredpixel.yasd.general.ui.Window;

public class WndDevSettings extends Window {

	private static final int WIDTH		    = 112;
	private static final int BTN_HEIGHT	    = 18;
	private static final int GAP 	 		= 6;
	public WndDevSettings() {
		super();
		IconTitle titlebar = new IconTitle();
		titlebar.label(Messages.get(this, "title"));
		titlebar.setRect(0, 0, WIDTH, 0);
		add( titlebar );

		RenderedTextBlock message = PixelScene.renderTextBlock( Messages.get(this, "debug_report_desc"), 6 );
		message.maxWidth(WIDTH);
		message.setPos(0, titlebar.bottom() + GAP);
		add( message );

		CheckBox chkDebugReport = new CheckBox(Messages.get(this, "debug_report")){
			@Override
			protected void onClick() {
				super.onClick();
				PPDSettings.debugReport(checked());
			}
		};
		chkDebugReport.setRect(0, message.bottom() + GAP, WIDTH, BTN_HEIGHT);
		chkDebugReport.checked(PPDSettings.debugReport());
		add(chkDebugReport);

		RenderedTextBlock message2 = PixelScene.renderTextBlock( Messages.get(this, "disable_cache_desc"), 6 );
		message2.maxWidth(WIDTH);
		message2.setPos(0, chkDebugReport.bottom() + GAP * 3);
		add( message2 );

		CheckBox chkDisableCache = new CheckBox(Messages.get(this, "disable_cache")){
			@Override
			protected void onClick() {
				super.onClick();
				PPDSettings.mapCache(!checked());
			}
		};
		chkDisableCache.setRect(0, message2.bottom() + GAP, WIDTH, BTN_HEIGHT);
		chkDisableCache.checked(!PPDSettings.mapCache());
		add(chkDisableCache);

		resize(WIDTH, (int) chkDisableCache.bottom());
	}
}
