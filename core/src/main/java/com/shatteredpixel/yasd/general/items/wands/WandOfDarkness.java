package com.shatteredpixel.yasd.general.items.wands;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.blobs.Blob;
import com.shatteredpixel.yasd.general.actors.blobs.DarkGas;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Terror;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.MagicMissile;
import com.shatteredpixel.yasd.general.effects.particles.SmokeParticle;
import com.shatteredpixel.yasd.general.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class WandOfDarkness extends Wand {

    {
        image = ItemSpriteSheet.WAND_STENCH;

        collisionProperties = Ballistica.STOP_TARGET | Ballistica.STOP_TERRAIN;
    }

    @Override
    public void onZap(Ballistica bolt) {
        float level = actualLevel();
        int pos = bolt.collisionPos;
        for( int i : PathFinder.NEIGHBOURS9) {
            if (!Dungeon.level.solid[pos + i]) {
                DarkGas gas = Blob.seed(pos + i, (int) (30 + 5 * level), DarkGas.class);
                CellEmitter.center(bolt.collisionPos).burst(SmokeParticle.SPEW, 10 );
                gas.setStrength(2 + (int)level);
                gas.setOwner(curUser);
                GameScene.add(gas);
            }
        }


        for (int i : PathFinder.NEIGHBOURS9) {
            Char ch = Actor.findChar(bolt.collisionPos + i);
            if (ch != null) {
                processSoulMark(ch, chargesPerCast());
            }
        }

        if (Actor.findChar(bolt.collisionPos) == null){
            Dungeon.level.pressCell(bolt.collisionPos);
        }
    }

    @Override
    protected void fx(Ballistica bolt, Callback callback) {
        MagicMissile.boltFromChar(
                curUser.sprite.parent,
                MagicMissile.DARK,
                curUser.sprite,
                bolt.collisionPos,
                callback);
        Sample.INSTANCE.play(Assets.SND_ZAP);
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        // lvl 0 - 33%
        // lvl 1 - 50%
        // lvl 2 - 60%
        if (Random.Int( level() + 3 ) >= 2) {
            Buff.affect( defender, Terror.class, 3 + staff.level() * 2);
        }
    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color( ColorMath.random( 0x000000, 0x382d2d) );
        particle.am = 0.6f;
        particle.setLifespan( 1f );
        particle.acc.set(0, 20);
        particle.setSize( 0.5f, 3f );
        particle.shuffleXY( 1f );
    }

}