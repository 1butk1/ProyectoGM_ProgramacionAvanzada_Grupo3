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
<<<<<<< HEAD
import com.badlogic.gdx.audio.Sound;

public class MainMenuScreen implements Screen {
 
	private static final String RUTA_FONDO = "fondos/menu.png";
	private final Sound sonidoMenu;
=======

public class MainMenuScreen implements Screen {

>>>>>>> origin/main
	private final GameLluviaMenu game;
	private final SpriteBatch batch;
	private final BitmapFont font;
	private final OrthographicCamera camera;
	private final ShapeRenderer shapeRenderer;
	private final Vector3 puntero;
	private final Boton botonJugar;
	private final Boton botonSalir;
<<<<<<< HEAD
	private Texture fondo;
 
=======

>>>>>>> origin/main
	public MainMenuScreen(final GameLluviaMenu game) {
		this.game = game;
		this.batch = game.getBatch();
		this.font = game.getFont();
		this.camera = new OrthographicCamera();
<<<<<<< HEAD
		this.sonidoMenu = Gdx.audio.newSound(Gdx.files.internal("sonidomenu.mp3"));
=======
>>>>>>> origin/main
		this.camera.setToOrtho(false, 800, 480);
		this.shapeRenderer = new ShapeRenderer();
		this.puntero = new Vector3();
		this.botonJugar = new Boton("Jugar (ENTER)", 300, 220, 200, 50);
		this.botonSalir = new Boton("Salir (ESC)", 300, 150, 200, 50);
<<<<<<< HEAD
 
		// Si el archivo no existe, el menu sigue funcionando con fondo liso
		if (Gdx.files.internal(RUTA_FONDO).exists()) {
			fondo = new Texture(Gdx.files.internal(RUTA_FONDO));
		}
	}
 
	@Override
	public void render(float delta) {
		ScreenUtils.clear(0.06f, 0.08f, 0.08f, 1);
		camera.update();
 
=======
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);
		camera.update();

>>>>>>> origin/main
		puntero.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(puntero);
		boolean sobreJugar = botonJugar.contiene(puntero.x, puntero.y);
		boolean sobreSalir = botonSalir.contiene(puntero.x, puntero.y);
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
		botonJugar.dibujarFondo(shapeRenderer, sobreJugar);
		botonSalir.dibujarFondo(shapeRenderer, sobreSalir);
		shapeRenderer.end();
<<<<<<< HEAD
 
		// 3) Textos
		batch.begin();
 
		// Titulo con sombra para que se lea sobre el fondo
		font.setColor(0f, 0f, 0f, 0.85f);
		Boton.dibujarTextoCentrado(batch, font, "Salvation Rain", 3.5f, 407, camera.viewportWidth + 6);
		font.setColor(0.86f, 0.84f, 0.76f, 1f);
		Boton.dibujarTextoCentrado(batch, font, "Salvation Rain", 3.5f, 410, camera.viewportWidth);
 
		font.setColor(0.58f, 0.64f, 0.60f, 1f);
		Boton.dibujarTextoCentrado(batch, font, "Little Hope...", 1.3f, 362, camera.viewportWidth);
 
		font.setColor(0.72f, 0.76f, 0.72f, 1f);
		Boton.dibujarTextoCentrado(batch, font, "Mejor puntaje: " + game.getHigherScore(), 1.5f, 320, camera.viewportWidth);
 
		botonJugar.dibujarTexto(batch, font, sobreJugar);
		botonSalir.dibujarTexto(batch, font, sobreSalir);
		font.setColor(Color.WHITE);
		batch.end();
 
		boolean click = Gdx.input.justTouched();
 
=======

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		Boton.dibujarTextoCentrado(batch, font, "Recolecta Gotas", 3f, 410, camera.viewportWidth);
		Boton.dibujarTextoCentrado(batch, font, "Mejor puntaje: " + game.getHigherScore(), 1.5f, 330, camera.viewportWidth);
		botonJugar.dibujarTexto(batch, font, sobreJugar);
		botonSalir.dibujarTexto(batch, font, sobreSalir);
		batch.end();

		boolean click = Gdx.input.justTouched();

>>>>>>> origin/main
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || (click && sobreJugar)) {
			game.setScreen(new GameScreen(game));
			return;
		}
<<<<<<< HEAD
 
=======

>>>>>>> origin/main
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || (click && sobreSalir)) {
			Gdx.app.exit();
		}
	}
<<<<<<< HEAD
 
	@Override
	public void show() {sonidoMenu.play(0.8f);
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
 
=======

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

>>>>>>> origin/main
	@Override
	public void hide() {
		dispose();
	}
<<<<<<< HEAD
 
	@Override
	public void dispose() {
		shapeRenderer.dispose();
		if (fondo != null) {
			fondo.dispose();
			fondo = null;
		}
		sonidoMenu.dispose();
	}
}
 
=======

	@Override
	public void dispose() {
		shapeRenderer.dispose();
	}
}
>>>>>>> origin/main
