package com.mygdx.game.logica;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.builder.LevelConfig;
import com.mygdx.game.personaje.Tarro;
import com.mygdx.game.template.CaidaAgua;
import com.mygdx.game.template.CaidaBuitre;
import com.mygdx.game.template.ObjetoAereo;
import com.mygdx.game.template.CaidaAguaNegra;
import com.mygdx.game.template.CaidaAguaToxica;
import com.mygdx.game.template.CaidaEscudo;
import com.mygdx.game.template.CaidaGotaDorada;
import com.mygdx.game.template.CaidaObject;
import com.mygdx.game.template.CaidaRadioactiva;
import com.mygdx.game.template.MarcaSuelo;
import com.mygdx.game.template.Visible;

public class Lluvia {

    private static final float ANCHO_PANTALLA = 800f;
    private static final float ALTO_PANTALLA = 480f;
    private static final float TAMANO_GOTA = 64f;
    private static final float MULT_VELOCIDAD = 1.35f;
    private static final float MULT_INTERVALO = 0.60f;
    private static final float INTERVALO_MINIMO = 0.22f;
    private static final float TIEMPO_RAMPA = 150f;
    private static final float SEPARACION_X = 150f;
    private static final float PROB_APUNTADA = 0.25f;
    private static final int NIVEL_MIN_BUITRE = 1;
    private static final float INTERVALO_BUITRE_MIN = 5f;
    private static final float INTERVALO_BUITRE_MAX = 8f;
    private static final float FACTOR_VELOCIDAD_BUITRE = 0.6f;
    private static final float ANCHO_BUITRE = 80f;
    private static final float VIENTO_MIN = 35f;
    private static final float VIENTO_MAX = 80f;
    private static final float PROB_RADIACTIVA = 0.16f;
    private final Array<CaidaObject> gotas;
    private final Array<MarcaSuelo> marcas;
    private float temporizadorAparicion;
    private float intervaloAparicion;
    private float tiempoJugado;
    private float ultimaX = ANCHO_PANTALLA / 2f;
    private Tarro tarroActual;
    private LevelConfig configActual;
    private GameManager ultimoGestor;
    private final Texture texturaAgua;
    private final Texture texturaToxica;
    private final Texture texturaEscudo;
    private final Texture texturaDorada;
    private final Texture texturaNegra;
    private final Texture texturaBuitre;
    private final Texture texturaRadiactiva;
    private final Texture texturaMarca;
    private final Texture texturaCharco;
    private boolean radiactivasActivas = false;
    private int nivelActual = 1;
    private float tiempoParaBuitre;
    private float tiempoViento;
    private float viento;
    private CaidaObject escudoEnPantalla;
    private ObjetoAereo buitre;
    private final Sound sonidoGota;
    private final Music musicaLluvia;
    
