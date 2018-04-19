package my.game.handlers;

import com.badlogic.gdx.Input;

public class MyTextInputListener implements Input.TextInputListener {

    private final my.game.Game game;

    public MyTextInputListener(final my.game.Game gam)
    {
        this.game = gam;
    }

    @Override
    public void input(String nameToSettings) {
        if(!nameToSettings.equals("")) {
            game.prefs.putString("name", nameToSettings);
            game.prefs.flush();
        }
    }

    @Override
    public void canceled() {
    }
}
