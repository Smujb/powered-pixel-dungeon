package com.shatteredpixel.yasd.actors.blobs;

import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.Actor;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.buffs.Buff;
import com.shatteredpixel.yasd.actors.buffs.Ooze;
import com.shatteredpixel.yasd.actors.buffs.Vertigo;
import com.shatteredpixel.yasd.actors.buffs.Weakness;
import com.shatteredpixel.yasd.effects.BlobEmitter;
import com.shatteredpixel.yasd.effects.CellEmitter;
import com.shatteredpixel.yasd.effects.Splash;
import com.shatteredpixel.yasd.effects.particles.ShadowParticle;
import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.sprites.GooSprite;
import com.watabou.utils.Random;

public class Miasma extends Blob {
    {
        //acts after mobs, to give them a chance to resist paralysis
        actPriority = MOB_PRIO - 1;
    }

    @Override
    protected void evolve() {
        super.evolve();

        Char ch;
        int cell;
        int buff = 0;

        for (int i = area.left; i < area.right; i++) {
            for (int j = area.top; j < area.bottom; j++) {
                cell = i + j * Dungeon.level.width();
                if (cur[cell] > 0 && (ch = Actor.findChar(cell)) != null) {
                    if (!ch.isImmune(this.getClass())) {
                        buff = Random.Int(3);
                        if (Dungeon.level.heroFOV[ch.pos]) {
                            CellEmitter.get(ch.pos).burst(ShadowParticle.UP, 5);
                        }
                        switch (buff) {//Has random chaotic effects
                            default:
                                Buff.prolong(ch, Vertigo.class, Vertigo.DURATION / 2f);
                            case 1:
                                Buff.affect(ch, Ooze.class).set(20f);
                                Splash.at(ch.pos, 0x000000, 5);
                                break;
                            case 2:
                                Buff.prolong(ch, Weakness.class, Weakness.DURATION / 2f);
                                break;
                            case 3:
                                ch.damage((int) (ch.HT / 10f), this);
                                break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );

        emitter.pour(GooSprite.GooParticle.FACTORY, 0.03f );
    }

    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }
}
