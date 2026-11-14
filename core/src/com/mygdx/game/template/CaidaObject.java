package com.mygdx.game.template;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.logica.GameManager;
import com.mygdx.game.personaje.Tarro;

public abstract class CaidaObject implements Visible, EfectoCaida {

    private static final float ANCHO_PANTALLA = 800f;
    private static final float ALTURA_SUELO = 24f;
    private static final float GRADOS_POR_VIENTO = 0.15f;
    private static final float INCLINACION_MAXIMA = 15f;
    private final Rectangle area;
    private final Texture textura;
    private float velocidad;
    private boolean activo;
    private float viento = 0f;
    private final float sensibilidadViento = MathUtils.random(0.7f, 1.2f);
    private MarcaSuelo marcaGenerada;

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

        if (debeAterrizar() && area.y <= ALTURA_SUELO) {
            alAterrizar();
            activo = false;
            return;
        }
        if (isOutOfBounds()) {activo = false;}
    }
    protected void updateMovement(float delta) {area.y -= velocidad * delta;}
    protected void updateSpecialBehavior(float delta) { }
    protected boolean afectadoPorViento() {return true;}
    protected boolean debeAterrizar() {return false;}
    protected void alAterrizar() { }
    public boolean requiereBalde() {return false;}
    public boolean reproduceSonidoAlAtrapar() {return false;}
    @Override
    public void colorVision(Color destino) {destino.set(0f, 0f, 0f, 0f);}
    protected final void generarMarca(MarcaSuelo marca) {this.marcaGenerada = marca;}
    public final MarcaSuelo retirarMarca() {
        MarcaSuelo m = marcaGenerada;
        marcaGenerada = null;return m;
    }
    public void setViento(float viento) {this.viento = viento;}
    private void aplicarViento(float delta) {
        if (!afectadoPorViento()) return;
        area.x += viento * sensibilidadViento * delta;
        mantenerEnPantalla();
    }
    protected final float getX() { return area.x; }
    protected final float getY() { return area.y; }
    protected final float getAncho() { return area.width; }
    protected final float getAlto() { return area.height; }
    protected final float centroX() { return area.x + area.width / 2f; }
    protected final float getVelocidad() { return velocidad; }
    protected final void setVelocidad(float velocidad) { this.velocidad = velocidad; }
    protected final void desplazar(float dx, float dy) {
        area.x += dx;
        area.y += dy;
    }
    protected final void mantenerEnPantalla() { area.x = MathUtils.clamp(area.x, 0f, ANCHO_PANTALLA - area.width);}
    @Override
    public Rectangle getBounds() {return new Rectangle(area);}
    public boolean colisionaCon(Rectangle otro) {return area.overlaps(otro);}
    public boolean isOutOfBounds() {return area.y + area.height < 0;}
    @Override
    public boolean isActive() {return activo;}
    public void deactivate() {activo = false;}
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

    public void dispose() {activo = false;}
    @Override
    public abstract void applyEffect(Tarro tarro, GameManager gestor);
}
