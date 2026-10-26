package com.mygdx.game.pantallas;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.GameLluviaMenu;
import com.mygdx.game.ui.Boton;
 
public class PausaScreen implements Screen {
 
    private static final String RUTA_FONDO = "fondos/pausa.png";
 
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
    private Texture fondo;
 
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

        if (Gdx.files.internal(RUTA_FONDO).exists()) {
            fondo = new Texture(Gdx.files.internal(RUTA_FONDO));
        }
    }
 
    private void crearBotonControles() {
        this.botonControles = new Boton("Control: " + juego.getNombreControl() + " (C)", 300, 160, 200, 50);
    }
 
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.08f, 0.06f, 0.12f, 1);
        camera.update();
 
        puntero.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(puntero);
        boolean sobreContinuar = botonContinuar.contiene(puntero.x, puntero.y);
        boolean sobreReiniciar = botonReiniciar.contiene(puntero.x, puntero.y);
        boolean sobreControles = botonControles.contiene(puntero.x, puntero.y);
        boolean sobreMenu = botonMenu.contiene(puntero.x, puntero.y);
 
        // 1) Fondo
        batch.setProjectionMatrix(camera.combined);
        if (fondo != null) {
            batch.begin();
            batch.draw(fondo, 0, 0, 800, 480);
            batch.end();
        }
 
        // 2) Botones
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        botonContinuar.dibujarFondo(shapeRenderer, sobreContinuar);
        botonReiniciar.dibujarFondo(shapeRenderer, sobreReiniciar);
        botonControles.dibujarFondo(shapeRenderer, sobreControles);
        botonMenu.dibujarFondo(shapeRenderer, sobreMenu);
        shapeRenderer.end();
 
        // 3) Textos
        batch.begin();
        if (fondo == null) {
            font.setColor(0f, 0f, 0f, 0.85f);
            Boton.dibujarTextoCentrado(batch, font, "PAUSA", 3.5f, 427, camera.viewportWidth + 6);
            font.setColor(0.86f, 0.84f, 0.76f, 1f);
            Boton.dibujarTextoCentrado(batch, font, "PAUSA", 3.5f, 430, camera.viewportWidth);
        }
 
        font.setColor(0.58f, 0.64f, 0.60f, 1f);
        Boton.dibujarTextoCentrado(batch, font, "Descansa en el refugio", 1.2f, 375, camera.viewportWidth);
        font.setColor(Color.WHITE);
 
        botonContinuar.dibujarTexto(batch, font, sobreContinuar);
        botonReiniciar.dibujarTexto(batch, font, sobreReiniciar);
        botonControles.dibujarTexto(batch, font, sobreControles);
        botonMenu.dibujarTexto(batch, font, sobreMenu);
        font.setColor(Color.WHITE);
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
            game.setScreen(new GameScreen(game, juego.getModo()));
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
        if (fondo != null) {
            fondo.dispose();
            fondo = null;
        }
    }
}
 