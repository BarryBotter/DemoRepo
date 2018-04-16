package my.game.handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;

import static my.game.handlers.B2DVars.P_HEIGHT;
import static my.game.handlers.B2DVars.P_WIDTH;

public class BoundedCamera extends OrthographicCamera {

    private float xmin;
    private float xmax;
    private float ymin;
    private float ymax;

    public BoundedCamera() {
        this(0, 0, 0, 0);
    }

    private BoundedCamera(float xmin, float xmax, float ymin, float ymax) {
        super();
        setBounds(xmin, xmax, ymin, ymax);
    }

    public void setBounds(float xmin, float xmax, float ymin, float ymax) {
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
    }

    public void setPosition(float x, float y) {
        setPosition(x, y, 0);
    }

    private void setPosition(float x, float y, float z) {
        position.set(x, y, z);
        fixBounds();
    }

    private void fixBounds() {
        if(position.x < xmin + P_WIDTH / 2) {
            position.x = xmin + P_WIDTH / 2;
        }
        if(position.x > xmax - P_HEIGHT/ 2) {
            position.x = xmax - P_HEIGHT / 2;
        }
        if(position.y < ymin +  P_WIDTH  / 2) {
            position.y = ymin + P_WIDTH  / 2;
        }
        if(position.y > ymax -P_HEIGHT / 2) {
            position.y = ymax - P_HEIGHT/ 2;
        }
    }
}
