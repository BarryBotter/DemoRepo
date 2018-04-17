package my.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

import my.game.Game;
import my.game.entities.Background;
import my.game.entities.Enemy;
import my.game.entities.HUD;
import my.game.entities.Melee;
import my.game.entities.PickUp;
import my.game.entities.Player;
import my.game.entities.Projectile;
import my.game.entities.TextureDraw;
import my.game.entities.Traps;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import my.game.handlers.Content;
import my.game.handlers.GameStateManager;
import my.game.handlers.MyContactListener;

import static my.game.handlers.B2DVars.BIT_BULLET;
import static my.game.handlers.B2DVars.BIT_CORNER;
import static my.game.handlers.B2DVars.BIT_CRYSTAL;
import static my.game.handlers.B2DVars.BIT_ENEMY;
import static my.game.handlers.B2DVars.BIT_GROUND;
import static my.game.handlers.B2DVars.BIT_JUMP;
import static my.game.handlers.B2DVars.BIT_MELEE;
import static my.game.handlers.B2DVars.BIT_PLAYER;
import static my.game.handlers.B2DVars.BIT_TRAP;
import static my.game.handlers.B2DVars.CRYSTALS_COLLECTED;
import static my.game.handlers.B2DVars.ENEMIES_DESTROYED;
import static my.game.handlers.B2DVars.HITS_TAKEN;
import static my.game.handlers.B2DVars.LVL_UNLOCKED;
import static my.game.handlers.B2DVars.PPM;
import static my.game.handlers.B2DVars.P_HEIGHT;
import static my.game.handlers.B2DVars.P_WIDTH;
import static my.game.handlers.B2DVars.SOUND_LEVEL;

/**
 * Created by Katriina on 20.3.2018.
 */

public class Play extends GameState {

    public static int level;
    private int levelS;
    private World world;

    public static float accumulator = 0;

    private Rectangle screenRightSide;
    private Rectangle screenLeftSide;
    private Rectangle screenTopRightSide;
    private Rectangle pauseMenuRightButton;
    private Rectangle pauseMenuLeftButton;
    private Rectangle pauseMenuMiddleButton;

    private TiledMap tileMap;
    private int tileMapWidth;
    private int tileMapHeight;
    private int tileSize;
    private OrthogonalTiledMapRenderer tmRenderer;

    private MyContactListener cl;

    private static long time;
    private static long startTime;

    private Player player;
    private Projectile bullet;
    private Enemy enemy;
    private Melee meleeHitBox;
    private Array<PickUp> crystals;
    private Array<Enemy> enemies;
    private Array<Projectile> bullets;
    private Array<Melee> meleeHitBoxes;
    private Array<Traps> traps;
    private TextureDraw win;
    private Vector3 touchPoint;

    private Background[] backgrounds;

    private HUD hud;
    private BitmapFont textFont;


