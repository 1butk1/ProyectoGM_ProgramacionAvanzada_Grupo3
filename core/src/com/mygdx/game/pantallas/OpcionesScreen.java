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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.GameLluviaMenu;
import com.mygdx.game.ui.Boton;


public class OpcionesScreen implements Screen {

	private static final String RUTA_FONDO = "fondos/menu.png";
	private static final float SLIDER_X = 250f;
	private static final float SLIDER_ANCHO = 300f;
	private static final float Y_MUSICA = 290f;
	private static final float Y_EFECTOS = 215f;
	private static final float ALTO_PISTA = 10f;
	private static final float ZONA_CLICK = 20f; 

	private final GameLluviaMenu game;
	private final SpriteBatch batch;
	private final BitmapFont font;
	private final OrthographicCamera camera;
	private final ShapeRenderer shapeRenderer;
	private final Vector3 puntero = new Vector3();
	private final Boton botonVolver;
	private final Sound sonidoPrueba;
	private Texture fondo;
	private int arrastrando = 0; 

	public OpcionesScreen(final GameLluviaMenu game) {
		this.game = game;
		this.batch = game.getBatch();
		this.font = game.getFont();
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, 800, 480);
		this.shapeRenderer = new ShapeRenderer();
		this.botonVolver = new Boton("Volver (ESC)", 300, 110, 200, 50);
		this.sonidoPrueba = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		if (Gdx.files.internal(RUTA_FONDO).exists()) {
			fondo = new Texture(Gdx.files.internal(RUTA_FONDO));
		}
	}

	private boolean sobreSlider(float y) {
		return puntero.x >= SLIDER_X - 10 && puntero.x <= SLIDER_X + SLIDER_ANCHO + 10
				&& puntero.y >= y - ZONA_CLICK && puntero.y <= y + ZONA_CLICK;
	}

	private float valorDesdeMouse() {
		return MathUtils.clamp((puntero.x - SLIDER_X) / SLIDER_ANCHO, 0f, 1f);
	}

	private void dibujarSlider(float y, float valor) {
		shapeRenderer.setColor(0.12f, 0.15f, 0.15f, 1f);
		shapeRenderer.rect(SLIDER_X, y - ALTO_PISTA / 2f, SLIDER_ANCHO, ALTO_PISTA);
		shapeRenderer.setColor(0.45f, 0.62f, 0.55f, 1f);
		shapeRenderer.rect(SLIDER_X, y - ALTO_PISTA / 2f, SLIDER_ANCHO * valor, ALTO_PISTA);
		shapeRenderer.setColor(0.86f, 0.84f, 0.76f, 1f);
		shapeRenderer.rect(SLIDER_X + SLIDER_ANCHO * valor - 6f, y - 14f, 12f, 28f);
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0.06f, 0.08f, 0.08f, 1);
		camera.update();

		puntero.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(puntero);
		boolean sobreVolver = botonVolver.contiene(puntero.x, puntero.y);

		if (Gdx.input.justTouched()) {
			if (sobreSlider(Y_MUSICA)) arrastrando = 1;
			else if (sobreSlider(Y_EFECTOS)) arrastrando = 2;
		}
		if (arrastrando != 0) {
			if (Gdx.input.isTouched()) {
				if (arrastrando == 1) game.setVolumenMusica(valorDesdeMouse());
				else game.setVolumenEfectos(valorDesdeMouse());
			} else {
				if (arrastrando == 2) sonidoPrueba.play(game.getVolumenEfectos());
				game.guardarVolumenes();
				arrastrando = 0;
			}
		}

		// 1) Fondo
		batch.setProjectionMatrix(camera.combined);
		if (fondo != null) {
			batch.begin();
			batch.draw(fondo, 0, 0, 800, 480);
			batch.end();
		}

		// 2) Sliders y boton
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		dibujarSlider(Y_MUSICA, game.getVolumenMusica());
		dibujarSlider(Y_EFECTOS, game.getVolumenEfectos());
		botonVolver.dibujarFondo(shapeRenderer, sobreVolver);
		shapeRenderer.end();

		// 3) Textos
		batch.begin();
		font.setColor(0f, 0f, 0f, 0.85f);
		Boton.dibujarTextoCentrado(batch, font, "Opciones", 3.0f, 407, camera.viewportWidth + 6);
		font.setColor(0.86f, 0.84f, 0.76f, 1f);
		Boton.dibujarTextoCentrado(batch, font, "Opciones", 3.0f, 410, camera.viewportWidth);

		font.setColor(0.72f, 0.76f, 0.72f, 1f);
		Boton.dibujarTextoCentrado(batch, font,
				"M\u00fasica: " + Math.round(game.getVolumenMusica() * 100) + "%",
				1.3f, Y_MUSICA + 40, camera.viewportWidth);
		Boton.dibujarTextoCentrado(batch, font,
				"Efectos: " + Math.round(game.getVolumenEfectos() * 100) + "%",
				1.3f, Y_EFECTOS + 40, camera.viewportWidth);

		botonVolver.dibujarTexto(batch, font, sobreVolver);
		font.setColor(Color.WHITE);
		batch.end();

		if (arrastrando == 0
				&& (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
				|| (Gdx.input.justTouched() && sobreVolver))) {
			game.guardarVolumenes();
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
		sonidoPrueba.dispose();
		if (fondo != null) {
			fondo.dispose();
			fondo = null;
		}
	}
}