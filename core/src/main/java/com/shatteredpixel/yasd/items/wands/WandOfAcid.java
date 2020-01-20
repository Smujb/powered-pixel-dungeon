package com.shatteredpixel.yasd.items.wands;

import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.Actor;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.blobs.AcidPool;
import com.shatteredpixel.yasd.actors.buffs.Buff;
import com.shatteredpixel.yasd.actors.buffs.Corrosion;
import com.shatteredpixel.yasd.actors.buffs.Ooze;
import com.shatteredpixel.yasd.actors.hero.Hero;
import com.shatteredpixel.yasd.effects.CellEmitter;
import com.shatteredpixel.yasd.effects.Speck;
import com.shatteredpixel.yasd.effects.particles.LeafParticle;
import com.shatteredpixel.yasd.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.mechanics.Ballistica;
import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.scenes.GameScene;
import com.shatteredpixel.yasd.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import static com.shatteredpixel.yasd.actors.blobs.Blob.*;

public class WandOfAcid extends DamageWand {
    {
        image = ItemSpriteSheet.WAND_ACID;
        collisionProperties = Ballistica.STOP_TARGET | Ballistica.STOP_TERRAIN;
    }
    @Override
    public float min(float lvl) {
        return 2 + lvl;
    }

    @Override
    public float max(float lvl) {
        return 8 + lvl * 4;
    }

    @Override
    public void onZap(Ballistica attack) {
        Char ch = Actor.findChar( attack.collisionPos );
        int pos = attack.collisionPos;
        if (ch != null) {

            hit(ch);

            ch.sprite.emitter().burst( Speck.factory(Speck.BUBBLE_GREEN), 3 );

            Buff.affect(ch, Ooze.class).set( 20f );

        } else {
            GameScene.add( seed( pos, 1, AcidPool.class ).setStrength((int)(damageRoll()*1.5f)));
        }
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        Buff.affect(defender, Corrosion.class).set(2f, 1 + staff.level()/2);
    }

    public static class AcidPuddle implements Bundlable {

        public int pos;
        private int damageOnTrigger;

        public void trigger(){

            Char ch = Actor.findChar(pos);

            if (ch instanceof Hero){
                ((Hero) ch).interrupt();
            }

            dispel();
            activate( ch );
        }

        public AcidPuddle setDamageOnTrigger(int damage) {
            damageOnTrigger = damage;
            return this;
        }

        public void activate( Char ch ) {
            if (ch != null) {
                ch.damage( damageOnTrigger, this );
                Buff.affect( ch, Ooze.class ).set( 20f );
            }
        }

        public void dispel() {
            //Dungeon.level.uproot( pos );

            if (Dungeon.level.heroFOV[pos]) {
                CellEmitter.get( pos ).burst( Speck.factory(Speck.BUBBLE_GREEN), 6 );
            }

        }

        private static final String POS	= "pos";
        private static final String DAMAGE = "damage";

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            damageOnTrigger = bundle.getInt( DAMAGE );
            pos = bundle.getInt( POS );
        }

        @Override
        public void storeInBundle( Bundle bundle ) {
            bundle.put( DAMAGE, damageOnTrigger );
            bundle.put( POS, pos );
        }

        public String desc() {
            return Messages.get(this, "desc");
        }
    }

    @Override
    protected int initialCharges() {
        return 4;
    }
}
