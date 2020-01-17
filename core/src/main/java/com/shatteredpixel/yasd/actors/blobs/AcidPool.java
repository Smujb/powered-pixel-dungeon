package com.shatteredpixel.yasd.actors.blobs;

import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.Actor;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.buffs.Buff;
import com.shatteredpixel.yasd.actors.buffs.Ooze;
import com.shatteredpixel.yasd.effects.BlobEmitter;
import com.shatteredpixel.yasd.effects.Speck;
import com.shatteredpixel.yasd.messages.Messages;
import com.watabou.utils.Bundle;

public class AcidPool extends Blob {
    {
        actPriority = MOB_PRIO + 1;
    }
    private int damageOnStep = 0;
    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }

    public AcidPool setStrength(int damage) {
        damageOnStep = damage;
        return this;
    }

    private static final String DAMAGE = "damage";
    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(DAMAGE,damageOnStep);
        super.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        damageOnStep = bundle.getInt(DAMAGE);
        super.restoreFromBundle(bundle);
    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );

        emitter.pour( Speck.factory(Speck.BUBBLE_GREEN), 0.1f );
    }

    @Override
    protected void evolve() {
        int cell;
        Char ch;
        for (int i=area.top-1; i <= area.bottom; i++) {
            for (int j = area.left-1; j <= area.right; j++) {
                cell = j + i* Dungeon.level.width();
                if (Dungeon.level.insideMap(cell)) {
                    off[cell] = cur[cell];
                    volume += off[cell];
                }
                if (cur[cell] > 0 && (ch = Actor.findChar( cell )) != null) {
                    if (!ch.isImmune(this.getClass()))
                        ch.damage(damageOnStep, this);
                        Buff.affect(ch, Ooze.class).set( 20f );
                        clear(cell);
                }
            }
        }
    }
}
