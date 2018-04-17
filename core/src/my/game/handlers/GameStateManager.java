package my.game.handlers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import java.util.Stack;

import my.game.Game;
import my.game.states.GameOver;
import my.game.states.GameState;
import my.game.states.LevelComplete;
import my.game.states.LevelSelect;
import my.game.states.Menu;
import my.game.states.Options;
import my.game.states.Play;
import my.game.states.cutScene;

/**
 * Created by Katriina on 20.3.2018.
 */

public class GameStateManager {
    private my.game.Game game;
    private Stack<GameState> gameStates;

    public static final int PLAY = 2182301;
    public static final int MENU = 823183;
    public static final int LEVEL_SELECT = 323971;
    public static final int GAMEOVER = 213212;
    public static final int LEVEL_COMPLETE = 281209;
    public static final int OPTIONS = 345678;
    public static final int CUTSCENE = 555768;

    public ImageButton.ImageButtonStyle
            playStyle,optionStyle,exitStyle,
            toothStyle,backStyle,rightStyle, soundStyle,
            bubbleStyle, easyStyle,normalStyle,hardStyle;

    Texture tex;
    TextureRegion menuButtons[];
    public  Stage stage;

    public GameStateManager(Game game){
        this.game = game;
        makeStyles();
        stage = new Stage();
        gameStates = new Stack<GameState>();
        pushState(MENU);
    }

    public Game game(){return game;}

    public void update(float dt){
        gameStates.peek().update(dt);
    }

    public void render(){
        gameStates.peek().render();
    }

    private GameState getState(int state){

        if (state == MENU){
            return new Menu(this);
        }
        if (state == PLAY)
        {
            return new Play(this);
        }
        if (state == LEVEL_SELECT)
        {
            return new LevelSelect(this);
        }
        if (state == GAMEOVER)
        {
            return new GameOver(this);
        }
        if (state == LEVEL_COMPLETE)
        {
            return new LevelComplete(this);
        }
        if (state == OPTIONS)
        {
            return new Options(this);
        }
        if (state == CUTSCENE)
        {
            return new cutScene(this);
        }
        return null;
    }

    public void setState(int state){
        popState();
        pushState(state);
    }

    private void pushState(int state){
        gameStates.push(getState(state));
    }

    private void popState(){
        GameState g = gameStates.pop();
        g.dispose();
    }

    private void makeStyles() {
        //styles for buttons

        //todo load from a bigger texture
        tex = my.game.Game.res.getTexture("main");
        menuButtons = new TextureRegion[5];
        menuButtons[0] =  new TextureRegion(tex, 340, 40, 200, 100);
        menuButtons[1] =  new TextureRegion(tex, 340, 125, 200, 100);

        TextureRegion play = new TextureRegion(game.res.getTexture("play"));
        TextureRegion options = new TextureRegion(game.res.getTexture("settings"));
        TextureRegion exit = new TextureRegion(game.res.getTexture("exit"));
        TextureRegion tooth = new TextureRegion(game.res.getTexture("tooth_80"));
        TextureRegion back = new TextureRegion(game.res.getTexture("back"));
        TextureRegion right = new TextureRegion(game.res.getTexture("right"));
        TextureRegion sound_off = new TextureRegion(game.res.getTexture("sound_off"));
        TextureRegion sound_on = new TextureRegion(game.res.getTexture("sound_on"));
        TextureRegion tooth_easy = new TextureRegion(game.res.getTexture("easy"));
        TextureRegion tooth_normal = new TextureRegion(game.res.getTexture("normal"));
        TextureRegion tooth_hard = new TextureRegion(game.res.getTexture("hard"));
        TextureRegion bubble = new TextureRegion(game.res.getTexture("bubble"));

        easyStyle = new  ImageButton.ImageButtonStyle();
        easyStyle.up = new TextureRegionDrawable(tooth_easy);
        normalStyle = new  ImageButton.ImageButtonStyle();
        normalStyle.up = new TextureRegionDrawable(tooth_normal);
        hardStyle = new  ImageButton.ImageButtonStyle();
        hardStyle.up = new TextureRegionDrawable(tooth_hard);
        soundStyle = new ImageButton.ImageButtonStyle();
        soundStyle.imageUp = new TextureRegionDrawable(sound_off);
        soundStyle.imageChecked = new TextureRegionDrawable(sound_on);
        bubbleStyle = new ImageButton.ImageButtonStyle();
        bubbleStyle.imageUp = new TextureRegionDrawable(bubble);
        backStyle = new ImageButton.ImageButtonStyle();
        backStyle.imageUp = new TextureRegionDrawable(back);
        rightStyle = new ImageButton.ImageButtonStyle();
        rightStyle.imageUp = new TextureRegionDrawable(right);
        playStyle = new ImageButton.ImageButtonStyle();
        playStyle.imageUp = new TextureRegionDrawable(play);
        optionStyle = new ImageButton.ImageButtonStyle();
        optionStyle.imageDown = new TextureRegionDrawable(options);
        optionStyle.imageUp = new TextureRegionDrawable(options);
        exitStyle = new ImageButton.ImageButtonStyle();
        exitStyle.imageDown = new TextureRegionDrawable(exit);
        exitStyle.imageUp = new TextureRegionDrawable(exit);
        toothStyle = new ImageButton.ImageButtonStyle(); // style which uses the appicon
        toothStyle.imageDown = new TextureRegionDrawable(tooth);
        toothStyle.imageUp = new TextureRegionDrawable(tooth);
    }

}
