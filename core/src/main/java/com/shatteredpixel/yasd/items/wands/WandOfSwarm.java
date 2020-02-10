package com.shatteredpixel.yasd.items.wands;

import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.mechanics.Ballistica;
import com.shatteredpixel.yasd.sprites.ItemSpriteSheet;

public class WandOfSwarm extends DamageWand {

    {
        image = ItemSpriteSheet.WAND_THORNVINES;
    }

    @Override
    public float min(float lvl){
        return 1+lvl;
    }
    @Override
    public float max(float lvl){
        return 5+3*lvl;
    }

    @Override
    public void onZap(Ballistica attack) {

    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {

    }
}
