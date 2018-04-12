package my.game.handlers;

import my.game.Game;

/**
 * Created by Katriina on 20.3.2018.
 */

public class B2DVars {
    //pixel per meter
    public static final float PPM = 100;

    //category bits
    public static final short BIT_PLAYER = 2;
    public static final short BIT_TRAP = 4;
    public static final short BIT_CORNER = 8;
    public static final short BIT_JUMP =16;
    public static final short BIT_CRYSTAL = 32;
    public static final short BIT_GROUND = 64;
    public static final short BIT_BULLET = 128;
    public static final short BIT_ENEMY = 256;
    public static final short BIT_MELEE = 512;
    public static final short BIT_WIN = 1024;

    //other vars
     public static int MAX_HEALTH = 3;
     public static int LVL_UNLOCKED = 10;
     public static int CRYSTALS_COLLECTED = 0;
     public static int HITS_TAKEN = 0;
     public static int ENEMIES_DESTROYED = 0;
     public static float SOUND_LEVEL;

}
