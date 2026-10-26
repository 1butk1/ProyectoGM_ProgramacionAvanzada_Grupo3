package com.mygdx.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.GameLluviaMenu;
import com.mygdx.game.builder.LevelConfig;
import com.mygdx.game.builder.LevelManager;
import com.mygdx.game.logica.Progreso;
import com.mygdx.game.modos.ModoJuego;

/**
  Selector de niveles del modo historia.
  Controles: flechas o WASD para moverse, ENTER / ESPACIO para jugar,
  ESC para volver, y tambien funciona con el mouse (pasar por encima y clic).
  Los niveles aun no alcanzados salen bloqueados (se desbloquean al llegar a ellos).
 **/

public class SelectorNivelesScreen implements Screen {

    // Poner en true para tener todos los niveles abiertos desde el inicio. 
    private static final boolean TODOS_ABIERTOS = false;
    private static final int COLUMNAS = 3;
    private static final float TARJETA_ANCHO = 220f;
    private static final float TARJETA_ALTO = 150f;
    private static final float SEPARACION = 30f;
    private static final float MINIATURA_ANCHO = 200f;
    private static final float MINIATURA_ALTO = 100f;
    private static final float BORDE = 4f;
    private static final float TOPE_CUADRICULA = 395f;
    private static final float DURACION_AVISO = 2.2f;
    private final GameLluviaMenu game;
    private final ModoJuego modo;
    private final Runnable alVolver;     
    private boolean volviendo = false;   
    private final OrthographicCamera camara;
    private final SpriteBatch batch;
    private final BitmapFont fuente;
    private final Vector3 puntero = new Vector3();
    private final int cantidad;
    private final String[] nombres;
    private final Texture[] miniaturas;
    private final Texture pixel;

    private int seleccionado = 0;
    private int nivelMaximo = 0;
    private String aviso = null;
    private float tiempoAviso = 0f;
    private boolean liberado = false;

    public SelectorNivelesScreen(final GameLluviaMenu game, ModoJuego modo, final Screen pantallaAnterior) {
        this(game, modo, new Runnable() {
            @Override
            public void run() {
                game.setScreen(pantallaAnterior);
            }
        });
    }

    public SelectorNivelesScreen(GameLluviaMenu game, ModoJuego modo, Runnable alVolver) {
        this.game = game;
        this.modo = modo;
        this.alVolver = alVolver;
        this.batch = game.getBatch();
        this.fuente = game.getFont();

        camara = new OrthographicCamera();
        camara.setToOrtho(false, 800, 480);

        LevelManager niveles = new LevelManager(modo);
        cantidad = niveles.getCantidadNiveles();
        nombres = new String[cantidad];
        miniaturas = new Texture[cantidad];
        for (int i = 0; i < cantidad; i++) {
            LevelConfig config = niveles.getConfigNivel(i);
            nombres[i] = config.getName();
            String ruta = config.getBackgroundPath();
            if (ruta != null && Gdx.files.internal(ruta).exists()) {
                miniaturas[i] = new Texture(Gdx.files.internal(ruta));
            }
        }

        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(1f, 1f, 1f, 1f);
        pm.fill();
        pixel = new Texture(pm);
        pm.dispose();

        refrescarProgreso();
        seleccionado = Math.min(nivelMaximo, cantidad - 1);
    }

    private void refrescarProgreso() {
        nivelMaximo = TODOS_ABIERTOS ? cantidad - 1 : Progreso.getNivelMaximo();
    }

    private boolean estaDesbloqueado(int indice) {
        return indice <= nivelMaximo;
    }

    private float tarjetaX(int i) {
        float ancho = COLUMNAS * TARJETA_ANCHO + (COLUMNAS - 1) * SEPARACION;
        float inicio = (800f - ancho) / 2f;
        return inicio + (i % COLUMNAS) * (TARJETA_ANCHO + SEPARACION);
    }

    private float tarjetaY(int i) {
        float arriba = TOPE_CUADRICULA - (i / COLUMNAS) * (TARJETA_ALTO + SEPARACION);
        return arriba - TARJETA_ALTO;
    }

    private int tarjetaBajoPuntero(float x, float y) {
        for (int i = 0; i < cantidad; i++) {
            float tx = tarjetaX(i);
            float ty = tarjetaY(i);
            if (x >= tx && x <= tx + TARJETA_ANCHO && y >= ty && y <= ty + TARJETA_ALTO) {
                return i;
            }
        }
        return -1;
    }

    // ---------- logica ----------

    private void mostrarAviso(String texto) {
        aviso = texto;
        tiempoAviso = DURACION_AVISO;
    }

    private void jugar(int indice) {
        if (!estaDesbloqueado(indice)) {
            mostrarAviso("Nivel bloqueado: llega hasta " + nombres[indice - 1] + " en la historia para abrirlo.");
            return;
        }
        game.setScreen(new GameScreen(game, modo, indice));
        dispose();
    }

    private void volver() {
        alVolver.run();
        dispose();
    }

