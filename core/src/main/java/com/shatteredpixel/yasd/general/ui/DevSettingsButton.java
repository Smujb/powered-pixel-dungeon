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

package com.shatteredpixel.yasd.general.ui;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.windows.WndDevSettings;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;

public class DevSettingsButton extends Button {


	private Image image;

	@Override
	protected void createChildren() {
		super.createChildren();

		image = Icons.WARNING.get();
		add( image );
	}

	@Override
	protected void layout() {
		super.layout();

		image.x = x + (width - image.width)/2f;
		image.y = y + (height - image.height)/2f;
		PixelScene.align(image);
	}

	@Override
	protected void onPointerDown() {
		image.brightness( 1.5f );
		Sample.INSTANCE.play( Assets.Sounds.CLICK );
	}

	@Override
	protected void onPointerUp() {
		image.resetColor();
	}

	@Override
	protected void onClick() {
		WndDevSettings wnd = new WndDevSettings();
		parent.add(wnd);
	}
}