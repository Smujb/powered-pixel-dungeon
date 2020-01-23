package com.shatteredpixel.yasd.levels.rooms.connection;

import com.shatteredpixel.yasd.levels.Level;

public class NonHiddenMazeConnectionRoom extends MazeConnectionRoom {
    @Override
    public void paint(Level level) {
        super.paint(level);
        for (Door door : connected.values()) {
            door.set( Door.Type.UNLOCKED );
        }
    }
}
