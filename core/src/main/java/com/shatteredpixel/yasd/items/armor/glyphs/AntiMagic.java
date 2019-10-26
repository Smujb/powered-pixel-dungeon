/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package com.shatteredpixel.yasd.items.armor.glyphs;

import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.buffs.Charm;
import com.shatteredpixel.yasd.actors.buffs.Weakness;
import com.shatteredpixel.yasd.actors.mobs.Eye;
import com.shatteredpixel.yasd.actors.mobs.Shaman;
import com.shatteredpixel.yasd.actors.mobs.Warlock;
import com.shatteredpixel.yasd.actors.mobs.Yog;
import com.shatteredpixel.yasd.items.armor.Armor;
import com.shatteredpixel.yasd.items.wands.WandOfBlastWave;
import com.shatteredpixel.yasd.items.wands.WandOfDisintegration;
import com.shatteredpixel.yasd.items.wands.WandOfFireblast;
import com.shatteredpixel.yasd.items.wands.WandOfFrost;
import com.shatteredpixel.yasd.items.wands.WandOfLightning;
import com.shatteredpixel.yasd.items.wands.WandOfLivingEarth;
import com.shatteredpixel.yasd.items.wands.WandOfMagicMissile;
import com.shatteredpixel.yasd.items.wands.WandOfPrismaticLight;
import com.shatteredpixel.yasd.items.wands.WandOfTransfusion;
import com.shatteredpixel.yasd.items.wands.WandOfWarding;
import com.shatteredpixel.yasd.levels.traps.DisintegrationTrap;
import com.shatteredpixel.yasd.levels.traps.GrimTrap;
import com.shatteredpixel.yasd.sprites.ItemSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class AntiMagic extends Armor.Glyph {

	private static ItemSprite.Glowing TEAL = new ItemSprite.Glowing( 0x88EEFF );
	
	public static final HashSet<Class> RESISTS = new HashSet<>();
	static {
		RESISTS.add( Charm.class );
		RESISTS.add( Weakness.class );
		
		RESISTS.add( DisintegrationTrap.class );
		RESISTS.add( GrimTrap.class );

		RESISTS.add( WandOfBlastWave.class );
		RESISTS.add( WandOfDisintegration.class );
		RESISTS.add( WandOfFireblast.class );
		RESISTS.add( WandOfFrost.class );
		RESISTS.add( WandOfLightning.class );
		RESISTS.add( WandOfLivingEarth.class );
		RESISTS.add( WandOfMagicMissile.class );
		RESISTS.add( WandOfPrismaticLight.class );
		RESISTS.add( WandOfTransfusion.class );
		RESISTS.add( WandOfWarding.Ward.class );
		
		RESISTS.add( Shaman.LightningBolt.class );
		RESISTS.add( Warlock.DarkBolt.class );
		RESISTS.add( Eye.DeathGaze.class );
		RESISTS.add( Yog.BurningFist.DarkBolt.class );
	}
	
	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {
		//no proc effect, see Hero.damage
		return damage;
	}
	
	public static int drRoll( int level ){
		return Random.NormalIntRange(level, 4 + (level*2));
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return TEAL;
	}

}