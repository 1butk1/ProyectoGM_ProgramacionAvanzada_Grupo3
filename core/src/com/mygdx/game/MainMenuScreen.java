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

public class MainMenuScreen implements Screen {

	private final GameLluviaMenu game;
	private final SpriteBatch batch;
	private final BitmapFont font;
	private final OrthographicCamera camera;
	private final ShapeRenderer shapeRenderer;
	private final Vector3 puntero;
	private final Boton botonJugar;
	private final Boton botonSalir;

	public MainMenuScreen(final GameLluviaMenu game) {
		this.game = game;
		this.batch = game.getBatch();
		this.font = game.getFont();
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, 800, 480);
		this.shapeRenderer = new ShapeRenderer();
		this.puntero = new Vector3();
		this.botonJugar = new Boton("Jugar (ENTER)", 300, 220, 200, 50);
		this.botonSalir = new Boton("Salir (ESC)", 300, 150, 200, 50);
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);
		camera.update();

		puntero.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(puntero);
		boolean sobreJugar = botonJugar.contiene(puntero.x, puntero.y);
		boolean sobreSalir = botonSalir.contiene(puntero.x, puntero.y);

		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		botonJugar.dibujarFondo(shapeRenderer, sobreJugar);
		botonSalir.dibujarFondo(shapeRenderer, sobreSalir);
		shapeRenderer.end();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		Boton.dibujarTextoCentrado(batch, font, "Recolecta Gotas", 3f, 410, camera.viewportWidth);
		Boton.dibujarTextoCentrado(batch, font, "Mejor puntaje: " + game.getHigherScore(), 1.5f, 330, camera.viewportWidth);
		botonJugar.dibujarTexto(batch, font, sobreJugar);
		botonSalir.dibujarTexto(batch, font, sobreSalir);
		batch.end();

		boolean click = Gdx.input.justTouched();

		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || (click && sobreJugar)) {
			game.setScreen(new GameScreen(game));
			return;
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || (click && sobreSalir)) {
			Gdx.app.exit();
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
