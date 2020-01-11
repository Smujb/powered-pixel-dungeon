package com.shatteredpixel.yasd.actors.mobs;

import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.buffs.Buff;
import com.shatteredpixel.yasd.actors.buffs.MagicalSleep;
import com.shatteredpixel.yasd.mechanics.Ballistica;
import com.shatteredpixel.yasd.sprites.WarlockSprite;
import com.watabou.utils.Random;

public class PlaceHolderRanged extends RangedMob {

    {
        spriteClass = WarlockSprite.class;
        HP = HT = 8;
        defenseSkill = 2;

        maxLvl = 5;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 1, 4 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 8;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 1);
    }

    @Override
    public boolean fleesAtMelee() {
        return true;
    }

    @Override
    public boolean canHit(Char enemy) {
        return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    @Override
    public int magicalDamageRoll() {
        return Random.Int( 1, 4 );
    }

    @Override
    public int magicalAttackProc(Char enemy, int damage) {
        if (Random.Int( 2 ) == 0) {
            Buff.affect( enemy, MagicalSleep.class );
        }
        return super.magicalAttackProc(enemy, damage);
    }

}
