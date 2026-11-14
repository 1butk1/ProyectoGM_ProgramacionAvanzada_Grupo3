package com.mygdx.game.template;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.logica.GameManager;
import com.mygdx.game.personaje.Tarro;

public abstract class ObjetoAereo implements Visible, EfectoCaida {
    private final Rectangle area;
    private final Texture textura;
    private float velocidad;
    private boolean activo = true;
    protected ObjetoAereo(float x, float y, float ancho, float alto, float velocidad, Texture textura) {
        this.area = new Rectangle(x, y, ancho, alto);
        this.velocidad = velocidad;
        this.textura = textura;
    }
    public final void update(float delta) {
        if (!activo) return;
        updateMovement(delta);          
        updateSpecialBehavior(delta);   
        updateLifecycle();              
    }
    protected abstract void updateMovement(float delta);
    protected void updateSpecialBehavior(float delta) { }
    protected void updateLifecycle() {
        if (isOutOfBounds()) {
            deactivate();
        }
    }
    public abstract boolean isOutOfBounds();
    public abstract void draw(SpriteBatch batch);
    @Override
    public abstract void applyEffect(Tarro tarro, GameManager gestor);
    @Override
    public abstract void colorVision(Color destino);
    protected final float getX() { return area.x; }
    protected final float getY() { return area.y; }
    protected final float getAncho() { return area.width; }
    protected final float getAlto() { return area.height; }
    protected final float centroX() { return area.x + area.width / 2f; }
    protected final float getVelocidad() { return velocidad; }
    protected final Texture getTextura() { return textura; }
    protected final void desplazar(float dx, float dy) {
        area.x += dx;
        area.y += dy;
    }
    protected final void setPosicion(float x, float y) {
        area.x = x;
        area.y = y;
    }
    @Override
    public Rectangle getBounds() {return new Rectangle(area);}
    public boolean colisionaCon(Rectangle otro) {return area.overlaps(otro);}
    @Override
    public boolean isActive() {return activo;}
    public void deactivate() {activo = false;}
    public void dispose() {activo = false;}
}
