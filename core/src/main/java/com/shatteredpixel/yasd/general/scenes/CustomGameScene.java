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

import com.shatteredpixel.yasd.general.Chrome;
import com.shatteredpixel.yasd.general.CustomGame;
import com.shatteredpixel.yasd.general.PPDGame;
import com.shatteredpixel.yasd.general.PPDSettings;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.ui.Archs;
import com.shatteredpixel.yasd.general.ui.CheckBox;
import com.shatteredpixel.yasd.general.ui.ExitButton;
import com.shatteredpixel.yasd.general.ui.Icons;
import com.shatteredpixel.yasd.general.ui.OptionSlider;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
import com.shatteredpixel.yasd.general.ui.StyledButton;
import com.shatteredpixel.yasd.general.ui.TransparentOptionSlider;
import com.shatteredpixel.yasd.general.ui.Window;
import com.shatteredpixel.yasd.general.windows.WndChallenges;
import com.shatteredpixel.yasd.general.windows.WndOptions;
import com.watabou.noosa.Camera;

import java.util.ArrayList;

public class CustomGameScene extends PixelScene {

    private static final int BUTTON_WIDTH   = 80;

    private static final int SLIDER_HEIGHT	= 24;
    private static final int BTN_HEIGHT	    = 18;
    private static final float GAP          = 2;

    private ArrayList<OptionSlider> sliders = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxes = new ArrayList<>();

    @Override
    public void create() {
        super.create();

        uiCamera.visible = false;

        int w = Camera.main.width;
        int h = Camera.main.height;

        Archs archs = new Archs();
        archs.setSize(w, h);
        add(archs);

        ExitButton btnExit = new ExitButton() {
            @Override
            protected void onClick() {
                onBackPressed();
            }
        };
        btnExit.setPos(w - btnExit.width(), 0);
        add(btnExit);

        RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
        title.hardlight(Window.TITLE_COLOR);
        title.setPos(
                (w - title.width()) / 2f,
                (20 - title.height()) / 2f
        );
        align(title);
        add(title);

        //########## COLUMN 1 ##########
        float yPos = title.bottom() + GAP;
        float xPos = 0;

        float btnWidth;
        if (PixelScene.landscape()) {
            btnWidth = (w - GAP) / 3f - GAP;
        } else {
            btnWidth = (w - GAP) / 2f - GAP;
        }

        for (CustomGame.Modifier modifier : CustomGame.Modifier.values()) {
            //0.5x to 4x (gets divided by 10)
            TransparentOptionSlider slider = new TransparentOptionSlider(Messages.get(this, modifier.name()), "0.5x", "4x", 1, 8) {
                @Override
                protected void onChange() {
                    modifier.setGlobalValue(getSelectedValue()/2f);
                }
            };
            //Set to 1 (default)
            slider.setSelectedValue((int) (modifier.getGlobal()*2));
            slider.setRect(xPos, yPos, btnWidth, SLIDER_HEIGHT);
            sliders.add(slider);
            add(slider);
            yPos = slider.bottom() + GAP;
        }

        //########## COLUMN 2 ##########
        //Only move to a new column on Landscape mode, on Portrait it looks better with 2 columns not 3
        if (PixelScene.landscape()) {
            xPos += btnWidth + GAP;
            yPos = title.bottom() + GAP;
        }

        for (CustomGame.Toggle toggle : CustomGame.Toggle.values()) {
            CheckBox checkBox = new CheckBox(Messages.get(this, toggle.name())) {

                @Override
                protected void layout() {
                    super.layout();
                    remove(bg);
                    bg = Chrome.get(Chrome.Type.GREY_BUTTON_TR);
                    bg.x = x;
                    bg.y = y;
                    bg.size( width, height );
                    add(bg);
                }

                @Override
                protected void onClick() {
                    super.onClick();
                    toggle.setGlobalValue(checked());
                }
            };
            checkBox.checked(toggle.getGlobal());
            checkBox.setRect(xPos, yPos, btnWidth, BTN_HEIGHT);
            checkBoxes.add(checkBox);
            add(checkBox);
            yPos = checkBox.bottom() + GAP;
        }

        //########## COLUMN 3 ##########
        yPos = title.bottom() + GAP;
        xPos += btnWidth + GAP;

        StyledButton btnChals = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "challenges")) {

            @Override
            protected void onClick() {
                PPDGame.scene().addToFront(new WndChallenges(PPDSettings.challenges(), true) {
                    public void onBackPressed() {
                        super.onBackPressed();
                        icon(Icons.get(PPDSettings.challenges() > 0 ? Icons.CHALLENGE_ON : Icons.CHALLENGE_OFF));
                    }
                });
            }
        };
        btnChals.setRect(xPos, yPos, btnWidth, BTN_HEIGHT);
        btnChals.icon(Icons.get(PPDSettings.challenges() > 0 ? Icons.CHALLENGE_ON : Icons.CHALLENGE_OFF));
        add(btnChals);

        yPos = btnChals.bottom() + GAP;

        StyledButton btnDefaults = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "defaults")) {

            @Override
            protected void onClick() {
                //Reset all modifiers
                for (CustomGame.Modifier modifier : CustomGame.Modifier.values())
                    modifier.setGlobalValue(1f);
                //Visually reset modifiers
                for (OptionSlider slider : sliders) {
                    slider.setSelectedValue(2);
                }

                //Reset all toggles
                for (CustomGame.Toggle toggle : CustomGame.Toggle.values())
                    toggle.setGlobalValue(false);
                //Visually rest toggles
                for (CheckBox checkBox : checkBoxes) {
                    checkBox.checked(false);
                }

                //Reset challenges
                PPDSettings.challenges(0);
                //Visually reset challenges
                btnChals.icon(Icons.get(Icons.CHALLENGE_OFF));
            }
        };
        btnDefaults.setRect(xPos, yPos, btnWidth, BTN_HEIGHT);
        add(btnDefaults);

        yPos = btnDefaults.bottom() + GAP;

        //At the bottom of the screen, across the entire screen always
        StyledButton btnStart = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "start")) {
            @Override
            protected void onClick() {
                super.onClick();
                if (CustomGame.calcTotalGlobalDifficultyFactor() < CustomGame.DIFFICULTY_MIN_BADGES) {
                    PPDGame.scene().addToFront(new WndOptions(Messages.get(CustomGameScene.this, "warning"), Messages.get(CustomGameScene.this, "difficulty_too_low"), Messages.get(CustomGameScene.this, "confirm"), Messages.get(CustomGameScene.this, "cancel")) {
                        @Override
                        protected void onSelect(int index) {
                            super.onSelect(index);
                            if (index == 0) {
                                HeroSelectScene.doInitRun(true);
                            }
                            hide();
                        }
                    });
                } else {
                    HeroSelectScene.doInitRun(true);
                }
            }
        };
        btnStart.setRect(0, h-BTN_HEIGHT, w, BTN_HEIGHT);
        add(btnStart);

        fadeIn();
    }

    @Override
    protected void onBackPressed() {
        PPDGame.switchScene(HeroSelectScene.class);
    }
}
