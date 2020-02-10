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

package com.shatteredpixel.yasd.actors;

import com.shatteredpixel.yasd.Assets;
import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.blobs.Blob;
import com.shatteredpixel.yasd.actors.blobs.Electricity;
import com.shatteredpixel.yasd.actors.blobs.ToxicGas;
import com.shatteredpixel.yasd.actors.buffs.Adrenaline;
import com.shatteredpixel.yasd.actors.buffs.AdrenalineSurge;
import com.shatteredpixel.yasd.actors.buffs.Aggression;
import com.shatteredpixel.yasd.actors.buffs.ArcaneArmor;
import com.shatteredpixel.yasd.actors.buffs.Barkskin;
import com.shatteredpixel.yasd.actors.buffs.Bleeding;
import com.shatteredpixel.yasd.actors.buffs.Bless;
import com.shatteredpixel.yasd.actors.buffs.Buff;
import com.shatteredpixel.yasd.actors.buffs.Burning;
import com.shatteredpixel.yasd.actors.buffs.Charm;
import com.shatteredpixel.yasd.actors.buffs.Chill;
import com.shatteredpixel.yasd.actors.buffs.Corrosion;
import com.shatteredpixel.yasd.actors.buffs.Corruption;
import com.shatteredpixel.yasd.actors.buffs.Cripple;
import com.shatteredpixel.yasd.actors.buffs.Doom;
import com.shatteredpixel.yasd.actors.buffs.Drowsy;
import com.shatteredpixel.yasd.actors.buffs.Drunk;
import com.shatteredpixel.yasd.actors.buffs.EarthImbue;
import com.shatteredpixel.yasd.actors.buffs.FireImbue;
import com.shatteredpixel.yasd.actors.buffs.Frost;
import com.shatteredpixel.yasd.actors.buffs.FrostImbue;
import com.shatteredpixel.yasd.actors.buffs.Haste;
import com.shatteredpixel.yasd.actors.buffs.Hunger;
import com.shatteredpixel.yasd.actors.buffs.Invisibility;
import com.shatteredpixel.yasd.actors.buffs.Levitation;
import com.shatteredpixel.yasd.actors.buffs.MagicalSleep;
import com.shatteredpixel.yasd.actors.buffs.Momentum;
import com.shatteredpixel.yasd.actors.buffs.Ooze;
import com.shatteredpixel.yasd.actors.buffs.Paralysis;
import com.shatteredpixel.yasd.actors.buffs.Poison;
import com.shatteredpixel.yasd.actors.buffs.Preparation;
import com.shatteredpixel.yasd.actors.buffs.ShieldBuff;
import com.shatteredpixel.yasd.actors.buffs.Slow;
import com.shatteredpixel.yasd.actors.buffs.Speed;
import com.shatteredpixel.yasd.actors.buffs.Stamina;
import com.shatteredpixel.yasd.actors.buffs.Terror;
import com.shatteredpixel.yasd.actors.buffs.Vertigo;
import com.shatteredpixel.yasd.actors.buffs.Weakness;
import com.shatteredpixel.yasd.actors.buffs.Wet;
import com.shatteredpixel.yasd.actors.hero.Belongings;
import com.shatteredpixel.yasd.actors.hero.Hero;
import com.shatteredpixel.yasd.actors.hero.HeroSubClass;
import com.shatteredpixel.yasd.actors.mobs.RangedMob;
import com.shatteredpixel.yasd.items.KindOfWeapon;
import com.shatteredpixel.yasd.items.armor.Armor;
import com.shatteredpixel.yasd.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.yasd.items.armor.glyphs.Potential;
import com.shatteredpixel.yasd.items.artifacts.CapeOfThorns;
import com.shatteredpixel.yasd.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.yasd.items.potions.elixirs.ElixirOfMight;
import com.shatteredpixel.yasd.items.rings.RingOfElements;
import com.shatteredpixel.yasd.items.rings.RingOfEvasion;
import com.shatteredpixel.yasd.items.rings.RingOfFuror;
import com.shatteredpixel.yasd.items.rings.RingOfHaste;
import com.shatteredpixel.yasd.items.rings.RingOfMight;
import com.shatteredpixel.yasd.items.rings.RingOfTenacity;
import com.shatteredpixel.yasd.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.yasd.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.yasd.items.wands.WandOfFireblast;
import com.shatteredpixel.yasd.items.wands.WandOfLightning;
import com.shatteredpixel.yasd.items.wands.WandOfLivingEarth;
import com.shatteredpixel.yasd.items.weapon.Weapon;
import com.shatteredpixel.yasd.items.weapon.enchantments.Blazing;
import com.shatteredpixel.yasd.items.weapon.enchantments.Blocking;
import com.shatteredpixel.yasd.items.weapon.enchantments.Grim;
import com.shatteredpixel.yasd.items.weapon.enchantments.Shocking;
import com.shatteredpixel.yasd.items.weapon.melee.Blunt;
import com.shatteredpixel.yasd.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.yasd.items.weapon.missiles.darts.ShockingDart;
import com.shatteredpixel.yasd.levels.Terrain;
import com.shatteredpixel.yasd.levels.features.Chasm;
import com.shatteredpixel.yasd.levels.features.Door;
import com.shatteredpixel.yasd.levels.traps.GrimTrap;
import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.plants.Earthroot;
import com.shatteredpixel.yasd.sprites.CharSprite;
import com.shatteredpixel.yasd.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public abstract class Char extends Actor {

	public int pos = 0;

	public CharSprite sprite;

	public String name;
	public int defenseSkill = 0;
	public int attackSkill = 0;

	public Belongings belongings = null;
	public int STR;
	//public boolean hasBelongings = false;


	public int HT;
	public int HP;

	protected float baseSpeed = 1;
	protected PathFinder.Path path;

	public int paralysed = 0;
	public boolean rooted = false;
	public boolean flying = false;
	public int invisible = 0;

	public float DLY = 1f;
	public float ACC = 1f;
	public float EVA = 1f;
	public float STE = 1f;

	//these are relative to the hero
	public enum Alignment {
		ENEMY,
		NEUTRAL,
		ALLY
	}

	public Alignment alignment;

	public int viewDistance = 8;

	public boolean[] fieldOfView = null;

	private HashSet<Buff> buffs = new HashSet<>();

	public boolean isFlying() {
		return ((flying || buff(Levitation.class) != null)
				& buff(Paralysis.class) == null
				& buff(Vertigo.class) == null
				& buff(Frost.class) == null);
	}

	public boolean shoot(Char enemy, MissileWeapon wep) {
		return belongings.shoot(enemy, wep);
	}

	public int missingHP() {
		return HT - HP;
	}

	public float hpPercent() {
		return HP / (float) HT;
	}

	public float missingHPPercent() {
		return 1f - hpPercent();
	}

	public boolean hasBelongings() {
		return belongings != null;
	}

	public int STR() {
		int STR = this.STR;

		STR += RingOfMight.strengthBonus(this);

		AdrenalineSurge buff = buff(AdrenalineSurge.class);
		if (buff != null) {
			STR += buff.boost();
		}

		return (buff(Weakness.class) != null) ? STR - 1 : STR;
	}

	public boolean canAttack(Char enemy) {
		if (enemy == null || pos == enemy.pos) {
			return false;
		}

		//can always attack adjacent enemies
		if (Dungeon.level.adjacent(pos, enemy.pos)) {
			return true;
		}

		if (hasBelongings()) {
			KindOfWeapon wep = belongings.getCurrentWeapon();
			if (wep != null) {
				return wep.canReach(this, enemy.pos);
			}
		}
		return false;
	}

	public void updateHT(boolean boostHP) {
		int curHT = HT;

		float multiplier = RingOfMight.HTMultiplier(this);
		HT = Math.round(multiplier * HT);

		if (buff(ElixirOfMight.HTBoost.class) != null) {
			HT += buff(ElixirOfMight.HTBoost.class).boost();
		}

		if (boostHP) {
			HP += Math.max(HT - curHT, 0);
		}
		HP = Math.min(HP, HT);
	}

	@Override
	protected boolean act() {
		if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()) {
			fieldOfView = new boolean[Dungeon.level.length()];
		}
		Dungeon.level.updateFieldOfView(this, fieldOfView);
		return false;
	}

	public boolean canInteract(Hero h) {
		return Dungeon.level.adjacent(pos, h.pos);
	}

	//swaps places by default
	public boolean interact() {

		if (!Dungeon.level.passable[pos] && !Dungeon.hero.flying) {
			return true;
		}

		int curPos = pos;

		moveSprite(pos, Dungeon.hero.pos);
		move(Dungeon.hero.pos);

		Dungeon.hero.sprite.move(Dungeon.hero.pos, curPos);
		Dungeon.hero.move(curPos);

		Dungeon.hero.spend(1 / Dungeon.hero.speed());
		Dungeon.hero.busy();

		return true;
	}

	protected boolean moveSprite(int from, int to) {

		if (sprite.isVisible() && (Dungeon.level.heroFOV[from] || Dungeon.level.heroFOV[to])) {
			sprite.move(from, to);
			return true;
		} else {
			sprite.turnTo(from, to);
			sprite.place(to);
			return true;
		}
	}

	protected static final String POS = "pos";
	protected static final String TAG_HP = "HP";
	protected static final String TAG_HT = "HT";
	protected static final String TAG_SHLD = "SHLD";
	protected static final String BUFFS = "buffs";

	@Override
	public void storeInBundle(Bundle bundle) {

		super.storeInBundle(bundle);

		bundle.put(POS, pos);
		bundle.put(TAG_HP, HP);
		bundle.put(TAG_HT, HT);
		bundle.put(BUFFS, buffs);

		if (hasBelongings()) {
			belongings.storeInBundle(bundle);
		}
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {

		super.restoreFromBundle(bundle);

		pos = bundle.getInt(POS);
		HP = bundle.getInt(TAG_HP);
		HT = bundle.getInt(TAG_HT);

		for (Bundlable b : bundle.getCollection(BUFFS)) {
			if (b != null) {
				((Buff) b).attachTo(this);
			}
		}

		if (hasBelongings()) {
			belongings.restoreFromBundle(bundle);
		}

		//pre-0.7.0
	}

	public boolean attack(Char enemy) {
		if (hasBelongings()) {
			belongings.nextWeapon();
		}

		if (enemy == null || enemy == this) return false;

		boolean visibleFight = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[enemy.pos];

		if (hit(this, enemy, false)) {

			int dr = enemy.drRoll();

			if (this instanceof Hero) {//Missile Weapons are always equipped in slot 1
				Hero h = (Hero) this;
				if (h.belongings.miscs[0] instanceof MissileWeapon
						&& h.subClass == HeroSubClass.SNIPER
						&& !Dungeon.level.adjacent(h.pos, enemy.pos)) {
					dr = 0;
				}
			}

			if (hasBelongings() && belongings.getCurrentWeapon() instanceof Blunt) {
				dr = 0;
			}

			int dmg;
			Preparation prep = buff(Preparation.class);
			if (prep != null) {
				dmg = prep.damageRoll(this, enemy);
			} else {
				dmg = damageRoll();
			}

			if (this.alignment == Alignment.ENEMY) {
				switch (Dungeon.difficulty) {
					case 1://Easy = -25% damage
						dmg *= 0.75f;
						break;
					case 2:
					default://Medium = normal damage
						break;
					case 3://Hard = +25% damage
						dmg *= 1.25f;
						break;
				}
			}

			if (hasBelongings()) {
				if (belongings.miscs[0] instanceof MissileWeapon) {//Missile Weapons are always equipped in slot 1
					dmg = ((MissileWeapon) belongings.miscs[0]).damageRoll(this);
				}
			}
			int effectiveDamage = enemy.defenseProc(this, dmg);
			effectiveDamage = Math.max(effectiveDamage - dr, 0);
			effectiveDamage = attackProc(enemy, effectiveDamage);

			if (visibleFight) {
				Sample.INSTANCE.play(Assets.SND_HIT, 1, 1, Random.Float(0.8f, 1.25f));
			}

			// If the enemy is already dead, interrupt the attack.
			// This matters as defence procs can sometimes inflict self-damage, such as armour glyphs.
			if (!enemy.isAlive()) {
				return true;
			}

			enemy.damage(effectiveDamage, this);
			if (buff(FireImbue.class) != null)
				buff(FireImbue.class).proc(enemy);
			if (buff(EarthImbue.class) != null)
				buff(EarthImbue.class).proc(enemy);
			if (buff(FrostImbue.class) != null)
				buff(FrostImbue.class).proc(enemy);

			enemy.sprite.bloodBurstA(sprite.center(), effectiveDamage);
			enemy.sprite.flash();

			if (!enemy.isAlive() && visibleFight) {
				if (enemy == Dungeon.hero) {

					Dungeon.fail(getClass());
					GLog.n(Messages.capitalize(Messages.get(Char.class, "kill", name)));

				} else if (this == Dungeon.hero) {
					GLog.i(Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name)));
				}
			}

			return true;

		} else {

			if (visibleFight) {
				String defense = enemy.defenseVerb();
				enemy.sprite.showStatus(CharSprite.NEUTRAL, defense);

				Sample.INSTANCE.play(Assets.SND_MISS);
			}

			return false;

		}
	}

	public static boolean hit(Char attacker, Char defender, boolean magic) {
		float acuRoll = Random.Float(attacker.attackSkill(defender));
		float defRoll = Random.Float(defender.defenseSkill(attacker));
		if (attacker.buff(Bless.class) != null) acuRoll *= 1.25f;
		if (defender.buff(Bless.class) != null) defRoll *= 1.25f;
		return (magic ? acuRoll * 3 : acuRoll) >= defRoll;
	}

	public void spendAndNext(float time) {
		spend(time);
		next();
	}

	public int attackSkill(Char target) {
		float accuracy = attackSkill;
		if (hasBelongings()) {
			accuracy = belongings.accuracyFactor(accuracy, target);
		}
		Drunk drunk = buff(Drunk.class);
		if (drunk != null) {
			accuracy *= drunk.accuracyFactor();
		}
		return (int) (accuracy * ACC);
	}


	public int defenseSkill(Char enemy) {
		float evasion = this.defenseSkill;
		if (buff(Wet.class) != null) {
			evasion *= buff(Wet.class).evasionFactor();
		}
		if (hasBelongings()) {
			evasion = belongings.EvasionFactor(evasion);

		}
		if (paralysed > 0) {
			evasion /= 2;
		}
		Drunk drunk = buff(Drunk.class);
		if (drunk != null) {
			evasion *= drunk.evasionFactor();
		}
		return Math.round(evasion * EVA);
	}

	public String defenseVerb() {
		return Messages.get(this, "def_verb");
	}

	public int magicalDRRoll() {
		int dr = 0;
		if (hasBelongings()) {
			dr += belongings.magicalDR();
		}
		return dr;
	}


	public int magicalDefenseProc(Char enemy, int damage) {
		if (hasBelongings()) {
			damage = belongings.magicalDefenseProc(enemy, damage);
		}
		return damage;
	}

	public int magicalAttackProc(Char enemy, int damage) {
		if (hasBelongings()) {
			damage = belongings.magicalAttackProc(enemy, damage);
		}
		return damage;
	}

	public int magicalDamageRoll() {
		return 0;
	}

	public float magicalAttackDelay() {
		return 1f;
	}

	public void onZapComplete() {
		next();
	}

	public boolean magicalAttack( Char enemy ) {
		if (enemy == null) return false;
		boolean visibleFight = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[enemy.pos];
		if (hit( this, enemy, true )) {
			int dr = enemy.magicalDRRoll();
			int dmg = magicalDamageRoll();
			int effectiveDamage = enemy.magicalDefenseProc( this, dmg );
			effectiveDamage = Math.max( effectiveDamage - dr, 0 );
			effectiveDamage = magicalAttackProc( enemy, effectiveDamage );

			// If the enemy is already dead, interrupt the attack.
			// This matters as defence procs can sometimes inflict self-damage, such as getArmors glyphs.
			if (!enemy.isAlive()){
				return true;
			}
			enemy.sprite.bloodBurstA( sprite.center(), effectiveDamage );
			enemy.sprite.flash();

			if (this instanceof RangedMob) {
				RangedMob RM = ((RangedMob)this);
				enemy.damage(effectiveDamage,RM.magicalSrc());
			} else {
				enemy.damage(effectiveDamage, this);
			}

			if (!enemy.isAlive() && visibleFight) {
				if (enemy == Dungeon.hero) {

					if (this == Dungeon.hero) {
						return true;
					}

					Dungeon.fail( getClass() );
					GLog.n( Messages.capitalize(Messages.get(Char.class, "kill", name)) );

				} else if (this == Dungeon.hero) {
					GLog.i( Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name)) );
				}
			}

			return true;
		} else {
			if (visibleFight) {
				String defense = enemy.defenseVerb();
				enemy.sprite.showStatus( CharSprite.NEUTRAL, defense );

				Sample.INSTANCE.play(Assets.SND_MISS);
			}

			return false;
		}

	}

	public int drRoll() {
		int dr = 0;
		if (hasBelongings()) {
			dr += belongings.drRoll();
		}
		Barkskin bark = buff(Barkskin.class);
		if (bark != null) dr += Random.NormalIntRange(0, bark.level());

		Blocking.BlockBuff block = buff(Blocking.BlockBuff.class);
		if (block != null) dr += block.blockingRoll();

		return dr;
	}

	public int damageRoll() {
		if (hasBelongings()) {
			return belongings.damageRoll();
		} else {
			return 1;
		}
	}

	public int attackProc( Char enemy, int damage ) {
		if (hasBelongings()) {
			damage = belongings.attackProc(enemy, damage);
		}
		return damage;
	}

	public int defenseProc( Char enemy, int damage ) {
		if (hasBelongings()) {
			damage = belongings.defenseProc(enemy, damage);
		}
		Earthroot.Armor armor = buff( Earthroot.Armor.class );
		if (armor != null) {
			damage = armor.absorb( damage );
		}

		WandOfLivingEarth.RockArmor rockArmor = buff(WandOfLivingEarth.RockArmor.class);
		if (rockArmor != null) {
			damage = rockArmor.absorb(damage);
		}
		return damage;
	}

	public float speed() {
		float speed = baseSpeed;
		if ( buff( Cripple.class ) != null ) speed /= 2f;
		if ( buff( Stamina.class ) != null) speed *= 1.5f;
		if ( buff( Adrenaline.class ) != null) speed *= 2f;
		if ( buff( Haste.class ) != null) speed *= 3f;
		if (hasBelongings()) {
			speed = belongings.SpeedFactor(speed);
		}
		Momentum momentum = buff(Momentum.class);
		if (momentum != null) {
			//((HeroSprite)sprite).sprint( 1f + 0.05f*momentum.stacks());
			speed *= momentum.speedMultiplier();
		}
		return speed;
	}

	public float attackDelay() {
		if (hasBelongings()) {
			return belongings.attackDelay();
		} else {
			return DLY;
		}
	}

	//used so that buffs(Shieldbuff.class) isn't called every time unnecessarily
	private int cachedShield = 0;
	public boolean needsShieldUpdate = true;

	public int shielding(){
		if (!needsShieldUpdate){
			return cachedShield;
		}

		cachedShield = 0;
		for (ShieldBuff s : buffs(ShieldBuff.class)){
			cachedShield += s.shielding();
		}
		needsShieldUpdate = false;
		return cachedShield;
	}

	public boolean canSurpriseAttack(){
		if (hasBelongings()) {
			return belongings.canSurpriseAttack();
		}
		return true;
	}

	public void damage( int dmg, Object src ) {
		if (this.buff(Drowsy.class) != null) {
			Buff.detach(this, Drowsy.class);
			GLog.w(Messages.get(this, "pain_resist"));
		}
		if (hasBelongings()) {
			dmg = belongings.affectDamage(dmg, src);
		}

		if (!isAlive() || dmg < 0) {
			return;
		}
		Terror t = buff(Terror.class);
		if (t != null){
			t.recover();
		}
		Charm c = buff(Charm.class);
		if (c != null){
			c.recover();
		}
		if (this.buff(Frost.class) != null){
			Buff.detach( this, Frost.class );
		}
		if (this.buff(MagicalSleep.class) != null){
			Buff.detach(this, MagicalSleep.class);
		}
		if (this.buff(Doom.class) != null){
			dmg *= 2;
		}

		Class<?> srcClass = src.getClass();
		if (isImmune( srcClass )) {
			dmg = 0;
		} else {
			dmg = Math.round( dmg * resist( srcClass ));
		}

		//TODO improve this when I have proper damage source logic
		if (AntiMagic.RESISTS.contains(src.getClass()) && buff(ArcaneArmor.class) != null){
			dmg -= Random.NormalIntRange(0, buff(ArcaneArmor.class).level());
			if (dmg < 0) dmg = 0;
		}

		if (buff( Paralysis.class ) != null) {
			buff( Paralysis.class ).processDamage(dmg);
		}

		int shielded = dmg;
		//FIXME: when I add proper damage properties, should add an IGNORES_SHIELDS property to use here.
		if (!(src instanceof Hunger)){
			for (ShieldBuff s : buffs(ShieldBuff.class)){
				dmg = s.absorbDamage(dmg);
				if (dmg == 0) break;
			}
		}
		shielded -= dmg;
		HP -= dmg;

		if (sprite != null) {
			sprite.showStatus(HP > HT / 2 ?
							CharSprite.WARNING :
							CharSprite.NEGATIVE,
					Integer.toString(dmg + shielded));
		}

		if (HP < 0) HP = 0;

		if (!isAlive()) {
			die( src );
		}
	}

	public void destroy() {
		HP = 0;
		Actor.remove( this );
	}

	public void die( Object src ) {
		destroy();
		if (src != Chasm.class) sprite.die();
	}

	public boolean isAlive() {
		return HP > 0;
	}

	public void busy() {

	}

	@Override
	public void spend( float time ) {

		float timeScale = 1f;
		if (buff( Slow.class ) != null) {
			timeScale *= 0.5f;
			//slowed and chilled do not stack
		} else if (buff( Chill.class ) != null) {
			timeScale *= buff( Chill.class ).speedFactor();
		}
		if (buff( Speed.class ) != null) {
			timeScale *= 2.0f;
		}

		super.spend( time / timeScale );
	}

	public synchronized HashSet<Buff> buffs() {
		return new HashSet<>(buffs);
	}

	@SuppressWarnings("unchecked")
	public synchronized <T extends Buff> HashSet<T> buffs( Class<T> c ) {
		HashSet<T> filtered = new HashSet<>();
		for (Buff b : buffs) {
			if (c.isInstance( b )) {
				filtered.add( (T)b );
			}
		}
		return filtered;
	}

	@SuppressWarnings("unchecked")
	public synchronized  <T extends Buff> T buff( Class<T> c ) {
		for (Buff b : buffs) {
			if (c.isInstance( b )) {
				return (T)b;
			}
		}
		return null;
	}

	public synchronized boolean isCharmedBy( Char ch ) {
		int chID = ch.id();
		for (Buff b : buffs) {
			if (b instanceof Charm && ((Charm)b).object == chID) {
				return true;
			}
		}
		return false;
	}

	public synchronized void add( Buff buff ) {

		buffs.add( buff );
		Actor.add( buff );

		if (sprite != null && buff.announced)
			switch(buff.type){
				case POSITIVE:
					sprite.showStatus(CharSprite.POSITIVE, buff.toString());
					break;
				case NEGATIVE:
					sprite.showStatus(CharSprite.NEGATIVE, buff.toString());
					break;
				case NEUTRAL: default:
					sprite.showStatus(CharSprite.NEUTRAL, buff.toString());
					break;
			}

	}

	public synchronized void remove( Buff buff ) {

		buffs.remove( buff );
		Actor.remove( buff );

	}

	public synchronized void remove( Class<? extends Buff> buffClass ) {
		for (Buff buff : buffs( buffClass )) {
			remove( buff );
		}
	}

	@Override
	protected synchronized void onRemove() {
		for (Buff buff : buffs.toArray(new Buff[buffs.size()])) {
			buff.detach();
		}
	}

	public synchronized void updateSpriteState() {
		for (Buff buff:buffs) {
			buff.fx( true );
		}
	}

	public float stealth() {
		float stealth = STE;
		if (hasBelongings()) {
			stealth = belongings.StealthFactor(stealth);
		}

		if (this.buff(Invisibility.class) != null) {
			stealth += 5;
		}
		return stealth;
	}

	public void move( int step ) {
		Drunk drunk = buff(Drunk.class);
		if (Dungeon.level.adjacent( step, pos ) && (buff( Vertigo.class ) != null || (drunk != null && drunk.stumbleChance()))) {
			sprite.interruptMotion();
			int newPos = pos + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
			if (!(Dungeon.level.passable[newPos] || Dungeon.level.avoid[newPos]) || Actor.findChar( newPos ) != null)
				return;
			else {
				sprite.move(pos, newPos);
				step = newPos;
			}
		}

		if (Dungeon.level.map[pos] == Terrain.OPEN_DOOR) {
			Door.leave( pos );
		}

		pos = step;

		if (this != Dungeon.hero) {
			sprite.visible = Dungeon.level.heroFOV[pos];
		}

		Dungeon.level.occupyCell(this );
	}

	public int distance( Char other ) {
		return Dungeon.level.distance( pos, other.pos );
	}

	public void onMotionComplete() {
		//Does nothing by default
		//The main actor thread already accounts for motion,
		// so calling next() here isn't necessary (see Actor.process)
	}

	public void onAttackComplete() {
		next();
	}

	public void onOperateComplete() {
		next();
	}

	protected final HashSet<Class> resistances = new HashSet<>();

	//returns percent effectiveness after resistances
	//TODO currently resistances reduce effectiveness by a static 50%, and do not stack.
	public float resist( Class effect ){
		HashSet<Class> resists = new HashSet<>(resistances);
		for (Property p : properties()){
			resists.addAll(p.resistances());
		}
		for (Buff b : buffs()){
			resists.addAll(b.resistances());
		}

		float result = 1f;
		for (Class c : resists){
			if (c.isAssignableFrom(effect)){
				result *= 0.5f;
			}
		}
		return result * RingOfElements.resist(this, effect);
	}

	protected final HashSet<Class> immunities = new HashSet<>();

	public boolean isImmune(Class effect ){
		HashSet<Class> immunes = new HashSet<>(immunities);
		for (Property p : properties()){
			immunes.addAll(p.immunities());
		}
		for (Buff b : buffs()){
			immunes.addAll(b.immunities());
		}

		for (Class c : immunes){
			if (c.isAssignableFrom(effect)){
				return true;
			}
		}
		if (hasBelongings()) {
			return belongings.isImmune(effect);
		} else {
			return false;
		}
	}

	protected HashSet<Property> properties = new HashSet<>();

	public HashSet<Property> properties() {
		return new HashSet<>(properties);
	}

	public enum Property{
		BOSS ( new HashSet<Class>( Arrays.asList(Grim.class, GrimTrap.class, ScrollOfRetribution.class, ScrollOfPsionicBlast.class)),
				new HashSet<Class>( Arrays.asList(Corruption.class, Aggression.class) )),
		MINIBOSS ( new HashSet<Class>(),
				new HashSet<Class>( Arrays.asList(Corruption.class) )),
		UNDEAD,
		DEMONIC,
		INORGANIC ( new HashSet<Class>(),
				new HashSet<Class>( Arrays.asList(Bleeding.class, ToxicGas.class, Poison.class) )),
		BLOB_IMMUNE ( new HashSet<Class>(),
				new HashSet<Class>( Arrays.asList(Blob.class) )),
		FIERY ( new HashSet<Class>( Arrays.asList(WandOfFireblast.class)),
				new HashSet<Class>( Arrays.asList(Burning.class, Blazing.class))),
		ACIDIC ( new HashSet<Class>( Arrays.asList(Corrosion.class)),
				new HashSet<Class>( Arrays.asList(Ooze.class))),
		ELECTRIC ( new HashSet<Class>( Arrays.asList(WandOfLightning.class, Shocking.class, Potential.class, Electricity.class, ShockingDart.class)),
				new HashSet<Class>()),
		IMMOVABLE;

		private HashSet<Class> resistances;
		private HashSet<Class> immunities;

		Property(){
			this(new HashSet<Class>(), new HashSet<Class>());
		}

		Property( HashSet<Class> resistances, HashSet<Class> immunities){
			this.resistances = resistances;
			this.immunities = immunities;
		}

		public HashSet<Class> resistances(){
			return new HashSet<>(resistances);
		}

		public HashSet<Class> immunities(){
			return new HashSet<>(immunities);
		}
	}
}