    public Lluvia(Sound sonidoGota, Music musicaLluvia) {
        this.sonidoGota = sonidoGota;
        this.musicaLluvia = musicaLluvia;
        gotas = new Array<CaidaObject>();
        marcas = new Array<MarcaSuelo>();
        temporizadorAparicion = 0f;
        intervaloAparicion = 1f;
        texturaAgua = new Texture("drop.png");
        texturaToxica = new Texture("dropBad.png");
        texturaEscudo = new Texture("dropShield.png");
        texturaDorada = new Texture("dropGolden.png");
        texturaNegra = new Texture("dropBlack.png");
        texturaBuitre = new Texture("buitre.png");
        texturaRadiactiva = new Texture("dropRadioactive.png");
        texturaMarca = new Texture("marca_radiactiva.png");
        texturaCharco = new Texture("charco_negro.png");
        tiempoParaBuitre = MathUtils.random(INTERVALO_BUITRE_MIN, INTERVALO_BUITRE_MAX);
    }
    public void startMusic() {
        musicaLluvia.setLooping(true);
        aplicarVolumenMusica();
        musicaLluvia.play();
    }
    public void stopMusic() {musicaLluvia.stop();}
    public void setVolumenEfectos(float v) {GameSettings.getInstance().setVolumenEfectos(v);}
    public void setVolumenMusica(float v) {
        GameSettings.getInstance().setVolumenMusica(v);
        aplicarVolumenMusica();
    }
    private void aplicarVolumenMusica() { musicaLluvia.setVolume(0.5f * GameSettings.getInstance().getVolumenMusicaEfectivo());}
    private void reproducirSonidoGota() {sonidoGota.play(GameSettings.getInstance().getVolumenEfectosEfectivo());}
    public void setLevelConfig(LevelConfig config) {
        this.configActual = config;
        this.intervaloAparicion = config.getSpawnInterval();
    }
    public void setNivelActual(int nivel) {this.nivelActual = nivel;}
    public void setRadiactivasActivas(boolean activas) {this.radiactivasActivas = activas;}
    private float dificultad() {return Math.min(1f, tiempoJugado / TIEMPO_RAMPA);}

   
    public void update(float delta, Tarro tarro, GameManager gestor) {
        this.ultimoGestor = gestor;
        this.tarroActual = tarro;
        if (configActual == null) return;

        tiempoJugado += delta;
        float dif = dificultad();
        actualizarMarcas(delta, tarro);
        actualizarViento(dif, delta);
        actualizarBuitre(delta, tarro, gestor, dif);

        float intervalo = intervaloAparicion * MULT_INTERVALO * (1f - 0.45f * dif);
        intervalo = Math.max(INTERVALO_MINIMO, intervalo);

        temporizadorAparicion += delta;
        if (temporizadorAparicion >= intervalo) {
            temporizadorAparicion = 0f;
            spawnDrop(configActual);

            if (dif >= 0.25f && tarro.hasBucket() && MathUtils.randomBoolean(0.05f + 0.10f * dif)) {
                spawnOleada(configActual);
            }
        }

        for (int i = gotas.size - 1; i >= 0; i--) {
            CaidaObject gota = gotas.get(i);
            gota.setViento(viento);
            gota.update(delta);   
            if (gota.isActive() && puedeAtrapar(gota, tarro)
                    && gota.colisionaCon(tarro.getBounds())) {
                gota.applyEffect(tarro, gestor);
                if (gota.reproduceSonidoAlAtrapar()) {
                    reproducirSonidoGota();
                }
                gota.deactivate();
            }
            recogerMarca(gota);
            if (!gota.isActive()) {
                gotas.removeIndex(i);
            }
        }
    }
    private void actualizarBuitre(float delta, Tarro tarro, GameManager gestor, float dif) {
        if (nivelActual >= NIVEL_MIN_BUITRE) {
            tiempoParaBuitre -= delta;
            if (tiempoParaBuitre <= 0f) {
                if (tarro.hasBucket() && !hayBuitre()) {
                    spawnBuitre(configActual, tarro);
                }
                tiempoParaBuitre = MathUtils.random(INTERVALO_BUITRE_MIN, INTERVALO_BUITRE_MAX)
                        * (1f - 0.35f * dif);
            }
        }
        if (buitre != null) {
            buitre.update(delta);
            if (buitre.isActive() && buitre.colisionaCon(tarro.getBounds())) {
                buitre.applyEffect(tarro, gestor);
                buitre.deactivate();
            }
            if (!buitre.isActive()) {
                buitre = null;
            }
        }
    }
    private boolean puedeAtrapar(CaidaObject gota, Tarro tarro) { return tarro.hasBucket() || !gota.requiereBalde();}
    private void recogerMarca(CaidaObject gota) {
        MarcaSuelo marca = gota.retirarMarca();
        if (marca != null) {
            marcas.add(marca);
        }
    }
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
        float velocidad = config.getDropSpeed() * MULT_VELOCIDAD * (1f + 0.5f * dificultad())
                * MathUtils.random(0.9f, 1.2f);
        if (radiactivasActivas && MathUtils.randomBoolean(PROB_RADIACTIVA)) {
            gotas.add(new CaidaRadioactiva(x, y, velocidad, texturaRadiactiva, texturaMarca));
            return;
        }
        float dado = MathUtils.random(100f);
        CaidaObject gota;
        if (dado < 50f) {
            gota = new CaidaAgua(x, y, velocidad, texturaAgua);
        } else if (dado < 75f) {
            gota = new CaidaAguaToxica(x, y, velocidad, texturaToxica);
        } else if (dado < 83f) {
            if (puedeCaerEscudo()) {
                gota = new CaidaEscudo(x, y, velocidad, texturaEscudo);
                escudoEnPantalla = gota;
            } else {
                gota = new CaidaAgua(x, y, velocidad, texturaAgua);
            }
        } else if (dado < 91f) {
            gota = new CaidaAguaNegra(x, y, velocidad, texturaNegra, texturaCharco);
        } else if (ultimoGestor != null && ultimoGestor.canSpawnGoldenDrop()) {
            gota = new CaidaGotaDorada(x, y, velocidad, texturaDorada);
        } else {
            gota = new CaidaAgua(x, y, velocidad, texturaAgua);
        }

