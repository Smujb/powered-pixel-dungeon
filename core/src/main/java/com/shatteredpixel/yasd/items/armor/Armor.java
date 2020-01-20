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

package com.shatteredpixel.yasd.items.armor;

import com.shatteredpixel.yasd.Badges;
import com.shatteredpixel.yasd.Constants;
import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.YASD;
import com.shatteredpixel.yasd.actors.Actor;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.buffs.Buff;
import com.shatteredpixel.yasd.actors.buffs.Hunger;
import com.shatteredpixel.yasd.actors.buffs.MagicImmune;
import com.shatteredpixel.yasd.actors.buffs.Momentum;
import com.shatteredpixel.yasd.actors.hero.Hero;
import com.shatteredpixel.yasd.effects.Speck;
import com.shatteredpixel.yasd.items.BrokenSeal;
import com.shatteredpixel.yasd.items.Item;
import com.shatteredpixel.yasd.items.KindofMisc;
import com.shatteredpixel.yasd.items.armor.curses.AntiEntropy;
import com.shatteredpixel.yasd.items.armor.curses.Bulk;
import com.shatteredpixel.yasd.items.armor.curses.Corrosion;
import com.shatteredpixel.yasd.items.armor.curses.Displacement;
import com.shatteredpixel.yasd.items.armor.curses.Metabolism;
import com.shatteredpixel.yasd.items.armor.curses.Multiplicity;
import com.shatteredpixel.yasd.items.armor.curses.Overgrowth;
import com.shatteredpixel.yasd.items.armor.curses.Stench;
import com.shatteredpixel.yasd.items.armor.glyphs.Affection;
import com.shatteredpixel.yasd.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.yasd.items.armor.glyphs.Brimstone;
import com.shatteredpixel.yasd.items.armor.glyphs.Camouflage;
import com.shatteredpixel.yasd.items.armor.glyphs.Entanglement;
import com.shatteredpixel.yasd.items.armor.glyphs.Flow;
import com.shatteredpixel.yasd.items.armor.glyphs.Obfuscation;
import com.shatteredpixel.yasd.items.armor.glyphs.Potential;
import com.shatteredpixel.yasd.items.armor.glyphs.Repulsion;
import com.shatteredpixel.yasd.items.armor.glyphs.Stone;
import com.shatteredpixel.yasd.items.armor.glyphs.Swiftness;
import com.shatteredpixel.yasd.items.armor.glyphs.Thorns;
import com.shatteredpixel.yasd.items.armor.glyphs.Viscosity;
import com.shatteredpixel.yasd.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.yasd.levels.Terrain;
import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.sprites.HeroSprite;
import com.shatteredpixel.yasd.sprites.ItemSprite;
import com.shatteredpixel.yasd.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.utils.GLog;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;

public class Armor extends KindofMisc {

	public float EVA = 1f;
	public float STE = 1f;
	public float speedFactor = 1f;
	public float DRfactor = 1f;
	public float magicalDRFactor = 0f;

	protected static final String AC_DETACH       = "DETACH";
	
	public enum Augment {
		EVASION (1.5f , -1f),
		DEFENSE (-1.5f, 1f),
		NONE	(0f   ,  0f);
		
		private float evasionFactor;
		private float defenceFactor;
		
		Augment(float eva, float df){
			evasionFactor = eva;
			defenceFactor = df;
		}
		
		public int evasionFactor(int level){
			return Math.round((2 + level) * evasionFactor);
		}
		
		public int defenseFactor(int level){
			return Math.round((2 + level) * defenceFactor);
		}
	}

	@Override
	public boolean canDegrade() {
		return Constants.DEGRADATION;
	}

	public Augment augment = Augment.NONE;
	
	public Glyph glyph;
	public boolean curseInfusionBonus = false;
	
	private BrokenSeal seal;
	
	public int tier;
	
	private static final int USES_TO_ID = 10;
	private int usesLeftToID = USES_TO_ID;
	private float availableUsesToID = USES_TO_ID/2f;
	
	public Armor( int tier ) {
		this.tier = tier;
	}
	
