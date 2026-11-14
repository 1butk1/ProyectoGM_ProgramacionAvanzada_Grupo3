package com.mygdx.game.template;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.logica.GameManager;
import com.mygdx.game.personaje.Tarro;

public class CaidaBuitre extends ObjetoAereo {
 
    private enum Fase { VOLANDO, PREPARANDO, PICADA, ASCENSO }
    private static final float ANCHO = 80f;
    private static final float ALTO = 56f;
    private static final float ANCHO_PANTALLA = 800f;
    private static final float ALTO_PANTALLA = 480f;
    private static final float DISTANCIA_ATAQUE = 140f;
    private static final float TIEMPO_AVISO = 0.5f;
    private static final float FACTOR_PICADA = 2.0f;
    private static final float VELOCIDAD_ASCENSO = 320f;
    private static final float AMPLITUD_ONDA = 18f;
    private static final int[] SECUENCIA_ALETEO = {0, 1, 2, 1};
    private final Tarro objetivo;
    private final float direccion;      
    private final float alturaVuelo;
    private Fase fase = Fase.VOLANDO;
    private float tiempo = 0f;
    private float tiempoFase = 0f;
    private float destinoX;
    private float destinoY;
 
    public CaidaBuitre(float x, float y, float velocidad, Texture textura, Tarro objetivo) {
        super(x, y, ANCHO, ALTO, velocidad, textura);
        this.objetivo = objetivo;
        this.direccion = (x < ANCHO_PANTALLA / 2f) ? 1f : -1f;
        this.alturaVuelo = y;
    }
 
    @Override
    protected void updateMovement(float delta) {
        tiempo += delta;
        float vel = getVelocidad();
 
        if (fase == Fase.VOLANDO) {
            desplazar(direccion * vel * delta, 0f);
            setPosicion(getX(), alturaVuelo + MathUtils.sin(tiempo * 3f) * AMPLITUD_ONDA);
            if (enPantalla() && Math.abs(centroX() - centroJugador()) < DISTANCIA_ATAQUE) {
                fase = Fase.PREPARANDO;
                tiempoFase = 0f;
            }
 
        } else if (fase == Fase.PREPARANDO) {
            tiempoFase += delta;
            desplazar(direccion * vel * 0.15f * delta, 0f);
            setPosicion(getX(), alturaVuelo + MathUtils.sin(tiempo * 14f) * 5f);
            if (tiempoFase >= TIEMPO_AVISO) {
                iniciarPicada();
            }
 
        } else if (fase == Fase.PICADA) {
            float paso = vel * FACTOR_PICADA * delta;
            float dx = destinoX - getX();
            float dy = destinoY - getY();
            float distancia = (float) Math.sqrt(dx * dx + dy * dy);
            if (distancia <= paso) {
                setPosicion(destinoX, destinoY);
                fase = Fase.ASCENSO;
            } else {
                desplazar(dx / distancia * paso, dy / distancia * paso);
            }
 
        } else {
            desplazar(direccion * vel * delta, VELOCIDAD_ASCENSO * delta);
        }
    }
 
    private void iniciarPicada() {
        destinoX = MathUtils.clamp(centroJugador() - ANCHO / 2f, 0f, ANCHO_PANTALLA - ANCHO);
        destinoY = objetivo.getBounds().y + 6f;
        fase = Fase.PICADA;
    }
    private float centroJugador() {return objetivo.getBounds().x + objetivo.getBounds().width / 2f;}
    private boolean enPantalla() {return getX() + getAncho() > 0f && getX() < ANCHO_PANTALLA;}
 
    @Override
    public boolean isOutOfBounds() {
        return getX() + getAncho() < -20f
                || getX() > ANCHO_PANTALLA + 20f
                || getY() > ALTO_PANTALLA + 80f;
    }
 
    @Override
    public void draw(SpriteBatch batch) {
        if (!isActive()) return;
        int cuadro;
        if (fase == Fase.PICADA) {
            cuadro = 3;
        } else {
            float ritmo = (fase == Fase.PREPARANDO) ? 22f : 9f;
            cuadro = SECUENCIA_ALETEO[((int) (tiempo * ritmo)) % SECUENCIA_ALETEO.length];
        }
        batch.draw(getTextura(), getX(), getY(), getAncho(), getAlto(),
                cuadro * (int) ANCHO, 0, (int) ANCHO, (int) ALTO,
                direccion < 0f, false);
    }
 
    @Override
    public void colorVision(Color destino) {destino.set(1.00f, 0.30f, 0.20f, 0.35f);}
 
    @Override
    public void applyEffect(Tarro tarro, GameManager gestor) {
        tarro.golpeDeBuitre();
    }
}
 