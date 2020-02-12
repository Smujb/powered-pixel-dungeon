package com.shatteredpixel.yasd.actors.blobs;

import com.shatteredpixel.yasd.Assets;
import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.Actor;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.buffs.Buff;
import com.shatteredpixel.yasd.actors.buffs.FlavourBuff;
import com.shatteredpixel.yasd.actors.hero.Hero;
import com.shatteredpixel.yasd.actors.mobs.Mob;
import com.shatteredpixel.yasd.effects.BlobEmitter;
import com.shatteredpixel.yasd.effects.Flare;
import com.shatteredpixel.yasd.effects.Wound;
import com.shatteredpixel.yasd.effects.particles.SacrificialParticle;
import com.shatteredpixel.yasd.items.Ankh;
import com.shatteredpixel.yasd.journal.Notes;
import com.shatteredpixel.yasd.levels.Level;
import com.shatteredpixel.yasd.scenes.GameScene;
import com.shatteredpixel.yasd.tiles.DungeonTilemap;
import com.shatteredpixel.yasd.ui.BuffIndicator;
import com.shatteredpixel.yasd.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class SacrificialFire extends Blob {

    private static final String TXT_WORTHY		= "\"Your sacrifice is worthy...\" ";
    private static final String TXT_UNWORTHY	= "\"Your sacrifice is unworthy...\" ";
    private static final String TXT_REWARD		= "\"Your sacrifice is worthy and so you are!\" ";

    protected int pos;

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );

        for (int i = 0; i < Dungeon.level.length(); i++) {
            if (cur[i] > 0) {
                pos = i;
                break;
            }
        }
    }

    @Override
    protected void evolve() {
        volume = off[pos] = cur[pos];
        Char ch = Actor.findChar( pos );
        if (ch != null) {
            if (Dungeon.hero.fieldOfView[pos] && ch.buff( Marked.class ) == null) {
                ch.sprite.emitter().burst( SacrificialParticle.FACTORY, 20 );
                Sample.INSTANCE.play( Assets.SND_BURNING );
            }
            Buff.prolong( ch, Marked.class, Marked.DURATION );
        }
        if (Dungeon.level.heroFOV[pos]) {
            Notes.add( Notes.Landmark.SACRIFICIAL_FIRE );
        }
    }

    @Override
    public void seed(Level level, int cell, int amount ) {
        if (cur == null) cur = new int[level.length()];
        if (off == null) off = new int[cur.length];

        cur[pos] = 0;
        pos = cell;
        volume = cur[pos] = amount;

        area.union(cell%level.width(), cell/level.width());

    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );

        emitter.pour( SacrificialParticle.FACTORY, 0.04f );
    }

    public static void sacrifice( Char ch ) {

        Wound.hit( ch );

        SacrificialFire fire = (SacrificialFire)Dungeon.level.blobs.get( SacrificialFire.class );
        if (fire != null) {

            int exp = 0;
            if (ch instanceof Mob) {
                exp = ((Mob)ch).EXP * Random.IntRange( 1, 3 );
            } else if (ch instanceof Hero) {
                exp = ((Hero)ch).maxExp();
            }

            if (exp > 0) {

                int volume = fire.volume - exp;
                if (volume > 0) {
                    fire.seed( Dungeon.level, fire.pos, volume );
                    GLog.w( TXT_WORTHY );
                } else {
                    fire.seed( Dungeon.level, fire.pos, 0 );
                    Notes.remove( Notes.Landmark.SACRIFICIAL_FIRE );

                    GLog.w( TXT_REWARD );
                    GameScene.effect( new Flare( 7, 32 ).color( 0x66FFFF, true ).show( ch.sprite.parent, DungeonTilemap.tileCenterToWorld( fire.pos ), 2f ) );
                    Dungeon.level.drop( new Ankh(), fire.pos ).sprite.drop();
                }
            } else {

                GLog.w( TXT_UNWORTHY );

            }
        }
    }

    @Override
    public String tileDesc() {
        return "Sacrificial fire burns here. Every creature touched by this fire is marked as an offering for the spirits of the dungeon.";
    }

    public static class Marked extends FlavourBuff {

        public static final float DURATION	= 5f;

        @Override
        public int icon() {
            return BuffIndicator.SACRIFICE;
        }

        @Override
        public String toString() {
            return "Marked for sacrifice";
        }

        @Override
        public void detach() {
            if (!target.isAlive()) {
                sacrifice( target );
            }
            super.detach();
        }
    }

}
