package com.shatteredpixel.yasd;

import java.util.ArrayList;

public class Constants {
    /*
    Purpose of this file is to have constants that can be used to quickly tweak in-game things.
    Makes modding easier and also helps me in future.
     */

    //Number of upgrades Curse Infusion adds.
    public static final int CURSE_INFUSION_BONUS_AMT = 1;
    //Default Upgrade Limit. Set to -1 to remove the limit.
    public static final int UPGRADE_LIMIT = 3;
    //Wand charges cap.
    public static final int WAND_CHARGE_CAP = 10;
    //Number of floors.
    public static final int NUM_FLOORS = 26;
    //Floors where descending is blocked.
    public static final ArrayList<Integer> FLOORS_NO_DESCEND = new ArrayList<>();
    static {
        FLOORS_NO_DESCEND.add(26);
    }
    //Floors where ascending is blocked.
    public static final ArrayList<Integer> FLOORS_NO_ASCEND = new ArrayList<>();

    //Chapter length. WIP.
    public static final int CHAPTER_LENGTH = 5;
}
