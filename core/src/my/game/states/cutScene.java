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
import com.badlogic.gdx.utils.viewport.StretchViewport;

import my.game.Game;
import my.game.handlers.GameStateManager;

/**
 * Created by velij on 4.4.2018.
 */

public class cutScene extends GameState {
    StretchViewport viewport;
    private TextureRegion bg,tutorialBg;
    private BitmapFont font;
    private String dialogString = "default_string";
    private int width = Game.V_WIDTH*2,height = Game.V_HEIGHT*2;
    private static Pixmap pixmap;
    private Texture pixmaptex;
    String[] strings;
    public int dialogNumber = 0;
    public boolean tutorialBool;

    public cutScene(final GameStateManager gsm){
        super(gsm);

        Texture tex = Game.res.getTexture("menubg"); //background
        bg = new TextureRegion(tex,0,0,width,height);

        //tutorial
        Texture tutorial = Game.res.getTexture("tutorial");
        tutorialBg = new TextureRegion(tutorial,0,0,width,height);
        if (dialogNumber == 0) tutorialBool = true; //changes the background

        //camera
        viewport = new StretchViewport(width, height, cam);

        //rectanglebox for dialogs
        getPixmapRoundedRectangle(250,250,50, Color.LIGHT_GRAY);
        pixmaptex = new Texture( pixmap );
        pixmap.dispose();

        //Text in the box
        font = game.font18;
        xmlRead();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                //tutorialScene
                if (dialogNumber == 0){
                    tutorialBool = true;
                    dialogString = strings[dialogNumber];

                    dispose();
                    gsm.setState(GameStateManager.LEVEL_SELECT);

                    return super.touchUp(screenX, screenY, pointer, button);
                }
                //scene for using username merge string to get a whole dialog
                else if (dialogNumber == 1) {
                    tutorialBool = false;
                    dialogString = strings[1];
                    dialogString += " " + game.prefs.getString("name", "no name stored");
                    dialogNumber++;
                    return super.touchUp(screenX, screenY, pointer, button);

                }
                else if (dialogNumber > 1 && dialogNumber < 4){
                    tutorialBool = false;
                    dialogString = strings[dialogNumber];
                    dialogNumber++;
                    return super.touchUp(screenX, screenY, pointer, button);

                }else {
                    dispose();
                    gsm.setState(GameStateManager.LEVEL_SELECT);
                    return super.touchUp(screenX, screenY, pointer, button);

                }
            }
        });

    }
    void xmlRead() {
        strings = new String[20];
        //Gdx.app.log("tag0", "method start");
        FileHandle xmlHandle = Gdx.files.internal("res/xml/dialogs.xml");
        XmlReader reader = new XmlReader();
        XmlReader.Element xml_element = reader.parse(xmlHandle);
        Array<XmlReader.Element> dialogs = xml_element.getChildrenByName("dialog");
        int i = 0;
        for (XmlReader.Element child : dialogs) {
            strings[i] = child.get("string");
            Gdx.app.log("string"+i, strings[i]);
            i++;
        }
        dialogString = strings[0];//by default the dialogString is the first string/tutorial
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {
        sb.begin();
        if (tutorialBool){
            sb.draw(tutorialBg,0,0);
        }else {
            sb.draw(bg,0,0);
        }
        sb.draw(pixmaptex,width/8,10,width*0.75f,height/4);
        font.draw(sb,dialogString,width*0.1875f ,height/4-10);
        sb.end();
    }

    @Override
    public void dispose() {
        pixmaptex.dispose();
        Gdx.input.setInputProcessor(null);
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
