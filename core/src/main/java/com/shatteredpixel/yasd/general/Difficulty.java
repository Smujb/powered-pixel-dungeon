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

package com.shatteredpixel.yasd.general;

import org.jetbrains.annotations.Contract;

public enum Difficulty {
	EASY,
	MEDIUM,
	HARD;

	@Contract(pure = true)
	public float mobHealthFactor() {
		switch (this) {
			case EASY:
				return 2/3f;
			case MEDIUM: default:
				return 1f;
			case HARD:
				return 1 + 1/3f;
		}
	}

	@Contract(pure = true)
	public float mobDamageFactor() {
		switch (this) {
			case EASY:
				return 0.5f;
			case MEDIUM: default:
				return 1f;
			case HARD:
				return 1.5f;
		}
	}

	@Contract(pure = true)
	public float moraleFactor() {
		switch (this) {
			case EASY:
				return 2/3f;
			case MEDIUM: default:
				return 1f;
			case HARD:
				return 1 + 1/3f;
		}
	}

	@Contract(pure = true)
	public float degradationAmount() {
		switch (this) {
			case EASY://Easy = 3 drop in durability per hit (200 hits until break)
				return 3;
			case MEDIUM:
			default://Medium = 6 drop in durability per hit (100 hits until break)
				return 6;
			case HARD://Hard = 12 drop in durability per hit (about 50 hits until break)
				return 12;
		}
	}
}
