package my.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import my.game.Game;

import static my.game.handlers.B2DVars.MAX_HEALTH;

public class Player extends  B2DSprite{
    //private static final int MAX_HEALTH = 3;
    private static final int MAX_NUMBER_OF_AMMO = 3;

    private static boolean gameOver;
    private static int numberOfAmmo;
    private static int playerHealth;
    private static int healthCount;
    private static int enemyKillCount;

    private int numCrystals;
    private int totalCrystals;
    private String animationKey;

    public Player(Body body){
        super(body);
        setPlayerAnimation("playerWalk");
        resetPlayer();
    }

    public void setPlayerAnimation(String animationKey) {
        this.animationKey = animationKey;
        Texture tex = Game.res.getTexture(animationKey);
        TextureRegion[] sprites = TextureRegion.split(tex,  36, 34)[0];
        setAnimation(sprites, 1 / 12f);
    }

    public String returnCurrentAnimation() {
        return animationKey;
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

    public static void increaseAmmo() {
        if(numberOfAmmo < MAX_NUMBER_OF_AMMO) {
            numberOfAmmo++;
        }
    }

    static void decreaseAmmo() {
        numberOfAmmo--;
    }

    public static void loseHealth() {
        // Decrease player health by 1 when hit.
        if(playerHealth != 0) {
            playerHealth--;
            countHealth();
        }
        // If the player health hits 0, start game over procedure.
        if(playerHealth == 0) {
            gameOver = true;
        }
    }

    private static void countHealth(){
        healthCount++;
    }

    public static int counterHealth(){
        return healthCount;
    }

    public static int returnHealth() {
        return playerHealth;
    }

    public static int returnMaxHealth() {
        return MAX_HEALTH;
    }

    public static void increaseEnemyKC() {
        enemyKillCount++;
        System.out.println(enemyKillCount);
    }

    public static int getEnemyKC() {
        return enemyKillCount;
    }

    public static boolean gameIsOver() {
        return gameOver;
    }

    private void resetPlayer() {
        gameOver = false;
        numberOfAmmo = MAX_NUMBER_OF_AMMO;
        playerHealth = MAX_HEALTH;
        enemyKillCount = 0;
        healthCount = 0;
    }
}