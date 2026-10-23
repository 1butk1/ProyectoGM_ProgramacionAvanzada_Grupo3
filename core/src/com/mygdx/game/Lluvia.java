package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class Lluvia {
	 
    private static final float ANCHO_PANTALLA = 800f;
    private static final float ALTO_PANTALLA = 480f;
    private static final float TAMANO_GOTA = 64f;
 
    private Array<CaidaObject> gotas;
    private float temporizadorAparicion;
    private float intervaloAparicion;
 
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
 
    public void update(float delta, Tarro tarro, GameManager gestor) {
        this.ultimoGestor = gestor;
        if (configActual == null) return;
 
        // Generacion
        temporizadorAparicion += delta;
        if (temporizadorAparicion >= intervaloAparicion) {
            temporizadorAparicion = 0f;
            spawnDrop(configActual);
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
 
    public void spawnDrop(LevelConfig config) {
        float x = MathUtils.random(0f, ANCHO_PANTALLA - TAMANO_GOTA);
        float y = ALTO_PANTALLA;
        float velocidad = config.getDropSpeed();
 
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
 
    public void render(SpriteBatch batch) {
        for (CaidaObject gota : gotas) {
            gota.draw(batch);
        }
    }
 
    /** Elimina todas las gotas (util al reiniciar la partida). */
    public void clear() {
        for (CaidaObject gota : gotas) {
            gota.dispose();
        }
        gotas.clear();
        temporizadorAparicion = 0f;
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
 