    private void leerEntrada(float delta) {
        if (tiempoAviso > 0f) {
            tiempoAviso -= delta;
            if (tiempoAviso <= 0f) aviso = null;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            volviendo = true;
        }
        if (volviendo) {
            if (!Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
                volver();
            }
            return;
        }

        boolean izq = Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.A);
        boolean der = Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.D);
        boolean arriba = Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W);
        boolean abajo = Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S);

        if (izq) seleccionado = (seleccionado - 1 + cantidad) % cantidad;
        if (der) seleccionado = (seleccionado + 1) % cantidad;
        if (arriba && seleccionado - COLUMNAS >= 0) seleccionado -= COLUMNAS;
        if (abajo && seleccionado + COLUMNAS < cantidad) seleccionado += COLUMNAS;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            jugar(seleccionado);
            return;
        }

        puntero.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camara.unproject(puntero);
        int bajo = tarjetaBajoPuntero(puntero.x, puntero.y);
        if (bajo >= 0 && (Gdx.input.getDeltaX() != 0 || Gdx.input.getDeltaY() != 0)) {
            seleccionado = bajo;
        }
        if (Gdx.input.justTouched() && bajo >= 0) {
            seleccionado = bajo;
            jugar(bajo);
        }
    }

    // ---------- dibujo ----------

    private void rectangulo(float x, float y, float w, float h, float r, float g, float b, float a) {
        batch.setColor(r, g, b, a);
        batch.draw(pixel, x, y, w, h);
        batch.setColor(1f, 1f, 1f, 1f);
    }

    private void texto(String t, float x, float y, float ancho, float escala,
                       float r, float g, float b) {
        float escalaX = fuente.getData().scaleX;
        float escalaY = fuente.getData().scaleY;
        fuente.getData().setScale(escala);
        fuente.setColor(r, g, b, 1f);
        fuente.draw(batch, t, x, y, ancho, Align.center, false);
        fuente.setColor(1f, 1f, 1f, 1f);
        fuente.getData().setScale(escalaX, escalaY);
    }

    private void dibujarTarjeta(int i) {
        float x = tarjetaX(i);
        float y = tarjetaY(i);
        boolean abierto = estaDesbloqueado(i);
        boolean activo = (i == seleccionado);

        // Borde: el seleccionado es amarillo y mas grueso (no depende solo del color)
        float grosor = activo ? BORDE + 2f : BORDE - 2f;
        if (activo) {
            rectangulo(x - grosor, y - grosor, TARJETA_ANCHO + 2 * grosor, TARJETA_ALTO + 2 * grosor,
                    1f, 0.85f, 0.25f, 1f);
        } else {
            rectangulo(x - grosor, y - grosor, TARJETA_ANCHO + 2 * grosor, TARJETA_ALTO + 2 * grosor,
                    0.35f, 0.38f, 0.40f, 1f);
        }
        rectangulo(x, y, TARJETA_ANCHO, TARJETA_ALTO, 0.08f, 0.10f, 0.12f, 1f);

        // Miniatura (oscurecida si esta bloqueado)
        float mx = x + (TARJETA_ANCHO - MINIATURA_ANCHO) / 2f;
        float my = y + TARJETA_ALTO - MINIATURA_ALTO - 8f;
        if (miniaturas[i] != null) {
            if (abierto) batch.setColor(1f, 1f, 1f, 1f);
            else batch.setColor(0.18f, 0.18f, 0.2f, 1f);
            batch.draw(miniaturas[i], mx, my, MINIATURA_ANCHO, MINIATURA_ALTO);
            batch.setColor(1f, 1f, 1f, 1f);
        } else {
            rectangulo(mx, my, MINIATURA_ANCHO, MINIATURA_ALTO, 0.2f, 0.25f, 0.3f, 1f);
        }
        if (!abierto) {
            texto("BLOQUEADO", mx, my + MINIATURA_ALTO / 2f + 8f, MINIATURA_ANCHO, 1.2f, 0.85f, 0.85f, 0.85f);
        }

        String etiqueta = (activo ? "> " : "") + (i + 1) + ". " + (abierto ? nombres[i] : "???");
        if (abierto) {
            texto(etiqueta, x, y + 24f, TARJETA_ANCHO, 1.2f, activo ? 1f : 0.9f, activo ? 0.9f : 0.9f, activo ? 0.4f : 0.9f);
        } else {
            texto(etiqueta, x, y + 24f, TARJETA_ANCHO, 1.2f, 0.6f, 0.6f, 0.6f);
        }
    }

    @Override
    public void render(float delta) {
        leerEntrada(delta);
        if (liberado) return;   // se cambio de pantalla en este frame

        ScreenUtils.clear(0.03f, 0.04f, 0.07f, 1f);
        camara.update();
        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        texto("SELECCIONA UN NIVEL", 0, 455f, 800f, 2f, 1f, 1f, 1f);
        for (int i = 0; i < cantidad; i++) {
            dibujarTarjeta(i);
        }
        texto("Flechas / WASD: mover     Enter / Espacio / Clic: jugar     Esc: volver",
                0, 36f, 800f, 1f, 0.8f, 0.85f, 0.9f);
        if (aviso != null) {
            texto(aviso, 0, 62f, 800f, 1.1f, 1f, 0.55f, 0.45f);
        }
        batch.end();
    }

    @Override
    public void show() {
        refrescarProgreso();
    }

    @Override public void resize(int width, int height) { }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override
    public void dispose() {
        if (liberado) return;
        liberado = true;
        pixel.dispose();
        for (Texture t : miniaturas) {
            if (t != null) t.dispose();
        }
    }
}