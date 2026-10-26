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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.GameLluviaMenu;
import com.mygdx.game.ui.Boton;

public class FinalScreen implements Screen {

    private static final float APARECE_BOTON = 4.5f; 

    private final GameLluviaMenu game;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final OrthographicCamera camera;
    private final ShapeRenderer shapeRenderer;
    private final Vector3 puntero = new Vector3();
    private final Boton botonMenu;
    private Texture fondo;
    private float t = 0f;
    private Music musica;

    public FinalScreen(final GameLluviaMenu game, int puntaje) {
        this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
        this.camera = new OrthographicCamera();  
        this.camera.setToOrtho(false, 800, 480);
        this.shapeRenderer = new ShapeRenderer();
        this.botonMenu = new Boton("Volver al menu (M)", 540, 120, 200, 50);
    
        if (Gdx.files.internal("musicafinal.mp3").exists()) {
            musica = Gdx.audio.newMusic(Gdx.files.internal("musicafinal.mp3"));
            musica.setLooping(true);
            musica.setVolume(0.6f);
        }
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
        batch.setProjectionMatrix(camera.combined);
        
        if (fondo != null) {
            batch.begin();
            batch.setColor(Color.WHITE);
            batch.draw(fondo, 0, 0, 800, 480);
            batch.end();
        }

        if (botonVisible) {
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            botonMenu.dibujarFondo(shapeRenderer, sobreMenu);
            shapeRenderer.end();
        }

        batch.begin();
        if (botonVisible) {
            font.setColor(Color.WHITE);
            botonMenu.dibujarTexto(batch, font, sobreMenu);
        }
        font.setColor(Color.WHITE);
        batch.end();
        if (!botonVisible) return;
        boolean click = Gdx.input.justTouched();
        if (Gdx.input.isKeyJustPressed(Input.Keys.M) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
                || Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || (click && sobreMenu)) {
        	game.setScreen(CinematicaScreen.epilogo(game));
        }
    }

    @Override public void show() {  if (musica != null) musica.play();} 
    @Override public void resize(int width, int height) { }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { dispose(); }

    @Override
    public void dispose() {
    	shapeRenderer.dispose();
        if (fondo != null) { fondo.dispose(); fondo = null; }
        if (musica != null) { musica.stop(); musica.dispose(); musica = null;
        }
    }
}