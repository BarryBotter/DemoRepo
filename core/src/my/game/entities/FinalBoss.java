package my.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import my.game.Game;

public class FinalBoss extends B2DSprite{
    public FinalBoss(Body body) {
        super(body);
        Texture tex = Game.res.getTexture("enemyTooth");
        TextureRegion[] sprites = TextureRegion.split (tex,tex.getWidth() /4,tex.getHeight())[0];
        setAnimation(sprites, 1/12f);
    }

    public void setBossLocation(float locationX, float locationY) {
        body.setTransform(locationX + 0.1f,locationY + 0.1f,0);
    }
}