package com.shatteredpixel.yasd.items.wands;

import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.buffs.Bleeding;
import com.shatteredpixel.yasd.actors.buffs.Buff;
import com.shatteredpixel.yasd.effects.Splash;
import com.shatteredpixel.yasd.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.mechanics.Ballistica;
import com.shatteredpixel.yasd.sprites.ItemSpriteSheet;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WandOfThornvines extends Wand {
    {
        image = ItemSpriteSheet.WAND_THORNVINES;
    }

    @Override
    protected void onZap(Ballistica attack) {

    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        if (Random.Int( staff.level() + 3 ) >= 2) {

            Buff.affect(defender, Bleeding.class).set(damage/3f);
            Splash.at( defender.sprite.center(), -PointF.PI / 2, PointF.PI / 6,
                    defender.sprite.blood(), 10 );

        }

    }
}
