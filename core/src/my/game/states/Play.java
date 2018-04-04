package my.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

import my.game.Game;
import my.game.entities.Background;
import my.game.entities.Enemy;
import my.game.entities.HUD;
import my.game.entities.PickUp;
import my.game.entities.Player;
import my.game.entities.Projectile;
import my.game.entities.TextureDraw;
import my.game.entities.Traps;
import my.game.handlers.B2DVars;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
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

import my.game.handlers.BoundedCamera;
import my.game.handlers.GameStateManager;
import my.game.handlers.MyContacListener;

import static my.game.handlers.B2DVars.BIT_CORNER;
import static my.game.handlers.B2DVars.BIT_CRYSTAL;
import static my.game.handlers.B2DVars.BIT_ENEMY;
import static my.game.handlers.B2DVars.BIT_GROUND;
import static my.game.handlers.B2DVars.BIT_JUMP;
import static my.game.handlers.B2DVars.BIT_PLAYER;
import static my.game.handlers.B2DVars.BIT_TRAP;
import static my.game.handlers.B2DVars.LVL_UNLOCKED;
import static my.game.handlers.B2DVars.PPM;

/**
 * Created by Katriina on 20.3.2018.
 */

public class Play extends GameState {

    public static int level;
    private int levelS;
    private World world;
    private Box2DDebugRenderer b2dr;

    public static float accumulator = 0;

    //private OrthographicCamera b2dCam;
    private BoundedCamera b2dCam;

    private Rectangle screenRightSide;
    private Rectangle screenLeftSide;

    private TiledMap tileMap;
    private int tileMapWidth;
    private int tileMapHeight;
    private int tileSize;
    private OrthogonalTiledMapRenderer tmRenderer;

    private MyContacListener cl;

    private Player player;
    private Projectile bullet;
    private Enemy enemy;
    private Array<PickUp> crystals;
    private Array<Enemy> enemies;
    private Array<Traps> traps;
    // private Array<Projectile> bullets;
    private TextureDraw win;
    private Vector3 touchPoint;

    private Background[] backgrounds;

    private HUD hud;

    public Play(GameStateManager gsm) {
        super(gsm);

        world = new World(new Vector2(0, -9.81f), true);
        cl = new MyContacListener();
        world.setContactListener(cl);
        b2dr = new Box2DDebugRenderer();

        // create player
        cretePlayer();

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

        // create backgrounds
        Texture bgs = Game.res.getTexture("bgones");
        TextureRegion sky = new TextureRegion(bgs, 0, 0, 320, 240);
        TextureRegion mountains = new TextureRegion(bgs, 0,235 , 320, 240);
        Texture trees = Game.res.getTexture("bgone");
        TextureRegion  treeLayer = new TextureRegion(trees, 0, 27, 320, 240);
        backgrounds = new Background[2];
        backgrounds[0] = new Background(sky, cam, 0f);
        backgrounds[1] = new Background(mountains, cam, 0.1f);
       // backgrounds[2] = new Background(treeLayer, cam, 0.2f);



        ///setup box2dcam
        //b2dCam = new BoundedCamera();
        //b2dCam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);
        //b2dCam.setBounds(0,(tileMapWidth * tileSize) / PPM,0,(tileMapHeight * tileSize ) / PPM);

        // set up hud
        hud = new HUD(player);

        //setup touch areas
        setupTouchControlAreas();

    }


    @Override
    public void handleInput() {
    }

    @Override
    public void update(float dt) {

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
                translateScreenToWorldCoordinates(x, y);

                if (rightSideTouched(touchPoint.x, touchPoint.y)) {
                    // If the player has ammo and bullet is not on cooldown, shoot the bullet.
                    if (player.returnNumberOfAmmo() > 0 && !bullet.returnCoolDownState()) {
                        bullet.resetBullet(player.getposition().x, player.getposition().y);
                        // Check if the touch is below or above player.
                        if(touchPoint.y / PPM >= player.getposition().y) {
                            bullet.shootBullet(touchPoint.x / PPM, touchPoint.y / PPM, false);
                        }
                        else {
                            bullet.shootBullet(touchPoint.x / PPM, (touchPoint.y / PPM) - player.getposition().y , true);
                        }

                    }
                    else {
                        // After teh bullet's been shot, deploy a little cool down.
                        bullet.checkBulletCoolDown();
                    }


                } else if (leftSideTouched(touchPoint.x, touchPoint.y)) {
                    Gdx.app.log("Puoli:", "vasen");
                    if (cl.isPlayerOnGround()) {
                        player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
                        player.getBody().applyLinearImpulse(0.4f, 6, 0, 0, true);

                        if (player.getBody().getLinearVelocity().x < 0.5f) {
                            float posX = player.getBody().getPosition().x;
                           // player.getBody().setTransform(posX - 0.75f, player.getBody().getPosition().y, 0);
                            player.getBody().setLinearVelocity(2f, 0);
                        }

                    }

                }

                return super.touchDown(x, y, pointer, button);
            }

