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
import my.game.states.Play;

import static my.game.handlers.B2DVars.PPM;

/**
 * Created by Katriina on 21.3.2018.
 */



public class MyContacListener implements ContactListener {

    private int numFootContacts;
    public boolean wincontact;
    private Array<Body> bodiesToRemove;

    public MyContacListener() {
        bodiesToRemove = new Array<Body>();
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
        if (fa.getUserData() != null && fa.getUserData().equals("crystal")) {
            //remove pickup
            bodiesToRemove.add(fa.getBody());
        }
        if (fb.getUserData() != null && fb.getUserData().equals("crystal")) {
            bodiesToRemove.add(fb.getBody());
        }
        if (fa.getUserData() != null && fa.getUserData().equals("win")) {
            wincontact = true;
        }
        if (fb.getUserData() != null && fb.getUserData().equals("win")) {
            wincontact = true;
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