    public Play(GameStateManager gsm) {
        super(gsm);

        cam.setToOrtho(false, 480, 320);//Game.V_WIDTH, Game.V_HEIGHT);
        //Resets rendering every time play state is started.
        Gdx.graphics.setContinuousRendering(true);


        world = new World(new Vector2(0, -9.81f), true);
        cl = new MyContactListener();
        world.setContactListener(cl);

        // create player
        createPlayer();

        //create tile
        createWalls();
        cam.setBounds(0, tileMapWidth * tileSize, 0, tileMapHeight * tileSize);

        //create crystals
        createCrystals();
        player.setTotalCrystals(crystals.size);

        //create traps
        createTrap();

        //create winblock
        createWin();

        //Create bullet
        createBullet();

        //Create enemy
        createEnemy();

        //create melee hitbox
        createMeleeHitBox();

        // create backgrounds
        Texture bgs = Game.res.getTexture("bgones");
        TextureRegion sky = new TextureRegion(bgs, 0, 0, 320, 240);
        TextureRegion mountains = new TextureRegion(bgs, 0, 235, 320, 340);
        Texture trees = Game.res.getTexture("bgone");
        TextureRegion treeLayer = new TextureRegion(trees, 0, 27, 320, 240);
        backgrounds = new Background[1];
        backgrounds[0] = new Background(sky, cam, 0f);
        //backgrounds[1] = new Background(mountains, cam, 0.2f);
        //backgrounds[2] = new Background(treeLayer, cam, 0f);

        // set up hud
        hud = new HUD(player);

        //setup touch areas
        setupTouchControlAreas();

        game.pauseMenuMusic();
        game.resumeMusic();

        startTime = System.currentTimeMillis();

        Gdx.input.setCatchBackKey(true);

    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {


        time = System.currentTimeMillis() - startTime;

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
                translateScreenToWorldCoordinates(x, y);

                // Pause button touched
                if (topRightSideTouched(touchPoint.x, touchPoint.y) && Gdx.graphics.isContinuousRendering()) {
                    Gdx.graphics.setContinuousRendering(false);
                }

                // Fighting stuff.
                else if (rightSideTouched(touchPoint.x, touchPoint.y) && Gdx.graphics.isContinuousRendering()) {
                    //When player runs out of ammo, start melee mode.
                    if (Player.returnNumberOfAmmo() == 0) {
                        meleeManager();
                    } else if (Player.returnNumberOfAmmo() <= Player.returnMaxAmmo()) {
                        bulletManager();
                    }
                }

                // Jumping stuff.
                else if (leftSideTouched(touchPoint.x, touchPoint.y) && Gdx.graphics.isContinuousRendering()) {
                    if (cl.isPlayerOnGround()) {
                        player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
                        player.getBody().applyLinearImpulse(0.4f, 6, 0, 0, true);
                        Game.res.getSound("jump").play(SOUND_LEVEL);

                        if (player.getBody().getLinearVelocity().x < 0.7f) {
                            player.getBody().setLinearVelocity(1.5f, 0);
                            if (player.getBody().getLinearVelocity().x < 0.5f) {
                                player.getBody().setLinearVelocity(2f, 0);
                            }
                        }
                    }
                }

                // Pause menu left button touched. Goes to menu.
                else if (pauseMenuLeftButtonTouched(touchPoint.x, touchPoint.y) && !Gdx.graphics.isContinuousRendering()) {
                    gsm.setState(GameStateManager.LEVEL_SELECT);
                }

                // Pause menu middle button touched. Restarts level.
                else if (pauseMenuMiddleButtonTouched(touchPoint.x, touchPoint.y) && !Gdx.graphics.isContinuousRendering()) {
                    gsm.setState(GameStateManager.PLAY);
                }

                // Pause menu right button touched. Resumes game.
                else if (pauseMenuRightButtonTouched(touchPoint.x, touchPoint.y) && !Gdx.graphics.isContinuousRendering()) {
                    Gdx.graphics.setContinuousRendering(true);
                }

                return super.touchDown(x, y, pointer, button);
            }

            @Override
            public boolean touchUp(int x, int y, int pointer, int button) {
                return true;
            }
        });

        // While the game is not paused keep updating the game.
        if (Gdx.graphics.isContinuousRendering()) {
            stepWorld();
            pickUpRemover();
            bulletRemover();
            meleeHitBoxRemover();
            trapRemover();
            enemyManager();

            player.update(dt);

            for (int i = 0; i < bullets.size; i++) {
                bullets.get(i).update(dt);
                bullet.checkBulletCoolDown();
            }

            for (int i = 0; i < meleeHitBoxes.size; i++) {
                meleeHitBoxes.get(i).update(dt);
                meleeHitBox.checkMeleeCoolDown();
            }

            for (int i = 0; i < crystals.size; i++) {
                crystals.get(i).update(dt);
            }

            for (int i = 0; i < enemies.size; i++) {
                enemies.get(i).update(dt);
            }

        }
        // Game over stuff
        if (player.getBody().getPosition().y < 0) {
            Game.res.getSound("scream").play(SOUND_LEVEL);
            gsm.setState(GameStateManager.GAMEOVER);
        }

        if (Player.gameIsOver()) {
            Game.res.getSound("scream").play(SOUND_LEVEL);
            gsm.setState(GameStateManager.GAMEOVER);
        }

        // Win stuff
        if (cl.isPlayerWin()) {
            if (level == 1) {
                unlockLevel();
                Collected();
                gsm.setState(GameStateManager.LEVEL_COMPLETE);
            } else if (level == 2) {
                unlockLevel();
                Collected();
                gsm.setState(GameStateManager.LEVEL_COMPLETE);
            } else if (level == 3) {
                unlockLevel();
                Collected();
                gsm.setState(GameStateManager.LEVEL_COMPLETE);
            } else if (level == 4) {
                unlockLevel();
                Collected();
                gsm.setState(GameStateManager.LEVEL_COMPLETE);
            } else if (level == 5) {
                unlockLevel();
                Collected();
                gsm.setState(GameStateManager.LEVEL_COMPLETE);
            } else if (level == 6) {
                unlockLevel();
                Collected();
                gsm.setState(GameStateManager.LEVEL_COMPLETE);
            } else if (level == 7) {
                unlockLevel();
                Collected();
                gsm.setState(GameStateManager.LEVEL_COMPLETE);
            } else if (level == 8) {
                unlockLevel();
                Collected();
                gsm.setState(GameStateManager.LEVEL_COMPLETE);
            } else if (level == 9) {
                unlockLevel();
                Collected();
                gsm.setState(GameStateManager.LEVEL_COMPLETE);
            }
        }

    }

    private boolean rightSideTouched(float x, float y) {
        return screenRightSide.contains(x, y);
    }

    private boolean leftSideTouched(float x, float y) {
        return screenLeftSide.contains(x, y);
    }

    private boolean topRightSideTouched(float x, float y) {
        return screenTopRightSide.contains(x, y);
    }

    private boolean pauseMenuLeftButtonTouched(float x, float y) {
        return pauseMenuLeftButton.contains(x, y);
    }

    private boolean pauseMenuRightButtonTouched(float x, float y) {
        return pauseMenuRightButton.contains(x, y);
    }

    private boolean pauseMenuMiddleButtonTouched(float x, float y) {
        return pauseMenuMiddleButton.contains(x, y);
    }

    private void translateScreenToWorldCoordinates(int x, int y) {
        game.getHUDCamera().unproject(touchPoint.set(x, y, 0));
    }

    private void setupTouchControlAreas() {
        touchPoint = new Vector3();
        final int BOX_ENLARGER = 10;
        screenTopRightSide = new Rectangle(game.getHUDCamera().viewportWidth - (hud.pauseTexture.getWidth() + BOX_ENLARGER), game.getHUDCamera().viewportHeight - (hud.pauseTexture.getHeight() + BOX_ENLARGER), hud.pauseTexture.getWidth() + BOX_ENLARGER, hud.pauseTexture.getHeight() + BOX_ENLARGER);
        screenRightSide = new Rectangle(game.getHUDCamera().viewportWidth / 2, 0, game.getHUDCamera().viewportWidth / 2,
                game.getHUDCamera().viewportHeight);
        screenLeftSide = new Rectangle(0, 0, game.getHUDCamera().viewportWidth / 2, game.getHUDCamera().viewportHeight);
        pauseMenuLeftButton = new Rectangle((Game.V_WIDTH / 2) - (hud.pauseMenuTexture.getWidth() / 2), (Game.V_HEIGHT / 2) - (hud.pauseMenuTexture.getHeight() / 2), hud.pauseMenuTexture.getWidth() / 3, hud.pauseMenuTexture.getHeight() / 2);
        pauseMenuMiddleButton = new Rectangle((Game.V_WIDTH / 2) - (hud.pauseMenuTexture.getWidth() / 2) + (hud.pauseMenuTexture.getWidth() / 3), (Game.V_HEIGHT / 2) - (hud.pauseMenuTexture.getHeight() / 2), hud.pauseMenuTexture.getWidth() / 3, hud.pauseMenuTexture.getHeight() / 2);
        pauseMenuRightButton = new Rectangle((Game.V_WIDTH / 2) - (hud.pauseMenuTexture.getWidth() / 2) + ((hud.pauseMenuTexture.getWidth() / 3) * 2), (Game.V_HEIGHT / 2) - (hud.pauseMenuTexture.getHeight() / 2), hud.pauseMenuTexture.getWidth() / 3, hud.pauseMenuTexture.getHeight() / 2);
    }

    @Override
    public void render() {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //set cam to follow player
        //cam with bounds and centered stage
        //cam.setPosition(player.getposition().x * PPM + P_WIDTH / 4, P_HEIGHT / 2);

        //cam without bounds and set to bottom
        //if (cam.position.x < tileMapWidth *28){
        cam.position.set(player.getposition().x * PPM + P_WIDTH / 4, P_HEIGHT / 2, 0);
        cam.update();
        //}

        cam.update();

        // draw bgs
        sb.setProjectionMatrix(hudCam.combined);
        for (Background background : backgrounds) {
            background.render(sb);
        }

        //draw tile maps
        tmRenderer.setView(cam);
        tmRenderer.render();

        //draw player
        sb.setProjectionMatrix(cam.combined);
        player.render(sb);

        //draw pickups
        for (int i = 0; i < crystals.size; i++) {
            crystals.get(i).render(sb);
        }
        //draw enemy
        for (int i = 0; i < enemies.size; i++) {
            enemies.get(i).render(sb);
        }
        //draw traps
        for (int i = 0; i < traps.size; i++) {
            traps.get(i).render(sb);
        }
        //draw bullets
        for (int i = 0; i < bullets.size; i++) {
            bullets.get(i).render(sb);
        }
        //draw melee hit box
        for (int i = 0; i < meleeHitBoxes.size; i++) {
            meleeHitBoxes.get(i).render(sb);
        }
        //draw win
        win.render(sb);

        //updateText();

        //draw hud
        sb.setProjectionMatrix(hudCam.combined);
        hud.render(sb);
    }

    @Override
    public void dispose() {
        //tileMap.dispose();
    }

    private void createPlayer() {
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        bdef.position.set(70 / PPM, 100 / PPM);
        bdef.linearVelocity.set(1.5f, 0);
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);
        body.setGravityScale(3);

        shape.setAsBox(14 / PPM, 15 / PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_PLAYER;
        fdef.filter.maskBits = BIT_GROUND | BIT_CRYSTAL | BIT_CORNER | BIT_ENEMY | BIT_TRAP | BIT_JUMP;
        body.createFixture(fdef).setUserData("player");
        shape.dispose();

        //create foot sensor
        shape.setAsBox(15 / PPM, 2 / PPM, new Vector2(0, -17 / PPM), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_PLAYER;
        fdef.filter.maskBits = BIT_GROUND | BIT_CORNER | BIT_JUMP;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("foot");
        shape.dispose();

        player = new Player(body);
    }

    private void createWalls() {
        // load tile map and map renderer
        try {
            tileMap = new TmxMapLoader().load("res/maps/level" + level + ".tmx");
        } catch (Exception e) {
            System.out.println("Cannot find file: res/maps/level" + level + ".tmx");
            //Gdx.app.exit();
        }
        tileMapWidth = tileMap.getProperties().get("width", Integer.class);
        tileMapHeight = tileMap.getProperties().get("height", Integer.class);
        tileSize = tileMap.getProperties().get("tilewidth", Integer.class);
        tmRenderer = new OrthogonalTiledMapRenderer(tileMap);

        TiledMapTileLayer layer;
        layer = (TiledMapTileLayer) tileMap.getLayers().get("platforms");

        if (layer != null)
            createBlocks(layer, BIT_GROUND);

        layer = (TiledMapTileLayer) tileMap.getLayers().get("corner");

        if (layer != null)
            createCorners(layer, BIT_CORNER);

        layer = (TiledMapTileLayer) tileMap.getLayers().get("jump");

        if (layer != null)
            createJump(layer, BIT_JUMP);
    }

    private void createBlocks(TiledMapTileLayer layer, short bits) {
        float ts = layer.getTileWidth();

        // go through all cells in layer
        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {

                // get cell
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);

                // check that there is a cell
                if (cell == null) continue;
                if (cell.getTile() == null) continue;

                // create body from cell
                BodyDef bdef = new BodyDef();
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((col + 0.4f) * ts / PPM, (row + 0.4f) * ts / PPM);
                ChainShape cs = new ChainShape();
                Vector2[] v = new Vector2[3];
                v[0] = new Vector2(-ts / 2 / PPM, -ts / 2 / PPM);
                v[1] = new Vector2(-ts / 2 / PPM, ts / 2 / PPM);
                v[2] = new Vector2(ts / 2 / PPM, ts / 2 / PPM);
                cs.createChain(v);
                FixtureDef fd = new FixtureDef();
                fd.friction = 0;
                fd.shape = cs;
                fd.filter.categoryBits = bits;
                fd.filter.maskBits = BIT_PLAYER | BIT_ENEMY | BIT_BULLET | BIT_TRAP;
                world.createBody(bdef).createFixture(fd).setUserData("ground");
                cs.dispose();
            }
        }
    }

    private void createCorners(TiledMapTileLayer layer, short bits) {
        float ts = layer.getTileWidth();

        // go through all cells in layer
        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {

                // get cell
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);

                // check that there is a cell
                if (cell == null) continue;
                if (cell.getTile() == null) continue;

                // create body from cell
                BodyDef bdef = new BodyDef();
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((col + 0.6f) * ts / PPM, (row + 0.4f) * ts / PPM);
                ChainShape cs = new ChainShape();
                Vector2[] v = new Vector2[4];
                v[0] = new Vector2(-ts / 2 / PPM, -ts / 2 / PPM);
                v[1] = new Vector2(-ts / 2 / PPM, ts / 2 / PPM);
                v[2] = new Vector2(ts / 2 / PPM, ts / 2 / PPM);
                v[3] = new Vector2(ts / 2 / PPM, -ts / 2 / PPM);
                cs.createChain(v);

                //PolygonShape cs = new PolygonShape();
                //cs.setAsBox(32,32);

                FixtureDef fd = new FixtureDef();
                fd.friction = 0;
                fd.shape = cs;
                fd.restitution = 1;
                fd.filter.categoryBits = bits;
                fd.filter.maskBits = BIT_PLAYER | BIT_ENEMY | BIT_BULLET;
                world.createBody(bdef).createFixture(fd).setUserData("corner");
                cs.dispose();

            }
        }
    }

    private void createJump(TiledMapTileLayer layer, short bits) {
        float ts = layer.getTileWidth();

        // go through all cells in layer
        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {

                // get cell
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);

                // check that there is a cell
                if (cell == null) continue;
                if (cell.getTile() == null) continue;

                // create body from cell
                BodyDef bdef = new BodyDef();
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((col + 0.4f) * ts / PPM, (row + 0.4f) * ts / PPM);
                ChainShape cs = new ChainShape();
                Vector2[] v = new Vector2[3];
                v[0] = new Vector2(-ts / 2 / PPM, -ts / 2 / PPM);
                v[1] = new Vector2(-ts / 2 / PPM, ts / 2 / PPM);
                v[2] = new Vector2(ts / 2 / PPM, ts / 2 / PPM);
                cs.createChain(v);
                FixtureDef fd = new FixtureDef();
                fd.friction = 0;
                fd.shape = cs;
                fd.restitution = 2.4f;
                fd.filter.categoryBits = bits;
                fd.filter.maskBits = BIT_PLAYER;
                world.createBody(bdef).createFixture(fd).setUserData("jump");
                cs.dispose();
            }
        }
    }

    private void createCrystals() {
        crystals = new Array<PickUp>();
        MapLayer layer = tileMap.getLayers().get("crystals");

        if (layer != null) {

            BodyDef bdef = new BodyDef();
            FixtureDef fdef = new FixtureDef();

            for (MapObject mo : layer.getObjects()) {

                bdef.type = BodyDef.BodyType.StaticBody;

                float x = mo.getProperties().get("x", float.class) / PPM;
                float y = mo.getProperties().get("y", float.class) / PPM;

                bdef.position.set(x, y);

                CircleShape cshape = new CircleShape();
                cshape.setRadius(8 / PPM);

                fdef.shape = cshape;
                fdef.isSensor = true;
                fdef.filter.categoryBits = BIT_CRYSTAL;
                fdef.filter.maskBits = BIT_PLAYER;

                Body body = world.createBody(bdef);
                body.createFixture(fdef).setUserData("crystal");
                cshape.dispose();

                PickUp c = new PickUp(body);
                crystals.add(c);

                body.setUserData(c);
            }
        }
    }

    private void createTrap() {
        traps = new Array<Traps>();
        MapLayer layer = tileMap.getLayers().get("trap");

        if (layer != null) {

            BodyDef bdef = new BodyDef();
            FixtureDef fdef = new FixtureDef();

            for (MapObject mo : layer.getObjects()) {

                bdef.type = BodyDef.BodyType.DynamicBody;

                float x = mo.getProperties().get("x", float.class) / PPM;
                float y = mo.getProperties().get("y", float.class) / PPM;

                bdef.position.set(x, y);

                CircleShape cshape = new CircleShape();
                cshape.setRadius(8 / PPM);

                fdef.shape = cshape;
                fdef.restitution = 1;
                fdef.filter.categoryBits = BIT_TRAP;
                fdef.filter.maskBits = BIT_PLAYER | BIT_GROUND | BIT_BULLET;

                Body body = world.createBody(bdef);
                body.createFixture(fdef).setUserData("trap");
                cshape.dispose();

                Traps trap = new Traps(body);
                traps.add(trap);
                body.setUserData(trap);
            }
        }
    }

    private void createWin() {
        MapLayer layer = tileMap.getLayers().get("win");

        if (layer != null) {
            BodyDef bdef = new BodyDef();
            FixtureDef fdef = new FixtureDef();

            for (MapObject mo : layer.getObjects()) {

                bdef.type = BodyDef.BodyType.StaticBody;

                float x = mo.getProperties().get("x", float.class) / PPM;
                float y = mo.getProperties().get("y", float.class) / PPM;

                bdef.position.set(x, y);

                CircleShape cshape = new CircleShape();
                cshape.setRadius(8 / PPM);

                fdef.shape = cshape;
                fdef.isSensor = true;
                fdef.filter.categoryBits = BIT_CRYSTAL;
                fdef.filter.maskBits = BIT_PLAYER;

                Body body = world.createBody(bdef);
                body.createFixture(fdef).setUserData("win");
                cshape.dispose();

                win = new TextureDraw(body, "olvi");

                body.setUserData(win);
            }
        }
    }

    private void createBullet() {
        bullets = new Array<Projectile>();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        bdef.position.set(-100, -100);
        bdef.linearVelocity.set(0, 0);
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);
        body.setGravityScale(0);

        shape.setAsBox(9 / PPM, 9 / PPM);
        fdef.shape = shape;
        fdef.restitution = 1;
        fdef.filter.categoryBits = BIT_BULLET;
        fdef.filter.maskBits = BIT_ENEMY | BIT_GROUND | BIT_TRAP | BIT_CORNER;
        body.createFixture(fdef).setUserData("bullet");
        shape.dispose();

        bullet = new Projectile(body);
        bullets.add(bullet);
        body.setUserData(bullet);
    }

    private void createEnemy() {
        enemies = new Array<Enemy>();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        bdef.position.set(player.getposition().x + 5, player.getposition().y + 2);
        bdef.linearVelocity.set(0, 0);
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);
        body.setGravityScale(3);

        shape.setAsBox(12 / PPM, 12 / PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_ENEMY;
        fdef.filter.maskBits = BIT_GROUND | BIT_PLAYER | BIT_BULLET | BIT_CORNER | BIT_MELEE;
        body.createFixture(fdef).setUserData("enemy");
        shape.dispose();

        //create enemy
        enemy = new Enemy(body);
        enemies.add(enemy);
        body.setUserData(enemy);
    }

    private void createMeleeHitBox() {
        meleeHitBoxes = new Array<Melee>();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        bdef.position.set(-150, -150);
        bdef.linearVelocity.set(0, 0);
        bdef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bdef);
        body.setGravityScale(0);

        shape.setAsBox(13 / PPM, 15 / PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_MELEE;
        fdef.filter.maskBits = BIT_ENEMY;
        body.createFixture(fdef).setUserData("melee");
        shape.dispose();

        meleeHitBox = new Melee(body);
        meleeHitBoxes.add(meleeHitBox);
        body.setUserData(meleeHitBox);
    }

    private void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();
        accumulator += Math.min(delta, 0.25f);

        if (accumulator >= Game.STEP) {
            accumulator -= Game.STEP;
            world.step(Game.STEP, 1, 1);
        }
    }

    private void updateText() {
        int score = Game.scores.getInteger("score" + String.valueOf(Play.level));

        sb.begin();

        textFont.draw(sb, String.valueOf(score), player.getposition().x + 50, player.getposition().y + 150);

        //textFont.draw(sb,String.valueOf(score), player.getposition().x + 50 , player.getposition().y  + 150);

        sb.end();
    }

    private void unlockLevel() {
        levelS = Game.lvls.getInteger("key");

        if (level < levelS) {
            //LVL_UNLOCKED = level;
        } else if (level >= levelS)
            LVL_UNLOCKED = LVL_UNLOCKED + 1;
        Game.lvls.putInteger("key", LVL_UNLOCKED);
        Game.lvls.flush();
    }

    private void Collected() {
        Game.lvls.getInteger("crystals");
        Game.lvls.getInteger("enemies");
        Game.lvls.getInteger("hits");

        CRYSTALS_COLLECTED = player.getNumCrystals();
        ENEMIES_DESTROYED = Player.getEnemyKC();
        HITS_TAKEN = Player.counterHealth();
        Game.lvls.putInteger("crystals", CRYSTALS_COLLECTED);
        Game.lvls.putInteger("enemies", ENEMIES_DESTROYED);
        Game.lvls.putInteger("hits", HITS_TAKEN);
        Game.lvls.flush();
    }

    private void enemyManager() {
        // Remove enemies.
        Array<Body> enemyBodies = cl.getEnemyBodiesToRemove();
        if (enemyBodies.size > 0) {
            Body b = enemy.getBody();
            enemies.removeValue((Enemy) b.getUserData(), true);
            world.destroyBody(b);
            createEnemy();
        }
        enemyBodies.clear();

        // If enemy falls out of boundaries, respawn it.
        if (enemies.get(0).getBody().getPosition().y < 0) {
            Body b = enemy.getBody();
            enemies.removeValue((Enemy) b.getUserData(), true);
            world.destroyBody(b);
            createEnemy();
        }

        //If enemy gets left behind player undestroyed, respawn it.
        if (enemies.get(0).getBody().getPosition().x < player.getposition().x - 2.5f) {
            Body b = enemy.getBody();
            enemies.removeValue((Enemy) b.getUserData(), true);
            world.destroyBody(b);
            createEnemy();
        }

        //Move enemy towards left side of the screen. If it stops, switch direction.
        if (enemy.returnCurrentLocation() - enemies.get(0).getBody().getPosition().x < 0 && enemy.returnDirection()) {
            enemies.get(0).getBody().setTransform(enemy.getposition().x + 0.05f, enemy.getposition().y - 0.01f, 0);
            enemy.switchDirection();
        } else if (enemy.returnCurrentLocation() - enemies.get(0).getBody().getPosition().x < 0 && !enemy.returnDirection()) {
            enemies.get(0).getBody().setTransform(enemy.getposition().x - 0.05f, enemy.getposition().y - 0.01f, 0);
            enemy.switchDirection();
        }

        // Go right
        if (!enemy.returnDirection()) {
            enemies.get(0).getBody().setTransform(enemy.getposition().x + 0.01f, enemy.getposition().y - 0.01f, 0);
        } else {  //Go left
            enemies.get(0).getBody().setTransform(enemy.getposition().x - 0.01f, enemy.getposition().y - 0.01f, 0);
        }

        // Set current location to compare if enemy has stopped or not.
        enemy.setCurrentLocation(enemies.get(0).getBody().getPosition().x);
    }

    private void bulletManager() {
        // If the player has ammo and bullet is not on cooldown, shoot the bullet.
        if (Player.returnNumberOfAmmo() > 0 && !bullet.returnCoolDownState() && !meleeHitBox.returnMeleeCoolDownState()) {
            bullet.resetBullet(player.getposition().x, player.getposition().y);
            // Check if the touch is below or above player.
            if (touchPoint.y / PPM >= player.getposition().y) {
                bullet.shootBullet(touchPoint.x / PPM, touchPoint.y / PPM, false);
            } else {
                bullet.shootBullet(touchPoint.x / PPM, (touchPoint.y / PPM) - player.getposition().y, true);
            }
        } else {
            // After teh bullet's been shot, deploy a little cool down.
            bullet.checkBulletCoolDown();
        }
    }

    private void meleeManager() {
        //If melee swing is not on cooldown make melee hitbox appear in front of the player.
        if (!meleeHitBox.returnMeleeCoolDownState() && !bullet.returnCoolDownState() && Player.returnNumberOfAmmo() == 0) {
            meleeHitBox.meleeSwing();
            meleeHitBox.getBody().setTransform(player.getposition().x + 0.4f, player.getposition().y, 0);
        } else {
            // Keep checking the cooldwon until it's ready to be used again.
            meleeHitBox.checkMeleeCoolDown();
        }
    }

    private void pickUpRemover() {
        Array<Body> bodies = cl.getBodiesToRemove();
        for (int i = 0; i < bodies.size; i++) {
            Body b = bodies.get(i);
            crystals.removeValue((PickUp) b.getUserData(), true);
            world.destroyBody(b);
            player.collectCrystal();
        }
        bodies.clear();
    }

    private void bulletRemover() {
        Array<Body> bulletBodies = cl.getBulletBodiesToRemove();

        if (bulletBodies.size > 0) {
            Body b = bullet.getBody();
            bullets.removeValue((Projectile) b.getUserData(), true);
            world.destroyBody(b);
            createBullet();
        }
        bulletBodies.clear();
    }

    private void meleeHitBoxRemover() {
        Array<Body> meleeBodies = cl.getMeleeHitBoxesToRemove();

        if (meleeBodies.size > 0) {
            Body b = meleeHitBox.getBody();
            meleeHitBoxes.removeValue((Melee) b.getUserData(), true);
            world.destroyBody(b);
            createMeleeHitBox();
        }
        meleeBodies.clear();
    }

    private void trapRemover() {
        Array<Body> trapBodies = cl.getTrapsToRemove();

        for (int i = 0; i < trapBodies.size; i++) {
            Body b = trapBodies.get(i);
            traps.removeValue((Traps) b.getUserData(), true);
            world.destroyBody(b);
        }
        trapBodies.clear();
    }


    public static long getTime(){
        return time;
    }
}