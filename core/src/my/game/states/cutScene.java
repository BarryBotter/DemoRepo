package my.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    OrthographicCamera camera;
    ExtendViewport viewport;
    private Background bg;

    TextureRegion rect;
    Texture fontTex;
    BitmapFont font;
    public String dialogString = "default_string";
    int screenWidth = 1920,screenHeight = 1080;

    public cutScene(final GameStateManager gsm){
        super(gsm);

        Texture tex = Game.res.getTexture("menubg");
        bg = new Background(new TextureRegion(tex),hudCam,5 );
        bg.setVector(0, 0);

        //camera
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(1920, 1080, camera);

        //?????
        fontTex = new Texture("kuva.png");
        rect = new TextureRegion(fontTex, 250, 250, 1, 1);

        //Text in the box
        font = new BitmapFont();
        font.getData().setScale(1f,1f);


        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                dispose();
                gsm.setState(GameStateManager.LEVEL_SELECT);
                return super.touchUp(screenX, screenY, pointer, button);
            }
        });
        TextChange();

    }

    void TextChange() {
        Gdx.app.log("tag0", "method start");
        FileHandle ProgressFileHandle = Gdx.files.internal("res/xml/dialogs.xml");
        XmlReader reader = new XmlReader();
        XmlReader.Element xml_element = reader.parse(ProgressFileHandle);
        Array<XmlReader.Element> dialogs = xml_element.getChildrenByName("dialog");

        for (XmlReader.Element child : dialogs) {
            dialogString = child.getChildByName("string").getAttribute("text");

        }
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
        sb.draw(rect, (float) (screenWidth*0.01), 25, (float) (screenWidth*0.1), 50);
        font.draw(sb,dialogString,(float) (screenWidth*0.01) ,60);
        sb.end();
    }

    @Override
    public void dispose() {
        fontTex.dispose();
        font.dispose();
        //backgroundTexture.dispose();
        Gdx.input.setInputProcessor(null);
    }
}
