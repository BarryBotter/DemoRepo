package my.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import my.game.Game;
import my.game.handlers.GameButton;
import my.game.handlers.GameStateManager;


public class LevelSelect extends GameState {

    private TextureRegion reg;
    private GameButton[][] buttons;
    private int lvl;
    private Skin mySkin;
    private Stage stage;
    private Button exitButton,cutSceneButton,rightButton,leftButton;
    Image lvlImg;
    private int width, height;
    BitmapFont font,font2;
    String lvlname,score,toothpaste;
    private StretchViewport viewport;

    private int levelScore;

    public LevelSelect(final GameStateManager gsm) {
        super(gsm);
        lvl = game.lvls.getInteger("key");
        width= Game.V_WIDTH*2;
        height= Game.V_HEIGHT*2;
        viewport = new StretchViewport(width,height, cam);
        mySkin = game.mySkin;
        stage = gsm.stage;
        font = game.font12;
        font.setColor(Color.BLACK);
        font2 = game.font24;
        font2.setColor(Color.BLACK);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        reg = new TextureRegion(Game.res.getTexture("menubg"), 0, 0, width, height);
        cam.setToOrtho(false, width, height);
        //todo current level from preferences
        Play.level = 1;
        lvlname = "Level number " + Play.level;
        toothpaste = "You collected 0/5 toothpaste";
        buttons();
        lvlSelectGrid();
        game.pauseMusic();
        game.resumeMenuMusic();
    }

    private void buttons(){
        //backbutton (not in lvlgridthingy)
        Button exitButton,cutSceneButton;
        exitButton = new TextButton("BACK",mySkin,"default");
        exitButton.setSize(200, 100);
        exitButton.setPosition(100,100);
        if (Play.level < 5)
        {
            lvlImg = new Image(Game.res.getTexture("olvi"));
        }
        else{
            lvlImg = new Image(Game.res.getTexture("testibg"));
        }
        lvlImg.setSize(width/2,height/2);
        lvlImg.setPosition(width/4,height/3);
        lvlImg.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                gsm.setState(GameStateManager.PLAY);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;}
        });
        stage.addActor(lvlImg);

        Table table = new Table();
        table.left().bottom();
        //button back to menu
        exitButton = new ImageButton(mySkin);
        exitButton.setStyle(gsm.backStyle);
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
        table.add(exitButton);
        //button for testing cutscene
        cutSceneButton = new ImageButton(mySkin);
        cutSceneButton.setStyle(gsm.toothStyle);
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
        table.add(cutSceneButton);

        stage.addActor(table);
        table.debug();

        //move to previous lvl
        leftButton = new ImageButton(mySkin);
        leftButton.setStyle(gsm.toothStyle);
        leftButton.setPosition(width/10,height/2,1);
        leftButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if (Play.level > 1)
                {
                    Play.level -= 1;
                    lvlname = "Level number " + Play.level;
                    buttons();
                }
                else if (Play.level == 1 ){
                    Play.level = 1;
                    lvlname = "Level number " + Play.level;
                    buttons();
                }
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;}
        });
        //move to next lvl
        rightButton = new ImageButton(mySkin);
        rightButton.setStyle(gsm.toothStyle);
        rightButton.setPosition(width*0.9f,height/2,1);
        rightButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if (Play.level < 9)
                {
                    Play.level += 1;
                    lvlname = "Level number " + Play.level;
                    buttons();
                }
                else if (Play.level == 9 ){
                    Play.level = 9;
                    lvlname = "Level number " + Play.level;
                    buttons();
                }
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;}
        });
        stage.addActor(leftButton);
        stage.addActor(rightButton);

    }

    @Override
    public void handleInput() {
    }

    @Override
    public void update(float dt) {
        levelScore = Game.scores.getInteger("score"+ String.valueOf(Play.level));

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
        sb.draw(reg, 0, 0); //background
        font2.draw(sb,lvlname,width/4,height*0.90f);//lvlname
        //(lvlImg,width/4,height/3,width/2,height/2,sb);
        font2.draw(sb,toothpaste,width/4,height/3.5f);
        font2.draw(sb,game.prefs.getString("name")+"'s highscore is:" + levelScore,width/4,height/4.5f);
        sb.end();

        stage.act();
        stage.draw();
    }

    void drawLvlImg(Image img, int x, int y, int width, int height, SpriteBatch sb){
        img.setSize(width,height);
        img.setPosition(x,y);
        img.draw(sb,1);
    }


    @Override
    public void dispose() {
        stage.clear();
    }
}