            @Override
            public boolean touchUp(int x, int y, int pointer, int button) {
                return true;
            }
        });

        stepWorld();

        //remove pickups
        Array<Body> bodies = cl.getBodiesToRemove();
        for (int i = 0; i < bodies.size; i++) {
            Body b = bodies.get(i);
            crystals.removeValue((PickUp) b.getUserData(), true);
            world.destroyBody(b);
            player.collectCrystal();
        }
        bodies.clear();

        Array<Body> enemyBodies = cl.getEnemyBodiesToRemove();

        if(enemyBodies.size > 0) {
            Body b = enemy.getBody();
            enemies.removeValue((Enemy) b.getUserData(), true);
            world.destroyBody(b);
            enemy.enemyDestroyed();
            createEnemy();
        }
        else {
            if(enemies.get(0).getBody().getPosition().y < 0) {
                enemies.get(0).getBody().setTransform(player.getposition().x + 5, player.getposition().y + 5, 0);
            }
            enemies.get(0).getBody().setTransform(enemy.getposition().x - 0.01f,enemy.getposition().y, 0);
        }
        enemyBodies.clear();

        /*
        Array<Body> bulletBodies = cl.getBulletBodiesToRemove();

        if(bulletBodies.size > 0) {
            Body b = bullet.getBody();
            bullets.removeValue((Projectile) b.getUserData(), true);
            world.destroyBody(b);
            createBullet();
        }
        bulletBodies.clear();*/
        /*

        for (int i = 0; i < bullets.size; i++) {
            bullets.get(i).update(dt);
            bullet.checkBulletCoolDown();
        }*/

        player.update(dt);

        if(Projectile.returnBulletHitState()) {
            bullet.getBody().setTransform(-100,-100,0);
            Projectile.bulletNotHit();
        }
        bullet.update(dt);
        bullet.checkBulletCoolDown();

        for (int i = 0; i < crystals.size; i++) {
            crystals.get(i).update(dt);
        }

        for (int i = 0; i < enemies.size; i++) {
            enemies.get(i).update(dt);
        }

        // Game over stuff
        if (player.getBody().getPosition().y < 0) {
            Game.res.getSound("snap").play();
            gsm.setState(GameStateManager.GAMEOVER);
        }

        if(Player.gameIsOver()) {
            Game.res.getSound("snap").play();
            gsm.setState(GameStateManager.GAMEOVER);
        }

        // Win stuff
        if (cl.isPlayerWin() == true) {
            if (level == 1) {
                unlockLevel();
                gsm.setState(GameStateManager.MENU);
            } else if (level == 2) {
                unlockLevel();
                gsm.setState(GameStateManager.MENU);
            } else if (level == 3) {
                unlockLevel();
                gsm.setState(GameStateManager.MENU);
            }
        }

    }

    private boolean rightSideTouched(float x, float y) {
        return screenRightSide.contains(x, y);
    }

    private boolean leftSideTouched(float x, float y) {
        return screenLeftSide.contains(x, y);
    }

    private void translateScreenToWorldCoordinates(int x, int y) {
        game.getHUDCamera().unproject(touchPoint.set(x, y, 0));
    }

    private void setupTouchControlAreas() {
        touchPoint = new Vector3();
        screenRightSide = new Rectangle(game.getHUDCamera().viewportWidth / 2, 0, game.getHUDCamera().viewportWidth / 2,
                game.getHUDCamera().viewportHeight);
        screenLeftSide = new Rectangle(0, 0, game.getHUDCamera().viewportWidth / 2, game.getHUDCamera().viewportHeight);
    }

    @Override
    public void render() {
        //set cam to follow player
        cam.position.set(
                player.getposition().x * PPM + Game.V_WIDTH / 4,
                Game.V_HEIGHT / 2/*player.getposition().y * PPM +Game.V_HEIGHT/4*/, 0);
        cam.update();

        // draw bgs
        sb.setProjectionMatrix(hudCam.combined);
        for (int i = 0; i < backgrounds.length; i++) {
            backgrounds[i].render(sb);
        }

        //draw tile map
        tmRenderer.setView(cam);
        tmRenderer.render();

        //draw player
        sb.setProjectionMatrix(cam.combined);
        player.render(sb);

        //draw crystals
        for (int i = 0; i < crystals.size; i++) {
            crystals.get(i).render(sb);
        }

        //draw enemy
        for (int i = 0; i < enemies.size; i++) {
            enemies.get(i).render(sb);
        }

        //draw win
        win.render(sb);

        //draw traps
        for (int i = 0; i < traps.size; i++) {
            traps.get(i).render(sb);
        }

        //draw hud
        sb.setProjectionMatrix(hudCam.combined);
        hud.render(sb);

        //draw enemy
        sb.setProjectionMatrix(cam.combined);
        /*
        for (int i = 0; i < bullets.size; i++) {
            bullets.get(i).render(sb);
        }*/
        bullet.render(sb);

    }

    @Override
    public void dispose() {
    }

    private void cretePlayer() {

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        bdef.position.set(70 / PPM, 100 / PPM);
        bdef.linearVelocity.set(1.5f, 0);
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);
        body.setGravityScale(3);

        shape.setAsBox(13 / PPM, 15 / PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_PLAYER;
        fdef.filter.maskBits = BIT_GROUND | BIT_CRYSTAL | BIT_CORNER | BIT_ENEMY | BIT_TRAP;
        body.createFixture(fdef).setUserData("player");
        shape.dispose();

        //create foot sensor
        shape.setAsBox(15 / PPM, 2 / PPM, new Vector2(0, -17 / PPM), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_PLAYER;
        fdef.filter.maskBits = BIT_GROUND | BIT_CORNER;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("foot");
        shape.dispose();

        //create player
        player = new Player(body);
    }

    private void createWalls() {

        // load tile map and map renderer
        try {
            tileMap = new TmxMapLoader().load("res/maps/level" + level + ".tmx");
        } catch (Exception e) {
            System.out.println("Cannot find file: res/maps/level" + level + ".tmx");
            Gdx.app.exit();
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

        layer = (TiledMapTileLayer) tileMap.getLayers().get("jumps");

        if (layer != null)
            createJump(layer, BIT_JUMP);
    }


    private void createBlocks(TiledMapTileLayer layer, short bits) {

        // tile size
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
                bdef.position.set((col + 0.5f) * ts / PPM, (row + 0.5f) * ts / PPM);
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
                fd.filter.maskBits = BIT_PLAYER | BIT_ENEMY | BIT_TRAP;
                world.createBody(bdef).createFixture(fd).setUserData("Ground");
                cs.dispose();

            }
        }

    }

    private void createCorners(TiledMapTileLayer layer, short bits) {

        // tile size
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
                bdef.position.set((col + 0.5f) * ts / PPM, (row + 0.5f) * ts / PPM);
                ChainShape cs = new ChainShape();
                Vector2[] v = new Vector2[3];
                v[0] = new Vector2(-ts / 2 / PPM, -ts / 2 / PPM);
                v[1] = new Vector2(-ts / 2 / PPM, ts / 2 / PPM);
                v[2] = new Vector2(ts / 2 / PPM, ts / 2 / PPM);
                cs.createChain(v);
                FixtureDef fd = new FixtureDef();
                fd.friction = 0;
                fd.shape = cs;
                fd.restitution = 1;
                fd.filter.categoryBits = bits;
                fd.filter.maskBits = BIT_PLAYER;
                world.createBody(bdef).createFixture(fd).setUserData("corner");
                cs.dispose();

            }
        }

    }

    private void createJump(TiledMapTileLayer layer, short bits) {

        // tile size
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
                bdef.position.set((col + 0.5f) * ts / PPM, (row + 0.5f) * ts / PPM);
                ChainShape cs = new ChainShape();
                Vector2[] v = new Vector2[3];
                v[0] = new Vector2(-ts / 2 / PPM, -ts / 2 / PPM);
                v[1] = new Vector2(-ts / 2 / PPM, ts / 2 / PPM);
                v[2] = new Vector2(ts / 2 / PPM, ts / 2 / PPM);
                cs.createChain(v);
                FixtureDef fd = new FixtureDef();
                fd.friction = 0;
                fd.shape = cs;
                fd.restitution = 3;
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

        if(layer != null){

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
    }}
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
                fdef.filter.maskBits = BIT_PLAYER | BIT_GROUND;

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

        if (layer != null){

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
        //bullets = new Array<Projectile>();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        bdef.position.set(-100, -100);
        bdef.linearVelocity.set(0, 0);
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);
        body.setGravityScale(0);

        shape.setAsBox(13 / PPM, 15 / PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_BULLET;
        fdef.filter.maskBits = BIT_ENEMY;
        body.createFixture(fdef).setUserData("bullet");
        shape.dispose();

        //create player
        bullet = new Projectile(body);
        // bullets.add(bullet);
        // body.setUserData(bullet);
    }


    private void createEnemy() {
        enemies = new Array<Enemy>();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        bdef.position.set(player.getposition().x + 5, player.getposition().y + 5);
        bdef.linearVelocity.set(0, 0);
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);
        body.setGravityScale(3);

        shape.setAsBox(13 / PPM, 15 / PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_ENEMY;
        fdef.filter.maskBits = B2DVars.BIT_GROUND | BIT_PLAYER | B2DVars.BIT_BULLET;
        body.createFixture(fdef).setUserData("enemy");
        shape.dispose();

        //create enemy
        enemy = new Enemy(body);
        enemies.add(enemy);
        body.setUserData(enemy);
    }

    public void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();
        accumulator += Math.min(delta, 0.25f);

        if (accumulator >= Game.STEP) {
            accumulator -= Game.STEP;
            world.step(Game.STEP, 1, 1);
        }
    }

    public void unlockLevel(){

        levelS = game.lvls.getInteger("key");

        if(level < levelS){
            //LVL_UNLOCKED = level;
        }
        else if (level >= levelS)
        LVL_UNLOCKED = LVL_UNLOCKED +1;
        game.lvls.putInteger("key",LVL_UNLOCKED);
        game.lvls.flush();
    }
}

