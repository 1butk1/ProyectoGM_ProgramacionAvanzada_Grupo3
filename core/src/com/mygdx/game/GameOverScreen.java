package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.audio.Sound;
 
/**
 * Pantalla de derrota: el cuarto del menu con el fuego apagado y la ventana rota.
 * El titulo "GAME OVER" ya viene dibujado en fondo_gameover.png; aqui van puntaje y opciones.
 */
public class GameOverScreen implements Screen {
 
	private static final String[] OPCIONES = { "Reintentar", "Menu principal" };
	private static final Color POLVO_FRIO = new Color(0.65f, 0.78f, 1f, 0.55f);
	// El titulo del PNG esta corrido a la derecha para no chocar con la ventana (116 * 4 px).
	private static final float CENTRO_X = 464f;
	private final Sound sonidoDerrota;
	private final GameLluviaMenu game;
	private final int puntaje;
	private final OrthographicCamera camera;
	private final SpriteBatch batch;
	private final Texture fondo;
	private final Visual estilo;
	private int seleccion = 0;
 
	public GameOverScreen(final GameLluviaMenu game, int puntaje) {
		this.game = game;
		this.puntaje = puntaje;
		this.batch = game.getBatch();
		this.fondo = new Texture(Gdx.files.internal("fondos/gameover.png"));
		this.estilo = new Visual(game.getFont());
		this.estilo.setCentroX(CENTRO_X);
		this.sonidoDerrota = Gdx.audio.newSound(Gdx.files.internal("gameover.mp3"));
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Visual.ANCHO, Visual.ALTO);
	}
 
	@Override
	public void render(float delta) {
		manejarEntrada();
 
		ScreenUtils.clear(0, 0, 0, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
 
		batch.begin();
		estilo.fondo(batch, fondo);
		estilo.velo(batch, 0.12f);
		estilo.polvo(batch, delta, POLVO_FRIO);
 
		estilo.texto(batch, "Recolectaste " + puntaje + " gotas antes de caer", 330, 1.2f, Visual.HUESO);
		estilo.texto(batch, "Mejor marca: " + game.getHigherScore(), 300, 1.0f, Visual.MUSGO);
 
		for (int i = 0; i < OPCIONES.length; i++) {
			estilo.opcion(batch, OPCIONES[i], 215 - i * 40, i == seleccion);
		}
		batch.end();
	}
 
	private void manejarEntrada() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)) {
			seleccion = (seleccion + OPCIONES.length - 1) % OPCIONES.length;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
			seleccion = (seleccion + 1) % OPCIONES.length;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			ejecutar(seleccion);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			ejecutar(1);
		}
	}
 
	private void ejecutar(int opcion) {
		if (opcion == 0) {
			game.setScreen(new GameScreen(game));
		} else {
			// Cambia MainMenuScreen por el nombre real de tu pantalla de menu.
			game.setScreen(new MainMenuScreen(game));
		}
		dispose();
	}
 
	@Override public void show() { sonidoDerrota.play(0.8f);}
	@Override public void resize(int width, int height) { }
	@Override public void pause() { }
	@Override public void resume() { }
	@Override public void hide() { }
 
	@Override
	public void dispose() {
		fondo.dispose();
		estilo.dispose();
		sonidoDerrota.dispose();
	}
}
 