package com.shatteredpixel.yasd;

import java.util.ArrayList;

public class Constants {
    /*
    Purpose of this file is to have constants that can be used to quickly tweak in-game things.
    Makes modding easier and also helps me in future.
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
    public static final ArrayList<Integer> FLOORS_NO_DESCEND = new ArrayList<>();
    static {
        FLOORS_NO_DESCEND.add(26);
    }

    //Floors where ascending is blocked.
    public static final ArrayList<Integer> FLOORS_NO_ASCEND = new ArrayList<>();

    //Chapter length. WIP.
    public static final int CHAPTER_LENGTH = 5;

    //Number of chapters.
    public static final int NUM_CHAPTERS = 5;

    //Bonus floors.
    public static final int BONUS_FLOORS = 1;

    //Number of floors.
    public static final int NUM_FLOORS = CHAPTER_LENGTH * NUM_CHAPTERS + BONUS_FLOORS;
    public static final int HERO_EXP_CAP = NUM_FLOORS + 4;

}
