/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Yet Another Pixel Dungeon
 * Copyright (C) 2015-2016 Considered Hamster
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.shatteredpixel.yasd.items.wands;

import com.shatteredpixel.yasd.Assets;
import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.Actor;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.hero.Hero;
import com.shatteredpixel.yasd.effects.CellEmitter;
import com.shatteredpixel.yasd.effects.Flare;
import com.shatteredpixel.yasd.effects.Lightning;
import com.shatteredpixel.yasd.effects.RedLightning;
import com.shatteredpixel.yasd.effects.Speck;
import com.shatteredpixel.yasd.effects.SpellSprite;
import com.shatteredpixel.yasd.effects.particles.SparkParticle;
import com.shatteredpixel.yasd.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.mechanics.Ballistica;
import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.sprites.CharSprite;
import com.shatteredpixel.yasd.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.tiles.DungeonTilemap;
import com.shatteredpixel.yasd.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WandOfLifeDrain extends DamageWand {

	{
		name = "Wand of Life Drain";
        image = ItemSpriteSheet.WAND_DISINTEGRATION;
	}

    @Override
    //consumes all available charges, needs at least one.
    protected int chargesPerCast() {
        return Math.max(1, curCharges);
    }


    @Override
    protected void onZap( Ballistica bolt ) {

        Char ch = Actor.findChar( bolt.collisionPos );
        if (ch != null) {

            processSoulMark(ch, chargesPerCast());
            int damage = damageRoll();
            ch.damage(damage, this);
            int healAmt = damage/2;
            if (!ch.properties().contains(Char.Property.UNDEAD) & curUser instanceof Hero) {
                curUser.HP += Math.min(curUser.HT - curUser.HP, healAmt);//Heal the hero
                curUser.sprite.emitter().start( Speck.factory( Speck.HEALING ), 0.4f, 1 );
                curUser.sprite.showStatus( CharSprite.POSITIVE, Integer.toString( healAmt ) );
            }
            ch.sprite.burst(0xFFFFFFFF, level() / 2 + 2);

        } else {
            Dungeon.level.pressCell(bolt.collisionPos);
        }
    }

    @Override
    protected void fx( Ballistica bolt, Callback callback ) {

        int cell = bolt.collisionPos;

        Char ch = Actor.findChar( cell );
        if (ch != null) {
        } else {
            CellEmitter.center( cell ).burst( SparkParticle.FACTORY, 3 );
        }

        //don't want to wait for the effect before processing damage.
        curUser.sprite.parent.addToFront( new RedLightning(Dungeon.hero.pos, cell, null) );
        Sample.INSTANCE.play( Assets.SND_LIGHTNING );
        callback.call();
    }

	@Override
	public String desc() {
		return
			"This wand will allow you to steal life energy from living creatures to restore your " +
            "own health. Using it against non-living creatures will just harm them, but it is " +
            "especially effective against targets which are sleeping or otherwise unaware of danger.";
	}


    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {

    }

    @Override
    protected int initialCharges() {
        return 4;
    }

    @Override
    public int min(int lvl) {
        return 2*chargesPerCast();
    }

    @Override
    public int max(int lvl) {
        return chargesPerCast();
    }
}
