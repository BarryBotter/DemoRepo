package my.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import my.game.Game;
import my.game.entities.Background;
import my.game.handlers.GameStateManager;
import my.game.handlers.MyTextInputListener;

/**
 * Created by velij on 27.3.2018.
 */

public class Options extends GameState {

    Image logo;
    private TextureRegion bg;
    String Name = "", soundvalue = "",difficultyString = "",hintname = "Your Name";
    private int row_height,col_width,width,height;
    private Label nameLabel,soundLabel,difficultyLabel;
    Skin mySkin;
    Stage stage;
    private ImageButton nameEditButton,difficultyButton,soundButton,exitButton;
    StretchViewport viewport;

    public Options(final GameStateManager gsm){
        super(gsm);
        getSettings();
        width= Game.V_WIDTH*2;
        height= Game.V_HEIGHT*2;
        row_height = Game.V_HEIGHT/ 6;
        col_width = Game.V_WIDTH /6;
        mySkin = game.mySkin;
        stage = gsm.stage;
        setup();
        createButtons(mySkin);
        optionsLayout(nameEditButton,difficultyButton,soundButton);
    }

    private void setup(){
        cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);
        //viewport = new StretchViewport(320,240, cam);
        viewport = new StretchViewport(640,480, cam);

        Texture tex = Game.res.getTexture("menubg");
        bg = new TextureRegion(Game.res.getTexture("menubg"), 0, 0, width, height);

        Texture logoTex =Game.res.getTexture("menulogo");
        logo = new Image(logoTex);
        logo.setSize(col_width,row_height);

        //skin and stage
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
    }

    private void createButtons(Skin skin){
        //exitbutton (not in the table)
        exitButton = new ImageButton(skin);
        exitButton.setStyle(gsm.backStyle);
        exitButton.setSize(col_width*1.5f, row_height*1.5f);
        exitButton.setPosition(col_width/2,row_height/2);
        exitButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                gsm.setState(GameStateManager.MENU);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }
        });
        stage.addActor(exitButton);

        //nameEditButton, makes a MyTextInputListener
        nameEditButton = new ImageButton(skin);
        nameEditButton.setStyle(gsm.toothStyle);
        nameEditButton.setSize(col_width,col_width);
        nameEditButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                //creates textinputlistener and inputs the output to Preferences
                MyTextInputListener listener = new MyTextInputListener(game);
                Gdx.input.getTextInput(listener, "Enter your name:", "", hintname);            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        //name label
        nameLabel = new Label(Name,skin);
        nameLabel.setFontScale(1,1);

        //Button for setting the sound on and off, does not have functionality(yet)
        soundButton = new ImageButton(skin);
        soundButton.setStyle(gsm.toothStyle);
        soundButton.setSize(col_width,col_width);
        soundButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                soundOption();//sound on/off
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        //sound label
        soundLabel = new Label(soundvalue, skin); //labeltest
        soundLabel.setFontScale(1,1);

        //chances the difficulty
        difficultyButton = new ImageButton(skin);
        difficultyButton.setStyle(gsm.toothStyle);
        difficultyButton.setSize(col_width,col_width);
        difficultyButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                difficultyChange();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(difficultyButton);

        //difficultylabel
        difficultyLabel = new Label(difficultyString, skin); //labeltest
        difficultyLabel.setFontScale(1,1);
    }

    private void optionsLayout(ImageButton nameEditButton,ImageButton difficultyButton, ImageButton soundButton){
        Table table = new Table();
        table.center();
        table.row();//first row
        table.add(logo).colspan(4).height(row_height*5).width(col_width*4);
/*        table.row();//filler row
        table.add().height(row_height);*/
        table.row();//second row
        table.add(nameEditButton);
        table.add(nameLabel).width(col_width*2);
        table.add(difficultyButton);
        table.add(difficultyLabel).width(col_width*2);
        table.row();//third row
        table.add(soundButton);
        table.add(soundLabel);
        table.add().width(col_width*2);
        table.row();//filler row
        table.add().height(row_height);
        table.setFillParent(true);
        stage.addActor(table);
        //table.debug();      // Turn on all debug lines (table, cell, and widget).
    }

    //not in use yet
    private void createImageButton(ImageButton button, ImageButton.ImageButtonStyle style, int width){
        button = new ImageButton(style);
        button.setStyle(gsm.toothStyle);
        button.setSize(width,width);
        button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                //difficultyChange();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(button);
    }
    //not in use yet
    private void createLabel(Label label, int scale, String text){

    }

    private void difficultyChange(){
        //Chances the difficulty
        int difficulty = game.prefs.getInteger("difficulty");
        switch (difficulty) {
            case 0: game.prefs.putInteger("difficulty", 1);
                difficultyString = "normal";
                break;
            case 1: game.prefs.putInteger("difficulty", 2);
                difficultyString = "hard";
                break;
            case 2: game.prefs.putInteger("difficulty", 0);
                difficultyString = "easy";
                break;
            default: difficultyString = "Invalid ";
                break;
        }
        game.prefs.flush();
    }

    private void soundOption(){
        //chances the value of the sound boolean
        if (!game.prefs.getBoolean("sound")){
            game.prefs.putBoolean("sound",true);
            soundvalue = "true";
        }else
        {
            game.prefs.putBoolean("sound",false);
            soundvalue = "false";
        }
        game.prefs.flush();
        game.isMusicPlaying();
    }

    private void getSettings(){
        //gets the characters name from Preferences
        Name = game.prefs.getString("name", "no name stored");  //getting name from preferences

        //checks the boolean from Preferences
        if(game.prefs.getBoolean("sound")){
            soundvalue = "true";
        }else {
            soundvalue = "false";
        }

        //difficulty is an integer(0-2) in the preferences
        int difficulty = game.prefs.getInteger("difficulty");
        switch (difficulty) {
            case 0:
                difficultyString = "easy";
                break;
            case 1:
                difficultyString = "normal";
                break;
            case 2:
                difficultyString = "hard";
                break;
            default: difficultyString = "Invalid ";
                break;
        }
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.57f, 0.95f, 0.45f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(bg, 0, 0); //background
        sb.end();

        Name = game.prefs.getString("name", "no name stored");  //getting name from preferences
        difficultyLabel.setText(difficultyString);
        nameLabel.setText(Name);
        soundLabel.setText(soundvalue);
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.clear();
    }
}

