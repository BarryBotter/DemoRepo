package my.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import my.game.Game;
import my.game.handlers.MyContacListener;

/**
 * Created by Katriina on 22.3.2018.
 */

public class Player extends  B2DSprite{
    static final int MAX_HEALTH = 3;
    static final int MAX_NUMBER_OF_AMMO = 3;
    static int numberOfAmmo = MAX_NUMBER_OF_AMMO;
    static int playerHealth = MAX_HEALTH;
    private int enemyKillCount = 0;
    public static boolean gameOver = false;
    private MyContacListener cl;

    private int numCrystals;
    private int totalCrystals;

    public Player(Body body){
        super(body);

        Texture tex = Game.res.getTexture("char");
        TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];
        setAnimation(sprites, 1 / 12f);

        gameOver = false;
        numberOfAmmo = MAX_NUMBER_OF_AMMO;
        playerHealth = MAX_HEALTH;
    }

    public void collectCrystal(){numCrystals++;}
    public int getNumCrystals(){return numCrystals;}
    public void setTotalCrystals(int i){totalCrystals = i;}
    public int getTotalCrystals(){return totalCrystals;}


    public static int returnNumberOfAmmo() {
        return numberOfAmmo;
    }

    public static int returnMaxAmmo() {
        return MAX_NUMBER_OF_AMMO;
    }

    public static void reload() {
        numberOfAmmo = MAX_NUMBER_OF_AMMO;
    }

    public static void increaseAmmo() {
        if(numberOfAmmo < MAX_NUMBER_OF_AMMO) {
            numberOfAmmo++;
        }
    }

    public static void loseHealth() {
        // Decrease player health by 1 when hit.
        if(playerHealth != 0) {
            playerHealth--;
        }
        // If the player health hits 0, start game over procedure.
        if(playerHealth == 0) {
            gameOver = true;
        }
    }

    public static void fullHeal() {
        playerHealth = MAX_HEALTH;
    }

    public static void gainHealth() {
        if(playerHealth < 3) {
            playerHealth++;
        }
    }

    public static int returnHealth() {
        return playerHealth;
    }

    public static int returnMaxHealth() {
        return MAX_HEALTH;
    }

    public void increaseEnemyKC() {
        enemyKillCount++;
        System.out.println(enemyKillCount);
    }

    public int getEnemyKC() {
        return enemyKillCount;
    }

    public static boolean gameIsOver() {
        return gameOver;
    }
}