	private static final String USES_LEFT_TO_ID = "uses_left_to_id";
	private static final String AVAILABLE_USES  = "available_uses";
	private static final String GLYPH			= "glyph";
	private static final String CURSE_INFUSION_BONUS = "curse_infusion_bonus";
	private static final String SEAL            = "seal";
	private static final String AUGMENT			= "augment";
	private static final String TIER = "tier";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( USES_LEFT_TO_ID, usesLeftToID );
		bundle.put( AVAILABLE_USES, availableUsesToID );
		bundle.put( GLYPH, glyph );
		bundle.put( CURSE_INFUSION_BONUS, curseInfusionBonus );
		bundle.put( SEAL, seal);
		bundle.put( AUGMENT, augment);
		bundle.put( TIER, tier );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		usesLeftToID = bundle.getInt( USES_LEFT_TO_ID );
		availableUsesToID = bundle.getInt( AVAILABLE_USES );
		inscribe((Glyph) bundle.get(GLYPH));
		curseInfusionBonus = bundle.getBoolean( CURSE_INFUSION_BONUS );
		seal = (BrokenSeal)bundle.get(SEAL);
		
		//pre-0.7.2 saves
		if (bundle.contains( "unfamiliarity" )){
			usesLeftToID = bundle.getInt( "unfamiliarity" );
			availableUsesToID = USES_TO_ID/2f;
		}
		
		augment = bundle.getEnum(AUGMENT, Augment.class);

