package my.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import my.game.Game;
import my.game.handlers.GameButton;
import my.game.handlers.GameStateManager;

import static my.game.handlers.B2DVars.SOUND_LEVEL;


public class LevelSelect extends GameState {

    private TextureRegion reg;
    private GameButton[][] buttons;
    private int lvl;
    private Skin mySkin;
    private Stage stage;
    private Button exitButton,cutSceneButton;

    public LevelSelect(final GameStateManager gsm) {
        super(gsm);
        lvl = Game.lvls.getInteger("key");
        //skin and stage
        mySkin = game.mySkin;
        stage = gsm.stage;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        reg = new TextureRegion(Game.res.getTexture("menubg"), 0, 0, 320, 240);
        cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);

        buttons();
        lvlSelectGrid();
    }

    private void buttons(){
        //backbutton (not in lvlgridthingy)
        exitButton = new TextButton("BACK",mySkin,"default");
        exitButton.setSize(200, 100);
        exitButton.setPosition(100,100);
        exitButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                gsm.setState(GameStateManager.MENU);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;}
        });
        stage.addActor(exitButton);

        //button for testing cutscene (not in lvlgridthingy)
        cutSceneButton = new TextButton("cutSceneDemo",mySkin,"default");
        cutSceneButton.setSize(200, 100);
        cutSceneButton.setPosition(100,250);
        cutSceneButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                gsm.setState(GameStateManager.CUTSCENE);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;}
        });
        stage.addActor(cutSceneButton);
    }

    private void lvlSelectGrid(){
        //todo levelselectgrid has some hitboxproblems
        TextureRegion buttonReg = new TextureRegion(Game.res.getTexture("hud"), 0, 0, 32, 32);
        buttons = new GameButton[3][3];
        for(int row = 0; row < buttons.length; row++) {
            for(int col = 0; col < buttons[0].length; col++) {
                buttons[row][col] = new GameButton(buttonReg, 150 + col *40, 160 - row * 40, cam);
                buttons[row][col].setText(row * buttons[0].length + col + 1 + "");
            }
        }
    }

    @Override
    public void handleInput() {
    }

    @Override
    public void update(float dt) {

        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[0].length; col++) {
                buttons[row][col].update(dt);
                if (buttons[row][col].isClicked()) {
                    Play.level = row * buttons[0].length + col + 1;
                    if (Play.level <= lvl) {
                        Game.res.getSound("snap").play(SOUND_LEVEL);
                        gsm.setState(GameStateManager.PLAY);
                    }
                }
            }
        }

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown (int keycode) {
                //If Android back button is pressed, go back to menu.
                if(keycode == Input.Keys.BACK) {
                    gsm.setState(GameStateManager.MENU);
                }
                return false;
            }
        });
    }

    @Override
    public void render() {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(reg, 0, 0);
        sb.end();
        for(int row = 0; row < buttons.length; row++) {
            for(int col = 0; col < buttons[0].length; col++) {
                buttons[row][col].render(sb);
            }
        }
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.clear();
    }
}

