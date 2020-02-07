package com.shatteredpixel.yasd.levels;

import com.shatteredpixel.yasd.items.Generator;
import com.shatteredpixel.yasd.items.potions.PotionOfExperience;
import com.shatteredpixel.yasd.items.scrolls.ScrollOfUpgrade;

public class LootLevel extends SewerLevel {

    @Override
    protected void createItems() {
        for (int i = 1; i < 20; i++) {
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
}