		if (Dungeon.version >= YASD.v0_2_0) {//Support older saves
			tier = bundle.getInt(TIER);
		}
	}

	@Override
	public void reset() {
		super.reset();
		usesLeftToID = USES_TO_ID;
		availableUsesToID = USES_TO_ID/2f;
		//armours can be kept in bones between runs, the seal cannot.
		seal = null;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (seal != null) actions.add(AC_DETACH);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_DETACH) && seal != null){
			BrokenSeal.WarriorShield sealBuff = hero.buff(BrokenSeal.WarriorShield.class);
			if (sealBuff != null) sealBuff.setArmor(null);

			if (seal.level() > 0){
				degrade();
			}
			GLog.i( Messages.get(Armor.class, "detach_seal") );
			hero.sprite.operate(hero.pos);
			if (!seal.collect()){
				Dungeon.level.drop(seal, hero.pos);
			}
			seal = null;
		}
	}


	@Override
	public void activate(Char ch) {
		if (seal != null) Buff.affect(ch, BrokenSeal.WarriorShield.class).setArmor(this);
	}

	public void affixSeal(BrokenSeal seal){
		this.seal = seal;
		if (seal.level() > 0){
			//doesn't trigger upgrading logic such as affecting curses/glyphs
			level(Math.min(level()+1,3));
			Badges.validateItemLevelAquired(this);
		}
		if (isEquipped(Dungeon.hero)){
			Buff.affect(Dungeon.hero, BrokenSeal.WarriorShield.class).setArmor(this);
		}
	}

	public int appearance() {
		return 1;
	}

	public BrokenSeal checkSeal(){
		return seal;
	}

	@Override
	protected float time2equip( Char hero ) {
		return 2 / hero.speed();
	}

	public final int DRMax(){
		return DRMax(level());
	}

	public int DRMax(int lvl){
		return Math.round(((tier*3) + (tier * lvl)) * DRfactor);
	}

	public final int DRMin(){
		return DRMin(level());
	}

	public int DRMin(int lvl){
		return Math.round((tier + lvl) * DRfactor);
	}

	public int DRRoll() {
		return DRRoll(level());
	}

	public int DRRoll(int lvl) {
		return Random.NormalIntRange(DRMin(lvl), DRMax(lvl));
	}




	public final int magicalDRMax(){
		return magicalDRMax(level());
	}

	public int magicalDRMax(int lvl){
		return Math.round(((tier*3) + (tier * lvl)) * magicalDRFactor);
	}

	public final int magicalDRMin(){
		return magicalDRMin(level());
	}

	public int magicalDRMin(int lvl){
		return Math.round((tier + lvl) * magicalDRFactor);
	}

	public int magicalDRRoll() {
		return magicalDRRoll(level());
	}

	public int magicalDRRoll(int lvl) {
		return Random.NormalIntRange(magicalDRMin(lvl), magicalDRMax(lvl));
	}
	
	public float evasionFactor( Char owner, float evasion ){
		
		if (hasGlyph(Stone.class, owner) && !((Stone)glyph).testingEvasion()){
			return 0;
		}
		
		if (owner instanceof Hero){
			int aEnc = STRReq() - ((Hero) owner).STR();
			if (aEnc > 0) evasion /= Math.pow(1.5, aEnc);
			
			Momentum momentum = owner.buff(Momentum.class);
			if (momentum != null){
				evasion += momentum.evasionBonus(Math.max(0, -aEnc));
			}
		}
		
		return Math.round(evasion + augment.evasionFactor(level()) * EVA);
	}

	public float speedFactor( Char owner, float speed ){
		
		if (owner instanceof Hero) {
			int aEnc = STRReq() - ((Hero) owner).STR();
			if (aEnc > 0) speed /= Math.pow(1.2, aEnc);
		}
		
		if (hasGlyph(Swiftness.class, owner)) {
			boolean enemyNear = false;
			for (Char ch : Actor.chars()){
				if (Dungeon.level.adjacent(ch.pos, owner.pos) && owner.alignment != ch.alignment){
					enemyNear = true;
					break;
				}
			}
			if (!enemyNear) speed *= (1.2f + 0.04f * level());
		} else if (hasGlyph(Flow.class, owner) && Dungeon.level.water[owner.pos]){
			speed *= 2f;
		}
		
		if (hasGlyph(Bulk.class, owner) &&
				(Dungeon.level.map[owner.pos] == Terrain.DOOR
						|| Dungeon.level.map[owner.pos] == Terrain.OPEN_DOOR )) {
			speed /= 3f;
		}
		
		return Math.round(speed * speedFactor);
		
	}

	public float stealthMultiplier(Char owner) {
		float stealth = owner.stealth();
		return (stealth/stealthFactor(owner,stealth));
	}
	
	public float stealthFactor( Char owner, float stealth ){
		
		if (hasGlyph(Obfuscation.class, owner)){
			stealth += 1 + level()/3f;
		}
		
		return Math.round(stealth * STE);
	}

	@Override
	public boolean doEquip(Hero hero) {
		boolean equipped = super.doEquip(hero);
		if (hero instanceof Hero) {
			((HeroSprite) hero.sprite).updateArmor();
		}
		return equipped;
	}

	@Override
	public boolean doUnequip(Char hero, boolean collect, boolean single) {
		boolean equipped = super.doUnequip(hero, collect, single);
		if (hero instanceof Hero) {
			((HeroSprite) hero.sprite).updateArmor();
		}
		return equipped;
	}

	@Override
	public int level() {
		return super.level() + (curseInfusionBonus ? Constants.CURSE_INFUSION_BONUS_AMT : 0);
	}
	
	@Override
	public Item upgrade() {
		return upgrade( false );
	}
	
	public Item upgrade( boolean inscribe ) {

		if (inscribe && (glyph == null || glyph.curse())){
			inscribe( Glyph.random() );
		} else if (!inscribe && level() >= 4 && Random.Float(10) < Math.pow(2, level()-4)){
			inscribe(null);
		}
		
		cursed = false;

		if (seal != null && seal.level() == 0)
			seal.upgrade();

		return super.upgrade();
	}
	
	public int proc( Char attacker, Char defender, int damage ) {
		
		if (glyph != null && defender.buff(MagicImmune.class) == null) {
			damage = glyph.proc( this, attacker, defender, damage );
		}
		
		if (!levelKnown && defender == Dungeon.hero && availableUsesToID >= 1) {
			availableUsesToID--;
			usesLeftToID--;
			if (usesLeftToID <= 0) {
				identify();
				GLog.p( Messages.get(Armor.class, "identify") );
				Badges.validateItemLevelAquired( this );
			}
		}
		
		return damage;
	}
	
	@Override
	public void onHeroGainExp(float levelPercent, Hero hero) {
		if (!levelKnown && isEquipped(hero) && availableUsesToID <= USES_TO_ID/2f) {
			//gains enough uses to ID over 0.5 levels
			availableUsesToID = Math.min(USES_TO_ID/2f, availableUsesToID + levelPercent * USES_TO_ID);
		}
	}
	
	@Override
	public String name() {
		return glyph != null && (cursedKnown || !glyph.curse()) ? glyph.name( super.name() ) : super.name();
	}
	
	@Override
	public String info() {
		String info = desc();
		
		if (levelKnown) {
			info += "\n\n" + Messages.get(Armor.class, "curr_absorb", tier, DRMin(), DRMax(), STRReq());

			if (magicalDRMax() > 0) {
				info += " " + Messages.get(Armor.class, "curr_absorb_magic",  magicalDRMin(), magicalDRMax());
			}
			
			if (STRReq() > Dungeon.hero.STR()) {
				info += " " + Messages.get(Armor.class, "too_heavy");
			}
		} else {
			info += "\n\n" + Messages.get(Armor.class, "avg_absorb", tier, DRMin(0), DRMax(0), STRReq(0));

			if (magicalDRMax() > 0) {
				info += " " +  Messages.get(Armor.class, "avg_absorb_magic", magicalDRMin(0), magicalDRMax(0));
			}

			if (STRReq(0) > Dungeon.hero.STR()) {
				info += " " + Messages.get(Armor.class, "probably_too_heavy");
			}
		}

		switch (augment) {
			case EVASION:
				info += "\n\n" + Messages.get(Armor.class, "evasion");
				break;
			case DEFENSE:
				info += "\n\n" + Messages.get(Armor.class, "defense");
				break;
			case NONE:
		}

		if (EVA != 1f || STE != 1f || speedFactor != 1f) {

			info += "\n";

			if (EVA > 1f) {
				info += "\n" + Messages.get(Armor.class, "eva_increase", Math.round((EVA-1f)*100));
			} else if (EVA < 1f) {
				info += "\n" + Messages.get(Armor.class, "eva_decrease", (double) Math.round((1f-EVA)*100));
			}

			if (STE > 1f) {
				info += "\n" + Messages.get(Armor.class, "ste_increase", (double) Math.round((STE-1f)*100));
			} else if (STE < 1f) {
				info += "\n" + Messages.get(Armor.class, "ste_decrease", (double) Math.round((1f-STE)*100));
			}

			if (speedFactor > 1f) {
				info += "\n" + Messages.get(Armor.class, "speed_increase", (double) Math.round((speedFactor-1f)*100));
			} else if (speedFactor < 1f) {
				info += "\n" + Messages.get(Armor.class, "speed_decrease", (double) Math.round((1f-speedFactor)*100));
			}
		}
		
		if (glyph != null  && (cursedKnown || !glyph.curse())) {
			info += "\n\n" +  Messages.get(Armor.class, "inscribed", glyph.name());
			info += " " + glyph.desc();
		}
		
		if (cursed && isEquipped( Dungeon.hero )) {
			info += "\n\n" + Messages.get(Armor.class, "cursed_worn");
		} else if (cursedKnown && cursed) {
			info += "\n\n" + Messages.get(Armor.class, "cursed");
		} else if (seal != null) {
			info += "\n\n" + Messages.get(Armor.class, "seal_attached");
		} else if (!isIdentified() && cursedKnown){
			info += "\n\n" + Messages.get(Armor.class, "not_cursed");
		}
		
		return info;
	}

	@Override
	public Emitter emitter() {
		if (seal == null) return super.emitter();
		Emitter emitter = new Emitter();
		emitter.pos(ItemSpriteSheet.film.width(image)/2f + 2f, ItemSpriteSheet.film.height(image)/3f);
		emitter.fillTarget = false;
		emitter.pour(Speck.factory( Speck.RED_LIGHT ), 0.6f);
		return emitter;
	}

	@Override
	public Item random() {
		//+0: 75% (3/4)
		//+1: 20% (4/20)
		//+2: 5%  (1/20)
		int n = 0;
		if (Random.Int(4) == 0) {
			n++;
			if (Random.Int(5) == 0) {
				n++;
			}
		}
		level(n);
		
		//30% chance to be cursed
		//15% chance to be inscribed
		float effectRoll = Random.Float();
		if (effectRoll < 0.3f) {
			inscribe(Glyph.randomCurse());
			cursed = true;
		} else if (effectRoll >= 0.85f){
			inscribe();
		}

		return this;
	}

	public int defaultSTRReq() {
		return Math.max(STRReq(level()),10);
	}

	public int STRReq() {
		if (isEquipped(Dungeon.hero)) {
			return Dungeon.hero.belongings.getArmorSTRReq();
		} else {
			return defaultSTRReq();
		}
	}


	public int STRReq(int lvl){
		lvl = Math.max(0, lvl);

		//strength req decreases at +1,+3,+6,+10,etc.
		return (8 + Math.round(tier * 2)) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;
	}
	
	@Override
	public int price() {
		if (seal != null) return 0;

		int price = 20 * tier;
		if (hasGoodGlyph()) {
			price *= 1.5;
		}
		if (cursedKnown && (cursed || hasCurseGlyph())) {
			price /= 2;
		}
		if (levelKnown && level() > 0) {
			price *= (level() + 1);
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}

	public Armor inscribe( Glyph glyph ) {
		if (glyph == null || !glyph.curse()) curseInfusionBonus = false;
		this.glyph = glyph;
		updateQuickslot();
		return this;
	}

	public Armor inscribe() {

		Class<? extends Glyph> oldGlyphClass = glyph != null ? glyph.getClass() : null;
		Glyph gl = Glyph.random( oldGlyphClass );

		return inscribe( gl );
	}

	public boolean hasGlyph(Class<?extends Glyph> type, Char owner) {
		return glyph != null && glyph.getClass() == type && owner.buff(MagicImmune.class) == null;
	}

	//these are not used to process specific glyph effects, so magic immune doesn't affect them
	public boolean hasGoodGlyph(){
		return glyph != null && !glyph.curse();
	}

	public boolean hasCurseGlyph(){
		return glyph != null && glyph.curse();
	}
	
	@Override
	public ItemSprite.Glowing glowing() {
		return glyph != null && (cursedKnown || !glyph.curse()) ? glyph.glowing() : null;
	}
	
	public static abstract class Glyph implements Bundlable {
		
		private static final Class<?>[] common = new Class<?>[]{
				Obfuscation.class, Swiftness.class, Viscosity.class, Potential.class };
		
		private static final Class<?>[] uncommon = new Class<?>[]{
				Brimstone.class, Stone.class, Entanglement.class,
				Repulsion.class, Camouflage.class, Flow.class };
		
		private static final Class<?>[] rare = new Class<?>[]{
				Affection.class, AntiMagic.class, Thorns.class };
		
		private static final float[] typeChances = new float[]{
				50, //12.5% each
				40, //6.67% each
				10  //3.33% each
		};

		private static final Class<?>[] curses = new Class<?>[]{
				AntiEntropy.class, Corrosion.class, Displacement.class, Metabolism.class,
				Multiplicity.class, Stench.class, Overgrowth.class, Bulk.class
		};
		
		public abstract int proc( Armor armor, Char attacker, Char defender, int damage );
		
		public String name() {
			if (!curse())
				return name( Messages.get(this, "glyph") );
			else
				return name( Messages.get(Item.class, "curse"));
		}
		
		public String name( String armorName ) {
			return Messages.get(this, "name", armorName);
		}

		public String desc() {
			return Messages.get(this, "desc");
		}

		public boolean curse() {
			return false;
		}
		
		@Override
		public void restoreFromBundle( Bundle bundle ) {
		}

		@Override
		public void storeInBundle( Bundle bundle ) {
		}
		
		public abstract ItemSprite.Glowing glowing();

		@SuppressWarnings("unchecked")
		public static Glyph random( Class<? extends Glyph> ... toIgnore ) {
			switch(Random.chances(typeChances)){
				case 0: default:
					return randomCommon( toIgnore );
				case 1:
					return randomUncommon( toIgnore );
				case 2:
					return randomRare( toIgnore );
			}
		}
		
		@SuppressWarnings("unchecked")
		public static Glyph randomCommon( Class<? extends Glyph> ... toIgnore ){
			ArrayList<Class<?>> glyphs = new ArrayList<>(Arrays.asList(common));
			glyphs.removeAll(Arrays.asList(toIgnore));
			if (glyphs.isEmpty()) {
				return random();
			} else {
				return (Glyph) Reflection.newInstance(Random.element(glyphs));
			}
		}
		
		@SuppressWarnings("unchecked")
		public static Glyph randomUncommon( Class<? extends Glyph> ... toIgnore ){
			ArrayList<Class<?>> glyphs = new ArrayList<>(Arrays.asList(uncommon));
			glyphs.removeAll(Arrays.asList(toIgnore));
			if (glyphs.isEmpty()) {
				return random();
			} else {
				return (Glyph) Reflection.newInstance(Random.element(glyphs));
			}
		}
		
		@SuppressWarnings("unchecked")
		public static Glyph randomRare( Class<? extends Glyph> ... toIgnore ){
			ArrayList<Class<?>> glyphs = new ArrayList<>(Arrays.asList(rare));
			glyphs.removeAll(Arrays.asList(toIgnore));
			if (glyphs.isEmpty()) {
				return random();
			} else {
				return (Glyph) Reflection.newInstance(Random.element(glyphs));
			}
		}
		
		@SuppressWarnings("unchecked")
		public static Glyph randomCurse( Class<? extends Glyph> ... toIgnore ){
			ArrayList<Class<?>> glyphs = new ArrayList<>(Arrays.asList(curses));
			glyphs.removeAll(Arrays.asList(toIgnore));
			if (glyphs.isEmpty()) {
				return random();
			} else {
				return (Glyph) Reflection.newInstance(Random.element(glyphs));
			}
		}
		
	}
	public Armor setTier(int tier) {
		this.tier = tier;
		updateTier();
		return this;
	}

	public Armor upgradeTier(int tier) {
		this.tier += tier;
		updateTier();
		return this;
	}

	public Armor degradeTier(int tier) {
		this.tier -= tier;
		updateTier();
		return this;
	}
	public void updateTier() {

	}
}
