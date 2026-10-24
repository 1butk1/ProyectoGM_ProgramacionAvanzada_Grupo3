package com.mygdx.game;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
 
public class Lluvia {
 
    private static final float ANCHO_PANTALLA = 800f;
    private static final float ALTO_PANTALLA = 480f;
    private static final float TAMANO_GOTA = 64f;
 
    // ---- Ajustes de dificultad (tocar aqui para balancear) ----
    /** Multiplica la velocidad que trae LevelConfig. */
    private static final float MULT_VELOCIDAD = 1.35f;
    /** Multiplica el intervalo que trae LevelConfig (menor = caen mas gotas). */
    private static final float MULT_INTERVALO = 0.60f;
    /** Nunca cae una gota con menos de este intervalo (segundos). */
    private static final float INTERVALO_MINIMO = 0.22f;
    /** Segundos de partida hasta llegar a la dificultad maxima. */
    private static final float TIEMPO_RAMPA = 150f;
    /** Separacion minima en X entre gotas consecutivas. */
    private static final float SEPARACION_X = 150f;
    /** Probabilidad base de que una gota apunte cerca del jugador. */
    private static final float PROB_APUNTADA = 0.25f;
 
    private Array<CaidaObject> gotas;
    private float temporizadorAparicion;
    private float intervaloAparicion;
 
    private float tiempoJugado;
    private float ultimaX = ANCHO_PANTALLA / 2f;
    private Tarro tarroActual;
 
    private LevelConfig configActual;
    private GameManager ultimoGestor;
 
    private Texture texturaAgua;
    private Texture texturaToxica;
    private Texture texturaEscudo;
    private Texture texturaDorada;
    private Texture texturaNegra;
 
    private Sound sonidoGota;
    private Music musicaLluvia;
 
    /**
     * Las texturas drop.png y dropBad.png son las originales del proyecto.
     * Las otras tres (dropShield, dropGolden, dropBlack) estan en assets/.
     */
    public Lluvia(Sound sonidoGota, Music musicaLluvia) {
        this.sonidoGota = sonidoGota;
        this.musicaLluvia = musicaLluvia;
 
        gotas = new Array<CaidaObject>();
        temporizadorAparicion = 0f;
        intervaloAparicion = 1f;
 
        texturaAgua = new Texture("drop.png");
        texturaToxica = new Texture("dropBad.png");
        texturaEscudo = new Texture("dropShield.png");
        texturaDorada = new Texture("dropGolden.png");
        texturaNegra = new Texture("dropBlack.png");
    }
 
    /** Inicia la musica de fondo (llamar en show() de GameScreen). */
    public void startMusic() {
        musicaLluvia.setLooping(true);
        musicaLluvia.play();
    }
 
    public void stopMusic() {
        musicaLluvia.stop();
    }
 
    /** GameScreen la llama cuando cambia de nivel (con LevelManager.getCurrentConfig()). */
    public void setLevelConfig(LevelConfig config) {
        this.configActual = config;
        this.intervaloAparicion = config.getSpawnInterval();
    }
 
    /** 0 al empezar, 1 tras TIEMPO_RAMPA segundos. */
    private float dificultad() {
        return Math.min(1f, tiempoJugado / TIEMPO_RAMPA);
    }
 
    public void update(float delta, Tarro tarro, GameManager gestor) {
        this.ultimoGestor = gestor;
        this.tarroActual = tarro;
        if (configActual == null) return;
 
        tiempoJugado += delta;
        float dif = dificultad();
 
        // Generacion: el intervalo se acorta con el nivel y con el tiempo jugado
        float intervalo = intervaloAparicion * MULT_INTERVALO * (1f - 0.45f * dif);
        intervalo = Math.max(INTERVALO_MINIMO, intervalo);
 
        temporizadorAparicion += delta;
        if (temporizadorAparicion >= intervalo) {
            temporizadorAparicion = 0f;
            spawnDrop(configActual);
 
            // Oleada ocasional: una fila de gotas peligrosas con un hueco para pasar
            // (no mientras el jugador busca su balde: ya esta en desventaja)
            if (dif >= 0.25f && tarro.hasBucket() && MathUtils.randomBoolean(0.05f + 0.10f * dif)) {
                spawnOleada(configActual);
            }
        }
 
        // Actualizacion y colisiones (recorrido inverso para poder eliminar)
        for (int i = gotas.size - 1; i >= 0; i--) {
            CaidaObject gota = gotas.get(i);
            gota.update(delta);
 
            if (gota.isActive() && gota.getBounds().overlaps(tarro.getBounds())) {
                ((EfectoCaida) gota).applyEffect(tarro, gestor);
                if (gota instanceof CaidaAgua || gota instanceof CaidaGotaDorada) {
                    sonidoGota.play();
                }
                gota.deactivate();
            }
 
            if (!gota.isActive()) {
                gotas.removeIndex(i);
            }
        }
    }
 
