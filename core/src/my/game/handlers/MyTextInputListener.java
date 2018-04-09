package my.game.handlers;

import com.badlogic.gdx.Input;

/**
 * Created by velij on 19.3.2018.
 */

public class MyTextInputListener implements Input.TextInputListener {

    private final my.game.Game game;
    private String nameToSettings;

    public MyTextInputListener(final my.game.Game gam)
    {
        this.game = gam;
    }

    @Override
    public void input(String text) {
        if(!text.equals("")) {
            nameToSettings = text;
            game.prefs.putString("name", nameToSettings);
            game.prefs.flush();
        }
    }

    @Override
    public void canceled() {
    }
}
