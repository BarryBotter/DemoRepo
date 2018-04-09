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
        Texture tex = Game.res.getTexture("happyTooth");
        TextureRegion[] sprites = TextureRegion.split(tex,35,34)[0];
        setAnimation(sprites, 1/12f);
    }

    public void setCurrentLocation(float x) {
        currentX = x;
    }

    public float returnCurrentLocation() {
        return currentX;
    }

    public void switchDirection() {
        directionToGo = !directionToGo;
    }

    public boolean returnDirection() {
        return directionToGo;
    }
}