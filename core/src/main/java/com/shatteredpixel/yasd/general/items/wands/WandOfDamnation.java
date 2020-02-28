/*
 *
 *  * Pixel Dungeon
 *  * Copyright (C) 2012-2015 Oleg Dolya
 *  *
 *  * Shattered Pixel Dungeon
 *  * Copyright (C) 2014-2019 Evan Debenham
 *  *
 *  * Yet Another Shattered Dungeon
 *  * Copyright (C) 2014-2020 Samuel Braithwaite
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.general.items.wands;


import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.DeferredDeath;
import com.shatteredpixel.yasd.general.actors.mobs.Bee;
import com.shatteredpixel.yasd.general.actors.mobs.King;
import com.shatteredpixel.yasd.general.actors.mobs.Mimic;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.actors.mobs.Piranha;
import com.shatteredpixel.yasd.general.actors.mobs.Statue;
import com.shatteredpixel.yasd.general.actors.mobs.Swarm;
import com.shatteredpixel.yasd.general.actors.mobs.Wraith;
import com.shatteredpixel.yasd.general.actors.mobs.Yog;
import com.shatteredpixel.yasd.general.effects.MagicMissile;
import com.shatteredpixel.yasd.general.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
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

            float corruptingPower = 3 + actualLevel();

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
        DeferredDeath buff = ch.buff(DeferredDeath.class);
        if (buff != null) {
            buff.recover(Random.Int(staff.level()*2+2));
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
