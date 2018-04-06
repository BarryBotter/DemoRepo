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
    // The possibility of spawning an enemy per one render cycle. 1/100
    private final int ROLL_MULTIPLIER = 1;
    private final int CHANCES_OUT_OF = 100;

    private Random rand = new Random();
    private boolean enemySpawned = true;
    private boolean rollState = false;

    public Enemy(Body body) {
        super(body);
        Texture tex = Game.res.getTexture("happyTooth");
        TextureRegion[] sprites = TextureRegion.split(tex,35,34)[0];
        setAnimation(sprites, 1/12f);
        enemySpawned = true;
        rollState = false;
    }

    public void enemySpawnRoller() {
        // Keep rolling until the enemy is spawned.
        if (!enemySpawned) {
            if (rand.nextInt(CHANCES_OUT_OF) == ROLL_MULTIPLIER) {
                enemySpawned = true;
                rollState = true;
            }
        }
    }

    public void enemySpawnState() {
        enemySpawned = false;
    }

    public boolean returnEnemySpawnState() {
        return enemySpawned;
    }

    public void readyToRoll() {
        rollState = false;
    }

    public boolean returnEnemyRollState() {
        return rollState;
    }
}