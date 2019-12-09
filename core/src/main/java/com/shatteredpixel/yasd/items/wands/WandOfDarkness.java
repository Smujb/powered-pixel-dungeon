package com.shatteredpixel.yasd.items.wands;

import com.shatteredpixel.yasd.Assets;
import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.Actor;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.blobs.Blob;
import com.shatteredpixel.yasd.actors.blobs.DarkGas;
import com.shatteredpixel.yasd.actors.buffs.Buff;
import com.shatteredpixel.yasd.actors.buffs.Ooze;
import com.shatteredpixel.yasd.effects.CellEmitter;
import com.shatteredpixel.yasd.effects.MagicMissile;
import com.shatteredpixel.yasd.effects.particles.CorrosionParticle;
import com.shatteredpixel.yasd.effects.particles.SmokeParticle;
import com.shatteredpixel.yasd.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.mechanics.Ballistica;
import com.shatteredpixel.yasd.scenes.GameScene;
import com.shatteredpixel.yasd.sprites.ItemSpriteSheet;
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
        DarkGas gas = Blob.seed(bolt.collisionPos, (int) (100 + 50 * level), DarkGas.class);
        CellEmitter.center(bolt.collisionPos).burst(SmokeParticle.SPEW, 10 );
        gas.setStrength(2 + (int)level);
        gas.setOwner(curUser);
        GameScene.add(gas);

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

            Buff.affect( defender, Ooze.class ).set( 20f );
            CellEmitter.center(defender.pos).burst( CorrosionParticle.SPLASH, 5 );

        }
    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color( ColorMath.random( 0xAAAAAA, 0xFF8800) );
        particle.am = 0.6f;
        particle.setLifespan( 1f );
        particle.acc.set(0, 20);
        particle.setSize( 0.5f, 3f );
        particle.shuffleXY( 1f );
    }

}