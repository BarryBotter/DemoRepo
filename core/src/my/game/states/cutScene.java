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

import my.game.Game;
import my.game.handlers.GameStateManager;

public class cutScene extends GameState {
    private TextureRegion bg,tutorialBg,cutScene1;
    private BitmapFont font;
    private String dialogString = "default_string",name;
    private int width = 320,height = 240;
    private static Pixmap pixmap;
    private Texture pixmaptex;
    private String[] strings;
    public static int dialogNumber; // changing this changes the dialog
    private boolean tutorialBool; //draws tutorial or default background
    private int i = 1;

    public static final float TIMEPERCHAR = 0.1f; // play with this for dif  speeds

    float ctimeperchar = 0;

    int numchars = 0;
    float delta; // To get delta time


    public cutScene(final GameStateManager gsm){
        super(gsm);
        xmlRead();

        //camera
        bigCam.setToOrtho(false,640,480);

        Texture tex,tutorial, cutSceneTex1;
        tex = Game.res.getTexture("menubg"); //Default background
        bg = new TextureRegion(tex,0,0,width,height);
        cutSceneTex1 = Game.res.getTexture("cutscene1");
        cutScene1 = new TextureRegion(cutSceneTex1,0,0,width,height);

        name = game.prefs.getString("name", "no name stored");
        if (dialogNumber == 1){
            dialogString = strings[1] + " " + name + "! " + strings[2];}
        if (dialogNumber == 9){
            dialogString = strings[1] + " " + name + "! " + strings[4];}

        //tutorial
        tutorial = Game.res.getTexture("tutorial");
        tutorialBg = new TextureRegion(tutorial,0,0,640,480);
        if (dialogNumber == 0) {
            tutorialBool = true; //changes the background
            i = 2;
            font = game.font18;
            boxCreate(500,500,100);
        }else {
            //Text in the box
            font = game.font8;
            boxCreate(250,250,50);}

        //for chancing the cutscene or exiting it
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                //tutorialScene
                if (dialogNumber == 0){
/*                    tutorialBool = true;
                    dialogString = strings[dialogNumber];*/
                    dispose();
                    gsm.setState(GameStateManager.LEVEL_SELECT);
                    return super.touchUp(screenX, screenY, pointer, button);
                }
                //scene for using username merge string to get a whole dialog
                else if (dialogNumber == 1) {
/*                    tutorialBool = false;
                    dialogString = strings[1];
                    dialogString += " " + name;*/
                    gsm.setState(GameStateManager.LEVEL_COMPLETE);
                    return super.touchUp(screenX, screenY, pointer, button);
                }
                else if (dialogNumber == 9){
                    gsm.setState(GameStateManager.LEVEL_COMPLETE);

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
        dialogString = strings[dialogNumber];
    }

    private void boxCreate(int width,int height, int radius){
        //rectanglebox for dialogs
        //getPixmapRoundedRectangle(250,250,50, Color.LIGHT_GRAY);
        getPixmapRoundedRectangle(width,height,radius, Color.LIGHT_GRAY);
        pixmaptex = new Texture( pixmap );
        pixmap.dispose();
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {
         delta = Gdx.graphics.getDeltaTime();
        sb.begin();
        if (tutorialBool){
            sb.draw(tutorialBg,0,0);
            sb.draw(pixmaptex,width/8*2,10,width*0.75f*2,height/4*2);
            font.draw(sb,dialogString,width*0.1875f *2,height/4*2-10);
        }else {
            sb.draw(cutScene1,0,0);
            sb.draw(pixmaptex,width/8,10,width*0.75f,height/4);

            if (numchars < dialogString.length()) { // if num of chars are lesser than string // length , if all chars are not parsed

                ctimeperchar += delta; // character time per char to be added with // delta

                if (ctimeperchar >= TIMEPERCHAR) { // if c time ie greater than time // for 1 char

                    ctimeperchar = 0; // make ctimeper char again 0

                    numchars++; // go to next character , to be printed

                }
            }
            String str = dialogString.substring(0, numchars); // get string to be printed
            font.draw(sb, str,width* 0.1875f ,height/4-10);
        }
        //sb.draw(pixmaptex,width/8,10,width*0.75f,height/4);
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
