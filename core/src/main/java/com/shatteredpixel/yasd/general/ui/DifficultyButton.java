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

import com.shatteredpixel.yasd.general.Chrome;
import com.shatteredpixel.yasd.general.Difficulty;
import com.shatteredpixel.yasd.general.PPDSettings;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DifficultyButton extends StyledButton {

	public static int WIDTH = 80;
	public static int HEIGHT = 20;

	private Difficulty difficulty;
	public static ArrayList<DifficultyButton> buttonArrayList = new ArrayList<>();

	public DifficultyButton(@NotNull Difficulty difficulty) {
		super(Chrome.Type.GREY_BUTTON_TR, difficulty.title(), 9);
		this.difficulty = difficulty;
		buttonArrayList.add(this);
		updateDifficulty();
	}

	@Override
	protected void onClick() {
		super.onClick();
		PPDSettings.difficulty(difficulty);
		for (DifficultyButton difficultyButton : buttonArrayList) {
			difficultyButton.updateDifficulty();
		}
	}

	@Override
	public void alpha(float value) {
		super.alpha(value);
		text.alpha(difficulty.isUnlocked() ? value*1f : value*0.3f);
	}

	private void updateDifficulty() {
		if (difficulty == PPDSettings.difficulty()) {
			icon(Icons.get(Icons.CHALLENGE_ON));
		} else {
			icon(Icons.get(Icons.CHALLENGE_OFF));
		}
		enable(difficulty.isUnlocked());
	}

	public static void reset() {
		buttonArrayList = new ArrayList<>();
	}
}
