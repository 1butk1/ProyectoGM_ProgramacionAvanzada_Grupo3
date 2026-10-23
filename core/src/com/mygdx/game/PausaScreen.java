package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class PausaScreen implements Screen {

	private final GameLluviaMenu game;
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
    }
 
    private void crearBotonControles() {
        this.botonControles = new Boton("Control: " + juego.getNombreControl() + " (C)", 300, 160, 200, 50);
    }
 
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.3f, 1);
        camera.update();
 
        puntero.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(puntero);
        boolean sobreContinuar = botonContinuar.contiene(puntero.x, puntero.y);
        boolean sobreReiniciar = botonReiniciar.contiene(puntero.x, puntero.y);
        boolean sobreControles = botonControles.contiene(puntero.x, puntero.y);
        boolean sobreMenu = botonMenu.contiene(puntero.x, puntero.y);
 
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        botonContinuar.dibujarFondo(shapeRenderer, sobreContinuar);
        botonReiniciar.dibujarFondo(shapeRenderer, sobreReiniciar);
        botonControles.dibujarFondo(shapeRenderer, sobreControles);
        botonMenu.dibujarFondo(shapeRenderer, sobreMenu);
        shapeRenderer.end();
 
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        Boton.dibujarTextoCentrado(batch, font, "Juego en pausa", 3f, 400, camera.viewportWidth);
        botonContinuar.dibujarTexto(batch, font, sobreContinuar);
        botonReiniciar.dibujarTexto(batch, font, sobreReiniciar);
        botonControles.dibujarTexto(batch, font, sobreControles);
        botonMenu.dibujarTexto(batch, font, sobreMenu);
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
    }
}