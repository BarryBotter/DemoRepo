package my.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import my.game.Game;
import my.game.handlers.GameStateManager;


import static my.game.handlers.B2DVars.SOUND_LEVEL;

/**
 * Created by Katriina on 23.3.2018.
 */

public class Menu extends GameState{
    private TextureRegion bg;
    private int row_height,col_width,width,height;
    private Image logo;
    private Stage stage;
    private ImageButton playButton,optionsButton,exitButton;

    public Menu(final GameStateManager gsm) {
        super(gsm);
        width= Game.V_WIDTH*2;
        height= Game.V_HEIGHT*2;
        row_height = Game.V_HEIGHT/6;
        col_width = Game.V_WIDTH /6;

        Skin mySkin;
        mySkin = game.mySkin;
        stage = gsm.stage;
        setup();
        createButtons(mySkin);
        tableLayout(optionsButton,playButton,exitButton);
    }

    private void setup(){
        cam.setToOrtho(false, width, height);

        StretchViewport viewport;
        viewport = new StretchViewport(width,height, cam);

        cam.setBounds(width,height,width,height);

        Texture logoTex =Game.res.getTexture("menulogo");
        logo = new Image(logoTex);
        logo.setSize(col_width,row_height);

        //background
        Texture tex = Game.res.getTexture("menubg");
        bg = new TextureRegion(tex, 0, 0, width, height);

        //stage
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
    }

    private void createButtons(Skin Skin){

        playButton = new ImageButton(Skin);
        playButton.setStyle(gsm.playStyle);
        playButton.setSize(col_width,row_height);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                my.game.Game.res.getSound("hit").play(SOUND_LEVEL);
                dispose();
                gsm.setState(GameStateManager.LEVEL_SELECT);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                playButton.setPosition(playButton.getX(), playButton.getY() +2);
                return true;
            }
        });

        optionsButton = new ImageButton(Skin);
        optionsButton.setStyle(gsm.optionStyle);
        optionsButton.setSize(col_width,row_height);
        optionsButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                my.game.Game.res.getSound("hit").play(SOUND_LEVEL);
                dispose();
                gsm.setState(GameStateManager.OPTIONS);
                //gsm.pushState(GameStateManager.OPTIONS);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        exitButton = new ImageButton(Skin);
        exitButton.setStyle(gsm.exitStyle);
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
        Table table, table1;
        table = new Table();
        table1 = new Table();
        table.center();
        table.row();//first row
        table.add(optionsButton).width(col_width*1.5f);
        table.row();//second row
        table.add();
        table.add(logo).colspan(3).height(row_height*6).width(col_width*5);
        table.add().width(col_width);
        table.add(table1); //nested table
        table1.add(playButton).width(col_width*4);
        table1.row();
        table1.add().height(row_height);
        table1.row();
        table1.add(exitButton).width(col_width*4);
        table.row();//third row
        table.add().colspan(3).height(row_height);
        table.setFillParent(true);
        stage.addActor(table);
        //table.debug();      // Turn on all debug lines (table, cell, and widget).
    }


    public void handleInput() {
    }

    @Override
    public void update(float dt) {
        //handleInput();
        //bg.update(dt);
    }

    public void render() {
        sb.setProjectionMatrix(cam.combined);
        // draw background
        //bg.render(sb);
        sb.begin();
        sb.draw(bg, 0, 0);
        sb.end();
        //stage for menubutton layout
        //stage.act();
        stage.draw();
    }

    public void dispose() {
        stage.clear();
    }
}


