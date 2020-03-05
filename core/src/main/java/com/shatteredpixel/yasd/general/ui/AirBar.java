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

package com.shatteredpixel.yasd.general.ui;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.LimitedAir;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.watabou.noosa.Image;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Component;

public class AirBar extends Component {
	private Image bar;
	private Image water;
	private Emitter bubbles;

	private static Char ch;
	private static String asset = Assets.BOSSHP;
	private static AirBar instance;

	AirBar() {
		super();
		visible = active = Dungeon.underwater();
		instance = this;
	}

	@Override
	protected void createChildren() {
		bar = new Image(asset, 0, 0, 64, 16);
		add(bar);

		width = bar.width;
		height = bar.height;

		water = new Image(asset, 15, 25, 47, 4);
		add(water);

		bubbles = new Emitter();
		bubbles.pos(bar);
		bubbles.pour(Speck.factory(Speck.BUBBLE), 0.3f);
		bubbles.autoKill = false;
		bubbles.on = false;
		add( bubbles );
	}

	@Override
	protected void layout() {
		bar.x = x;
		bar.y = y;

		water.x = bar.x+15;
		water.y = bar.y+6;
	}

	@Override
	public void update() {
		super.update();
		if (ch != null){
			if (!ch.isAlive()){
				ch = null;
				visible = active = false;
			} else {
				LimitedAir limitedAir = ch.buff(LimitedAir.class);
				if (limitedAir != null) {
					water.scale.x = limitedAir.percentage();
					bubbles.on = true;
				} else {
					bubbles.on = false;
				}

			}
		}
	}

	public static boolean isAssigned(){
		return ch != null && ch.isAlive() && ch.buff(LimitedAir.class) != null;
	}

	public static void assignChar(Char ch){
		AirBar.ch = ch;
		if (instance != null) {
			instance.visible = instance.active = true;
		}
	}
}
