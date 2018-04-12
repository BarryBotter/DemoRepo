package my.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import my.game.Game;
import my.game.entities.Background;
import my.game.handlers.B2DVars;
import my.game.handlers.Content;
import my.game.handlers.GameButton;
import my.game.handlers.GameStateManager;

import static my.game.handlers.B2DVars.SOUND_LEVEL;

/**
 * Created by Katriina on 23.3.2018.
 */

public class Menu extends GameState{
    private int row_height,col_width;
    private Image logo;
    private Table table, table1;
    private Stage stage;
    private ImageButton playButton,optionsButton,exitButton;
    private Skin mySkin;
    private Background bg;
    private World world;

    public Menu(final GameStateManager gsm) {
        super(gsm);
        row_height = 1080 / 12;
        col_width = 1920 / 12;
        mySkin = game.mySkin;
        stage = gsm.stage;
        setup();
        createButtons(mySkin);
        tableLayout(optionsButton,playButton,exitButton);
    }

    private void setup(){
        //background
        Texture tex = Game.res.getTexture("menubg");
        bg = new Background(new TextureRegion(tex),hudCam,5 );
        bg.setVector(0, 0);

        cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);
        Texture logoTex =Game.res.getTexture("menulogo");
        logo = new Image(logoTex);

        world = new World(new Vector2(0, -9.8f * 5), true);

        //stage
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    private void createButtons(Skin skin){
        playButton = new ImageButton(skin);
        playButton.setStyle(gsm.playButtonStyle);
        playButton.setSize(col_width*2,row_height*2);
        playButton.setScale(2f,2f);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                my.game.Game.res.getSound("snap").play(SOUND_LEVEL);
                dispose();
                gsm.setState(GameStateManager.LEVEL_SELECT);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                playButton.setPosition(playButton.getX(), playButton.getY() +2);
                return true;
            }
        });

        optionsButton = new ImageButton(skin);
        optionsButton.setStyle(gsm.optionButtonStyle);
        optionsButton.setSize(col_width,row_height);
        optionsButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                my.game.Game.res.getSound("snap").play(SOUND_LEVEL);
                dispose();
                gsm.setState(GameStateManager.OPTIONS);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        exitButton = new ImageButton(skin);
        exitButton.setStyle(gsm.exitButtonStyle);
        exitButton.setSize(col_width,row_height);
        exitButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
    }

    private void tableLayout(ImageButton optionsButton, ImageButton playButton, ImageButton exitButton) {
        table = new Table();
        table1 = new Table();
        table.center();
        table.row();//first row
        table.add(optionsButton).width(col_width*2);
        table.row();//second row
        table.add();
        table.add(logo).colspan(2).height(750);
        table.add().width(col_width);
        table.add(table1); //nested table
        table1.add(playButton);
        table1.row();
        table1.add().height(row_height);
        table1.row();
        table1.add(exitButton);
        table.row();//third row
        table.add().colspan(5).height(row_height*3);
        table.setFillParent(true);
        stage.addActor(table);
        //table.debug();      // Turn on all debug lines (table, cell, and widget).
    }

    public void handleInput() {
    }

    @Override
    public void update(float dt) {
    }

    public void render() {
        sb.setProjectionMatrix(cam.combined);
        bg.render(sb);
        sb.begin();
        sb.end();
        stage.draw();
    }

    public void dispose() {
        stage.clear();
    }
}


