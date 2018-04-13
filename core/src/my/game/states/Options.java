package my.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
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

import my.game.Game;
import my.game.entities.Background;
import my.game.handlers.GameStateManager;
import my.game.handlers.MyTextInputListener;

public class Options extends GameState {

    private Image logo;
    private Background bg;
    private String Name = "", soundvalue = "",difficultyString = "",hintname = "Your Name";
    private int row_height,col_width;
    private Label nameLabel,soundLabel,difficultyLabel;
    private Stage stage;
    private ImageButton nameEditButton,difficultyButton,soundButton;
    private Button exitButton;

    public Options(final GameStateManager gsm){
        super(gsm);
        getSettings();
        row_height = 1080 / 12;
        col_width = 1920 / 12;
        Skin mySkin;
        mySkin = game.mySkin;
        stage = gsm.stage;
        setup();
        createButtons(mySkin);
        optionsLayout(nameEditButton,difficultyButton,soundButton);
    }

    private void setup(){
        Texture tex = Game.res.getTexture("menubg");
        bg = new Background(new TextureRegion(tex),hudCam,5 );
        bg.setVector(0, 0);

        Texture logoTex =Game.res.getTexture("menulogo");
        logo = new Image(logoTex);

        //skin and stage
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    private void createButtons(Skin skin){
        //exitbutton (not in the table)
        exitButton = new TextButton("EXIT",skin,"default");
        exitButton.setSize(col_width*2, row_height*2);
        exitButton.setPosition(col_width,row_height);
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

        //nameLabel
        nameLabel = new Label(Name,skin);
        nameLabel.setFontScale(5,5);

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
        soundLabel.setFontScale(5,5);

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

        //difficulty label
        difficultyLabel = new Label(difficultyString, skin); //labeltest
        difficultyLabel.setFontScale(5,5);
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

    private void optionsLayout(ImageButton nameEditButton,ImageButton difficultyButton, ImageButton soundButton){
        Table table = new Table();
        table.center();
        table.row();//first row
        table.add(logo).colspan(4).height(500);
        table.row();//filler row
        table.add().height(200);
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
        table.add().height(200);
        table.setFillParent(true);
        stage.addActor(table);
        //table.debug();      // Turn on all debug lines (table, cell, and widget).
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
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown (int keycode) {
                // Go back to menu when Andoird back button is pressed.
                if(keycode == Input.Keys.BACK) {
                    gsm.setState(GameStateManager.MENU);
                }
                return false;
            }
        });
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.57f, 0.95f, 0.45f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.setProjectionMatrix(cam.combined);
        // draw background
        bg.render(sb);
        sb.begin();
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
