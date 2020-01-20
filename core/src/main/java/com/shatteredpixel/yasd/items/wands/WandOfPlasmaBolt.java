package com.shatteredpixel.yasd.items.wands;

import com.shatteredpixel.yasd.Assets;
import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.Actor;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.effects.MagicMissile;
import com.shatteredpixel.yasd.items.weapon.enchantments.Unstable;
import com.shatteredpixel.yasd.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.mechanics.Ballistica;
import com.shatteredpixel.yasd.sprites.CharSprite;
import com.shatteredpixel.yasd.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class WandOfPlasmaBolt extends DamageWand {
    {
        image = ItemSpriteSheet.WAND_PLASMA;
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
    protected void fx(Ballistica bolt, Callback callback) {
        MagicMissile.boltFromChar(curUser.sprite.parent,
                MagicMissile.PLASMA_BOLT,
                curUser.sprite,
                bolt.collisionPos,
                callback);
        Sample.INSTANCE.play(Assets.SND_ZAP);
    }

    public int hit(Char enemy, float lvl) {
        if (curUser == null) {
            curUser = Dungeon.hero;
        }
        int damage = damageRoll(lvl);
        damage = enemy.defenseProc(curUser, damage);
        damage -= enemy.drRoll();
        if (damage > 0) {
            enemy.damage(damage, this);
        }
        return damage;
    }

    @Override
    public void onZap(Ballistica bolt) {
        Char ch = Actor.findChar( bolt.collisionPos );
        if (ch != null) {
            if (Char.hit(curUser,ch,true)) {

                processSoulMark(ch, chargesPerCast());
                hit(ch);

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
        new Unstable().proc(staff, attacker, defender, damage);
    }
}
