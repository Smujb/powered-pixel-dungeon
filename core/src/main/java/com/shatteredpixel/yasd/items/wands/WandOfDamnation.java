package com.shatteredpixel.yasd.items.wands;


import com.shatteredpixel.yasd.Assets;
import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.Actor;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.blobs.Blob;
import com.shatteredpixel.yasd.actors.blobs.CorrosiveGas;
import com.shatteredpixel.yasd.actors.blobs.Miasma;
import com.shatteredpixel.yasd.actors.buffs.Buff;
import com.shatteredpixel.yasd.actors.buffs.DeferredDeath;
import com.shatteredpixel.yasd.actors.buffs.Ooze;
import com.shatteredpixel.yasd.actors.buffs.Weakness;
import com.shatteredpixel.yasd.actors.mobs.Bee;
import com.shatteredpixel.yasd.actors.mobs.King;
import com.shatteredpixel.yasd.actors.mobs.Mimic;
import com.shatteredpixel.yasd.actors.mobs.Mob;
import com.shatteredpixel.yasd.actors.mobs.Piranha;
import com.shatteredpixel.yasd.actors.mobs.Statue;
import com.shatteredpixel.yasd.actors.mobs.Swarm;
import com.shatteredpixel.yasd.actors.mobs.Wraith;
import com.shatteredpixel.yasd.actors.mobs.Yog;
import com.shatteredpixel.yasd.effects.CellEmitter;
import com.shatteredpixel.yasd.effects.MagicMissile;
import com.shatteredpixel.yasd.effects.Splash;
import com.shatteredpixel.yasd.effects.particles.ShadowParticle;
import com.shatteredpixel.yasd.items.weapon.enchantments.Grim;
import com.shatteredpixel.yasd.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.mechanics.Ballistica;
import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.scenes.GameScene;
import com.shatteredpixel.yasd.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WandOfDamnation extends Wand {

    {
        image = ItemSpriteSheet.WAND_DAMNATION;
    }

    @Override
    public void onZap(Ballistica attack) {
        Char ch = Actor.findChar(attack.collisionPos);
        Mob enemy;
        if (ch instanceof Mob) {
            enemy = ((Mob)ch);

            float corruptingPower = 3 + level()*2;

            //base enemy resistance is usually based on their exp, but in special cases it is based on other criteria
            float enemyResist = 1 + enemy.EXP;
            if (ch instanceof Mimic || ch instanceof Statue) {
                enemyResist = 1 + Dungeon.depth;
            } else if (ch instanceof Piranha || ch instanceof Bee) {
                enemyResist = 1 + Dungeon.depth / 2f;
            } else if (ch instanceof Wraith) {
                //divide by 3 as wraiths are always at full HP and are therefore ~3x harder to corrupt
                enemyResist = (1f + Dungeon.depth / 3f) / 3f;
            } else if (ch instanceof Yog.BurningFist || ch instanceof Yog.RottingFist) {
                enemyResist = 1 + 30;
            } else if (ch instanceof Yog.Larva || ch instanceof King.Undead) {
                enemyResist = 1 + 5;
            } else if (ch instanceof Swarm) {
                //child swarms don't give exp, so we force this here.
                enemyResist = 1 + 3;
            }

            //100% health: 3x resist   75%: 2.1x resist   50%: 1.5x resist   25%: 1.1x resist
            enemyResist *= 1 + 2 * Math.pow(enemy.HP / (float) enemy.HT, 2);


            if (ch != null) {
                Buff.affect(ch, DeferredDeath.class, enemyResist/corruptingPower*((float)maxCharges/(float)curCharges));
            }
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
    @Override
    protected void fx(Ballistica bolt, Callback callback) {
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.SHADOW,
                curUser.sprite,
                bolt.collisionPos,
                callback);
        Sample.INSTANCE.play( Assets.SND_ZAP );
    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color( 0 );
        particle.am = 0.6f;
        particle.setLifespan(2f);
        particle.speed.set(0, 5);
        particle.setSize( 0.5f, 2f);
        particle.shuffleXY(1f);
    }
}
