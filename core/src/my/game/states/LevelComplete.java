package my.game.states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import my.game.Game;
import my.game.entities.Background;
import my.game.entities.Player;
import my.game.handlers.GameButton;
import my.game.handlers.GameStateManager;

import static my.game.handlers.B2DVars.SOUND_LEVEL;

public class LevelComplete extends GameState {
    private Background bg;
    private GameButton playButton;
    private GameButton exitButton;

    private World world;
    private BitmapFont textFont;

    private int heartScore;
    private int totalScore;
    private int scoreCount = 0;
    private int compareScore;
    private int crystalScore;

    public LevelComplete(GameStateManager gsm) {
        super(gsm);

        Texture tex = Game.res.getTexture("complete");
        bg = new Background(new TextureRegion(tex), hudCam, 5);
        bg.setVector(0, 0);

        textFont = game.textFont;

        heartScore =  Game.lvls.getInteger("hits");

        tex = Game.res.getTexture("main");

        TextureRegion[] menuButtons;
        menuButtons = new TextureRegion[5];
        menuButtons[0] = new TextureRegion(tex, 340, 40, 200, 100);
        menuButtons[1] = new TextureRegion(tex, 340, 125, 200, 100);
        playButton = new GameButton(menuButtons[0], 350, 100, cam);
        exitButton = new GameButton(menuButtons[1], 100, 110, cam);

        cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);

        game.pauseMusic();
        Game.res.getSound("complete").play(SOUND_LEVEL);

        getScore();
        setScore();

        world = new World(new Vector2(0, -9.8f * 5), true);
    }

    @Override
    public void handleInput() {
        if (playButton.isClicked()) {
            Game.res.getSound("buttonClick").play(SOUND_LEVEL);
            if (Play.level < 9){
                Play.level++;
            }
            else {
                Play.level = 1;
            }
            gsm.setState(GameStateManager.PLAY);
        } else if (exitButton.isClicked()) {
            Game.res.getSound("buttonClick").play(SOUND_LEVEL);
            gsm.setState(GameStateManager.MENU);
            game.resumeMenuMusic();
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

        textFont.draw(sb, Game.lvls.getInteger("crystals") + "/6 toothpaste collected", 90, 215);
        textFont.draw(sb, String.valueOf(settime()) + "s completion time",90,195);
        textFont.draw(sb, Game.lvls.getInteger("enemies") + " enemies destroyed", 90, 175);
        textFont.draw(sb, String.valueOf(Player.returnHealth()) + " health left",90,155);
        textFont.draw(sb, heartScore + " hits taken",90,135);
        textFont.draw(sb,"total score",90, 80);

        for (int i = 0; i < totalScore /1000; i++) {
            if (scoreCount == totalScore)
                textFont.draw(sb, String.valueOf(scoreCount), 90, 100);
            else
            scoreCount = scoreCount + 5;
            textFont.draw(sb, String.valueOf(scoreCount), 90, 100);
        }

        if(compareScore < getScore()) {
            textFont.draw(sb, "NEW HIGHSCORE!", 90, 40);
        }

        sb.end();
    }

    @Override
    public void dispose() { }

    private int getScore() {
        int crystalScore;
        int enemyScore;
        int hitScore;
        float timescore;
        int heartsLeft;

        crystalScore = Game.lvls.getInteger("crystals") * 100;
        enemyScore = Game.lvls.getInteger("enemies") * 100;
        hitScore = Game.lvls.getInteger("hits");
        timescore = (60 - (Play.gettime()/1000)) * 1000;
        heartsLeft = Player.returnHealth() * 2;

        if (hitScore == 0)
            totalScore = (int) ((int) ((timescore * heartsLeft) + ((enemyScore +crystalScore)*5))* 1.5f);
        else
            totalScore= (int) ((timescore * heartsLeft) + ((enemyScore +crystalScore)*5));

        return totalScore;
    }

    private void setScore(){
        int collectCompare;
        compareScore = Game.scores.getInteger("score"+String.valueOf(Play.level));
        if(compareScore < totalScore) {
            Game.scores.putInteger("score" + String.valueOf(Play.level), totalScore);
            Game.scores.flush();
        }
        collectCompare = Game.scores.getInteger("collect"+String.valueOf(Play.level));
        if(collectCompare < crystalScore) {
            Game.scores.putInteger("collect" + String.valueOf(Play.level), crystalScore /100);
            Game.scores.flush();
        }
    }

    private float settime(){
        float time;
        time = Play.gettime() / 1000;
        return time;
    }
}
