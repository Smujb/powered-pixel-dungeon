package com.shatteredpixel.yasd.actors.mobs;

import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.mechanics.Ballistica;
import com.watabou.utils.Callback;

public abstract class RangedMob extends Mob implements Callback {

    public boolean magical = true;

    public boolean canHit(Char enemy) {
        return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    public abstract boolean fleesAtMelee();

    public static class MagicalDamage{}

    public MagicalDamage magicalSrc() {
        return new MagicalDamage();
    }

    @Override
    public boolean canAttack(Char enemy) {
        if (fleesAtMelee() & Dungeon.level.adjacent(enemy.pos, pos)) {
            return false;
        } else {
            return canHit(enemy);
        }
    }

    protected boolean doAttack(Char enemy ) {

        if (Dungeon.level.adjacent( pos, enemy.pos ) || !magical) {

            return super.doAttack( enemy );

        } else {
            return doMagicAttack( enemy );
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
        if (magical) {
            onZapComplete();
        } else {
            onAttackComplete();
        }
    }
}
