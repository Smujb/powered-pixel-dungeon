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
import com.watabou.noosa.NinePatch;

public abstract class TransparentOptionSlider extends OptionSlider {

    protected NinePatch chrome(boolean slider) {
        if (slider) return Chrome.get(Chrome.Type.GREY_BUTTON);
        else return Chrome.get(Chrome.Type.GREY_BUTTON_TR);
    }

    public TransparentOptionSlider(String title, String minTxt, String maxTxt, int minVal, int maxVal) {
        super(title, minTxt, maxTxt, minVal, maxVal);
    }
}
