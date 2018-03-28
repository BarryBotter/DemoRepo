package my.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.Random;

import my.game.Game;
import my.game.states.Play;

/**
 package com.mygdx.game;

 import com.badlogic.gdx.physics.box2d.Body;

 import java.util.Random;

 /**
 * Created by jimbo on 20.3.2018.
 */

public class Enemy extends B2DSprite{
    private static final float ENEMY_SPEED = 0.9f;
    private static final float MAX_ENEMY_RIGHT_MOVEMENT = 75;
    private static final float MIN_ENEMY_RIGHT_MOVEMENT = 0;

    // The possibility of spawning an enemy per one render cycle. 1/100
    private static final int ROLL_MULTIPLIER = 1;
    private static final int CHANCES_OUT_OF = 100;

    Random rand = new Random();
    public static boolean enemySpawned = false;
    public static boolean rollComplete = false;
    public static boolean enemyReadyToBeDestroyed = false;

    public Enemy(Body body) {
        super(body);
        Texture tex = Game.res.getTexture("happyTooth");
        TextureRegion[] sprites = TextureRegion.split(tex,35,34)[0];
        setAnimation(sprites, 1/12f);
    }

    public void enemyManager() {
        // Keep rolling until the enemy is spawned.
        if (!enemySpawned) {
            if (rand.nextInt(CHANCES_OUT_OF) == ROLL_MULTIPLIER) {
                rollComplete = true;
                enemySpawned = true;
            }
        }
    }

    public void enemyDestroyed() {
        enemySpawned = false;
    }

    public boolean returnEnemySpawnState() {
        return enemySpawned;
    }

    public boolean returnRollState() {
        return rollComplete;
    }

    public void revertRollState() {
        rollComplete = false;
    }

    public static void setDestroyState() {
        enemyReadyToBeDestroyed = true;
    }

    public void revertDestroyState() {
        enemyReadyToBeDestroyed = false;
    }


    public boolean returnDestroyState() {
        return enemyReadyToBeDestroyed;
    }
}