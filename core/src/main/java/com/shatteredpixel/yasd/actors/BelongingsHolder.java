package com.shatteredpixel.yasd.actors;

import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.buffs.AdrenalineSurge;
import com.shatteredpixel.yasd.actors.buffs.Barkskin;
import com.shatteredpixel.yasd.actors.buffs.Weakness;
import com.shatteredpixel.yasd.actors.hero.Belongings;
import com.shatteredpixel.yasd.actors.mobs.Mob;
import com.shatteredpixel.yasd.items.KindOfWeapon;
import com.shatteredpixel.yasd.items.armor.Armor;
import com.shatteredpixel.yasd.items.potions.elixirs.ElixirOfMight;
import com.shatteredpixel.yasd.items.rings.RingOfAccuracy;
import com.shatteredpixel.yasd.items.rings.RingOfEvasion;
import com.shatteredpixel.yasd.items.rings.RingOfFuror;
import com.shatteredpixel.yasd.items.rings.RingOfMight;
import com.shatteredpixel.yasd.items.weapon.enchantments.Blocking;
import com.shatteredpixel.yasd.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.yasd.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.yasd.messages.Messages;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class BelongingsHolder extends Mob {
    public Belongings belongings;
    public int currentWeapon = 0;
    public int STR;
    public int attackSkill;
    public int defenseSkill;

    @Override
    public int attackSkill( Char target ) {
        resetWeapon();
        KindOfWeapon wep = getCurrentWeapon();

        float accuracy = 1;
        accuracy *= RingOfAccuracy.accuracyMultiplier( this );

        if (wep instanceof MissileWeapon){
            if (Dungeon.level.adjacent( pos, target.pos )) {
                accuracy *= 0.5f;
            } else {
                accuracy *= 1.5f;
            }
        }


        if (wep != null) {
            return (int)(attackSkill * accuracy * wep.accuracyFactor( this ));
        } else {
            return (int)(attackSkill * accuracy);
        }
    }

    @Override
    public int defenseSkill( Char enemy ) {

        float evasion = defenseSkill;

        evasion *= RingOfEvasion.evasionMultiplier( this );

        if (paralysed > 0) {
            evasion /= 2;
        }
        if (belongings != null) {
            evasion = belongings.EvasionFactor(evasion);
        }


        return Math.round(evasion);
    }

    public void updateHT( boolean boostHP ){
        int curHT = HT;

        float multiplier = RingOfMight.HTMultiplier(this);
        HT = Math.round(multiplier * HT);

        if (buff(ElixirOfMight.HTBoost.class) != null){
            HT += buff(ElixirOfMight.HTBoost.class).boost();
        }

        if (boostHP){
            HP += Math.max(HT - curHT, 0);
        }
        HP = Math.min(HP, HT);
    }

    public float attackDelay() {
        float multiplier = 1f;
        multiplier /= numberOfWeapons();
        if (getCurrentWeapon() != null) {

            return getCurrentWeapon().speedFactor( this )*multiplier;//Two weapons = 1/2 attack speed

        } else {
            //Normally putting furor speed on unarmed attacks would be unnecessary
            //But there's going to be that one guy who gets a furor+force ring combo
            //This is for that one guy, you shall get your fists of fury!
            return RingOfFuror.attackDelayMultiplier(this);
        }
    }

    public int numberOfWeapons() {
        return belongings.getWeapons().size();
    }

    public void resetWeapon() {//After hitting with each getWeapons, return to first
        if (currentWeapon > (numberOfWeapons() - 1)) {
            currentWeapon = 0;
        }
    }

    public KindOfWeapon getCurrentWeapon() {
        resetWeapon();
        if (belongings.miscs[0] instanceof MissileWeapon) {
            return ((MissileWeapon)belongings.miscs[0]);
        }
        return Dungeon.hero.belongings.getWeapons().get(currentWeapon);
    }

    public int STR() {
        int STR = this.STR;

        STR += RingOfMight.strengthBonus( this );

        AdrenalineSurge buff = buff(AdrenalineSurge.class);
        if (buff != null){
            STR += buff.boost();
        }

        return (buff(Weakness.class) != null) ? STR - 1 : STR;
    }

    @Override
    public int drRoll() {
        int dr = 0;
        if (belongings.getArmors() != null) {
            ArrayList<Armor> Armors = belongings.getArmors();
            for (int i=0; i < Armors.size(); i++) {
                int armDr = Random.NormalIntRange( Armors.get(i).DRMin(), Armors.get(i).DRMax());
                if (STR() < Armors.get(i).STRReq()){
                    armDr -= 2*(Armors.get(i).STRReq() - STR());
                }
                if (armDr > 0) dr += armDr;
            }
        } if (belongings.getWeapons() != null)  {
            ArrayList<KindOfWeapon> Weapons = belongings.getWeapons();
            for (int i=0; i < Weapons.size(); i++) {
                int wepDr = Random.NormalIntRange(0,Weapons.get(i).defenseFactor(this));
                if (Weapons.get(i) instanceof MeleeWeapon & STR() < ((MeleeWeapon)Weapons.get(i)).STRReq()) {
                    wepDr -= 2 * (((MeleeWeapon)Weapons.get(i)).STRReq()) - STR();
                }
                if (wepDr > 0) dr += wepDr;
            }
        }
        Barkskin bark = buff(Barkskin.class);
        if (bark != null)               dr += Random.NormalIntRange( 0 , bark.level() );

        Blocking.BlockBuff block = buff(Blocking.BlockBuff.class);
        if (block != null)              dr += block.blockingRoll();

        return dr;
    }

    public BelongingsHolder() {
        super();
        name = Messages.get(this, "name");

        belongings = new Belongings( this );

    }

    @Override
    public boolean attack(Char enemy) {
        currentWeapon += 1;
        return super.attack(enemy);
    }
}
