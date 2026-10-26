package com.mygdx.game.template;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.personaje.Tarro;
import com.mygdx.game.logica.*;


public class CaidaBuitre extends CaidaObject implements EfectoCaida {

	private enum Fase { VOLANDO, PREPARANDO, PICADA, ASCENSO }
	 
    private static final float ANCHO = 80f;
    private static final float ALTO = 56f;
    private static final float ANCHO_PANTALLA = 800f;
    private static final float ALTO_PANTALLA = 480f;
 
    // Distancia horizontal al jugador a la que se detiene a prepararse. 
    private static final float DISTANCIA_ATAQUE = 140f;
    // Segundos de aviso antes de lanzarse (aletea rapido sobre el jugador). 
    private static final float TIEMPO_AVISO = 0.5f;
    // Velocidad de la picada = velocidad de vuelo x este factor. 
    private static final float FACTOR_PICADA = 2.0f;
    private static final float VELOCIDAD_ASCENSO = 320f;
    private static final float AMPLITUD_ONDA = 18f;
 
    private static final int[] SECUENCIA_ALETEO = {0, 1, 2, 1};
 
    private final Tarro objetivo;
    private final float direccion;      // +1 vuela a la derecha, -1 a la izquierda
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
 
    // Template Method, no cae como una gota, vuela segun su fase. 
    @Override
    protected void updateMovement(float delta) {
        tiempo += delta;
 
        if (fase == Fase.VOLANDO) {
            area.x += direccion * velocidad * delta;
            area.y = alturaVuelo + MathUtils.sin(tiempo * 3f) * AMPLITUD_ONDA;
            if (enPantalla() && Math.abs(centroX() - centroJugador()) < DISTANCIA_ATAQUE) {
                fase = Fase.PREPARANDO;
                tiempoFase = 0f;
            }
 
        } else if (fase == Fase.PREPARANDO) {
            tiempoFase += delta;
            area.x += direccion * velocidad * 0.15f * delta;
            area.y = alturaVuelo + MathUtils.sin(tiempo * 14f) * 5f;
            if (tiempoFase >= TIEMPO_AVISO) {
                iniciarPicada();
            }
 
        } else if (fase == Fase.PICADA) {
            float paso = velocidad * FACTOR_PICADA * delta;
            float dx = destinoX - area.x;
            float dy = destinoY - area.y;
            float distancia = (float) Math.sqrt(dx * dx + dy * dy);
            if (distancia <= paso) {
                area.x = destinoX;
                area.y = destinoY;
                fase = Fase.ASCENSO;
            } else {
                area.x += dx / distancia * paso;
                area.y += dy / distancia * paso;
            }
 
        } else { 
            area.x += direccion * velocidad * delta;
            area.y += VELOCIDAD_ASCENSO * delta;
        }
    }
    private void iniciarPicada() {
        destinoX = MathUtils.clamp(centroJugador() - ANCHO / 2f, 0f, ANCHO_PANTALLA - ANCHO);
        destinoY = objetivo.getBounds().y + 6f;
        fase = Fase.PICADA;
    }
    private float centroX() {return area.x + area.width / 2f;}
    private float centroJugador() {return objetivo.getBounds().x + objetivo.getBounds().width / 2f;}
    private boolean enPantalla() {return area.x + area.width > 0f && area.x < ANCHO_PANTALLA;}
    @Override
    protected boolean afectadoPorViento() {return false;}
    @Override
    public boolean isOutOfBounds() {
        return area.x + area.width < -20f
                || area.x > ANCHO_PANTALLA + 20f
                || area.y > ALTO_PANTALLA + 80f;
    }
 
    @Override
    public void draw(SpriteBatch batch) {
        if (!activo) return;
        int cuadro;
        if (fase == Fase.PICADA) {
            cuadro = 3;
        } else {
            float ritmo = (fase == Fase.PREPARANDO) ? 22f : 9f;
            cuadro = SECUENCIA_ALETEO[((int) (tiempo * ritmo)) % SECUENCIA_ALETEO.length];
        }
        batch.draw(textura, area.x, area.y, area.width, area.height,
                cuadro * (int) ANCHO, 0, (int) ANCHO, (int) ALTO,
                direccion < 0f, false);
    }
 
    @Override
    public void applyEffect(Tarro tarro, GameManager gestor) {tarro.golpeDeBuitre();}
}
 