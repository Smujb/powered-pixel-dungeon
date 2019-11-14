package com.shatteredpixel.yasd.items.wands;

import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.buffs.Bleeding;
import com.shatteredpixel.yasd.actors.buffs.Buff;
import com.shatteredpixel.yasd.actors.mobs.Mob;
import com.shatteredpixel.yasd.actors.mobs.npcs.NPC;
import com.shatteredpixel.yasd.effects.MagicMissile;
import com.shatteredpixel.yasd.effects.Splash;
import com.shatteredpixel.yasd.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.mechanics.Ballistica;
import com.shatteredpixel.yasd.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.sprites.ThornVineSprite;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WandOfThornvines extends Wand {
    {
        image = ItemSpriteSheet.WAND_THORNVINES;
    }

    @Override
    protected void onZap(Ballistica attack) {

    }

    class ThornVine extends NPC {
        {
            spriteClass = ThornVineSprite.class;
            properties.add(Property.IMMOVABLE);
        }


    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        if (Random.Int( staff.level() + 3 ) >= 2) {

            Buff.affect(defender, Bleeding.class).set(damage/3f);
            Splash.at( defender.sprite.center(), -PointF.PI / 2, PointF.PI / 6,
                    defender.sprite.blood(), 10 );

        }

    }

    @Override
    protected void fx(Ballistica bolt, Callback callback) {
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.FOLIAGE_CONE,
                curUser.sprite,
                bolt.path.get(bolt.dist),
                callback );
    }
    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color( ColorMath.random(0x004400, 0x88CC44) );
        particle.am = 1f;
        particle.setLifespan(1f);
        particle.setSize( 1f, 1.5f);
        particle.shuffleXY(0.5f);
        float dst = Random.Float(11f);
        particle.x -= dst;
        particle.y += dst;
    }

    @Override
    protected int initialCharges() {
        return 4;
    }

    protected int chargesPerCast() {
        return Math.max(1, curCharges);
    }
}
