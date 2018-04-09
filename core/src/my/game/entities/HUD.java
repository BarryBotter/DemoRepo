package my.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import my.game.Game;
import my.game.handlers.GameButton;

/**
 * Created by Katriina on 22.3.2018.
 */

public class HUD {

    private Player player;
    private TextureRegion[] blocks;
    private TextureRegion toothpaste;
    private TextureRegion[] font;

    private Texture heartTexture;
    private Texture heartSilhoutteTexture;
    private Texture ammoTexture;
    private Texture ammoSilhoutteTexture;

    public Texture pauseTexture;
    public Texture pauseMenuTexture;
    private GameButton pauseButton;
    private TextureRegion pauseButtonRegion;
    protected OrthographicCamera hudCam;

    public HUD(Player player) {
        this.player = player;

        Texture tex = Game.res.getTexture("hud");

        blocks = new TextureRegion[3];
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = new TextureRegion(tex, 32 + i * 16, 0, 16, 16);
        }

        Texture tooth = Game.res.getTexture("toothpaste");
        toothpaste = new TextureRegion(tooth, 0, 0, 32, 32);

        font = new TextureRegion[11];
        for (int i = 0; i < 6; i++) {
            font[i] = new TextureRegion(tex, 32 + i * 9, 16, 9, 9);
        }
        for (int i = 0; i < 5; i++) {
            font[i + 6] = new TextureRegion(tex, 32 + i * 9, 25, 9, 9);
        }

        heartTexture = Game.res.getTexture("heart");
        heartSilhoutteTexture = Game.res.getTexture("heartSilhoutte");
        ammoTexture = Game.res.getTexture("ammo");
        ammoSilhoutteTexture = Game.res.getTexture("ammoSilhoutte");

        // Setup pause button
        pauseTexture = Game.res.getTexture("pauseButton");
        // Setup pause menu button
        pauseMenuTexture = Game.res.getTexture("pauseMenu");
    }

    public void render(SpriteBatch sb) {
        sb.begin();

        // Draw the hearts and empty hearts.
        for(int i = 0; i < Player.returnMaxHealth(); i++) {
            sb.draw(heartSilhoutteTexture, 5 + (i*17),222, 12,12);
        }
        for(int i = 0; i < player.returnHealth(); i++) {
            sb.draw(heartTexture, 5 + (i*17),222, 12,12);
        }

        // Draw the ammo and empty ammo.
        for(int i = 0; i < Player.returnMaxAmmo(); i++) {
            sb.draw(ammoSilhoutteTexture, 5 + (i*17),190, 12,24);
        }
        for(int i = 0; i < Player.returnNumberOfAmmo(); i++) {
            sb.draw(ammoTexture, 5 + (i*17),190, 12,24);
        }

        // draw crystal amount
        sb.draw(toothpaste, 100, 222);
        drawString(sb, player.getNumCrystals() + " / " + player.getTotalCrystals(), 132, 222);



        //draw pause menu if game is paused
        if(!Gdx.graphics.isContinuousRendering()) {
            sb.draw(pauseMenuTexture, (Game.V_WIDTH / 2) - (pauseMenuTexture.getWidth() / 2 ),(Game.V_HEIGHT / 2 ) - (pauseMenuTexture.getHeight() / 2), pauseMenuTexture.getWidth(),pauseMenuTexture.getHeight());
        } else {
            //draw pause button only when the game is not paused.
            sb.draw(pauseTexture, 280,200, 32,32);
        }

        sb.end();
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
