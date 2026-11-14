package com.mygdx.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.GameLluviaMenu;
import com.mygdx.game.logica.GameSettings;
import com.mygdx.game.modos.ModoHistoria;
import com.mygdx.game.ui.Boton;
import com.mygdx.game.ui.Visual;

public class FinalScreen implements Screen {

    private static final float APARECE_BOTON = 4.5f;
    private static final float DURACION_FADE = 1.2f;
    private static final float INICIO_CONTEO = 0.8f;
    private static final float DURACION_CONTEO = 2.2f;
    private static final Color COLOR_POLVO = new Color(0.7f, 0.9f, 1f, 0.5f);
    private final GameLluviaMenu game;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final OrthographicCamera camera;
    private final ShapeRenderer shapeRenderer;
    private final Vector3 puntero = new Vector3();
    private final Boton botonMenu;
    private final Visual visual;
    private final int puntaje;
    private Texture fondo;
    private float t = 0f;
    private Music musica;

    public FinalScreen(final GameLluviaMenu game, int puntaje) {
        this.game = game;
        this.puntaje = puntaje;
        this.batch = game.getBatch();
        this.font = game.getFont();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 800, 480);
        this.shapeRenderer = new ShapeRenderer();
        this.visual = new Visual(font);
        this.botonMenu = new Boton("Volver al menu (M)", 300, 60, 200, 50);

        if (Gdx.files.internal("fondos/final.png").exists()) {
            fondo = new Texture(Gdx.files.internal("fondos/final.png"));
        }
        if (Gdx.files.internal("musicafinal.mp3").exists()) {
            musica = Gdx.audio.newMusic(Gdx.files.internal("musicafinal.mp3"));
            musica.setLooping(true);
            musica.setVolume(0.6f * GameSettings.getInstance().getVolumenMusicaEfectivo());
        }
    }

    private String rango() {
        if (puntaje >= ModoHistoria.META + 300) return "Guardi\u00e1n del agua";
        if (puntaje >= ModoHistoria.META + 100) return "Superviviente";
        return "Recolector de esperanza";
    }

    @Override
    public void render(float delta) {
        t += delta;
        ScreenUtils.clear(0.03f, 0.06f, 0.07f, 1);
        camera.update();
        boolean botonVisible = t >= APARECE_BOTON;
        puntero.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(puntero);
        boolean sobreMenu = botonVisible && botonMenu.contiene(puntero.x, puntero.y);

        float avance = MathUtils.clamp((t - INICIO_CONTEO) / DURACION_CONTEO, 0f, 1f);
        int mostrado = Math.round(puntaje * (1f - (1f - avance) * (1f - avance)));
        float alfaTexto = MathUtils.clamp((t - 0.4f) / 0.8f, 0f, 1f);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if (fondo != null) visual.fondo(batch, fondo);
        visual.velo(batch, 0.35f);
        visual.polvo(batch, delta, COLOR_POLVO);
        visual.vineta(batch);
        batch.end();

        if (botonVisible) {
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            botonMenu.dibujarFondo(shapeRenderer, sobreMenu);
            shapeRenderer.end();
        }

        batch.begin();
        visual.titulo(batch, "Has llegado al agua limpia", 440f, 2.0f,
                new Color(Visual.HUESO.r, Visual.HUESO.g, Visual.HUESO.b, alfaTexto));
        visual.texto(batch, "GOTAS RECOGIDAS", 330f, 1.1f,
                new Color(Visual.APAGADO.r, Visual.APAGADO.g, Visual.APAGADO.b, alfaTexto));
        visual.titulo(batch, String.valueOf(mostrado), 285f, 4.5f,
                new Color(Visual.SELECCION.r, Visual.SELECCION.g, Visual.SELECCION.b, alfaTexto));

        if (avance >= 1f) {
            float a = MathUtils.clamp((t - INICIO_CONTEO - DURACION_CONTEO) / 0.6f, 0f, 1f);
            visual.texto(batch, rango(), 190f, 1.6f,
                    new Color(Visual.HUESO.r, Visual.HUESO.g, Visual.HUESO.b, a));
        }

        if (botonVisible) {
            botonMenu.dibujarTexto(batch, font, sobreMenu);
        }
        font.setColor(Color.WHITE);

        float fade = 1f - MathUtils.clamp(t / DURACION_FADE, 0f, 1f);
        if (fade > 0f) visual.velo(batch, fade);
        batch.end();

        if (!botonVisible) return;
        boolean click = Gdx.input.justTouched();
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)
                || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
                || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
                || (click && sobreMenu)) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override public void show() { if (musica != null) musica.play(); }
    @Override public void resize(int width, int height) { }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { dispose(); }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        visual.dispose();
        if (fondo != null) { fondo.dispose(); fondo = null; }
        if (musica != null) { musica.stop(); musica.dispose(); musica = null; }
    }
}