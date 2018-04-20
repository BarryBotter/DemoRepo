package my.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import my.game.handlers.BoundedCamera;
import my.game.handlers.Content;
import my.game.handlers.GameStateManager;
import static my.game.handlers.B2DVars.CRYSTALS_COLLECTED;
import static my.game.handlers.B2DVars.ENEMIES_DESTROYED;
import static my.game.handlers.B2DVars.HITS_TAKEN;
import static my.game.handlers.B2DVars.LVL_UNLOCKED;
import static my.game.handlers.B2DVars.SOUND_LEVEL;

public class Game implements ApplicationListener {
	public static final int V_WIDTH = 320;
	public static final int V_HEIGHT = 240;

	public static final float STEP = 1 / 90f;

	private SpriteBatch sb;
	private static BoundedCamera cam;
	private OrthographicCamera hudCam,bigCam;

	private GameStateManager gsm;

	public static Content res;
	public Preferences prefs;
	public static Preferences lvls;
	public static Preferences scores;

	public SpriteBatch getSpriteBatch(){return sb;}
	public static BoundedCamera getCamera(){return cam;}
	public OrthographicCamera getHUDCamera(){return hudCam;}
	public OrthographicCamera getBigCam(){return bigCam;}

	public Skin mySkin;
	public BitmapFont font8,font12,font18,font24,textFont;

	@Override
	public void create() {

		res = new Content();
		loadTextures();
		loadSounds();
		loadFont();

		sb = new SpriteBatch();
		cam = new BoundedCamera();
		cam.setToOrtho(false, V_WIDTH,V_HEIGHT);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		bigCam = new OrthographicCamera();
		bigCam.setToOrtho(false,640,480);
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
			prefs.putInteger("difficulty", 0);
			prefs.putBoolean("sound", true);
			prefs.putString("name", "Eero");
			prefs.flush();
		}

		// Catch the back key so it's not passed onto OS.
		Gdx.input.setCatchBackKey(true);

		scores = Gdx.app.getPreferences("highscores");
		if (!scores.contains("score1")){
			scores.putInteger("score1",0);
			scores.putInteger("collect1",0);
			scores.flush();
		}

		res.getMusic("theme").setLooping(true);
		if (prefs.getBoolean("sound")) {
			SOUND_LEVEL = 1;
			res.getMusic("theme").setVolume(1);
			res.getMusic("theme").play();
		}
		else if (!prefs.getBoolean("sound")) {
			SOUND_LEVEL = 0;
			res.getMusic("theme").setVolume(0);
			res.getMusic("bbsong").setVolume(0);
		}
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

	private void loadFont(){
		FreeTypeFontGenerator.setMaxTextureSize(2048);
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("res/fonts/Gauge-Regular.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 8;
		font8 = generator.generateFont(parameter);
		parameter.size = 12;
		font12 = generator.generateFont(parameter);
		parameter.size = 18;
		font18 = generator.generateFont(parameter);
		parameter.size = 24;
		font24 = generator.generateFont(parameter);
		parameter.size = 16;
		parameter.shadowOffsetX = 3;
		parameter.shadowOffsetY = 3;
		parameter.color = Color.GREEN;
		textFont = generator.generateFont(parameter);
		generator.dispose(); // don't forget to dispose to avoid memory leaks!
	}

	private void loadTextures(){
		res.loadTexture("res/images/hud.png","hud");
		res.loadTexture("res/images/bgs.png","bg");
		res.loadTexture("res/images/menu.png","menu");
		res.loadTexture("kuva.png","olvi");
		res.loadTexture("res/UI_final/play.png", "play");
		res.loadTexture("res/UI_final/settings.png","settings");
		res.loadTexture("res/UI_final/exit.png","exit");
		res.loadTexture("res/UI_final/tooth_80.png", "tooth_80");
		res.loadTexture("res/UI_final/back_80.png","back");
		res.loadTexture("res/UI_final/right_80.png","right");
		res.loadTexture("res/UI_final/background_640.png","menubg");
		res.loadTexture("res/UI_final/resized_paavalikko.png","main");
		res.loadTexture("res/UI_final/menu_logo.png","menulogo");
		res.loadTexture("res/UI_final/sound_off.png","sound_off");
		res.loadTexture("res/UI_final/sound_on.png","sound_on");
		res.loadTexture("res/UI_final/tooth_easy_80.png","easy");
		res.loadTexture("res/UI_final/tooth_normal_80.png","normal");
		res.loadTexture("res/UI_final/tooth_hard_80.png","hard");
		res.loadTexture("res/UI_final/tooth_bubble_120.png","bubble");
		res.loadTexture("res/UI_final/tutorial.png","tutorial");
		res.loadTexture("res/background/testimaa.png","bgone");
		res.loadTexture("res/background/rsz_karkkimaas.png","bgones");
		res.loadTexture("res/background/mountains.png", "mountain");
		res.loadTexture("res/images/complete.png", "complete");
		res.loadTexture("res/images/testibg.png", "testibg");
		res.loadTexture("res/images/testitausta.png","taustatesti");

		//Player animations
		//res.loadTexture("res/playerAnimations/playerWalk.png","playerWalk");
		res.loadTexture("res/playerAnimations/playerAttack.png","playerAttack");
		res.loadTexture("res/playerAnimations/walkingTest.png", "playerWalk");
		res.loadTexture("res/playerAnimations/jumpframe.png", "playerJump");

		//UI
		res.loadTexture("res/UI_assets/GameOverScreen.png", "gameOver");
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
		res.loadSound("res/sfx/scream.ogg","scream");
		res.loadSound("res/sfx/completeSound.mp3", "complete");
		res.loadSound("res/sfx/enemySound.mp3","enemy");
		res.loadSound("res/sfx/hitSound.mp3","hit");
		res.loadSound("res/sfx/jumpSound.mp3","jump");
		res.loadSound("res/sfx/overSound.mp3", "over");
		res.loadSound("res/sfx/meleeHit.mp3", "melee");
        res.loadSound("res/sfx/pickupSound.mp3", "pickup");

        // music
		res.loadMusic("res/music/menuSong.mp3","theme");
		res.loadMusic("res/music/bbsong.ogg", "bbsong");
		res.getMusic("bbsong").setLooping(true);


	}

	public void isMusicPlaying(){

		if (prefs.getBoolean("sound")) {
			res.getMusic("bbsong").setVolume(1);
			res.getMusic("theme").setVolume(1);
			res.getMusic("theme").play();
		}
		else if (!prefs.getBoolean("sound")) {
			res.getMusic("bbsong").setVolume(0);
			res.getMusic("theme").setVolume(0);
		}
	}

	public void pauseMusic(){
	    res.getMusic("bbsong").pause();
    }
    public void resumeMusic(){ res.getMusic("bbsong").play(); }

	public void pauseMenuMusic(){
		res.getMusic("theme").pause();
	}
	public void resumeMenuMusic(){ res.getMusic("theme").play(); }

	public void decreaseMusicLevel() { res.getMusic("bbsong").setVolume(0.33f); }
	public void increaseMusicLevel() { res.getMusic("bbsong").setVolume(1); }
}
