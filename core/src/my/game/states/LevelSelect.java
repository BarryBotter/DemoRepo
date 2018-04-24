package my.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import my.game.Game;
import my.game.entities.Player;
import my.game.handlers.GameStateManager;

import static my.game.handlers.B2DVars.LVL_UNLOCKED;
import static my.game.handlers.B2DVars.SOUND_LEVEL;

public class LevelSelect extends GameState {
    private TextureRegion reg;
    private Skin mySkin;
    private Stage stage;
    private Button rightButton,leftButton;
    private int width, height;
    private BitmapFont font2;
    private String lvlname,toothpaste;
    private int levelScore;
    private int pasteScore;

    private int lvl;

    public LevelSelect(final GameStateManager gsm) {
        super(gsm);

        width= Game.V_WIDTH*2;
        height= Game.V_HEIGHT*2;

        StretchViewport viewport;
        viewport = new StretchViewport(width,height, cam);

        mySkin = game.mySkin;
        stage = gsm.stage;

        BitmapFont font;
        font = game.font12;
        font.setColor(Color.BLACK);
        font2 = game.font24;
        font2.setColor(Color.BLACK);

        stage = new Stage(viewport);
        reg = new TextureRegion(Game.res.getTexture("menubg"), 0, 0, width, height);
        cam.setToOrtho(false, width, height);
        game.pauseMusic();
        game.resumeMenuMusic();
        lvl = Game.lvls.getInteger("key");
        Play.level = LVL_UNLOCKED;
        lvlname = "Level number " + Play.level;

        buttons();
        Gdx.input.setInputProcessor(stage);

    }

    private void buttons(){
        updateImg();

       // Table table = new Table();
        //table.left().bottom();
        //button back to menu
        Button exitButton,tutorialButton;
        exitButton = new ImageButton(mySkin);
        exitButton.setSize(60,60);
        exitButton.setStyle(gsm.backStyle);
        exitButton.setPosition(width *0.94f,height * 0.92f,1);
        exitButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                Game.res.getSound("buttonClick").play(SOUND_LEVEL);
                gsm.setState(GameStateManager.MENU);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;}
        });
        stage.addActor(exitButton);
        //table.add(exitButton);
        //button tutorial
        tutorialButton = new ImageButton(mySkin);
        tutorialButton.setSize(60,60);
        tutorialButton.setStyle(gsm.toothStyle);
        tutorialButton.setPosition(width * 0.06f,height * 0.92f,1);
        tutorialButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                Game.res.getSound("buttonClick").play(SOUND_LEVEL);
                cutScene.dialogNumber = 0;
                gsm.setState(GameStateManager.CUTSCENE);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;}
        });
        stage.addActor(tutorialButton);
        //table.add(tutorialButton);

        //stage.addActor(table);
        //table.debug();

        //move to previous lvl
        leftButton = new ImageButton(mySkin);
        leftButton.setStyle(gsm.backStyle);
        leftButton.setSize(80,80);
        leftButton.setPosition(width/10,height/2,1);
        leftButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if ( event.getStageX() < width/4 && leftButton.isPressed() && Play.level > 1) {
                    if(Play.level == lvl){
                        Play.level -=1;
                        lvlname = "Level number " + Play.level;
                        updateImg();
                    }
                    else {
                        Play.level -= 1;
                        lvlname = "Level number " + Play.level;
                        updateImg();
                    }
                }
                Game.res.getSound("buttonClick").play(SOUND_LEVEL);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;}
        });
        stage.addActor(leftButton);

        //move to next lvl
        rightButton = new ImageButton(mySkin);
        rightButton.setStyle(gsm.rightStyle);
        rightButton.setSize(80,80);
        rightButton.setPosition(width*0.9f,height/2,1);
        rightButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if (event.getStageX() > width*0.75  && rightButton.isPressed() && Play.level < 9) {
                    if(Play.level == lvl){
                        Play.level = lvl;
                    }
                    else {
                        Play.level += 1;
                        lvlname = "Level number " + Play.level;
                        updateImg();
                    }
                }
                Game.res.getSound("buttonClick").play(SOUND_LEVEL);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;}
        });
        stage.addActor(rightButton);
    }

    private void updateImg(){
        Image lvlImg;

        if (Play.level < 5) {
            lvlImg = new Image(Game.res.getTexture("preview1"));
        }
        else{
            lvlImg = new Image(Game.res.getTexture("preview2"));
        }
        lvlImg.setSize(width/2,height/2);
        lvlImg.setPosition(width/4,height/3);
        lvlImg.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if ( event.getStageX() > width/4 && event.getStageX() < width*0.75f
                        && Play.level > 0 && Play.level <10) {
                    dispose();
                    Game.res.getSound("buttonClick").play(SOUND_LEVEL);
                    gsm.setState(GameStateManager.PLAY);
                }
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;}
        });
        stage.addActor(lvlImg);
    }

    @Override
    public void handleInput() {
    }

    @Override
    public void update(float dt) {
        levelScore = Game.scores.getInteger("score"+ String.valueOf(Play.level));
        pasteScore = Game.scores.getInteger("collect" + String.valueOf(Play.level));
    }

    @Override

    public void render() {
        if (Play.level == 1){
            leftButton.setVisible(false);
        }else leftButton.setVisible(true);
        if (Play.level == 9){
            rightButton.setVisible(false);
        }else rightButton.setVisible(true);
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(reg, 0, 0); //background
        font2.draw(sb,lvlname,width/4,height*0.90f);//lvlname
        //(lvlImg,width/4,height/3,width/2,height/2,sb);
        font2.draw(sb,"Toothpaste collected:" + pasteScore + "/6", width/4,height/3.5f);
        font2.draw(sb,game.prefs.getString("name")+"'s highscore is:" + levelScore,width/4,height/4.5f);
        sb.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.clear();
    }
}

