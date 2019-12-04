package com.shatteredpixel.yasd.items.wands;

import com.shatteredpixel.yasd.Assets;
import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.Actor;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.mechanics.Ballistica;
import com.shatteredpixel.yasd.sprites.CharSprite;
import com.shatteredpixel.yasd.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class WandOfPlasmaBolt extends DamageWand {
    {
        image = ItemSpriteSheet.WAND_DISINTIGRATION_YAPD;
    }
    @Override
    public float min(float lvl) {
        return 4 + 2*lvl;
    }

    @Override
    public float max(float lvl) {
        return 15 + 8*lvl;
    }

    @Override
    public void onZap(Ballistica bolt) {
        Char ch = Actor.findChar( bolt.collisionPos );
        if (ch != null) {
            if (curUser.hit(curUser,ch,true)) {
                int dr = ch.drRoll();
                int damage = damageRoll()-dr;
                damage = ch.defenseProc(curUser,damage);
                damage = Math.max(0,damage);
                processSoulMark(ch, chargesPerCast());
                ch.damage(damage, this);

                ch.sprite.burst(0xFFFFFFFF, (int) actualLevel() / 2 + 2);
            } else {
                String defense = ch.defenseVerb();
                ch.sprite.showStatus( CharSprite.NEUTRAL, defense );

                Sample.INSTANCE.play(Assets.SND_MISS);
            }

        } else {
            Dungeon.level.pressCell(bolt.collisionPos);
        }
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {

    }
}
