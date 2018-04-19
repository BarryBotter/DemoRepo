package my.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import my.game.Game;

public class HUD {
    private Player player;
    private TextureRegion crystal;
    private TextureRegion[] font;

    private TextureRegion heartTexture;
    private TextureRegion heartSilhouetteTexture;
    private TextureRegion toothPasteTexture;
    private TextureRegion toothPasteSilhouetteTexture;

    public Texture pauseMenuTexture;
    public TextureRegion pauseButton;

    public HUD(Player player) {
        this.player = player;

        Texture tex = Game.res.getTexture("hud");

        TextureRegion[] blocks = new TextureRegion[3];
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
        // Load hud icon textures.
        Texture hudIconTextures = Game.res.getTexture("hudIcons");
        heartTexture = new TextureRegion(hudIconTextures,0,0,160,160);
        heartSilhouetteTexture = new TextureRegion(hudIconTextures,160,0,160,160);
        toothPasteTexture = new TextureRegion(hudIconTextures,650,0,160,160);
        toothPasteSilhouetteTexture = new TextureRegion(hudIconTextures,830,0,160,160);

        // Setup pause button
        Texture pauseTexture = Game.res.getTexture("buttonMap");
        pauseButton = new TextureRegion(pauseTexture,670,190,450,450);

        // Setup pause menu.
        pauseMenuTexture = Game.res.getTexture("pauseMenu");
    }

    public void render(SpriteBatch sb) {
        float DOWNSCALE_MULTIPLIER = 12;
        float HUD_ICONS_MULTIPLIER = 8;
        float HUD_ICON_OFFSET = 22;

        sb.begin();

        // Draw the hearts and empty hearts.
        for(int i = 0; i < Player.returnMaxHealth(); i++) {
            sb.draw(heartSilhouetteTexture, 3 + (i*HUD_ICON_OFFSET),Game.V_HEIGHT - HUD_ICON_OFFSET - 3,
                    heartSilhouetteTexture.getRegionWidth() / HUD_ICONS_MULTIPLIER,
                    heartSilhouetteTexture.getRegionHeight() / HUD_ICONS_MULTIPLIER);
        }
        for(int i = 0; i < Player.returnHealth(); i++) {
            sb.draw(heartTexture, 3 + (i*HUD_ICON_OFFSET),Game.V_HEIGHT - HUD_ICON_OFFSET - 3,
                    heartTexture.getRegionWidth() / HUD_ICONS_MULTIPLIER,
                    heartTexture.getRegionHeight() / HUD_ICONS_MULTIPLIER);
        }

        // Draw the ammo and empty ammo.
        for(int i = 0; i < Player.returnMaxAmmo(); i++) {
            sb.draw(toothPasteSilhouetteTexture, 5 + (i*HUD_ICON_OFFSET),Game.V_HEIGHT - (heartSilhouetteTexture.getRegionHeight() / HUD_ICONS_MULTIPLIER) - HUD_ICON_OFFSET - 6,
                    toothPasteSilhouetteTexture.getRegionWidth() / HUD_ICONS_MULTIPLIER,
                    toothPasteSilhouetteTexture.getRegionHeight() / HUD_ICONS_MULTIPLIER);
        }
        for(int i = 0; i < Player.returnNumberOfAmmo(); i++) {
            sb.draw(toothPasteTexture, 5 + (i*HUD_ICON_OFFSET),Game.V_HEIGHT - (heartTexture.getRegionHeight() / HUD_ICONS_MULTIPLIER) - HUD_ICON_OFFSET - 6,
                    toothPasteTexture.getRegionWidth() / HUD_ICONS_MULTIPLIER,
                    toothPasteTexture.getRegionHeight() / HUD_ICONS_MULTIPLIER);
        }

        // draw crystal amount
        sb.draw(crystal, Game.V_WIDTH - 206, Game.V_HEIGHT - 22);
        drawString(sb, player.getNumCrystals() + " / " + player.getTotalCrystals(), Game.V_WIDTH - 180, Game.V_HEIGHT - 18);

        //draw pause menu if game is paused
        if(!Gdx.graphics.isContinuousRendering()) {
            sb.draw(pauseMenuTexture, 0,0, pauseMenuTexture.getWidth(),pauseMenuTexture.getHeight());
        } else {
            //draw pause button only when the game is not paused.
            sb.draw(pauseButton,Game.V_WIDTH - (pauseButton.getRegionWidth() / DOWNSCALE_MULTIPLIER) - 5,
                    Game.V_HEIGHT - (pauseButton.getRegionHeight() / DOWNSCALE_MULTIPLIER)- 5,
                    (pauseButton.getRegionWidth() / DOWNSCALE_MULTIPLIER),
                    (pauseButton.getRegionHeight() / DOWNSCALE_MULTIPLIER));
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
