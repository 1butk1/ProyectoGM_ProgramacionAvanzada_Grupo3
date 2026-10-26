package com.mygdx.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.GameLluviaMenu;
import com.mygdx.game.builder.LevelConfig;
import com.mygdx.game.builder.LevelManager;
import com.mygdx.game.builder.Lluvia;
import com.mygdx.game.builder.Transcion;
import com.mygdx.game.builder.VisionLimitada;
import com.mygdx.game.logica.GameManager;
import com.mygdx.game.logica.Progreso;
import com.mygdx.game.modos.ModoJuego;
import com.mygdx.game.personaje.Tarro;
import com.mygdx.game.strategy.ControlMouse;
import com.mygdx.game.strategy.ControlTeclado;
import com.mygdx.game.strategy.EstrategiaControles;
 
public class GameScreen implements Screen {
 
	private static final int VIDAS_INICIALES = 3;
	private final Music musicaNivel;
	private final Sound hurtSound;
    private final GameLluviaMenu game;
    private final ModoJuego modo;
    private final int nivelInicial;   
    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Tarro tarro;
    private final Lluvia lluvia;
    private final GameManager manager;
    private final LevelManager levelManager;
    private final Transcion transicion = new Transcion();
    private final EstrategiaControles controlTeclado = new ControlTeclado();
    private final EstrategiaControles controlMouse = new ControlMouse();
    private boolean usandoMouse = false;
    private Texture fondo;
    private boolean liberado = false;
    private boolean ganando = false;
    private boolean finalListo = false;
    private final VisionLimitada vision = new VisionLimitada();
    private boolean oscuroActivo = false;

    private int nivelMostrado;
    private String nombreMostrado;

    public GameScreen(final GameLluviaMenu game, ModoJuego modo) {
        this(game, modo, 0);
    }

    public GameScreen(final GameLluviaMenu game, ModoJuego modo, int nivelInicial) {
        this.game = game;
        this.modo = modo;
        this.nivelInicial = nivelInicial;
        this.batch = game.getBatch();
        this.font = game.getFont();
        this.manager = new GameManager(VIDAS_INICIALES);
        this.levelManager = new LevelManager(modo, nivelInicial);
   
        if (!modo.esCiclico() && nivelInicial > 0) {
            this.manager.addScore(levelManager.getPuntajeInicialNivel(levelManager.getIndiceNivel()));
        }
        this.musicaNivel = Gdx.audio.newMusic(Gdx.files.internal("sonidonivel.mp3"));
        this.musicaNivel.setLooping(true);
        this.musicaNivel.setVolume(0.7f * game.getVolumenMusica());
        this.hurtSound = Gdx.audio.newSound(Gdx.files.internal("hurt.mp3"));
        

        Texture texPrincipal = new Texture(Gdx.files.internal("bucket.png"));
        Texture texSinBalde = new Texture(Gdx.files.internal("personaje_sin_balde.png"));
        Texture texBaldeCaido = new Texture(Gdx.files.internal("balde_caido.png"));

        tarro = new Tarro(texPrincipal, texSinBalde, texBaldeCaido, controlTeclado, hurtSound);

        Sound dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        Music rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        lluvia = new Lluvia(dropSound, rainMusic);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        nivelMostrado = levelManager.getCurrentLevel();
        nombreMostrado = levelManager.getCurrentConfig().getName();
        aplicarNivel();
        tarro.setVolumenEfectos(game.getVolumenEfectos());
        lluvia.setVolumenEfectos(game.getVolumenEfectos());
        lluvia.setVolumenMusica(game.getVolumenMusica());

    
        if (nivelInicial > 0 || modo.getMensajeZona(0, 0) != null) {
            iniciarCambioDeNivel();
        }
       
    }

    public ModoJuego getModo() {
        return modo;
    }

    private void aplicarNivel() {
        LevelConfig config = levelManager.getCurrentConfig();
        lluvia.setLevelConfig(config);
        lluvia.setNivelActual(levelManager.getCurrentLevel());
        tarro.setSaltoHabilitado(config.isJumpEnabled());
        oscuroActivo = config.getName().equals("Mina");
        if (!modo.esCiclico()) {
            Progreso.desbloquear(levelManager.getIndiceNivel());  
        }

        if (fondo != null) {
            fondo.dispose();
            fondo = null;
        }
        String ruta = config.getBackgroundPath();
        if (ruta != null && Gdx.files.internal(ruta).exists()) {
            fondo = new Texture(Gdx.files.internal(ruta));
        }
    }

