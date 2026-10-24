package com.mygdx.game;

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
import com.badlogic.gdx.audio.Sound;
 
public class GameScreen implements Screen {
 
	private static final int VIDAS_INICIALES = 3;
	private final Sound sonidoNivel;
    private final GameLluviaMenu game;
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
 
    // Lo que muestra el HUD: se actualiza al quedar la pantalla en negro, no antes
    private int nivelMostrado;
    private String nombreMostrado;
 
    public GameScreen(final GameLluviaMenu game) {
        this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
        this.manager = new GameManager(VIDAS_INICIALES);
        this.levelManager = new LevelManager();
        this.sonidoNivel = Gdx.audio.newSound(Gdx.files.internal("sonidonivel.mp3"));
 
        Sound hurtSound = Gdx.audio.newSound(Gdx.files.internal("hurt.ogg"));
        Texture texPrincipal = new Texture(Gdx.files.internal("bucket.png"));
        Texture texSinBalde = new Texture(Gdx.files.internal("personaje_sin_balde.png"));   // Reemplaza con tu archivo
        Texture texBaldeCaido = new Texture(Gdx.files.internal("balde_caido.png")); // Reemplaza con tu archivo
 
        tarro = new Tarro(texPrincipal, texSinBalde, texBaldeCaido, controlTeclado, hurtSound);
 
        Sound dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        Music rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        lluvia = new Lluvia(dropSound, rainMusic);
 
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
 
        aplicarNivel(); // imprescindible: sin config no cae ninguna gota
        nivelMostrado = levelManager.getCurrentLevel();
        nombreMostrado = levelManager.getCurrentConfig().getName();
    }
 
    /** Aplica la config del nivel actual a la lluvia y carga su fondo (si existe). */
    private void aplicarNivel() {
        LevelConfig config = levelManager.getCurrentConfig();
        lluvia.setLevelConfig(config);
 
        if (fondo != null) {
            fondo.dispose();
            fondo = null;
        }
        String ruta = config.getBackgroundPath();
        if (ruta != null && Gdx.files.internal(ruta).exists()) {
            fondo = new Texture(Gdx.files.internal(ruta));
        }
    }
 
    /**
     * Arranca la transicion: el juego se congela, la pantalla oscurece y SOLO cuando esta
     * en negro se cambian fondo, configuracion y gotas.
     */
    private void iniciarCambioDeNivel() {
        final LevelConfig nueva = levelManager.getCurrentConfig();
        final int numero = levelManager.getCurrentLevel();
        transicion.iniciar("Nivel " + numero, nueva.getName(), new Runnable() {
            @Override
            public void run() {
                aplicarNivel();
                lluvia.limpiarGotas();
                nivelMostrado = numero;
                nombreMostrado = nueva.getName();
            }
        });
    }
 
    // ---- usado desde PausaScreen ----
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
 
        if (manager.isPlaying() && !tarro.isHurt() && !transicion.bloqueaJuego()) {
            tarro.updateMovement(delta);
            lluvia.update(delta, tarro, manager);
 
            // No se evalua un nuevo nivel mientras otra transicion sigue en curso
            if (!transicion.estaActiva() && levelManager.updateForScore(manager.getScore())) {
                iniciarCambioDeNivel();
            }
        }
 
        // Game over solo cuando termina la animacion de herido
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
            transicion.dibujarFondo(batch, fondo); // aplica el zoom de la transicion (1.0 si no hay)
        }
        font.draw(batch, "Gotas totales: " + manager.getScore(), 5, 475);
        font.draw(batch, "HighScore : " + game.getHigherScore(), camera.viewportWidth / 2 - 50, 475);
        font.draw(batch, "Vidas : " + tarro.getLives(), 670, 475);
        font.draw(batch, "ESC / P: Pausa", 5, 455);
        font.draw(batch, "Nivel " + nivelMostrado + " - " + nombreMostrado, 5, 435);
        tarro.draw(batch);
        lluvia.render(batch);
        transicion.dibujarOverlay(batch, font); // oscurece, barras y titulo (siempre al final)
        batch.end();
    }
 
    private void pausarJuego() {
        manager.pause();
        lluvia.stopMusic();
        game.setScreen(new PausaScreen(game, this));
    }
 
    private void terminarPartida() {
        int puntajeFinal = manager.getScore();
        if (game.getHigherScore() < puntajeFinal) {
            game.setHigherScore(puntajeFinal);
        }
        lluvia.stopMusic();
        game.setScreen(new GameOverScreen(game, puntajeFinal));
        dispose();
    }
 
    @Override
    public void show() {
        manager.resume();
        lluvia.startMusic();
        sonidoNivel.play(0.7f);
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
        if (liberado) return; // evita liberar dos veces
        liberado = true;
        tarro.dispose();   // textura + sonido de herido
        lluvia.dispose();  // texturas de gotas, sonido y musica
        transicion.dispose();
        if (fondo != null) fondo.dispose();
        sonidoNivel.dispose();
    }
}
 