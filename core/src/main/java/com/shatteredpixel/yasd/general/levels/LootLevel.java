package com.shatteredpixel.yasd.general.levels;

import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.potions.PotionOfExperience;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfUpgrade;

public class LootLevel extends SewerLevel {

    @Override
    protected void createItems() {
        for (int i = 1; i < 15; i++) {
            addItemToSpawn(new ScrollOfUpgrade());
            addItemToSpawn(new PotionOfExperience());
            addItemToSpawn(Generator.randomWeapon().identify());
            addItemToSpawn(Generator.randomArmor().identify());
            addItemToSpawn(Generator.randomMissile());
            addItemToSpawn(Generator.random(Generator.Category.SCROLL).identify());
            addItemToSpawn(Generator.random(Generator.Category.STONE).identify());
            addItemToSpawn(Generator.random(Generator.Category.SEED).identify());
            addItemToSpawn(Generator.random(Generator.Category.POTION).identify());
            addItemToSpawn(Generator.random().identify());
        }
        super.createItems();
    }

    @Override
    public float respawnTime() {
        return super.respawnTime()/5;
    }

    @Override
    protected int standardRooms() {
        return 20;
    }
}
