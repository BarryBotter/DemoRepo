package my.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import my.game.Game;
import my.game.handlers.B2DVars;

public class TextureDraw extends B2DSprite {
    private Texture tex;

    public TextureDraw(Body body, String key) {
        super(body);
        tex = Game.res.getTexture(key);
    }

    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(tex,body.getPosition().x * B2DVars.PPM - width / 2, body.getPosition().y * B2DVars.PPM - height/2,tex.getWidth(),tex.getHeight());
        sb.end();
    }
}
