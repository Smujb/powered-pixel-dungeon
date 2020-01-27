package com.shatteredpixel.yasd;

import com.shatteredpixel.yasd.levels.CavesBossLevel;
import com.shatteredpixel.yasd.levels.CavesLevel;
import com.shatteredpixel.yasd.levels.CityBossLevel;
import com.shatteredpixel.yasd.levels.CityLevel;
import com.shatteredpixel.yasd.levels.DeadEndLevel;
import com.shatteredpixel.yasd.levels.HallsBossLevel;
import com.shatteredpixel.yasd.levels.HallsLevel;
import com.shatteredpixel.yasd.levels.LastLevel;
import com.shatteredpixel.yasd.levels.LastShopLevel;
import com.shatteredpixel.yasd.levels.Level;
import com.shatteredpixel.yasd.levels.NewPrisonBossLevel;
import com.shatteredpixel.yasd.levels.PrisonLevel;
import com.shatteredpixel.yasd.levels.SewerBossLevel;
import com.shatteredpixel.yasd.levels.SewerLevel;

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

    //Floors where descending is blocked.
    public static ArrayList<Integer> FLOORS_NO_DESCEND = new ArrayList<>();
    static {
        FLOORS_NO_DESCEND.add(26);
    }

    //Floors where ascending is blocked.
    public static ArrayList<Integer> FLOORS_NO_ASCEND = new ArrayList<>();

    //Chapter length. WIP.
    public static final int CHAPTER_LENGTH = 6;

    //Number of chapters.
    public static final int NUM_CHAPTERS = 5;

    //Bonus floors.
    public static final int BONUS_FLOORS = 1;

    //Number of floors.
    public static final int NUM_FLOORS = CHAPTER_LENGTH * NUM_CHAPTERS + BONUS_FLOORS;
    public static final int HERO_EXP_CAP = NUM_FLOORS + 4;

    //SoU per chapter
    public static final int SOU_PER_CHAPTER = 3;

    //Level types. Change it if you want to change depth system.
    public static ArrayList<Class <? extends Level>> LEVEL_TYPES = new ArrayList<>(Arrays.asList(
            DeadEndLevel.class,//Floor 0
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
    ));

}
