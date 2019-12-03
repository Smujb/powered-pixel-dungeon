package com.shatteredpixel.yasd.items.wands;

import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.mechanics.Ballistica;
import com.shatteredpixel.yasd.sprites.ItemSpriteSheet;

public class WandOfFlow extends DamageWand {
    {
        image = ItemSpriteSheet.WAND_FLOW;
    }

    @Override
    public int min(int lvl) {
        return 0;
    }

    @Override
    public int max(int lvl) {
        return 0;
    }

    @Override
    public void onZap(Ballistica attack) {

    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {

    }
}
