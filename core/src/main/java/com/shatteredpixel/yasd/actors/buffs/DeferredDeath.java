package com.shatteredpixel.yasd.actors.buffs;

import com.shatteredpixel.yasd.actors.blobs.Blob;
import com.shatteredpixel.yasd.actors.blobs.CorrosiveGas;
import com.shatteredpixel.yasd.actors.blobs.Miasma;
import com.shatteredpixel.yasd.items.weapon.enchantments.Grim;
import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.scenes.GameScene;
import com.shatteredpixel.yasd.sprites.CharSprite;
import com.shatteredpixel.yasd.ui.BuffIndicator;

public class DeferredDeath extends FlavourBuff {

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.DARKENED);
        else if (target.invisible == 0) target.sprite.remove(CharSprite.State.DARKENED);
    }

    @Override
    public int icon() {
        return BuffIndicator.CORRUPT;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc");
    }

    @Override
    public boolean act() {
        return super.act();
    }

    @Override
    public void detach() {
        super.detach();
        GameScene.add(Blob.seed(target.pos, 100, Miasma.class));
        target.damage(target.HP, Grim.class);
    }
}