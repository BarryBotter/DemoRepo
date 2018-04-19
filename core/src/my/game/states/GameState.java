package my.game.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import my.game.Game;
import my.game.handlers.BoundedCamera;
import my.game.handlers.GameStateManager;

/**
 * Created by Katriina on 20.3.2018.
 */

public abstract class GameState{

    protected Game game;
    protected SpriteBatch sb;
    protected BoundedCamera cam;
    protected OrthographicCamera bigCam;
    OrthographicCamera hudCam;
    GameStateManager gsm;

    GameState(GameStateManager gsm){
        this.gsm = gsm;
        game = gsm.game();
        sb = game.getSpriteBatch();
        cam = Game.getCamera();
        hudCam = game.getHUDCamera();
        bigCam = game.getBigCam();
    }

    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();

}


