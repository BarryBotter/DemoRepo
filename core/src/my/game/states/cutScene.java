package my.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import my.game.Game;
import my.game.entities.Background;
import my.game.handlers.GameStateManager;

/**
 * Created by velij on 4.4.2018.
 */

public class cutScene extends GameState {

    ExtendViewport viewport;
    private Background bg;
    private BitmapFont font;
    private String dialogString = "default_string";
    private int screenWidth = 1920,screenHeight = 1080;
    private static Pixmap pixmap;
    private Texture pixmaptex;
    String[] strings;
    int dialogNumber = 1;
    public cutScene(final GameStateManager gsm){
        super(gsm);

        Texture tex = Game.res.getTexture("menubg"); //background
        bg = new Background(new TextureRegion(tex),hudCam,5 );
        bg.setVector(0, 0);

        //camera
        viewport = new ExtendViewport(screenWidth, screenHeight,screenWidth,screenHeight, cam);

        //rectanglebox for dialogs
        getPixmapRoundedRectangle(250,250,50, Color.LIGHT_GRAY);
        pixmaptex = new Texture( pixmap );
        //pixmaptex.
        pixmap.dispose();

        //Text in the box
        font = new BitmapFont();
        font.getData().setScale(1f,1f);
        xmlRead();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (dialogNumber < 3){
                    dialogString = strings[dialogNumber];
                    dialogNumber++;
                }else {
                    dispose();
                    gsm.setState(GameStateManager.LEVEL_SELECT);
                }
                return super.touchUp(screenX, screenY, pointer, button);
            }
        });

    }

    void xmlRead() {
        strings = new String[20];
        //Gdx.app.log("tag0", "method start");
        FileHandle ProgressFileHandle = Gdx.files.internal("res/xml/dialogs.xml");
        XmlReader reader = new XmlReader();
        XmlReader.Element xml_element = reader.parse(ProgressFileHandle);
        Array<XmlReader.Element> dialogs = xml_element.getChildrenByName("dialog");
        int i = 0;
        for (XmlReader.Element child : dialogs) {
            strings[i] = child.get("string");
            Gdx.app.log("string"+i, strings[i]);
            i++;
        }
        dialogString = strings[0];
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {
        bg.render(sb);
        sb.begin();
        sb.draw(pixmaptex,10,10,300,80);
        font.draw(sb,dialogString,(float) (screenWidth*0.01) ,75);
        sb.end();
    }

    @Override
    public void dispose() {
        pixmaptex.dispose();
        font.dispose();
        //Gdx.input.setInputProcessor(null);
    }

    private void getPixmapRoundedRectangle(int width, int height, int radius, Color color) {
        // todo transparency
        pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        //pixmap = new Pixmap(width, height, Pixmap.Format.Alpha);
        pixmap.setBlending(Blending.None);
        pixmap.setColor(color);
        // Pink rectangle
        pixmap.fillRectangle(0, radius, pixmap.getWidth(), pixmap.getHeight()-2*radius);
        // Green rectangle
        pixmap.fillRectangle(radius, 0, pixmap.getWidth() - 2*radius, pixmap.getHeight());
        // Bottom-left circle
        pixmap.fillCircle(radius, radius, radius);
        // Top-left circle
        pixmap.fillCircle(radius, pixmap.getHeight()-radius, radius);
        // Bottom-right circle
        pixmap.fillCircle(pixmap.getWidth()-radius, radius, radius);
        // Top-right circle
        pixmap.fillCircle(pixmap.getWidth()-radius, pixmap.getHeight()-radius, radius);
    }

}
