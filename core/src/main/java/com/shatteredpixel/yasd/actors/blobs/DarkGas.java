package com.shatteredpixel.yasd.actors.blobs;

import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.Actor;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.buffs.Aggression;
import com.shatteredpixel.yasd.actors.buffs.Barrier;
import com.shatteredpixel.yasd.actors.buffs.Buff;
import com.shatteredpixel.yasd.actors.mobs.Mob;
import com.shatteredpixel.yasd.effects.BlobEmitter;
import com.shatteredpixel.yasd.effects.Speck;
import com.shatteredpixel.yasd.levels.Level;
import com.shatteredpixel.yasd.levels.Terrain;
import com.shatteredpixel.yasd.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class DarkGas extends Blob {

    private int strength = 0;
    private int ownerID = 0;

    @Override
    protected void evolve() {
        super.evolve();

        if (volume == 0){
            strength = 0;
        } else {
            Char ch;
            int cell;
            Level l = Dungeon.level;
            for (int i = area.left; i < area.right; i++){
                for (int j = area.top; j < area.bottom; j++){
                    cell = i + j*l.width();
                    l.losBlocking[cell] = off[cell] > 0 || (Terrain.flags[l.map[cell]] & Terrain.LOS_BLOCKING) != 0;
                    if (cur[cell] > 0 && (ch = Actor.findChar( cell )) != null) {
                        int actualStrength = strength*volumeAt(cell, this.getClass())/30;//Multiply by volume (stronger at the center)
                        if (!ch.isImmune(this.getClass())) {
                            Buff.affect(ch, Aggression.class, 1 + actualStrength);

                            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                                if (l.distance(mob.pos, cell) < 5) {//All mobs within 5-tile radius are attracted to the location
                                    mob.beckon(cell);
                                }
                            }
                            ch.damage(Random.Int(actualStrength / 3, actualStrength+2), this);//Take some direct damage. Also prevents the hero standing in it for bonus shielding/stealth without consequence
                            Char owner = (Char) Actor.findById(ownerID);
                            if (owner != null) {
                                int existingShield = 0;
                                Barrier barrier = owner.buff(Barrier.class);
                                if (barrier != null) {//Extend shield if possible
                                    existingShield = barrier.shielding();
                                }
                                int shield = owner.HT / Random.IntRange(10,20) + existingShield;
                                if (shield > 1f) {//If it won't even last a turn, adding it is useless
                                    Buff.affect(owner, Barrier.class).setShield(shield);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public DarkGas setStrength(int str){
        if (str > strength) {
            strength = str;
        }
        return this;
    }

    public DarkGas setOwner(Char entity) {
        this.ownerID = entity.id();
        return this;
    }

    private static final String STRENGTH = "strength";
    private static final String OWNER = "ownerID";

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );

        emitter.pour( Speck.factory(Speck.SMOKE), 0.4f );
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        strength = bundle.getInt( STRENGTH );
        ownerID = bundle.getInt( OWNER );
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put( STRENGTH, strength );
        bundle.put( OWNER, ownerID);
    }



    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }
}
