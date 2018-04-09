package my.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;

import my.game.Game;
import my.game.entities.Background;
import my.game.entities.Player;
import my.game.handlers.GameButton;
import my.game.handlers.GameStateManager;

import static my.game.handlers.B2DVars.MAX_HEALTH;

public class LevelComplete extends GameState {


    private Background bg;
    private GameButton playButton;
    private GameButton exitButton;

    private World world;
    private TextureRegion[] menuButtons;

    private TextureRegion crystal;
    private TextureRegion[] font;
    private BitmapFont textFont;

    private int crystalScore;
    private int enemyScore;
    private int hitScore;
    private int scoreCounter = 0;
    private int hearthScore;
    private int totalScore;
    private int hearthsLeft;
    private int scoreCount = 0;



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

        hearthScore =  Game.lvls.getInteger("hits");

        tex = Game.res.getTexture("main");
        menuButtons = new TextureRegion[5];
        menuButtons[0] = new TextureRegion(tex, 340, 40, 200, 100);
        menuButtons[1] = new TextureRegion(tex, 340, 125, 200, 100);
        playButton = new GameButton(menuButtons[0], 350, 100, cam);
        exitButton = new GameButton(menuButtons[1], 100, 110, cam);


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

        getScore();

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
        textFont.draw(sb,"crystals collected", 130,215);
        textFont.draw(sb, "enemies destroyed",130, 195);
        textFont.draw(sb, "Hits taken",130,175);
        textFont.draw(sb, "Hearths left", 130 ,155);
        textFont.draw(sb,"total score",110, 80);


        // draw crystal amount
        textFont.draw(sb, game.lvls.getInteger("crystals") + "", 110, 215);
        textFont.draw(sb, game.lvls.getInteger("enemies") + "", 110, 195);
        textFont.draw(sb, hearthScore + "",110,175);
        textFont.draw(sb, String.valueOf(Player.returnHealth()),110,155);

        for (int i = 0; i < totalScore /1000; i++) {
            if (scoreCount == totalScore)
                textFont.draw(sb, String.valueOf(scoreCount), 110, 100);
            else
            scoreCount = scoreCount + 5;
            textFont.draw(sb, String.valueOf(scoreCount), 110, 100);
        }

        sb.end();

    }

    @Override
    public void dispose() {
        //dispose
    }


    public int getScore()
    {
        crystalScore = Game.lvls.getInteger("crystals") * 100;
        enemyScore = Game.lvls.getInteger("enemies") * 100;
        hitScore = Game.lvls.getInteger("hits");
        hearthsLeft = Player.returnHealth() * 2;

        if (hitScore == 0)
            totalScore=  (crystalScore + enemyScore) * hearthsLeft * 5;
        else
            totalScore=  (crystalScore + enemyScore) * hearthsLeft;

        return totalScore;
    }
}
