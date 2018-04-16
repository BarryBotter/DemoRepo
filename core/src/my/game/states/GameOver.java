package my.game.states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import my.game.Game;
import my.game.entities.Background;
import my.game.handlers.GameButton;
import my.game.handlers.GameStateManager;

import static my.game.handlers.B2DVars.PPM;
import static my.game.handlers.B2DVars.SOUND_LEVEL;

/**
 * Created by Katriina on 27.3.2018.
 */

public class GameOver extends GameState {

    private Background bg;
    private GameButton playButton;
    private GameButton exitButton;
    Play play;

    private World world;
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

        play = new Play(gsm);

        cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);

        game.pauseMusic();
        Game.res.getSound("over").play(SOUND_LEVEL);

        world = new World(new Vector2(0, -9.8f * 5), true);

    }


    public void handleInput() {

        if(playButton.isClicked()) {
            Game.res.getSound("over").stop();
            gsm.setState(GameStateManager.PLAY);
        }
        else if(exitButton.isClicked()){
            Game.res.getSound("over").stop();
            game.resumeMenuMusic();
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


    }

    public void dispose() {
        play.dispose();
    }

}