    /** Elige una X alejada de la ultima; a veces apunta cerca del jugador. */
    private float elegirX() {
        float x = MathUtils.random(0f, ANCHO_PANTALLA - TAMANO_GOTA);
        for (int intento = 0; intento < 10 && Math.abs(x - ultimaX) < SEPARACION_X; intento++) {
            x = MathUtils.random(0f, ANCHO_PANTALLA - TAMANO_GOTA);
        }
        float probApuntada = PROB_APUNTADA + 0.15f * dificultad();
        if (tarroActual != null && MathUtils.randomBoolean(probApuntada)) {
            float centro = tarroActual.getBounds().x + MathUtils.random(-60f, 60f);
            x = MathUtils.clamp(centro, 0f, ANCHO_PANTALLA - TAMANO_GOTA);
        }
        ultimaX = x;
        return x;
    }
 
    public void spawnDrop(LevelConfig config) {
        float x = elegirX();
        float y = ALTO_PANTALLA;
        // cada gota varia un poco de velocidad para que no caigan "en bloque"
        float velocidad = config.getDropSpeed() * MULT_VELOCIDAD * (1f + 0.5f * dificultad())
                * MathUtils.random(0.9f, 1.2f);
 
        float dado = MathUtils.random(100f);
        CaidaObject gota;
 
        if (dado < 50f) {
            gota = new CaidaAgua(x, y, velocidad, texturaAgua);
        } else if (dado < 75f) {
            gota = new CaidaAguaToxica(x, y, velocidad, texturaToxica);
        } else if (dado < 83f) {
            gota = new CaidaEscudo(x, y, velocidad, texturaEscudo);
        } else if (dado < 91f) {
            gota = new CaidaAguaNegra(x, y, velocidad, texturaNegra);
        } else if (ultimoGestor != null && ultimoGestor.canSpawnGoldenDrop()) {
            gota = new CaidaGotaDorada(x, y, velocidad, texturaDorada);
        } else {
            gota = new CaidaAgua(x, y, velocidad, texturaAgua);
        }
 
        gotas.add(gota);
    }
 
    /** Pared de gotas toxicas en 5 columnas con una libre; hay que colarse por el hueco. */
    private void spawnOleada(LevelConfig config) {
        int columnas = 5;
        float ancho = ANCHO_PANTALLA / columnas;
        int hueco = MathUtils.random(columnas - 1);
        float velocidad = config.getDropSpeed() * MULT_VELOCIDAD * (1f + 0.5f * dificultad());
        for (int c = 0; c < columnas; c++) {
            if (c == hueco) continue;
            float x = c * ancho + (ancho - TAMANO_GOTA) / 2f;
            // un poco de desfase vertical para que no sea una linea perfecta
            float y = ALTO_PANTALLA + MathUtils.random(0f, 40f);
            gotas.add(new CaidaAguaToxica(x, y, velocidad, texturaToxica));
        }
    }
 
    public void render(SpriteBatch batch) {
        for (CaidaObject gota : gotas) {
            gota.draw(batch);
        }
    }
 
    /** Quita las gotas en pantalla sin reiniciar la dificultad (para cambios de nivel). */
    public void limpiarGotas() {
        for (CaidaObject gota : gotas) {
            gota.dispose();
        }
        gotas.clear();
        temporizadorAparicion = 0f;
    }
 
    /** Elimina todas las gotas y reinicia la dificultad (util al reiniciar la partida). */
    public void clear() {
        limpiarGotas();
        tiempoJugado = 0f;
        ultimaX = ANCHO_PANTALLA / 2f;
    }
 
    public void dispose() {
        clear();
        texturaAgua.dispose();
        texturaToxica.dispose();
        texturaEscudo.dispose();
        texturaDorada.dispose();
        texturaNegra.dispose();
        sonidoGota.dispose();
        musicaLluvia.dispose();
    }
}
 