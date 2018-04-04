package my.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import my.game.Game;
import my.game.states.Play;

/**
 * Created by jimbo on 3.4.2018.
 */

public class Melee extends Projectile {
    // Cooldown between shooting swinging melee attack.
    private static final float MELEE_COOLDOWN_TIMER = 0.3f;
    private boolean meleeCoolDownSet = false;
    private float actionBeginTime = 0;

    public Melee(Body body) {
        super(body);
        Texture tex = Game.res.getTexture("bullet");
        TextureRegion[] sprites = TextureRegion.split(tex,10,10)[0];
        setAnimation(sprites, 1/8f);
    }

    public void checkMeleeCoolDown() {
        if(Play.accumulator - actionBeginTime > MELEE_COOLDOWN_TIMER && meleeCoolDownSet){
            meleeCoolDownSet = false;
            actionBeginTime = 0;
            body.setTransform(-500,-500,0);
        }
    }

    public boolean returnMeleeCoolDownState() {
        return meleeCoolDownSet;
    }

    public void meleeSwing() {
        if(!meleeCoolDownSet) {
            actionBeginTime = Play.accumulator;
            meleeCoolDownSet = true;
        }
    }
}
