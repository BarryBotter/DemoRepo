package my.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

import my.game.Game;
import my.game.states.Play;

import static my.game.handlers.B2DVars.PPM;

public class Bullet extends B2DSprite implements Pool.Poolable {

    public Vector2 position;
    public boolean alive;

    //Velocity of the bullet.
    private static final float BULLET_SPEED = 300f;
    // Cooldown between shooting bullets.
    private static final float BULLET_COOL_DOWN_TIMER = 0.4f;

    private static boolean coolDownSet = false;
    private float actionBeginTime = 0;

    /**
     * Bullet constructor. Just initialize variables.
     */
    public Bullet(Body body) {
        super(body);
        Texture tex = Game.res.getTexture("bulletParticles");
        TextureRegion[] sprites = TextureRegion.split(tex,tex.getWidth(),tex.getHeight())[0];
        setAnimation(sprites, 1/8f);
        this.position = new Vector2();
        this.alive = false;
    }

    /**
     * Initialize the bullet. Call this method after getting a bullet from the pool.
     */
    public void init(float touchPointX, float touchPointY, boolean belowPlayer) {
        position.set(touchPointX,  touchPointY);
        alive = true;
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

    /**
     * Callback method when the object is freed. It is automatically called by Pool.free()
     * Must reset every meaningful field of this bullet.
     */
    @Override
    public void reset() {
        position.set(0,0);
        alive = false;
        body.setTransform(0,0, 0);
        body.setLinearVelocity(0,0);
        body.setAngularVelocity(0);
    }

    /**
     * Method called each frame, which updates the bullet.
     */
    public void update (float delta) {

        // update bullet position
        position.add(1*delta*60, 1*delta*60);

        // if bullet is out of screen, set it to dead
        if (isOutOfScreen()) alive = false;
    }

    private boolean isOutOfScreen() {
        if(position.x >= Gdx.graphics.getWidth() || position.y >= Gdx.graphics.getHeight()) {
            return true;
        }
        return false;
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

    public void bulletManager(Body playerBody, float touchPointX, float touchPointY) {
        // If the player has ammo and bullet is not on cool down, shoot the bullet.
        if (Player.returnNumberOfAmmo() > 0 && !returnCoolDownState() && !Melee.returnMeleeCoolDownState()) {
            //resetBullet(playerBody.getPosition().x, playerBody.getPosition().y);
            reset();
            // Check if the touch is below or above player.
            if (touchPointY / PPM >= playerBody.getPosition().y) {
                init(touchPointX / PPM, touchPointY / PPM, false);
            } else {
                init(touchPointX / PPM, (touchPointY / PPM) - playerBody.getPosition().y, true);
            }
        }
        else {
            // After teh bullet's been shot, deploy a little cool down.
            checkBulletCoolDown();
        }
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

    private void resetBullet(float playerX, float playerY) {
        body.setTransform(playerX,playerY, 0);
        body.setLinearVelocity(0,0);
        body.setAngularVelocity(0);
    }
}