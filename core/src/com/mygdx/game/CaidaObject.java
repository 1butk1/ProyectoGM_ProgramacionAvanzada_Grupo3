package com.mygdx.game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
 
public abstract class CaidaObject {
	protected Rectangle area;
    protected float velocidad;
    protected Texture textura;
    protected boolean activo;
 
    public CaidaObject(float x, float y, float ancho, float alto, float velocidad, Texture textura) {
        this.area = new Rectangle(x, y, ancho, alto);
        this.velocidad = velocidad;
        this.textura = textura;
        this.activo = true;
    }
 
    /** Template Method: no se puede sobreescribir. */
    public final void update(float delta) {
        if (!activo) return;
        updateMovement(delta);
        updateSpecialBehavior(delta);
        if (isOutOfBounds()) {
            activo = false;
        }
    }
 
    /** Paso 1: movimiento (por defecto cae en linea recta). */
    protected void updateMovement(float delta) {
        area.y -= velocidad * delta;
    }
 
    /** Paso 2: comportamiento propio de cada gota (por defecto ninguno). */
    protected void updateSpecialBehavior(float delta) {
    }
 
    public boolean isOutOfBounds() {
        return area.y + area.height < 0;
    }
 
    public Rectangle getBounds() {
        return area;
    }
 
    public boolean isActive() {
        return activo;
    }
 
    /** Marca la gota como inactiva (al ser recogida, por ejemplo). */
    public void deactivate() {
        activo = false;
    }
 
    public void draw(SpriteBatch batch) {
        if (activo) {
            batch.draw(textura, area.x, area.y, area.width, area.height);
        }
    }
 
    /** Las texturas son compartidas (las libera Lluvia), aqui solo se desactiva. */
    public void dispose() {
        activo = false;
    }
}
 