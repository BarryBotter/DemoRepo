package my.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;

import my.game.Game;
import my.game.handlers.BoundedCamera;
import my.game.states.Play;

/**
 * Created by jimbo on 26.3.2018.
 */

public class Projectile extends B2DSprite{
    //Velocity of the bullet.
    private static final float BULLET_SPEED = 300f;
    // Cooldown between shooting bullets.
    private static final float BULLET_COOLDOWN_TIMER = 0.4f;

    private boolean coolDownSet = false;
    private float actionBeginTime = 0;

    public Projectile(Body body) {
        super(body);
        Texture tex = Game.res.getTexture("bullet");
        TextureRegion[] sprites = TextureRegion.split(tex,10,10)[0];
        setAnimation(sprites, 1/8f);
    }

    public void resetBullet(float playerX, float playerY) {
        body.setTransform(playerX,playerY, 0);
        body.setLinearVelocity(0,0);
        body.setAngularVelocity(0);
    }

    public void checkBulletCoolDown() {
        if(Play.accumulator - actionBeginTime > BULLET_COOLDOWN_TIMER && coolDownSet){
            coolDownSet = false;
            actionBeginTime = 0;
        }
    }

    public boolean returnCoolDownState() {
        return coolDownSet;
    }

    public void shootBullet(float touchPointX, float touchPointY, boolean belowPlayer) {
        if(!coolDownSet) {
            if(body.getLinearVelocity().epsilonEquals(0,0)) {
                if (Player.returnNumberOfAmmo() > 0) {
                    if(belowPlayer) {
                       // body.applyLinearImpulse(touchPointX * BULLET_SPEED,-(touchPointY * BULLET_SPEED), body.getWorldCenter().x,body.getWorldCenter().y,true);
                        body.applyForceToCenter(touchPointX * BULLET_SPEED, touchPointY * BULLET_SPEED, true);
                    }
                    else {
                       // body.applyLinearImpulse(touchPointX * BULLET_SPEED,touchPointY * BULLET_SPEED, body.getWorldCenter().x,body.getWorldCenter().y,true);
                        body.applyForceToCenter(touchPointX * BULLET_SPEED, touchPointY * BULLET_SPEED, true);
                    }
                    actionBeginTime = Play.accumulator;
                    coolDownSet = true;
                    Player.decreaseAmmo();
                }
            }
        }
    }
}