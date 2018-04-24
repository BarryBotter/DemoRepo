package my.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
    private int collectCompare;
    private int crystalScore;

    private int enemyScore;
    private int hitScore;
    private float timescore;
    private int heartsLeft;

    public LevelComplete(GameStateManager gsm) {
        super(gsm);

        Texture tex = Game.res.getTexture("complete");
        bg = new Background(new TextureRegion(tex), hudCam, 5);
        bg.setVector(0, 0);

        textFont = game.textFont;

        heartScore =  Game.lvls.getInteger("hits");

        tex = Game.res.getTexture("playButton");

        TextureRegion[] menuButtons;
        menuButtons = new TextureRegion[5];
        menuButtons[0] = new TextureRegion(tex, 0, 0, 80, 80);
        tex = Game.res.getTexture("exitButton");
        menuButtons[1] = new TextureRegion(tex, 0, 0, 80, 80);
        playButton = new GameButton(menuButtons[0], 290, 70, cam);
        exitButton = new GameButton(menuButtons[1], 210, 70, cam);

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
            if (Play.level < 9){
                Play.level++;
                gsm.setState(GameStateManager.PLAY);
            }
            else {
                gsm.setState(GameStateManager.LEVEL_SELECT);
            }
        } else if (exitButton.isClicked()) {
            gsm.setState(GameStateManager.LEVEL_SELECT);
            game.resumeMenuMusic();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        //world.step(dt / 5, 8, 3);

       // bg.update(dt);
        playButton.update(dt);
        exitButton.update(dt);
    }

    @Override
    public void render() {


        sb.setProjectionMatrix(cam.combined);


        // draw background
        //bg.render(sb);
        Gdx.gl.glClearColor(135/255f, 206/255f, 235/255f, 1);
        //Gdx.gl.glClearColor(255/255f, 255/255f, 255/255f, 1);
        //Gdx.gl.glClearColor(0/255f, 0/255f, 0/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // draw button
        playButton.render(sb);
        exitButton.render(sb);

        sb.begin();

        int x = 15;

        textFont.draw(sb,"Level Complete!",80,230);
        textFont.draw(sb, Game.lvls.getInteger("crystals") + "/6 toothpaste collected", x, 200);
        textFont.draw(sb,settime() + "s completion time",x,180);
        textFont.draw(sb, Game.lvls.getInteger("enemies") + " enemies destroyed", x, 160);
        textFont.draw(sb, String.valueOf(Player.returnHealth()) + " health left",x,140);
        textFont.draw(sb, heartScore + " hits taken",x,120);
        textFont.draw(sb,"total score",x, 65);

        for (int i = 0; i < totalScore /1000; i++) {
            if (scoreCount == totalScore) {
                textFont.draw(sb, String.valueOf(scoreCount), x, 85);
                break;
            }
            else
            scoreCount = scoreCount + 5;
            textFont.draw(sb, String.valueOf(scoreCount), x, 85);
        }

        if(compareScore < getScore()) {
            textFont.draw(sb, "NEW HIGHSCORE!", x, 25);
        }

        sb.end();
    }

    @Override
    public void dispose() { }

    private int getScore() {

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
