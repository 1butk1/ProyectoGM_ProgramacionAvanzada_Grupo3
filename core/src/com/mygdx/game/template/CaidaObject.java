package com.mygdx.game.template;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
 
public abstract class CaidaObject {
	
	protected Rectangle area;
    protected float velocidad;
    protected Texture textura;
    protected boolean activo;
    private static final float ANCHO_PANTALLA = 800f;
    private static final float GRADOS_POR_VIENTO = 0.15f;   
    private static final float INCLINACION_MAXIMA = 15f;    
    private float viento = 0f;
    private final float sensibilidadViento = MathUtils.random(0.7f, 1.2f);
 
    public CaidaObject(float x, float y, float ancho, float alto, float velocidad, Texture textura) {
        this.area = new Rectangle(x, y, ancho, alto);
        this.velocidad = velocidad;
        this.textura = textura;
        this.activo = true;
    }
    public final void update(float delta) {
        if (!activo) return;
        updateMovement(delta);
        aplicarViento(delta);
        updateSpecialBehavior(delta);
        if (isOutOfBounds()) {
            activo = false;
        }
    }
    protected void updateMovement(float delta) {
        area.y -= velocidad * delta;
    }
    protected void updateSpecialBehavior(float delta) {
    }
    public void setViento(float viento) {
        this.viento = viento;
    }
    protected boolean afectadoPorViento() {
        return true;
    }
    private void aplicarViento(float delta) {
        if (!afectadoPorViento()) return;
        area.x += viento * sensibilidadViento * delta;
        if (area.x < 0) area.x = 0;
        if (area.x > ANCHO_PANTALLA - area.width) area.x = ANCHO_PANTALLA - area.width;
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
    public void deactivate() {
        activo = false;
    }
 
    public void draw(SpriteBatch batch) {
        if (activo) {
            float inclinacion = afectadoPorViento()
                    ? MathUtils.clamp(viento * sensibilidadViento * GRADOS_POR_VIENTO,
                            -INCLINACION_MAXIMA, INCLINACION_MAXIMA)
                    : 0f;
            batch.draw(textura, area.x, area.y, area.width / 2f, area.height / 2f,
                    area.width, area.height, 1f, 1f, inclinacion,
                    0, 0, textura.getWidth(), textura.getHeight(), false, false);
        }
    }
    public void dispose() {
        activo = false;
    }
}
 
