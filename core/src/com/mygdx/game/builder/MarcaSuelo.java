package com.mygdx.game.builder;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.personaje.Tarro;
 
/*
  Base de todo lo que una gota deja en el suelo (charco radiactivo, charco de petroleo)
  Template Method, igual que CaidaObject: la base maneja tiempo, vida y dibujo;
  cada subclase decide su efecto, su transparencia y su escala.
 */
public abstract class MarcaSuelo {
 
    protected static final float ANCHO_PANTALLA = 800f;
 
    protected final Rectangle area;
    private final Rectangle pies = new Rectangle();
    private final Texture textura;
    private final float duracion;
    protected float tiempo = 0f;
 
    protected MarcaSuelo(float centroX, float ancho, float alto, float y, float duracion, Texture textura) {
        this.textura = textura;
        this.duracion = duracion;
        float x = MathUtils.clamp(centroX - ancho / 2f, 0f, ANCHO_PANTALLA - ancho);
        this.area = new Rectangle(x, y, ancho, alto);
    }
 
    public final void update(float delta, Tarro tarro) {
        tiempo += delta;
        aplicarEfecto(delta, tarro);
    }
    protected abstract void aplicarEfecto(float delta, Tarro tarro);
    protected abstract float getAlfa();
    protected float getEscala() {return 1f;}
 
    
    protected boolean pisando(Tarro tarro) {
        Rectangle t = tarro.getBounds();
        pies.set(t.x + 14f, t.y, t.width - 28f, 14f);
        return pies.overlaps(area);
    }
 
    protected float getDuracion() {
        return duracion;
    }
 
    public boolean isActive() {
        return tiempo < duracion;
    }
 
    public final void draw(SpriteBatch batch) {
        float escala = getEscala();
        float w = area.width * escala;
        float h = area.height * escala;
        batch.setColor(1f, 1f, 1f, MathUtils.clamp(getAlfa(), 0f, 1f));
        batch.draw(textura, area.x + (area.width - w) / 2f, area.y + (area.height - h) / 2f, w, h);
        batch.setColor(1f, 1f, 1f, 1f);
    }
}
 