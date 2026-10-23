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

public class GameOverScreen implements Screen {

	private final GameLluviaMenu game;
	private final int puntajeFinal;
	private final SpriteBatch batch;
	private final BitmapFont font;
	private final OrthographicCamera camera;
	private final ShapeRenderer shapeRenderer;
	private final Vector3 puntero;
	private final Boton botonReiniciar;
	private final Boton botonMenu;

	public GameOverScreen(final GameLluviaMenu game, int puntajeFinal) {
		this.game = game;
		this.puntajeFinal = puntajeFinal;
		this.batch = game.getBatch();
		this.font = game.getFont();
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, 800, 480);
		this.shapeRenderer = new ShapeRenderer();
		this.puntero = new Vector3();
		this.botonReiniciar = new Boton("Reiniciar (R)", 300, 190, 200, 50);
		this.botonMenu = new Boton("Menu principal (M)", 300, 120, 200, 50);
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);
		camera.update();

		puntero.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(puntero);
		boolean sobreReiniciar = botonReiniciar.contiene(puntero.x, puntero.y);
		boolean sobreMenu = botonMenu.contiene(puntero.x, puntero.y);

		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		botonReiniciar.dibujarFondo(shapeRenderer, sobreReiniciar);
		botonMenu.dibujarFondo(shapeRenderer, sobreMenu);
		shapeRenderer.end();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		Boton.dibujarTextoCentrado(batch, font, "GAME OVER", 3f, 400, camera.viewportWidth);
		Boton.dibujarTextoCentrado(batch, font, "Puntaje final: " + puntajeFinal, 1.5f, 320, camera.viewportWidth);
		Boton.dibujarTextoCentrado(batch, font, "Mejor puntaje: " + game.getHigherScore(), 1.5f, 285, camera.viewportWidth);
		botonReiniciar.dibujarTexto(batch, font, sobreReiniciar);
		botonMenu.dibujarTexto(batch, font, sobreMenu);
		batch.end();

		boolean click = Gdx.input.justTouched();

		if (Gdx.input.isKeyJustPressed(Input.Keys.R) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || (click && sobreReiniciar)) {
			game.setScreen(new GameScreen(game));
			return;
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.M) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || (click && sobreMenu)) {
			game.setScreen(new MainMenuScreen(game));
		}
	}

	@Override
	public void show() {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
	}
}

