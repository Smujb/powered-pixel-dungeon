package com.shatteredpixel.yasd.actors.blobs;

import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.Actor;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.buffs.Buff;
import com.shatteredpixel.yasd.actors.buffs.Ooze;
import com.shatteredpixel.yasd.actors.buffs.Terror;
import com.shatteredpixel.yasd.effects.BlobEmitter;
import com.shatteredpixel.yasd.effects.Speck;
import com.shatteredpixel.yasd.levels.Level;
import com.shatteredpixel.yasd.levels.Terrain;
import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.scenes.GameScene;
import com.watabou.utils.Bundle;

import javax.print.DocFlavor;

public class AcidPool extends Blob {
    {
        actPriority = VFX_PRIO;
    }

    private int damageOnStep = 0;
    private int terrain;

    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }

    public AcidPool setStrength(int damage) {
        damageOnStep = damage;
        return this;
    }

    private static final String DAMAGE = "damage";
    private static final String TERRAIN = "terrain";

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(DAMAGE,damageOnStep);
        bundle.put(TERRAIN, terrain);
        super.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        damageOnStep = bundle.getInt(DAMAGE);
        terrain = bundle.getInt(TERRAIN);
        super.restoreFromBundle(bundle);
    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );

        emitter.pour( Speck.factory(Speck.BUBBLE_GREEN), 0.1f );
    }

    @Override
    public void seed(Level level, int cell, int amount) {
        super.seed(level, cell, amount);
        terrain = level.map[cell];
        Level.set(cell, Terrain.WATER);
        GameScene.updateMap(cell);
    }

    @Override
    public void clear(int cell) {
        super.clear(cell);
        Level.set(cell, terrain);
        GameScene.updateMap(cell);
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
