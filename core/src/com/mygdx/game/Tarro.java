package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;


public class Tarro {
	 
    private static final float ANCHO_PANTALLA = 800f;
    private static final float VELOCIDAD_BASE = 400f;
    private static final float FACTOR_LENTITUD = 0.4f;
    private static final int VIDAS_INICIALES = 3;
    private static final int FRAMES_HERIDO = 50;
 
    private Rectangle area;
    private int vidas;
    private float velocidadMovimiento;
    private boolean escudoActivo;
    private float temporizadorLentitud;
    private EstrategiaControles estrategiaControl;
    private Texture textura;
    private Sound sonidoHerido;
 
    // animacion de herido (igual que en la version original)
    private boolean herido;
    private int temporizadorHerido;
 
    public Tarro(Texture textura, EstrategiaControles estrategiaControl, Sound sonidoHerido) {
        this.textura = textura;
        this.estrategiaControl = estrategiaControl;
        this.sonidoHerido = sonidoHerido;
        this.area = new Rectangle(ANCHO_PANTALLA / 2f - 32f, 20f, 64f, 64f);
        this.vidas = VIDAS_INICIALES;
        this.velocidadMovimiento = VELOCIDAD_BASE;
        this.escudoActivo = false;
        this.temporizadorLentitud = 0f;
        this.herido = false;
    }
 
    /** Actualiza movimiento y temporizadores. */
    public void updateMovement(float delta) {
        float velocidad = velocidadMovimiento;
        if (temporizadorLentitud > 0f) {
            velocidad *= FACTOR_LENTITUD;
            temporizadorLentitud -= delta;
            if (temporizadorLentitud < 0f) temporizadorLentitud = 0f;
        }
 
        estrategiaControl.movimiento(area, velocidad, delta);
 
        // Mantener el balde dentro de la pantalla
        if (area.x < 0) area.x = 0;
        if (area.x > ANCHO_PANTALLA - area.width) area.x = ANCHO_PANTALLA - area.width;
    }
 
    /** Cambia la estrategia de control en caliente (usado desde PauseMenu). */
    public void setControlStrategy(EstrategiaControles estrategia) {
        this.estrategiaControl = estrategia;
    }
 
    /** Recibe dano: si hay escudo lo consume, si no pierde una vida. */
    public void receiveDamage() {
        if (escudoActivo) {
            escudoActivo = false;
            return;
        }
        if (vidas > 0) vidas--;
        herido = true;
        temporizadorHerido = FRAMES_HERIDO;
        sonidoHerido.play();
    }
 
    public void activateShield() {
        escudoActivo = true;
    }
 
    public void applySlow(float duracion) {
        temporizadorLentitud = duracion;
    }
 
    public Rectangle getBounds() {
        return area;
    }
 
    public boolean hasShield() {
        return escudoActivo;
    }
 
    public int getLives() {
        return vidas;
    }
 
    public boolean isGameOver() {
        return vidas <= 0;
    }
 
    /** True mientras dura la animacion de herido. */
    public boolean isHurt() {
        return herido;
    }
 
    /** Dibuja el balde (tinte si esta lento o con escudo, y temblor si esta herido). */
    public void draw(SpriteBatch batch) {
        if (temporizadorLentitud > 0f) {
            batch.setColor(0.5f, 0.5f, 0.5f, 1f);
        } else if (escudoActivo) {
            batch.setColor(0.4f, 0.8f, 1f, 1f);
        }
 
        float y = area.y;
        if (herido) {
            y += MathUtils.random(-5, 5);
            temporizadorHerido--;
            if (temporizadorHerido <= 0) herido = false;
        }
        batch.draw(textura, area.x, y, area.width, area.height);
        batch.setColor(1f, 1f, 1f, 1f);
    }
 
    public void dispose() {
        textura.dispose();
        sonidoHerido.dispose();
    }
}
 