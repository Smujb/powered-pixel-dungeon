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

import com.shatteredpixel.yasd.general.Chrome;
import com.shatteredpixel.yasd.general.Difficulty;
import com.shatteredpixel.yasd.general.YASDSettings;

import org.jetbrains.annotations.NotNull;

public class DifficultyButton extends StyledButton {

	private Difficulty difficulty;

	public DifficultyButton(@NotNull Difficulty difficulty) {
		super(Chrome.Type.GREY_BUTTON_TR, difficulty.title(), 9);
		this.difficulty = difficulty;
	}

	@Override
	protected void onClick() {
		super.onClick();
		YASDSettings.difficulty(difficulty);
	}

	public static class Easy extends DifficultyButton {
		public Easy() {
			super(Difficulty.EASY);
		}
	}

	public static class Medium extends DifficultyButton {
		public Medium() {
			super(Difficulty.MEDIUM);
		}
	}

	public static class Hard extends DifficultyButton {
		public Hard() {
			super(Difficulty.HARD);
		}
	}

	public static class Impossible extends DifficultyButton {
		public Impossible() {
			super(Difficulty.IMPOSSIBLE);
		}
	}
}
