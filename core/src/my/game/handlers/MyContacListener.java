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
        if (fa.getUserData() != null && fa.getUserData().equals("jump")) {
            System.out.print("jump baby");
        }
        if (fb.getUserData() != null && fb.getUserData().equals("jump")) {
            System.out.print("jump baby");
        }
        if (fa.getUserData() != null && fa.getUserData().equals("corner")) {
           // cornerCollision = true;

            float player_bottom = fa.getBody().getPosition().y + 15 / PPM;
            float tiles_bottom = fb.getBody().getPosition().y + 32/ PPM;
            float player_right = fa.getBody().getPosition().x + 13 / PPM;
            float tiles_right = fb.getBody().getPosition().x + 32 / PPM;

            float b_collision = tiles_bottom -fa.getBody().getPosition().y;
            float t_collision = player_bottom - fb.getBody().getPosition().y;
            float l_collision = player_right - fa.getBody().getPosition().x;
            float r_collision = tiles_right - fb.getBody().getPosition().x;


            if (l_collision < r_collision && l_collision < t_collision && l_collision < b_collision)
            {
                Game.res.getSound("hit").play();
            }
            if (t_collision < b_collision && t_collision < l_collision && t_collision < r_collision )
            {
                Game.res.getSound("hit").play();
            }

        }
        if (fb.getUserData() != null && fb.getUserData().equals("corner")) {
           // cornerCollision = true;

            float player_bottom = fb.getBody().getPosition().y+ 15 / PPM;
            float tiles_bottom = fa.getBody().getPosition().y + 32 /PPM;
            float player_right = fb.getBody().getPosition().x + 13 / PPM;
            float tiles_right = fa.getBody().getPosition().x + 32 / PPM;

            float b_collision = tiles_bottom -fb.getBody().getPosition().y;
            float t_collision = player_bottom - fa.getBody().getPosition().y;
            float l_collision = player_right - fb.getBody().getPosition().x;
            float r_collision = tiles_right - fa.getBody().getPosition().x;


            if (l_collision < r_collision && l_collision < t_collision && l_collision < b_collision)
            {
                Game.res.getSound("hit").play();
            }
            if (t_collision < b_collision && t_collision < l_collision && t_collision > r_collision )
            {
                Game.res.getSound("hit").play();
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
