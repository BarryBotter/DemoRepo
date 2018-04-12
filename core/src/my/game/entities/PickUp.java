package my.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import my.game.Game;

public class PickUp extends B2DSprite {
    public PickUp(Body body) {
        super(body);
        Texture tex = Game.res.getTexture("toothPaste");
        TextureRegion[] sprites = TextureRegion.split(tex,tex.getWidth(),tex.getHeight())[0];
        setAnimation(sprites, 1 / 12f);
    }
}
