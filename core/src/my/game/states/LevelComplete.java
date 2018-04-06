package my.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import my.game.Game;
import my.game.entities.Background;
import my.game.entities.Player;
import my.game.handlers.GameButton;
import my.game.handlers.GameStateManager;

public class LevelComplete extends GameState {


    private Background bg;
    private GameButton playButton;
    private GameButton exitButton;

    private World world;
    private TextureRegion[] menuButtons;

    private int totalCrystals;
    private Player player;
    private TextureRegion crystal;
    private TextureRegion[] font;
    private BitmapFont textFont;


    public LevelComplete(GameStateManager gsm) {
        super(gsm);

        Texture tex = Game.res.getTexture("complete");
        bg = new Background(new TextureRegion(tex), hudCam, 5);
        bg.setVector(4, -5);

        tex = Game.res.getTexture("hud");

        crystal = new TextureRegion(tex, 80, 0, 16, 16);

        font = new TextureRegion[11];
        for (int i = 0; i < 6; i++) {
            font[i] = new TextureRegion(tex, 32 + i * 9, 16, 9, 9);
        }

        textFont = new BitmapFont(Gdx.files.internal("res/images/fontstyle.fnt"), false);

        tex = Game.res.getTexture("main");
        menuButtons = new TextureRegion[5];
        menuButtons[0] = new TextureRegion(tex, 340, 40, 200, 100);
        menuButtons[1] = new TextureRegion(tex, 340, 125, 200, 100);
        playButton = new GameButton(menuButtons[0], 250, 100, cam);
        exitButton = new GameButton(menuButtons[1], 150, 110, cam);


        cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);

        world = new World(new Vector2(0, -9.8f * 5), true);
    }

    @Override
    public void handleInput() {

        if (playButton.isClicked()) {
            gsm.setState(GameStateManager.PLAY);
        } else if (exitButton.isClicked()) {
            gsm.setState(GameStateManager.MENU);
        }


    }

    @Override
    public void update(float dt) {

        handleInput();

        world.step(dt / 5, 8, 3);

        bg.update(dt);

        playButton.update(dt);

        exitButton.update(dt);



    }

    @Override
    public void render() {

        sb.setProjectionMatrix(cam.combined);

        // draw background
        bg.render(sb);

        // draw button
        playButton.render(sb);
        exitButton.render(sb);

        sb.begin();
        textFont.draw(sb,"crystals collected", 130,200);
        textFont.draw(sb, "enemies destroyed",130, 180);
        textFont.draw(sb, "Hearths left",130,160);

        // draw crystal amount
        drawString(sb, game.lvls.getInteger("crystals") + "", 110, 184);
        drawString(sb, game.lvls.getInteger("enemies") + "", 110, 164);
        //drawString(sb,game.lvls.getInteger("hearths") + "",108,144);
        sb.end();

    }

    @Override
    public void dispose() {

    }

    private void drawString(SpriteBatch sb, String s, float x, float y) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '/') c = 10;
            else if (c >= '0' && c <= '9') c -= '0';
            else continue;
            sb.draw(font[c], x + i * 9, y);
        }
    }
}
