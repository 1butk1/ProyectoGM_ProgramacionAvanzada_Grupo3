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
import com.mygdx.game.modos.ModoHistoria;
import com.mygdx.game.modos.ModoInfinito;
import com.mygdx.game.ui.Boton;

public class SeleccionModo implements Screen {

	private static final String RUTA_FONDO = "fondos/menu.png";
	private final GameLluviaMenu game;
	private final SpriteBatch batch;
	private final BitmapFont font;
	private final OrthographicCamera camera;
	private final ShapeRenderer shapeRenderer;
	private final Vector3 puntero;
	private final Boton botonHistoria;
	private final Boton botonInfinito;
	private final Boton botonVolver;
	private Texture fondo;

	public SeleccionModo(final GameLluviaMenu game) {
		this.game = game;
		this.batch = game.getBatch();
		this.font = game.getFont();
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, 800, 480);
		this.shapeRenderer = new ShapeRenderer();
		this.puntero = new Vector3();
		this.botonHistoria = new Boton("Modo historia (H)", 300, 230, 200, 50);
		this.botonInfinito = new Boton("Modo infinito (I)", 300, 165, 200, 50);
		this.botonVolver = new Boton("Volver (ESC)", 300, 100, 200, 50);

		if (Gdx.files.internal(RUTA_FONDO).exists()) {
			fondo = new Texture(Gdx.files.internal(RUTA_FONDO));
		}
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0.06f, 0.08f, 0.08f, 1);
		camera.update();

		puntero.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(puntero);
		boolean sobreHistoria = botonHistoria.contiene(puntero.x, puntero.y);
		boolean sobreInfinito = botonInfinito.contiene(puntero.x, puntero.y);
		boolean sobreVolver = botonVolver.contiene(puntero.x, puntero.y);

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
		botonHistoria.dibujarFondo(shapeRenderer, sobreHistoria);
		botonInfinito.dibujarFondo(shapeRenderer, sobreInfinito);
		botonVolver.dibujarFondo(shapeRenderer, sobreVolver);
		shapeRenderer.end();

		// 3) Textos
		batch.begin();

		font.setColor(0f, 0f, 0f, 0.85f);
		Boton.dibujarTextoCentrado(batch, font, "Salvation Rain", 3.5f, 407, camera.viewportWidth + 6);
		font.setColor(0.86f, 0.84f, 0.76f, 1f);
		Boton.dibujarTextoCentrado(batch, font, "Salvation Rain", 3.5f, 410, camera.viewportWidth);
		font.setColor(0.58f, 0.64f, 0.60f, 1f);
		Boton.dibujarTextoCentrado(batch, font, "Elige como jugar", 1.3f, 362, camera.viewportWidth);

		font.setColor(0.72f, 0.76f, 0.72f, 1f);
		if (sobreHistoria) {
			Boton.dibujarTextoCentrado(batch, font,
					"Tres zonas hasta encontrar el manantial. Meta: " + ModoHistoria.META + " gotas.",
					1.0f, 335, camera.viewportWidth);
			Boton.dibujarTextoCentrado(batch, font,
					"Mejor puntaje: " + game.getHigherScore(), 1.0f, 312, camera.viewportWidth);
		} else if (sobreInfinito) {
			Boton.dibujarTextoCentrado(batch, font,
					"Sin final: cada vuelta es m\u00e1s dif\u00edcil. Juega por el r\u00e9cord.",
					1.0f, 335, camera.viewportWidth);
			Boton.dibujarTextoCentrado(batch, font,
					"Mejor puntaje: " + game.getHigherScoreInfinito(), 1.0f, 312, camera.viewportWidth);
		}

		botonHistoria.dibujarTexto(batch, font, sobreHistoria);
		botonInfinito.dibujarTexto(batch, font, sobreInfinito);
		botonVolver.dibujarTexto(batch, font, sobreVolver);
		font.setColor(Color.WHITE);
		batch.end();

		boolean click = Gdx.input.justTouched();

		if (Gdx.input.isKeyJustPressed(Input.Keys.H) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
		        || (click && sobreHistoria)) {
		    game.setScreen(new SelectorNivelesScreen(game, new ModoHistoria(), new Runnable() {
		        @Override
		        public void run() {
		            game.setScreen(new SeleccionModo(game));
		        }
		    }));
		    return;
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.I) || (click && sobreInfinito)) {
			game.setScreen(new GameScreen(game, new ModoInfinito()));
			return;
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || (click && sobreVolver)) {
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