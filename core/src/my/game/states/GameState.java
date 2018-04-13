package my.game.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import my.game.Game;
import my.game.handlers.BoundedCamera;
import my.game.handlers.GameStateManager;

public abstract class GameState{

    protected Game game;
    protected SpriteBatch sb;
    protected BoundedCamera cam;
    OrthographicCamera hudCam;
    GameStateManager gsm;

    GameState(GameStateManager gsm){
        this.gsm = gsm;
        game = gsm.game();
        sb = game.getSpriteBatch();
        cam = Game.getCamera();
        hudCam = game.getHUDCamera();
    }

    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();

}


