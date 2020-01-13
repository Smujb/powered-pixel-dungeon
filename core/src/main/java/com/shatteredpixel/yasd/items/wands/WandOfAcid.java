package com.shatteredpixel.yasd.items.wands;

import com.shatteredpixel.yasd.actors.Actor;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.blobs.AcidPool;
import com.shatteredpixel.yasd.actors.buffs.Buff;
import com.shatteredpixel.yasd.actors.buffs.Corrosion;
import com.shatteredpixel.yasd.actors.buffs.Ooze;
import com.shatteredpixel.yasd.effects.Speck;
import com.shatteredpixel.yasd.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.mechanics.Ballistica;
import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.scenes.GameScene;
import com.shatteredpixel.yasd.sprites.ItemSpriteSheet;

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

            ch.sprite.emitter().burst( Speck.factory(Speck.BUBBLE), 3 );

            Buff.affect(ch, Ooze.class).set( 20f );

        } else {
            GameScene.add( seed( pos, 1, AcidPool.class ).setStrength((int)(damageRoll()*1.5f)));
        }


    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        Buff.affect(defender, Corrosion.class).set(2f, 1 + staff.level()/2);
    }

    @Override
    protected int initialCharges() {
        return 4;
    }
}
