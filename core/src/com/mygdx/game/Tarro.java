package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;


public class Tarro {
	 
    private static final float SCREEN_WIDTH = 800f;
    private static final float BASE_SPEED = 400f;
    private static final float SLOW_FACTOR = 0.4f;
    private static final int INITIAL_LIVES = 3;
    private static final int HURT_FRAMES = 50;
 
    private Rectangle bounds;
    private int lives;
    private float moveSpeed;
    private boolean shieldActive;
    private float slowTimer;
    private ControlStrategy controlStrategy;
    private Texture texture;
    private Sound hurtSound;
 
    // animacion de herido (igual que en la version original)
    private boolean hurt;
    private int hurtTimer;
 
    public Tarro(Texture texture, ControlStrategy controlStrategy, Sound hurtSound) {
        this.texture = texture;
        this.controlStrategy = controlStrategy;
        this.hurtSound = hurtSound;
        this.bounds = new Rectangle(SCREEN_WIDTH / 2f - 32f, 20f, 64f, 64f);
        this.lives = INITIAL_LIVES;
        this.moveSpeed = BASE_SPEED;
        this.shieldActive = false;
        this.slowTimer = 0f;
        this.hurt = false;
    }
 
    /** Actualiza movimiento y temporizadores. */
    public void updateMovement(float delta) {
        float speed = moveSpeed;
        if (slowTimer > 0f) {
            speed *= SLOW_FACTOR;
            slowTimer -= delta;
            if (slowTimer < 0f) slowTimer = 0f;
        }
 
        controlStrategy.move(bounds, speed, delta);
 
        // Mantener el balde dentro de la pantalla
        if (bounds.x < 0) bounds.x = 0;
        if (bounds.x > SCREEN_WIDTH - bounds.width) bounds.x = SCREEN_WIDTH - bounds.width;
    }
 
    /** Cambia la estrategia de control en caliente (usado desde PauseMenu). */
    public void setControlStrategy(ControlStrategy strategy) {
        this.controlStrategy = strategy;
    }
 
    /** Recibe dano: si hay escudo lo consume, si no pierde una vida. */
    public void receiveDamage() {
        if (shieldActive) {
            shieldActive = false;
            return;
        }
        if (lives > 0) lives--;
        hurt = true;
        hurtTimer = HURT_FRAMES;
        hurtSound.play();
    }
 
    public void activateShield() {
        shieldActive = true;
    }
 
    public void applySlow(float duration) {
        slowTimer = duration;
    }
 
    public Rectangle getBounds() {
        return bounds;
    }
 
    public boolean hasShield() {
        return shieldActive;
    }
 
    public int getLives() {
        return lives;
    }
 
    public boolean isGameOver() {
        return lives <= 0;
    }
 
    /** True mientras dura la animacion de herido. */
    public boolean isHurt() {
        return hurt;
    }
 
    /** Dibuja el balde (tinte si esta ralentizado o con escudo, y temblor si esta herido). */
    public void draw(SpriteBatch batch) {
        if (slowTimer > 0f) {
            batch.setColor(0.5f, 0.5f, 0.5f, 1f);
        } else if (shieldActive) {
            batch.setColor(0.4f, 0.8f, 1f, 1f);
        }
 
        float y = bounds.y;
        if (hurt) {
            y += MathUtils.random(-5, 5);
            hurtTimer--;
            if (hurtTimer <= 0) hurt = false;
        }
        batch.draw(texture, bounds.x, y, bounds.width, bounds.height);
        batch.setColor(1f, 1f, 1f, 1f);
    }
 
    public void dispose() {
        texture.dispose();
        hurtSound.dispose();
    }

	public void activateShield() {
		// TODO Auto-generated method stub
		
	}
}
 