        gotas.add(gota);
    }

    private void spawnBuitre(LevelConfig config, Tarro tarro) {
        boolean desdeIzquierda = MathUtils.randomBoolean();
        float x = desdeIzquierda ? -ANCHO_BUITRE : ANCHO_PANTALLA;
        float y = MathUtils.random(330f, 400f);
        float velocidad = config.getDropSpeed() * MULT_VELOCIDAD * FACTOR_VELOCIDAD_BUITRE;
        buitre = new CaidaBuitre(x, y, velocidad, texturaBuitre, tarro);
    }

    private void spawnOleada(LevelConfig config) {
        int columnas = 5;
        float ancho = ANCHO_PANTALLA / columnas;
        int hueco = MathUtils.random(columnas - 1);
        float velocidad = config.getDropSpeed() * MULT_VELOCIDAD * (1f + 0.5f * dificultad());
        for (int c = 0; c < columnas; c++) {
            if (c == hueco) continue;
            float x = c * ancho + (ancho - TAMANO_GOTA) / 2f;
            float y = ALTO_PANTALLA + MathUtils.random(0f, 40f);
            gotas.add(new CaidaAguaToxica(x, y, velocidad, texturaToxica));
        }
    }
    
    private void actualizarViento(float dif, float delta) {
        tiempoViento += delta;
        float onda = MathUtils.sin(tiempoViento * 0.35f) * 0.7f
                   + MathUtils.sin(tiempoViento * 0.90f + 1.7f) * 0.3f;
        float fuerza = VIENTO_MIN + (VIENTO_MAX - VIENTO_MIN) * dif;
        viento = onda * fuerza;
    }
    private boolean hayEscudoEnPantalla() {return escudoEnPantalla != null && escudoEnPantalla.isActive();}
    
    private boolean puedeCaerEscudo() {
        boolean jugadorConEscudo = (tarroActual != null && tarroActual.hasShield());
        return !jugadorConEscudo && !hayEscudoEnPantalla();
    }
    
    private boolean hayBuitre() {return buitre != null && buitre.isActive();}
    public void forEachVisible(AccionVisible accion) {
        for (int i = 0; i < gotas.size; i++) {
            accion.ejecutar(gotas.get(i));
        }
        if (hayBuitre()) {
            accion.ejecutar(buitre);
        }
    }
    
    public interface AccionVisible {void ejecutar(Visible visible);}
    public int cantidadGotas() {return gotas.size;}
    private void actualizarMarcas(float delta, Tarro tarro) {
        for (int m = marcas.size - 1; m >= 0; m--) {
            MarcaSuelo marca = marcas.get(m);
            marca.update(delta, tarro);
            if (!marca.isActive()) {
                marcas.removeIndex(m);
            }
        }
    }

    public void renderSuelo(SpriteBatch batch) {
        for (int i = 0; i < marcas.size; i++) {
            marcas.get(i).draw(batch);
        }
    }

    public void render(SpriteBatch batch) {
        for (CaidaObject gota : gotas) {
            gota.draw(batch);
        }
        if (buitre != null) {
            buitre.draw(batch);
        }
    }
    
    public void limpiarGotas() {
        for (CaidaObject gota : gotas) {
            gota.dispose();
        }
        gotas.clear();
        marcas.clear();
        if (buitre != null) {
            buitre.dispose();
        }
        escudoEnPantalla = null;
        buitre = null;
        temporizadorAparicion = 0f;
    }
    
    public void clear() {
        limpiarGotas();
        tiempoJugado = 0f;
        ultimaX = ANCHO_PANTALLA / 2f;
        tiempoParaBuitre = MathUtils.random(INTERVALO_BUITRE_MIN, INTERVALO_BUITRE_MAX);
    }

    public void dispose() {
        clear();
        texturaAgua.dispose();
        texturaToxica.dispose();
        texturaEscudo.dispose();
        texturaDorada.dispose();
        texturaNegra.dispose();
        texturaBuitre.dispose();
        texturaRadiactiva.dispose();
        texturaMarca.dispose();
        texturaCharco.dispose();
        sonidoGota.dispose();
        musicaLluvia.dispose();
    }
}
	 