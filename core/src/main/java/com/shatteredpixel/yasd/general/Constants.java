package com.shatteredpixel.yasd.general;

import com.shatteredpixel.yasd.general.levels.CavesBossLevel;
import com.shatteredpixel.yasd.general.levels.CavesLevel;
import com.shatteredpixel.yasd.general.levels.CityBossLevel;
import com.shatteredpixel.yasd.general.levels.CityLevel;
import com.shatteredpixel.yasd.general.levels.DeadEndLevel;
import com.shatteredpixel.yasd.general.levels.HallsBossLevel;
import com.shatteredpixel.yasd.general.levels.HallsLevel;
import com.shatteredpixel.yasd.general.levels.LastLevel;
import com.shatteredpixel.yasd.general.levels.LastShopLevel;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.LootLevel;
import com.shatteredpixel.yasd.general.levels.NewPrisonBossLevel;
import com.shatteredpixel.yasd.general.levels.OldPrisonBossLevel;
import com.shatteredpixel.yasd.general.levels.PrisonLevel;
import com.shatteredpixel.yasd.general.levels.SewerBossLevel;
import com.shatteredpixel.yasd.general.levels.SewerLevel;
import com.shatteredpixel.yasd.general.levels.TestBossLevel;

import java.util.ArrayList;
import java.util.Arrays;

public class Constants {
    /*
    Purpose of this file is to have constants that can be used to quickly tweak in-game things.
    Makes modding easier and also helps me in future.

    (I may rework some of my other mods off this in the future as I'm quite happy with engine functionality atm)
     */
    //
    //############################## EQUIPMENT STUFF ##############################
    //
    //Want to mod YASD without degradation? Go ahead.
    public static final boolean DEGRADATION = true;

    //Number of upgrades Curse Infusion adds.
    public static final int CURSE_INFUSION_BONUS_AMT = 1;

    //Default Upgrade Limit. Set to -1 to remove the limit.
    public static final int UPGRADE_LIMIT = 3;

    //Wand charges cap.
    public static final int WAND_CHARGE_CAP = 10;

    //Can't find items above this tier.
    public static final int MAXIMUM_TIER = 5;


    //
    //############################## CHAR/MOB STUFF ##############################
    //
    //Number of misc slots for char.
    public static final int MISC_SLOTS = 5;

    //Want to mod YASD without morale? Go ahead.
    public static final boolean MORALE = true;

    //
    //############################## DUNGEON STUFF ##############################
    //


    //Chapter length. WIP.
    public static final int CHAPTER_LENGTH = 6;

    //Number of chapters.
    public static final int NUM_CHAPTERS = 5;

    //Bonus floors.
    public static final int BONUS_FLOORS = 1;

    //Number of floors.
    public static final int NUM_FLOORS = CHAPTER_LENGTH * NUM_CHAPTERS + BONUS_FLOORS;

    //Number of paths.
    public static final int NUM_PATHS = 3;

    //Hero exp cap
    public static final int HERO_EXP_CAP = NUM_FLOORS + 4;

    //SoU per chapter
    public static final int SOU_PER_CHAPTER = 3;

    //Level types. Change it if you want to change depth system.
    public static final ArrayList<ArrayList<Class <? extends Level>>> LEVEL_TYPES = new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList(
                    DeadEndLevel.class,//Floor 0, shouldn't ever be here
                    SewerLevel.class,
                    SewerLevel.class,
                    SewerLevel.class,
                    SewerLevel.class,
                    SewerLevel.class,
                    SewerBossLevel.class,//Floor 6, boss
                    PrisonLevel.class,
                    PrisonLevel.class,
                    PrisonLevel.class,
                    PrisonLevel.class,
                    PrisonLevel.class,
                    NewPrisonBossLevel.class,//Floor 12, boss.
                    CavesLevel.class,
                    CavesLevel.class,
                    CavesLevel.class,
                    CavesLevel.class,
                    CavesLevel.class,
                    CavesBossLevel.class,//Floor 18, boss
                    CityLevel.class,
                    CityLevel.class,
                    CityLevel.class,
                    CityLevel.class,
                    CityLevel.class,
                    CityBossLevel.class,//Floor 24, boss
                    LastShopLevel.class,//Floor 25, Imp
                    HallsLevel.class,
                    HallsLevel.class,
                    HallsLevel.class,
                    HallsLevel.class,
                    HallsBossLevel.class,//Floor 30, boss
                    LastLevel.class//Floor 31, last level
            )),
            new ArrayList<>(Arrays.asList(
                    LootLevel.class,
                    LootLevel.class,
                    LootLevel.class,
                    LootLevel.class,
                    LootLevel.class
            )),
            new ArrayList<>(Arrays.asList(
                    DeadEndLevel.class,
                    TestBossLevel.class,
                    NewPrisonBossLevel.class,
                    OldPrisonBossLevel.class,
                    CavesBossLevel.class,
                    CityBossLevel.class,
                    HallsBossLevel.class
            ))

    ));

    //
    //############################## UI STUFF ##############################
    //

    //TODO: add ability for more quickslots
    public static final int MAX_QUICKSLOTS = 6;
    public static final int MIN_QUICKSLOTS = 2;

}
