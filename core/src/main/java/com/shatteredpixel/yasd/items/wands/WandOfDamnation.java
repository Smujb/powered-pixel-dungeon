package com.shatteredpixel.yasd.items.wands;


import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.Actor;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.blobs.Blob;
import com.shatteredpixel.yasd.actors.blobs.CorrosiveGas;
import com.shatteredpixel.yasd.actors.blobs.Miasma;
import com.shatteredpixel.yasd.actors.buffs.Buff;
import com.shatteredpixel.yasd.actors.buffs.Ooze;
import com.shatteredpixel.yasd.actors.buffs.Weakness;
import com.shatteredpixel.yasd.effects.CellEmitter;
import com.shatteredpixel.yasd.effects.Splash;
import com.shatteredpixel.yasd.effects.particles.ShadowParticle;
import com.shatteredpixel.yasd.items.weapon.enchantments.Grim;
import com.shatteredpixel.yasd.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.mechanics.Ballistica;
import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.scenes.GameScene;
import com.shatteredpixel.yasd.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class WandOfDamnation extends Wand {

    {
        image = ItemSpriteSheet.WAND_DAMNATION;
    }

    @Override
    protected void onZap(Ballistica attack) {
        Char ch = Actor.findChar(attack.collisionPos);
        if (ch != null) {
            ch.damage(ch.HP, Grim.class);
            GameScene.add(Blob.seed(attack.collisionPos, 50 + 10 * level(), Miasma.class));
        }
    }

    protected int chargesPerCast() {
        return Math.max(1, curCharges);
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

    @Override
    protected int initialCharges() {
        return 4;
    }
}
