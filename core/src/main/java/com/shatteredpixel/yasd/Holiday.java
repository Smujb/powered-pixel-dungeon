package com.shatteredpixel.yasd;

import java.util.Calendar;

public enum Holiday {
    NONE,
    EASTER, //1st and 2nd week of April
    HWEEN,//2nd week of october though first day of november
    XMAS; //3rd week of december through first week of january

    public static Holiday getHoliday() {
        final Calendar calendar = Calendar.getInstance();
        switch(calendar.get(Calendar.MONTH)){
            case Calendar.JANUARY:
                if (calendar.get(Calendar.WEEK_OF_MONTH) == 1)
                    return XMAS;
                break;
            case Calendar.OCTOBER:
                if (calendar.get(Calendar.WEEK_OF_MONTH) >= 2)
                    return HWEEN;
                break;
            case Calendar.NOVEMBER:
                if (calendar.get(Calendar.DAY_OF_MONTH) == 1)
                    return HWEEN;
                break;
            case Calendar.DECEMBER:
                if (calendar.get(Calendar.WEEK_OF_MONTH) >= 3)
                    return XMAS;
                break;
            case Calendar.APRIL:
                if (calendar.get(Calendar.WEEK_OF_MONTH) < 3) {
                    return EASTER;
                }
                break;
        }
        return NONE;
    }
}
