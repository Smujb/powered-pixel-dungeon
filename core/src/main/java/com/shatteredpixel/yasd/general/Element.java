package com.shatteredpixel.yasd.general;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Burning;
import com.shatteredpixel.yasd.general.actors.buffs.Ooze;
import com.shatteredpixel.yasd.general.actors.buffs.Weakness;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.hero.HeroSubClass;
import com.shatteredpixel.yasd.general.effects.Beam;
import com.shatteredpixel.yasd.general.effects.Lightning;
import com.shatteredpixel.yasd.general.effects.MagicMissile;
import com.shatteredpixel.yasd.general.items.weapon.melee.Blunt;
import com.shatteredpixel.yasd.general.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.yasd.general.items.weapon.missiles.ThrowingKnife;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.MissileSprite;
import com.shatteredpixel.yasd.general.tiles.DungeonTilemap;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public enum Element {
	/*
	The purpose of this file is to make it easier to add more types of ranged attacks for mobs.
	 */
	PHYSICAL( false ),
	RANGED( false ),
	IGNORE( true ),
	MAGICAL( true ),
	DESTRUCTION( true ),
	FIRE( true),
	WATER( true ),
	EARTH( false ),
	GRASS( false ),
	AIR( true ),
	ACID( true ),
	ELECTRIC( true ),
	HOLY( true ),
	DARK( true );


	public int attackProc(int damage, Char attacker, Char defender) {
		switch (this) {
			case PHYSICAL:
				break;
			case FIRE:
				Buff.affect(defender, Burning.class).reignite(defender);
				break;
			case ACID:
				Buff.affect(defender, Ooze.class).set(20f);
				break;
			case DARK:
				Buff.affect(defender, Weakness.class, Weakness.DURATION);
				break;
		}
		int dr = defender.drRoll(this);
		if(!magical) {
			if (attacker instanceof Hero) {//Missile Weapons are always equipped in slot 1
				Hero h = (Hero) attacker;
				if (h.belongings.miscs[0] instanceof MissileWeapon
						&& h.subClass == HeroSubClass.SNIPER) {
					dr = 0;
				}
			}

			if (attacker.hasBelongings() && attacker.belongings.getCurrentWeapon() instanceof Blunt) {
				dr = 0;
			}
		}

		if (attacker.alignment == Char.Alignment.ENEMY) {
			switch (Dungeon.difficulty) {
				case 1://Easy = -25% damage
					damage *= 0.75f;
					break;
				case 2: default://Medium = normal damage
					break;
				case 3://Hard = +25% damage
					damage *= 1.25f;
					break;
			}
		}
		damage = attacker.attackProc(defender, damage);

		if (Dungeon.hero.fieldOfView[defender.pos] || Dungeon.hero.fieldOfView[attacker.pos]) {
			Sample.INSTANCE.play(Assets.SND_HIT, 1, 1, Random.Float(0.8f, 1.25f));
		}
		return damage;
	}

	public int defenseProc(int damage, Char attacker, Char defender) {
		damage = defender.defenseProc(attacker, damage, Element.PHYSICAL);
		return damage;
	}

	public int affectDamage(Char ch, int damage) {
		if (this != IGNORE) {//This is used to ignore DR roll in the case of damage such as from fire or bleeding
			damage = Math.max(damage - ch.drRoll(this), 0);
		}
		return damage;
	}
	public void FX(Char ch, int cell) {
		Callback attack = new Callback() {
			@Override
			public void call() {
				ch.sprite.idle();
				ch.onAttackComplete();
			}
		};
		FX(ch, cell, attack);
	}


	public void FX(Char ch, int cell, Callback attack) {

		switch (this) {
			case PHYSICAL:
				attack.call();
			case RANGED:
				((MissileSprite)ch.sprite.parent.recycle( MissileSprite.class )).
						reset( ch.pos, cell, new ThrowingKnife(), attack );
				break;
			case IGNORE:
			case MAGICAL:
				break;
			case DESTRUCTION:
				ch.sprite.parent.add(
						new Beam.DeathRay(ch.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(cell)));
				attack.call();
				break;
			case FIRE:
				MagicMissile.boltFromChar( ch.sprite.parent,
						MagicMissile.FIRE,
						ch.sprite,
						cell,
						attack);
				break;
			case WATER:
				MagicMissile.boltFromChar( ch.sprite.parent,
						MagicMissile.WATER_CONE,
						ch.sprite,
						cell,
						attack);
				break;
			case EARTH:
				MagicMissile.boltFromChar( ch.sprite.parent,
						MagicMissile.EARTH,
						ch.sprite,
						cell,
						attack);
				break;
			case GRASS:
				MagicMissile.boltFromChar( ch.sprite.parent,
						MagicMissile.FOLIAGE_CONE,
						ch.sprite,
						cell,
						attack);
				break;
			case AIR: case HOLY:
				ch.sprite.parent.add(
						new Beam.LightRay(ch.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(cell)));
				attack.call();
				break;
			case ACID:
				MagicMissile.boltFromChar( ch.sprite.parent,
						MagicMissile.CORROSION,
						ch.sprite,
						cell,
						attack);
				break;
			case ELECTRIC:
				ch.sprite.parent.add(
						new Lightning(ch.pos, DungeonTilemap.raisedTileCenterToWorld(cell), null));//No callback as damaging after lightning anim finishes looks messy
				attack.call();
				break;
			case DARK:
				MagicMissile.boltFromChar( ch.sprite.parent,
						MagicMissile.SHADOW,
						ch.sprite,
						cell,
						attack);
				break;
		}
	}

	Element(boolean magical) {
		this.magical = magical;
	}

	private boolean magical;

	public boolean isMagical() {
		return magical;
	}

	public String label() {
		return Messages.get(this, this.name());
	}
}
