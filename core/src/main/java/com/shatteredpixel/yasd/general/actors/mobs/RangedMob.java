package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.watabou.utils.Callback;

public abstract class RangedMob extends Mob implements Callback {

    public boolean canHit(Char enemy) {
        return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    public boolean fleesAtMelee() {
        return false;
    }

    public static class MagicalDamage{}

    public MagicalDamage magicalSrc() {
        return new  MagicalDamage();
    }

    @Override
    public boolean canAttack(Char enemy) {
        if (fleesAtMelee() & Dungeon.level.adjacent(enemy.pos, pos)) {
            return false;
        } else {
            return canHit(enemy);
        }
    }

    @Override
    protected boolean getCloser( int target ) {
        if (state == HUNTING && fleesAtMelee()) {
            return enemySeen && getFurther( target );
        } else {
            return super.getCloser( target );
        }
    }

    @Override
    public void call() {
        onAttackComplete();
    }
}
