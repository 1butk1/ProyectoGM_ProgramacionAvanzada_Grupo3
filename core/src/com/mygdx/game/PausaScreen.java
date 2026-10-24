package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
<<<<<<< HEAD
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
=======
import com.badlogic.gdx.graphics.OrthographicCamera;
>>>>>>> origin/main
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class PausaScreen implements Screen {

<<<<<<< HEAD
	private static final String RUTA_FONDO = "fondos/pausa.png";
	 
    private final GameLluviaMenu game;
=======
	private final GameLluviaMenu game;
>>>>>>> origin/main
    private final GameScreen juego;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final OrthographicCamera camera;
    private final ShapeRenderer shapeRenderer;
    private final Vector3 puntero;
    private final Boton botonContinuar;
    private final Boton botonReiniciar;
    private final Boton botonMenu;
    private Boton botonControles;
<<<<<<< HEAD
    private Texture fondo;
=======
>>>>>>> origin/main
 
    public PausaScreen(final GameLluviaMenu game, GameScreen juego) {
        this.game = game;
        this.juego = juego;
        this.batch = game.getBatch();
        this.font = game.getFont();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 800, 480);
        this.shapeRenderer = new ShapeRenderer();
        this.puntero = new Vector3();
        this.botonContinuar = new Boton("Continuar (ESC)", 300, 290, 200, 50);
        this.botonReiniciar = new Boton("Reiniciar (R)", 300, 225, 200, 50);
        this.botonMenu = new Boton("Menu principal (M)", 300, 95, 200, 50);
        crearBotonControles();
<<<<<<< HEAD
 
        // Si el archivo no existe, la pausa funciona igual con fondo liso
        if (Gdx.files.internal(RUTA_FONDO).exists()) {
            fondo = new Texture(Gdx.files.internal(RUTA_FONDO));
        }
=======
>>>>>>> origin/main
    }
 
    private void crearBotonControles() {
        this.botonControles = new Boton("Control: " + juego.getNombreControl() + " (C)", 300, 160, 200, 50);
    }
 
    @Override
    public void render(float delta) {
<<<<<<< HEAD
        ScreenUtils.clear(0.08f, 0.06f, 0.12f, 1);
=======
        ScreenUtils.clear(0, 0, 0.3f, 1);
>>>>>>> origin/main
        camera.update();
 
        puntero.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(puntero);
        boolean sobreContinuar = botonContinuar.contiene(puntero.x, puntero.y);
        boolean sobreReiniciar = botonReiniciar.contiene(puntero.x, puntero.y);
        boolean sobreControles = botonControles.contiene(puntero.x, puntero.y);
        boolean sobreMenu = botonMenu.contiene(puntero.x, puntero.y);
 
<<<<<<< HEAD
        // 1) Fondo
        batch.setProjectionMatrix(camera.combined);
        if (fondo != null) {
            batch.begin();
            batch.draw(fondo, 0, 0, 800, 480);
            batch.end();
        }
 
        // 2) Botones
=======
>>>>>>> origin/main
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        botonContinuar.dibujarFondo(shapeRenderer, sobreContinuar);
        botonReiniciar.dibujarFondo(shapeRenderer, sobreReiniciar);
        botonControles.dibujarFondo(shapeRenderer, sobreControles);
        botonMenu.dibujarFondo(shapeRenderer, sobreMenu);
        shapeRenderer.end();
 
<<<<<<< HEAD
        // 3) Textos
        batch.begin();
        font.setColor(0f, 0f, 0f, 0.85f);
        Boton.dibujarTextoCentrado(batch, font, "", 3.5f, 427, camera.viewportWidth + 6);
        font.setColor(1f, 0.6f, 0.2f, 1f);
        Boton.dibujarTextoCentrado(batch, font, "", 3.5f, 430, camera.viewportWidth);
 
        font.setColor(0.9f, 0.85f, 0.75f, 1f);
        Boton.dibujarTextoCentrado(batch, font, "   ", 1.2f, 375, camera.viewportWidth);
        font.setColor(Color.WHITE);
 
=======
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        Boton.dibujarTextoCentrado(batch, font, "Juego en pausa", 3f, 400, camera.viewportWidth);
>>>>>>> origin/main
        botonContinuar.dibujarTexto(batch, font, sobreContinuar);
        botonReiniciar.dibujarTexto(batch, font, sobreReiniciar);
        botonControles.dibujarTexto(batch, font, sobreControles);
        botonMenu.dibujarTexto(batch, font, sobreMenu);
<<<<<<< HEAD
        font.setColor(Color.WHITE);
=======
>>>>>>> origin/main
        batch.end();
 
        boolean click = Gdx.input.justTouched();
 
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.P)
                || (click && sobreContinuar)) {
            game.setScreen(juego);
            return;
        }
 
        if (Gdx.input.isKeyJustPressed(Input.Keys.C) || (click && sobreControles)) {
            juego.alternarControles();
            crearBotonControles();
            return;
        }
 
        if (Gdx.input.isKeyJustPressed(Input.Keys.R) || (click && sobreReiniciar)) {
            juego.dispose();
            game.setScreen(new GameScreen(game));
            return;
        }
 
        if (Gdx.input.isKeyJustPressed(Input.Keys.M) || (click && sobreMenu)) {
            juego.dispose();
            game.setScreen(new MainMenuScreen(game));
        }
    }
 
    @Override public void show() { }
    @Override public void resize(int width, int height) { }
    @Override public void pause() { }
    @Override public void resume() { }
 
    @Override
    public void hide() {
        dispose();
    }
 
    @Override
    public void dispose() {
        shapeRenderer.dispose();
<<<<<<< HEAD
        if (fondo != null) {
            fondo.dispose();
            fondo = null;
        }
    }
}
 
=======
    }
}
>>>>>>> origin/main
