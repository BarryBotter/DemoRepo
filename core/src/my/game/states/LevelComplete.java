package my.game.states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import my.game.Game;
import my.game.entities.Background;
import my.game.handlers.GameStateManager;

public class LevelComplete extends GameState {


    private Background bg;

    protected LevelComplete(GameStateManager gsm) {
        super(gsm);

        Texture tex = Game.res.getTexture("menubg");
        bg = new Background(new TextureRegion(tex),hudCam,5 );
        bg.setVector(0, 0);
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {

    }

    @Override
    public void dispose() {

    }
}
