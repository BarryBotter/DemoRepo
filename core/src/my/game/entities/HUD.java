package my.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import my.game.Game;
import my.game.handlers.B2DVars;

/**
 * Created by Katriina on 22.3.2018.
 */

public class HUD {

    private Player player;
    private TextureRegion[] blocks;
    private TextureRegion crystal;
    private TextureRegion[] font;

    private Texture heartTexture;
    private Texture heartSilhoutteTexture;
    private Texture ammoTexture;
    private Texture ammoSilhoutteTexture;

    public HUD(Player player) {
        this.player = player;

        Texture tex = Game.res.getTexture("hud");

        blocks = new TextureRegion[3];
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = new TextureRegion(tex, 32 + i * 16, 0, 16, 16);
        }

        crystal = new TextureRegion(tex, 80, 0, 16, 16);

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
    }

    public void render(SpriteBatch sb) {

        sb.begin();

        // draw crystal
        sb.draw(crystal, 100, 208);

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
        drawString(sb, player.getNumCrystals() + " / " + player.getTotalCrystals(), 132, 211);
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
//