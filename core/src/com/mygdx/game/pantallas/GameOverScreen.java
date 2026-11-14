package com.mygdx.game.pantallas;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.GameLluviaMenu;
import com.mygdx.game.modos.ModoJuego;
import com.mygdx.game.ui.Boton;

public class GameOverScreen implements Screen {

	private static final String RUTA_FONDO = "fondos/gameover.png";
	private static final int OPCION_REINICIAR = 0;
	private static final int OPCION_MENU = 1;

	private final Sound sonidoDerrota;
	private final GameLluviaMenu game;
	private final int puntajeFinal;
	private final SpriteBatch batch;
	private final BitmapFont font;
	private final OrthographicCamera camera;
	private final ShapeRenderer shapeRenderer;
	private final Vector3 puntero;
	private final Boton botonReiniciar;
	private final Boton botonMenu;
	private Texture fondo;
	private final ModoJuego modo;
	private int seleccion = OPCION_REINICIAR;

	public GameOverScreen(final GameLluviaMenu game, int puntajeFinal, ModoJuego modo) {
		this.modo = modo;
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
		this.sonidoDerrota = Gdx.audio.newSound(Gdx.files.internal("gameover.mp3"));

		if (Gdx.files.internal(RUTA_FONDO).exists()) {
			fondo = new Texture(Gdx.files.internal(RUTA_FONDO));
		}
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0.05f, 0.07f, 0.07f, 1);
		camera.update();

		puntero.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(puntero);
		boolean sobreReiniciar = botonReiniciar.contiene(puntero.x, puntero.y);
		boolean sobreMenu = botonMenu.contiene(puntero.x, puntero.y);
		actualizarSeleccion(sobreReiniciar, sobreMenu);

		batch.setProjectionMatrix(camera.combined);
		if (fondo != null) {
			batch.begin();
			batch.draw(fondo, 0, 0, 800, 480);
			batch.end();
		}

		boolean resaltaReiniciar = seleccion == OPCION_REINICIAR;
		boolean resaltaMenu = seleccion == OPCION_MENU;
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		botonReiniciar.dibujarFondo(shapeRenderer, resaltaReiniciar);
		botonMenu.dibujarFondo(shapeRenderer, resaltaMenu);
		shapeRenderer.end();

		batch.begin();
		if (fondo == null) {
			font.setColor(0.62f, 0.14f, 0.12f, 1f);
			Boton.dibujarTextoCentrado(batch, font, "GAME OVER", 3.5f, 400, camera.viewportWidth);
		}
		font.setColor(0f, 0f, 0f, 0.8f);
		Boton.dibujarTextoCentrado(batch, font, "Puntaje final: " + puntajeFinal, 1.5f, 317, camera.viewportWidth + 4);
		font.setColor(0.86f, 0.84f, 0.76f, 1f);
		Boton.dibujarTextoCentrado(batch, font, "Puntaje final: " + puntajeFinal, 1.5f, 320, camera.viewportWidth);
		font.setColor(0.62f, 0.68f, 0.64f, 1f);
		Boton.dibujarTextoCentrado(batch, font, "Mejor puntaje: " + modo.getHighScore(game), 1.5f, 285, camera.viewportWidth);
		font.setColor(Color.WHITE);
		botonReiniciar.dibujarTexto(batch, font, resaltaReiniciar);
		botonMenu.dibujarTexto(batch, font, resaltaMenu);
		font.setColor(0.62f, 0.68f, 0.64f, 0.9f);
		Boton.dibujarTextoCentrado(batch, font, "Flechas / W-S: elegir    Espacio: aceptar", 1f, 70, camera.viewportWidth);
		font.setColor(Color.WHITE);
		batch.end();

		manejarAcciones(sobreReiniciar, sobreMenu);
	}

	private void actualizarSeleccion(boolean sobreReiniciar, boolean sobreMenu) {
		boolean mouseMovio = Gdx.input.getDeltaX() != 0 || Gdx.input.getDeltaY() != 0;
		if (mouseMovio) {
			if (sobreReiniciar) seleccion = OPCION_REINICIAR;
			else if (sobreMenu) seleccion = OPCION_MENU;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)) {
			seleccion = Math.max(OPCION_REINICIAR, seleccion - 1);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
			seleccion = Math.min(OPCION_MENU, seleccion + 1);
		}
	}
	private void manejarAcciones(boolean sobreReiniciar, boolean sobreMenu) {
		boolean click = Gdx.input.justTouched();
		boolean aceptar = Gdx.input.isKeyJustPressed(Input.Keys.SPACE)
				|| Gdx.input.isKeyJustPressed(Input.Keys.ENTER);

		boolean reiniciar = Gdx.input.isKeyJustPressed(Input.Keys.R)
				|| (aceptar && seleccion == OPCION_REINICIAR)
				|| (click && sobreReiniciar);
		boolean menu = Gdx.input.isKeyJustPressed(Input.Keys.M)
				|| Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
				|| (aceptar && seleccion == OPCION_MENU)
				|| (click && sobreMenu);
		if (reiniciar) {
			game.setScreen(new GameScreen(game, modo));
		} else if (menu) {
			game.setScreen(new MainMenuScreen(game));
		}
	}

	@Override
	public void show() { sonidoDerrota.play(0.8f); }
	@Override
	public void resize(int width, int height) {}
	@Override
	public void pause() {}
	@Override
	public void resume() {}
	@Override
	public void hide() { dispose(); }
	@Override
	public void dispose() {
		shapeRenderer.dispose();
		if (fondo != null) {
			fondo.dispose();
			fondo = null;
		}
		sonidoDerrota.dispose();
	}
}