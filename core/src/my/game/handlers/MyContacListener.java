package my.game.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

import my.game.Game;
import my.game.entities.Player;
import my.game.entities.Projectile;
import my.game.states.Play;

import static my.game.handlers.B2DVars.PPM;

/**
 * Created by Katriina on 21.3.2018.
 */



public class MyContacListener implements ContactListener {

    private int numFootContacts;
    public boolean wincontact;
    private Array<Body> bodiesToRemove;
    private Array<Body> enemyBodiesToRemove;
    private Array<Body> bulletBodiesToRemove;

    public MyContacListener() {
        bodiesToRemove = new Array<Body>();
        enemyBodiesToRemove = new Array<Body>();
        bulletBodiesToRemove = new Array<Body>();
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
            numFootContacts++;
        }
        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            numFootContacts++;
        }

        // Check collision between player and pickups.
        if(fa.getUserData() != null && fa.getUserData().equals("crystal")){
            //remove pickup
            bodiesToRemove.add(fa.getBody());
            Player.increaseAmmo();
        }
        if(fb.getUserData() != null && fb.getUserData().equals("crystal")){
            bodiesToRemove.add(fb.getBody());
            Player.increaseAmmo();
        }

        // Check collision between player and win block.
        if(fa.getUserData() != null && fa.getUserData().equals("win")){
            wincontact = true;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("win")){
            wincontact = true;
        }

        // Check collisions between enemy and player/bullet.
        if(fa.getUserData() != null && fa.getUserData().equals("enemy")) {
            if(fb.getUserData().equals("player")) {
                enemyBodiesToRemove.add(fa.getBody());
                Game.res.getSound("snap").play();
                Player.loseHealth();
            }
            else if(fb.getUserData().equals("bullet")) {
                enemyBodiesToRemove.add(fa.getBody());
                Game.res.getSound("hit").play();
                Projectile.bulletHit();
                // bulletBodiesToRemove.add(fb.getBody());
            }
        }
        if(fb.getUserData() != null && fb.getUserData().equals("enemy")) {
            if(fa.getUserData().equals("player")) {
                enemyBodiesToRemove.add(fb.getBody());
                Game.res.getSound("snap").play();
                Player.loseHealth();
            }
            else if(fa.getUserData().equals("bullet")) {
                enemyBodiesToRemove.add(fb.getBody());
                Game.res.getSound("hit").play();
                Projectile.bulletHit();
                //  bulletBodiesToRemove.add(fa.getBody());
            }
        }
        if (fb.getUserData() != null && fb.getUserData().equals("trap")) {
            if (fa.getUserData().equals("player")) {
                Player.loseHealth();
            }
        }
        if (fa.getUserData() != null && fa.getUserData().equals("trap")) {
            if (fb.getUserData().equals("player")) {
                Player.loseHealth();
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
            numFootContacts--;
        }
        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            numFootContacts--;
        }
    }

    public boolean isPlayerOnGround() {
        return numFootContacts > 0;
    }

    public Array<Body> getBodiesToRemove() {
        return bodiesToRemove;
    }
    public Array<Body> getEnemyBodiesToRemove(){return enemyBodiesToRemove;}
    public Array<Body> getBulletBodiesToRemove(){return bulletBodiesToRemove;}
    public boolean isPlayerWin() {
        return wincontact;
    }



    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
