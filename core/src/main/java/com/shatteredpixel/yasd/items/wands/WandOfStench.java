package com.shatteredpixel.yasd.items.wands;

import com.shatteredpixel.yasd.Assets;
import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.Actor;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.buffs.BlobImmunity;
import com.shatteredpixel.yasd.actors.buffs.Buff;
import com.shatteredpixel.yasd.actors.buffs.Ooze;
import com.shatteredpixel.yasd.actors.buffs.StenchHolder;
import com.shatteredpixel.yasd.effects.CellEmitter;
import com.shatteredpixel.yasd.effects.MagicMissile;
import com.shatteredpixel.yasd.effects.particles.CorrosionParticle;
import com.shatteredpixel.yasd.effects.particles.StenchParticle;
import com.shatteredpixel.yasd.items.armor.curses.Stench;
import com.shatteredpixel.yasd.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.mechanics.Ballistica;
import com.shatteredpixel.yasd.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.Random;

public class WandOfStench extends Wand {

    {
        image = ItemSpriteSheet.WAND_STENCH;
    }

    @Override
    public void onZap(Ballistica attack) {
        Char ch = Actor.findChar( attack.collisionPos );
        if (ch != null) {

            processSoulMark(ch, chargesPerCast());
            StenchHolder buff = Buff.affect(ch, StenchHolder.class, 4 + level()/2);
            buff.minDamage = level();
            buff.maxDamage = 3 + level()*2;

            ch.sprite.burst(0xFF1d4636, level() / 2 + 2);

        } else {
            Dungeon.level.pressCell(attack.collisionPos);
        }
    }

    @Override
    protected void fx(Ballistica bolt, Callback callback) {
        MagicMissile.boltFromChar(
                curUser.sprite.parent,
                MagicMissile.STENCH,
                curUser.sprite,
                bolt.collisionPos,
                callback);
        Sample.INSTANCE.play(Assets.SND_ZAP);
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        if (Random.Int( level() + 3 ) >= 2) {

            Buff.affect( attacker, BlobImmunity.class, Random.IntRange(0, level()) );
            CellEmitter.center(defender.pos).burst(StenchParticle.FACTORY, 5 );

        }
    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color( ColorMath.random( 0x0bb34d, 0x1d4636) );
        particle.am = 0.75f;
        particle.setLifespan( 1.2f );
        particle.acc.set(0, 30);
        particle.setSize( 0.5f, 3f );
        particle.shuffleXY( 1f );
    }
}