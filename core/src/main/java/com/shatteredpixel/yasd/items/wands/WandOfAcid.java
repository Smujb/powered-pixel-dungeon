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
    public int min(int lvl) {
        return 2 + lvl;
    }

    @Override
    public int max(int lvl) {
        return 8 + lvl * 7;
    }

    @Override
    protected void onZap(Ballistica attack) {
        Char ch = Actor.findChar( attack.collisionPos );
        int pos = attack.collisionPos;
        if (ch != null) {

            processSoulMark(ch, chargesPerCast());
            ch.damage(damageRoll(), this);

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
    public String statsDesc() {
        if (levelKnown)
            return Messages.get(this, "stats_desc",  min(), max());
        else
            return Messages.get(this, "stats_desc",  min(0), max(0));
    }
}
