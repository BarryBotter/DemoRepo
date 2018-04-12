package my.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import my.game.Game;
import my.game.states.Play;

public class Melee extends Projectile {
    // Cool down between shooting swinging melee attack.
    private static final float MELEE_COOL_DOWN_TIMER = 0.3f;
    private static boolean meleeCoolDownSet = false;
    private float actionBeginTime = 0;

    public Melee(Body body) {
        super(body);
        Texture tex = Game.res.getTexture("bullet");
        TextureRegion[] sprites = TextureRegion.split(tex,10,10)[0];
        setAnimation(sprites, 1/8f);
    }

    public void checkMeleeCoolDown() {
        if(Play.accumulator - actionBeginTime > MELEE_COOL_DOWN_TIMER && meleeCoolDownSet){
            meleeCoolDownSet = false;
            actionBeginTime = 0;
            body.setTransform(-500,-500,0);
        }
    }

    public static boolean returnMeleeCoolDownState() {
        return meleeCoolDownSet;
    }

    private void meleeSwing() {
        if(!meleeCoolDownSet) {
            actionBeginTime = Play.accumulator;
            meleeCoolDownSet = true;
        }
    }

    public void meleeManager(Body playerBody) {
        //If melee swing is not on cool down make melee hit box appear in front of the player.
        if (!returnMeleeCoolDownState() && !Projectile.returnCoolDownState() && Player.returnNumberOfAmmo() == 0) {
            meleeSwing();

            body.setTransform(playerBody.getPosition().x + 0.4f, playerBody.getPosition().y, 0);
        } else {
            // Keep checking the cool down until it's ready to be used again.
            checkMeleeCoolDown();
        }
    }
}
