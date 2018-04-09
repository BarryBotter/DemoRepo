package my.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import my.game.Game;

public class Traps extends B2DSprite {

    public Traps(Body body) {
            super(body);
            Texture tex = Game.res.getTexture("trap");
            TextureRegion[] sprites = TextureRegion.split(tex,32,32)[0];
            setAnimation(sprites, 1 / 8f);
        }
}
