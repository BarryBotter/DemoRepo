package my.game.handlers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

import my.game.Game;
import my.game.entities.Player;

import static my.game.handlers.B2DVars.SOUND_LEVEL;

public class MyContactListener implements ContactListener {
    private int numFootContacts;
    private boolean winContact;

    private Array<Body> bodiesToRemove;
    private Array<Body> enemyBodiesToRemove;
    private Array<Body> bulletBodiesToRemove;
    private Array<Body> meleeBodiesToRemove;
    private Array<Body> trapBodiesToRemove;

    public MyContactListener() {
        bodiesToRemove = new Array<Body>();
        enemyBodiesToRemove = new Array<Body>();
        bulletBodiesToRemove = new Array<Body>();
        meleeBodiesToRemove = new Array<Body>();
        trapBodiesToRemove = new Array<Body>();
        winContact = false;
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
            Game.res.getSound("pickup").play(SOUND_LEVEL);
            bodiesToRemove.add(fa.getBody());
            Player.increaseAmmo();
        }
        if(fb.getUserData() != null && fb.getUserData().equals("crystal")){
            Game.res.getSound("pickup").play(SOUND_LEVEL);
            bodiesToRemove.add(fb.getBody());
            Player.increaseAmmo();
        }

        // Check collision between player and win block.
        if(fa.getUserData() != null && fa.getUserData().equals("win")){
            if(fb.getUserData().equals("player")) {
                winContact = true;
            }
        }
        if(fb.getUserData() != null && fb.getUserData().equals("win")){
            if(fa.getUserData().equals("player")) {
                winContact = true;
            }
        }

        // Check collisions between enemy and player/bullet/melee attack.
        if(fa.getUserData() != null && fa.getUserData().equals("enemy")) {
            if(fb.getUserData().equals("player")) {
                enemyBodiesToRemove.add(fa.getBody());
                Game.res.getSound("snap").play(SOUND_LEVEL);
                Player.loseHealth();
            }
            else if(fb.getUserData().equals("bullet")) {
                enemyBodiesToRemove.add(fa.getBody());
                Game.res.getSound("hit").play();
                bulletBodiesToRemove.add(fb.getBody());
                Player.increaseEnemyKC();
            }
            else if(fb.getUserData().equals("melee")) {
                enemyBodiesToRemove.add(fa.getBody());
                Game.res.getSound("hit").play(SOUND_LEVEL);
                meleeBodiesToRemove.add(fb.getBody());
                Player.increaseEnemyKC();
            }
        }
        if(fb.getUserData() != null && fb.getUserData().equals("enemy")) {
            if(fa.getUserData().equals("player")) {
                enemyBodiesToRemove.add(fb.getBody());
                Game.res.getSound("snap").play(SOUND_LEVEL);
                Player.loseHealth();
            }
            else if(fa.getUserData().equals("bullet")) {
                enemyBodiesToRemove.add(fb.getBody());
                Game.res.getSound("hit").play(SOUND_LEVEL);
                bulletBodiesToRemove.add(fa.getBody());
                Player.increaseEnemyKC();
            }
            else if(fa.getUserData().equals("melee")) {
                enemyBodiesToRemove.add(fb.getBody());
                Game.res.getSound("hit").play(SOUND_LEVEL);
                meleeBodiesToRemove.add(fa.getBody());
                Player.increaseEnemyKC();
            }
        }
        // Check collision between player/bullet and traps.
        if (fb.getUserData() != null && fb.getUserData().equals("trap")) {
            if (fa.getUserData().equals("player")) {
                Player.loseHealth();
                Game.res.getSound("snap").play();
                trapBodiesToRemove.add(fb.getBody());
            }
            else if (fa.getUserData().equals("bullet")) {
                Game.res.getSound("hit").play();
                trapBodiesToRemove.add(fb.getBody());
                bulletBodiesToRemove.add(fa.getBody());
                Player.increaseEnemyKC();
            }
            else if (fa.getUserData().equals("melee")) {
                Game.res.getSound("hit").play();
                trapBodiesToRemove.add(fb.getBody());
                meleeBodiesToRemove.add(fa.getBody());
                Player.increaseEnemyKC();
            }
        }
        if (fa.getUserData() != null && fa.getUserData().equals("trap")) {
            if (fb.getUserData().equals("player")) {
                Player.loseHealth();
                Game.res.getSound("snap").play();
                trapBodiesToRemove.add(fa.getBody());
            }
            else if (fb.getUserData().equals("bullet")) {
                Game.res.getSound("hit").play();
                trapBodiesToRemove.add(fa.getBody());
                bulletBodiesToRemove.add(fb.getBody());
                Player.increaseEnemyKC();
            }
            else if (fb.getUserData().equals("melee")) {
                Game.res.getSound("hit").play();
                trapBodiesToRemove.add(fa.getBody());
                meleeBodiesToRemove.add(fb.getBody());
                Player.increaseEnemyKC();
            }
        }

        // Check collision between bullet and ground
        if(fa.getUserData() != null && fa.getUserData().equals("ground")){
            if(fb.getUserData().equals("bullet")) {
                //bulletBodiesToRemove.add(fb.getBody());
            }
        }
        if(fb.getUserData() != null && fb.getUserData().equals("ground")){
            if(fa.getUserData().equals("bullet")) {
                //bulletBodiesToRemove.add(fa.getBody());
            }
        }
        if (fa.getUserData() != null && fa.getUserData().equals("jump")) {
            if (fb.getUserData().equals("player")) {
                fb.getBody().applyLinearImpulse(1,6,0,0,true);
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
    public Array<Body> getMeleeHitBoxesToRemove(){return meleeBodiesToRemove;}
    public Array<Body> getTrapsToRemove(){return trapBodiesToRemove;}
    public boolean isPlayerWin() {
        return winContact;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
