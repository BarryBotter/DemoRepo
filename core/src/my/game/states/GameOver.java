package my.game.states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import my.game.Game;
import my.game.entities.Background;
import my.game.handlers.GameButton;
import my.game.handlers.GameStateManager;

import static my.game.handlers.B2DVars.SOUND_LEVEL;

public class GameOver extends GameState {

    private Background bg;
    private GameButton playButton;
    private GameButton exitButton;

    public GameOver(GameStateManager gsm) {
        super(gsm);

        Texture tex = Game.res.getTexture("gameOver");
        bg = new Background(new TextureRegion(tex),hudCam,5 );
        bg.setVector(0, 0);

        tex = Game.res.getTexture("main");
        TextureRegion[] menuButtons;
        menuButtons = new TextureRegion[5];
        menuButtons[0] =  new TextureRegion(tex, 340, 40, 200, 100);
        menuButtons[1] =  new TextureRegion(tex, 340, 125, 200, 100);
        playButton = new GameButton(menuButtons[0], Game.V_WIDTH, 0, cam);
        exitButton = new GameButton(menuButtons[1], 0, 0, cam);

        cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);

        game.pauseMusic();
        Game.res.getSound("over").play(SOUND_LEVEL);
    }


    public void handleInput() {
        if(playButton.isClicked()) {
            Game.res.getSound("over").stop();
            Game.res.getSound("buttonClick").play(SOUND_LEVEL);
            gsm.setState(GameStateManager.PLAY);
        }
        else if(exitButton.isClicked()){
            Game.res.getSound("over").stop();
            Game.res.getSound("buttonClick").play(SOUND_LEVEL);
            gsm.setState(GameStateManager.MENU);
            game.pauseMusic();
            game.resumeMenuMusic();
        }
    }

    public void update(float dt) {
        handleInput();
        bg.update(dt);
        playButton.update(dt);
        exitButton.update(dt);
    }

    public void render() {
        sb.setProjectionMatrix(cam.combined);
        bg.render(sb);
        playButton.render(sb);
        exitButton.render(sb);
    }

    public void dispose() {
    }
}
