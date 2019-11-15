package com.shatteredpixel.yasd.items.wands;

import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.Actor;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.blobs.AcidPool;
import com.shatteredpixel.yasd.actors.buffs.Bleeding;
import com.shatteredpixel.yasd.actors.buffs.Buff;
import com.shatteredpixel.yasd.actors.buffs.Cripple;
import com.shatteredpixel.yasd.actors.buffs.Recharging;
import com.shatteredpixel.yasd.actors.hero.Hero;
import com.shatteredpixel.yasd.actors.mobs.Mob;
import com.shatteredpixel.yasd.actors.mobs.Wraith;
import com.shatteredpixel.yasd.actors.mobs.npcs.NPC;
import com.shatteredpixel.yasd.effects.MagicMissile;
import com.shatteredpixel.yasd.effects.Splash;
import com.shatteredpixel.yasd.effects.particles.ShadowParticle;
import com.shatteredpixel.yasd.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.mechanics.Ballistica;
import com.shatteredpixel.yasd.scenes.GameScene;
import com.shatteredpixel.yasd.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.sprites.ThornVineSprite;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WandOfThornvines extends Wand {
    {
        image = ItemSpriteSheet.WAND_THORNVINES;
        collisionProperties = Ballistica.STOP_TARGET | Ballistica.STOP_TERRAIN;
    }

    private ThornVine thornVine = null;

    public ThornVine findThornVine() {
        if (Dungeon.level != null) {
            for (Mob m : Dungeon.level.mobs) {
                if (m instanceof ThornVine) {
                    thornVine = (ThornVine) m;
                }
            }
        }
        return thornVine;
    }

    @Override
    public void activate(Char ch) {
        thornVine = findThornVine();
        super.activate(ch);
    }

    @Override
    public boolean tryToZap(Hero owner, int target) {
        thornVine = findThornVine();
        return super.tryToZap(owner, target) & thornVine == null;//Can't zap if a thorn vine already exists
    }

    @Override
    protected void onZap(Ballistica bolt) {
        Char ch = Actor.findChar(bolt.collisionPos);


        if (findThornVine() == null) {
            new ThornVine().spawnAt(bolt.collisionPos, level(), curCharges/(float)maxCharges);
        }
    }

    private static class ThornVine extends NPC {
        {
            spriteClass = ThornVineSprite.class;
            properties.add(Property.IMMOVABLE);
            alignment = Alignment.ALLY;
        }

        int level;
        float chargesPercent;

        private final String LEVEL = "level";
        private final String CHARGESPERCENT = "chargespercent";


        public ThornVine(int level, float chargesPercent) {
            this.level = level;
            this.chargesPercent = chargesPercent;
        }

        public ThornVine() {
            this(0, 1f);
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            bundle.put(LEVEL, level);
            bundle.put(CHARGESPERCENT, chargesPercent);
            super.storeInBundle(bundle);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            level = bundle.getInt(LEVEL);
            chargesPercent = bundle.getInt(CHARGESPERCENT);
            super.restoreFromBundle(bundle);
        }

        @Override
        protected boolean getCloser(int target) {
            return true;
        }

        @Override
        protected boolean getFurther(int target) {
            return true;
        }

        @Override
        public int damageRoll() {
            return (int) (Random.Int(10 + level*10)*chargesPercent);
        }

        @Override
        public int defenseSkill(Char enemy) {
            return 0;
        }
        //Always hit, always hits
        @Override
        public int attackSkill(Char target) {
            return Integer.MAX_VALUE;
        }

        private int setHP() {
            return (int) ((20 + this.level*10)*chargesPercent);
        }

        @Override
        public int attackProc(Char enemy, int damage) {
            if (Random.Int(  3 ) >= 2) {

                Buff.affect(enemy, Bleeding.class).set(damage/3f);
                Splash.at( enemy.sprite.center(), -PointF.PI / 2, PointF.PI / 6,
                        enemy.sprite.blood(), 10 );

            }
            Buff.prolong( enemy, Cripple.class, 3f );
            return super.attackProc(enemy, damage);
        }

        @Override
        public int drRoll() {
            return 1 + level*2;
        }

        @Override
        public boolean interact() {
            Buff.affect(Dungeon.hero, Recharging.class, ((float)this.HP/(float)this.HT)*5f);
            die(WandOfThornvines.class);
            return true;
        }

        public ThornVine spawnAt(int pos, int level, float chargesPercent ) {
            if (Dungeon.level.passable[pos]) {
                ThornVine TV = new ThornVine(level, chargesPercent);
                if (Actor.findChar(pos) == null) {
                    TV.pos = pos;
                } else {
                    int closest = -1;
                    boolean[] passable = Dungeon.level.passable;
                    for (int n : PathFinder.NEIGHBOURS9) {
                        int c = pos + n;
                        if (passable[c] && Actor.findChar( c ) == null
                                && (closest == -1 || (Dungeon.level.trueDistance(c, curUser.pos) < (Dungeon.level.trueDistance(closest, curUser.pos))))) {
                            closest = c;
                        }
                    }

                    if (closest == -1){
                        curUser.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + level/2);
                        return null; //do not spawn Thorn Vine
                    } else {
                        TV.pos = closest;
                    }
                }

                TV.HP = TV.HT = setHP();
                GameScene.add(TV);
                TV.state = TV.HUNTING;

                TV.sprite.centerEmitter().burst(MagicMissile.EarthParticle.BURST, 5 + level / 2);

                return TV;
            } else {
                return null;
            }
        }
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        if (Random.Int( staff.level() + 3 ) >= 2) {

            Buff.affect(defender, Bleeding.class).set(damage/3f);
            Splash.at( defender.sprite.center(), -PointF.PI / 2, PointF.PI / 6,
                    defender.sprite.blood(), 10 );

        }

    }

    @Override
    protected void fx(Ballistica bolt, Callback callback) {
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.FOLIAGE_CONE,
                curUser.sprite,
                bolt.path.get(bolt.dist),
                callback );
    }
    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color( ColorMath.random(0x004400, 0x88CC44) );
        particle.am = 1f;
        particle.setLifespan(1f);
        particle.setSize( 1f, 1.5f);
        particle.shuffleXY(0.5f);
        float dst = Random.Float(11f);
        particle.x -= dst;
        particle.y += dst;
    }

    @Override
    protected int initialCharges() {
        return 4;
    }

    protected int chargesPerCast() {
        return Math.max(1, curCharges);
    }
}