    private void iniciarCambioDeNivel() {
        final LevelConfig nueva = levelManager.getCurrentConfig();
        final int numero = levelManager.getCurrentLevel();
        final int vuelta = levelManager.getVuelta();

        String titulo = (vuelta > 0 ? "Vuelta " + (vuelta + 1) + " - " : "")
                + "Nivel " + numero + " - " + nueva.getName();
        String mensaje = modo.getMensajeZona(levelManager.getIndiceNivel(), vuelta);

        transicion.iniciar(titulo, mensaje, new Runnable() {
            @Override
            public void run() {
                aplicarNivel();
                lluvia.limpiarGotas();
                nivelMostrado = numero;
                nombreMostrado = nueva.getName();
            }
        });
    }

    private void iniciarFinal() {
        ganando = true;
        transicion.iniciar("", "", new Runnable() {
            @Override
            public void run() {
                finalListo = true;
            }
        });
    }

    public void alternarControles() {
        usandoMouse = !usandoMouse;
        tarro.setControlStrategy(usandoMouse ? controlMouse : controlTeclado);
    }

    public String getNombreControl() {
        return usandoMouse ? "Mouse" : "Teclado";
    }

    @Override
    public void render(float delta) {
        if (manager.isPlaying()
                && (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.P))) {
            pausarJuego();
            return;
        }

        transicion.actualizar(delta);

        if (finalListo) {
            irAlFinal();
            return;
        }

        if (manager.isPlaying() && !tarro.isHurt() && !transicion.bloqueaJuego()) {
            tarro.updateMovement(delta);
            lluvia.update(delta, tarro, manager);

            if (!transicion.estaActiva() && !ganando) {
                if (modo.haGanado(manager.getScore())) {
                    iniciarFinal();
                } else if (levelManager.updateForScore(manager.getScore())) {
                    iniciarCambioDeNivel();
                }
            }
        }

        if (tarro.isGameOver() && !tarro.isHurt()) {
            manager.finishGame();
        }
        if (manager.isGameOver()) {
            terminarPartida();
            return;
        }

        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        if (fondo != null) {
            transicion.dibujarFondo(batch, fondo);
            batch.draw(fondo, 0, 0, 800, 480);
        }
        font.draw(batch, "Gotas totales: " + manager.getScore(), 5, 475);
        font.draw(batch, "HighScore : " + modo.getHighScore(game), camera.viewportWidth / 2 - 50, 475);
        font.draw(batch, "Vidas : " + tarro.getLives(), 670, 475);
        font.draw(batch, "ESC / P: Pausa", 5, 455);
        font.draw(batch, "Nivel " + nivelMostrado + " - " + nombreMostrado, 5, 435);
        
        lluvia.renderSuelo(batch);
        tarro.draw(batch);
        lluvia.render(batch);
        if (oscuroActivo) {
            vision.dibujar(batch, tarro, lluvia.getGotas(), delta);
        }
        transicion.dibujarOverlay(batch, font);
        batch.end();
    }

    private void pausarJuego() {
        manager.pause();
        lluvia.stopMusic();
        musicaNivel.pause();
        game.setScreen(new PausaScreen(game, this));
    }

    private void guardarHighScore(int puntaje) {
        if (nivelInicial == 0 && modo.getHighScore(game) < puntaje) {
            modo.setHighScore(game, puntaje);
        }
    }

    private void terminarPartida() {
        int puntajeFinal = manager.getScore();
        guardarHighScore(puntajeFinal);
        lluvia.stopMusic();
        game.setScreen(new GameOverScreen(game, puntajeFinal, modo));
        dispose();
    }

    private void irAlFinal() {
        int puntajeFinal = manager.getScore();
        guardarHighScore(puntajeFinal);
        lluvia.stopMusic();
        game.setScreen(CinematicaScreen.intro(game, puntajeFinal));
        dispose();
    }

    @Override
    public void show() {
        manager.resume();
        lluvia.startMusic();
        if (!musicaNivel.isPlaying()) {
            musicaNivel.play();
        }
    }

    @Override
    public void pause() {
        if (manager.isPlaying()) {
            pausarJuego();
        }
    }

    @Override public void resize(int width, int height) { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override
    public void dispose() {
        if (liberado) return;
        liberado = true;
        vision.dispose();
        tarro.dispose();
        lluvia.dispose();
        transicion.dispose();
        if (fondo != null) fondo.dispose();
        musicaNivel.dispose();
    }
}