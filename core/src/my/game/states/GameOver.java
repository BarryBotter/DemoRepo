package my.game.states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import my.game.Game;
import my.game.entities.Background;
import my.game.handlers.Animation;
import my.game.handlers.B2DVars;
import my.game.handlers.GameButton;
import my.game.handlers.GameStateManager;

import static my.game.handlers.B2DVars.PPM;

/**
 * Created by Katriina on 27.3.2018.
 */

public class GameOver extends GameState {


    private boolean debug = false;

    private Background bg;
    private GameButton playButton;
    private GameButton exitButton;

    private World world;
    private Box2DDebugRenderer b2dRenderer;

    private TextureRegion[] menuButtons;



    public GameOver(GameStateManager gsm) {

        super(gsm);

        Texture tex = Game.res.getTexture("gameover");
        bg = new Background(new TextureRegion(tex),hudCam,5 );
        bg.setVector(0, 0);


        tex = Game.res.getTexture("main");
        menuButtons = new TextureRegion[5];
        menuButtons[0] =  new TextureRegion(tex, 340, 40, 200, 100);
        menuButtons[1] =  new TextureRegion(tex, 340, 125, 200, 100);
        playButton = new GameButton(menuButtons[0], 250, 160, cam);
        exitButton = new GameButton(menuButtons[1], 150, 170, cam);



        cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);

        world = new World(new Vector2(0, -9.8f * 5), true);
        //world = new World(new Vector2(0, 0), true);
        b2dRenderer = new Box2DDebugRenderer();

    }


    public void handleInput() {

        if(playButton.isClicked()) {
            gsm.setState(GameStateManager.PLAY);
        }
        else if(exitButton.isClicked()){
            gsm.setState(GameStateManager.MENU);
        }

    }

    public void update(float dt) {

        handleInput();

        world.step(dt / 5, 8, 3);

        bg.update(dt);

        playButton.update(dt);

        exitButton.update(dt);

    }

    public void render() {

        sb.setProjectionMatrix(cam.combined);

        // draw background
        bg.render(sb);

        // draw button
        playButton.render(sb);
        exitButton.render(sb);


        // debug draw box2d
        if(debug) {
            cam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);
            b2dRenderer.render(world, cam.combined);
            cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);
        }

    }

    public void dispose() {

    }

}
