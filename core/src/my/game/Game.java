package my.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import my.game.handlers.BoundedCamera;
import my.game.handlers.Content;
import my.game.handlers.GameStateManager;
import static my.game.handlers.B2DVars.CRYSTALS_COLLECTED;
import static my.game.handlers.B2DVars.ENEMIES_DESTROYED;
import static my.game.handlers.B2DVars.HITS_TAKEN;
import static my.game.handlers.B2DVars.LVL_UNLOCKED;

public class Game implements ApplicationListener {
	public static final int V_WIDTH = 320;
	public static final int V_HEIGHT = 240;

	public static final float STEP = 1 / 90f;

	private SpriteBatch sb;
	private static BoundedCamera cam;
	private OrthographicCamera hudCam;

	private GameStateManager gsm;

	public static Content res;
	public Preferences prefs;
	public static Preferences lvls;

	public SpriteBatch getSpriteBatch(){return sb;}
	public static BoundedCamera getCamera(){return cam;}
	public OrthographicCamera getHUDCamera(){return hudCam;}

	public Skin mySkin;

	@Override
	public void create() {

		res = new Content();
		loadTextures();
		loadSounds();

		res.loadMusic("res/music/bbsong.ogg", "bbsong");
		res.getMusic("bbsong").setLooping(true);
		res.getMusic("bbsong").setVolume(0.5f);
		res.getMusic("bbsong").play();

		sb = new SpriteBatch();
		cam = new BoundedCamera();
		cam.setToOrtho(false, V_WIDTH,V_HEIGHT);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		mySkin = new Skin(Gdx.files.internal("res/skin/glassy-ui.json"));

		gsm = new GameStateManager(this);

		lvls = Gdx.app.getPreferences("mylvls");
		if(!lvls.contains("key")) lvls.putInteger("key", LVL_UNLOCKED);
		if(!lvls.contains("crystals")) lvls.putInteger("crystals", CRYSTALS_COLLECTED);
		if(!lvls.contains("hits")) lvls.putInteger("hits", HITS_TAKEN);
		if(!lvls.contains("enemies")) lvls.putInteger("enemies", ENEMIES_DESTROYED);
		lvls.flush();

		prefs = Gdx.app.getPreferences("My Preferences");
		//dont insert preferences here this is just to set default values if there is none (maybe works)
		if(!prefs.contains("name")) {
			prefs.putInteger("difficulty", 1);
			prefs.putBoolean("sound", true);
			prefs.putString("name", "Eero");
			prefs.flush();
		}

		// Catch the back key so it's not passed onto OS.
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void render() {
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		res.removeAll();
	}

	public void loadTextures(){
		res.loadTexture("res/images/hud.png","hud");
		res.loadTexture("res/images/bgs.png","bg");
		res.loadTexture("res/images/menu.png","menu");
		res.loadTexture("res/UI_final/rebg.png","menubg");
		res.loadTexture("res/UI_final/resized_paavalikko.png","main");
		res.loadTexture("res/UI_final/menu_logo.png","menulogo");
		res.loadTexture("res/images/GameOverScreen.png", "gameOver");
		res.loadTexture("res/background/testimaa.png","bgone");
		res.loadTexture("res/background/rsz_karkkimaas.png","bgones");
		res.loadTexture("res/background/mountains.png", "mountain");
		res.loadTexture("res/images/complete.png", "complete");
		res.loadTexture("res/images/testibg.png", "testibg");

		//Player animations
		res.loadTexture("res/playerAnimations/playerWalk.png","playerWalk");
		res.loadTexture("res/playerAnimations/playerAttack.png","playerAttack");

		//UI
		res.loadTexture("res/UI_assets/pauseMenu.png","pauseMenu");
		res.loadTexture("res/UI_assets/buttons.png","buttonMap");
		res.loadTexture("res/UI_assets/HUD_Icons.png","hudIcons");

		//Enemies
		res.loadTexture("res/enemies/enemyBat.png","enemyBat");
		res.loadTexture("res/enemies/enemyTooth.png","enemyTooth");
		res.loadTexture("res/enemies/trap.png","trap");

		//Particles
		res.loadTexture("res/particles/bulletParticle.png","bulletParticles");
		res.loadTexture("res/particles/bullet.png","bullet");

		//PickUps
		res.loadTexture("res/pickups/crystal.png", "Crystal");
		res.loadTexture("res/pickups/toothPastePickUp.png", "toothPaste");
	}

	private void loadSounds(){
		res.loadSound("res/sfx/necksnap.mp3","snap");
		res.loadSound("res/sfx/hit.wav","hit");
		res.loadSound("res/sfx/scream.ogg","scream");
	}
}
