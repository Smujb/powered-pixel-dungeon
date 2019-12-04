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
import com.shatteredpixel.yasd.items.weapon.enchantments.Vampiric;
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
        image = ItemSpriteSheet.WAND_LIFE_DRAIN;
	}

    @Override
    //consumes all available charges, needs at least one.
    protected int chargesPerCast() {
        return Math.max(1, curCharges);
    }


    @Override
    public void onZap(Ballistica bolt) {

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
        RedLightning lightning = new RedLightning(Dungeon.hero.pos, cell, null);
        //lightning.setColour(0x66002);
        curUser.sprite.parent.addToFront( lightning  );
        Sample.INSTANCE.play( Assets.SND_LIGHTNING );
        callback.call();
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        new Vampiric().proc(staff, attacker, defender, damage);
    }

    @Override
    protected int initialCharges() {
        return 4;
    }

    @Override
    public String statsDesc() {
        if (levelKnown)
            return Messages.get(this, "stats_desc", maxCharges, min(), max());
        else
            return Messages.get(this, "stats_desc", chargesPerCast(), min(0), max(0));
    }

    public int min(int lvl){
        return (1+lvl) * chargesPerCast();
    }

    public float max(float lvl){
        return (6+2*lvl) * chargesPerCast();
    }
}
