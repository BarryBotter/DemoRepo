package my.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import my.game.Game;

/**
 * Created by Katriina on 22.3.2018.
 */

public class PickUp extends B2DSprite {
    public PickUp(Body body) {
        super(body);
        Texture tex = Game.res.getTexture("toothpaste");
        TextureRegion[] sprites = TextureRegion.split(tex,32,32)[0];
        setAnimation(sprites, 1 / 6f);
    }
}
