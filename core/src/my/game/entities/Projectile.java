package my.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import my.game.Game;
import my.game.states.Play;

import static my.game.handlers.B2DVars.PPM;

public class Projectile extends B2DSprite{
    //Velocity of the bullet.
    private static final float BULLET_SPEED = 300f;
    // Cooldown between shooting bullets.
    private static final float BULLET_COOL_DOWN_TIMER = 0.4f;

    private static boolean coolDownSet = false;
    private float actionBeginTime = 0;

    public Projectile(Body body) {
        super(body);
        Texture tex = Game.res.getTexture("bulletParticles");
        TextureRegion[] sprites = TextureRegion.split(tex,tex.getWidth(),tex.getHeight())[0];
        setAnimation(sprites, 1/8f);
    }

    private void resetBullet(float playerX, float playerY) {
        body.setTransform(playerX,playerY, 0);
        body.setLinearVelocity(0,0);
        body.setAngularVelocity(0);
    }

    public void checkBulletCoolDown() {
        if(Play.accumulator - actionBeginTime > BULLET_COOL_DOWN_TIMER && coolDownSet){
            coolDownSet = false;
            actionBeginTime = 0;
        }
    }

    static boolean returnCoolDownState() {
        return coolDownSet;
    }

    private void shootBullet(float touchPointX, float touchPointY, boolean belowPlayer) {
        if(!coolDownSet) {
            if(body.getLinearVelocity().epsilonEquals(0,0)) {
                if (Player.returnNumberOfAmmo() > 0) {
                    if(belowPlayer) {
                        body.applyForceToCenter(touchPointX * BULLET_SPEED, touchPointY * BULLET_SPEED, true);
                    }
                    else {
                        body.applyForceToCenter(touchPointX * BULLET_SPEED, touchPointY * BULLET_SPEED, true);
                    }
                    actionBeginTime = Play.accumulator;
                    coolDownSet = true;
                    Player.decreaseAmmo();
                }
            }
        }
    }

    public void bulletManager(Body playerBody, float touchPointX, float touchPointY) {
        // If the player has ammo and bullet is not on cool down, shoot the bullet.
        if (Player.returnNumberOfAmmo() > 0 && !returnCoolDownState() && !Melee.returnMeleeCoolDownState()) {
            resetBullet(playerBody.getPosition().x, playerBody.getPosition().y);
            // Check if the touch is below or above player.
            if (touchPointY / PPM >= playerBody.getPosition().y) {
                shootBullet(touchPointX / PPM, touchPointY / PPM, false);
            } else {
                shootBullet(touchPointX / PPM, (touchPointY / PPM) - playerBody.getPosition().y, true);
            }
        }
        else {
            // After teh bullet's been shot, deploy a little cool down.
            checkBulletCoolDown();
        }
    }
}