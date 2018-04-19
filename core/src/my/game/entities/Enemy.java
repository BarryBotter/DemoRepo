package my.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import my.game.Game;

public class Enemy extends B2DSprite{
    //False to right, true to left
    private boolean directionToGo = false;
    private float currentX;

    public Enemy(Body body) {
        super(body);
        Texture tex = Game.res.getTexture("enemyTooth");
        TextureRegion[] sprites = TextureRegion.split (tex,tex.getWidth() /4,tex.getHeight())[0];
        setAnimation(sprites, 1/12f);
    }

    private void setCurrentLocation(float x) {
        currentX = x;
    }
    private float returnCurrentLocation() {
        return currentX;
    }
    private void switchDirection() {
        directionToGo = !directionToGo;
    }
    private boolean returnDirection() {
        return directionToGo;
    }

    public void enemyManager() {
        //Move enemy towards left side of the screen. If it stops, switch direction.
        if (returnCurrentLocation() - body.getPosition().x < 0 && returnDirection()) {
            body.setTransform(body.getPosition().x +0.05f,body.getPosition().y -0.01f, 0);
            switchDirection();
        }
        else if (returnCurrentLocation() - body.getPosition().x < 0 && !returnDirection()){
            body.setTransform(body.getPosition().x -0.05f,body.getPosition().y -0.01f, 0);
            switchDirection();
        }

        // Go right
        if(!returnDirection()) {
            body.setTransform(body.getPosition().x +0.01f,body.getPosition().y -0.01f, 0);
        }
        else {  //Go left
            body.setTransform(body.getPosition().x -0.01f,body.getPosition().y -0.01f, 0);
        }

        // Set current location to compare if enemy has stopped or not.
         setCurrentLocation(body.getPosition().x);
    }
}