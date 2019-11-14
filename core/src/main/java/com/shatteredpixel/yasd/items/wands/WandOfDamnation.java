package com.shatteredpixel.yasd.items.wands;


import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.buffs.Buff;
import com.shatteredpixel.yasd.actors.buffs.Ooze;
import com.shatteredpixel.yasd.actors.buffs.Weakness;
import com.shatteredpixel.yasd.effects.CellEmitter;
import com.shatteredpixel.yasd.effects.Splash;
import com.shatteredpixel.yasd.effects.particles.ShadowParticle;
import com.shatteredpixel.yasd.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.mechanics.Ballistica;
import com.watabou.utils.Random;

public class WandOfDamnation extends Wand {
    @Override
    protected void onZap(Ballistica attack) {

    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        Char ch = defender;
        int buff = Random.Int(3);
        if (Dungeon.level.heroFOV[ ch.pos ]){
            CellEmitter.get(ch.pos).burst(ShadowParticle.UP, 5);
        }
        switch (buff) {//Has random chaotic effects
            default:
                break;
            case 1:
                Buff.affect(ch, Ooze.class).set( 20f );
                Splash.at( ch.pos, 0x000000, 5);
                break;
            case 2:
                Buff.prolong( ch, Weakness.class, Weakness.DURATION/2f );
                break;
            case 3:
                ch.damage(ch.HT/20, this);
                break;
        }
    }
}
