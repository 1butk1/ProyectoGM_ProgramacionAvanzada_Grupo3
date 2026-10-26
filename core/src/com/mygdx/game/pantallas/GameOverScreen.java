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
		botonReiniciar.dibujarFondo(shapeRenderer, sobreReiniciar);
		botonMenu.dibujarFondo(shapeRenderer, sobreMenu);
		shapeRenderer.end();
 
		// 3) Textos
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
		botonReiniciar.dibujarTexto(batch, font, sobreReiniciar);
		botonMenu.dibujarTexto(batch, font, sobreMenu);
		font.setColor(Color.WHITE);
		batch.end();
 
		boolean click = Gdx.input.justTouched();
 
		if (Gdx.input.isKeyJustPressed(Input.Keys.R) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || (click && sobreReiniciar)) {
			game.setScreen(new GameScreen(game, modo));
			return;
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.M) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || (click && sobreMenu)) {
			game.setScreen(new MainMenuScreen(game));
		}
	}
	@Override
	public void show() { sonidoDerrota.play(0.8f);}
	@Override
	public void resize(int width, int height) {}
	@Override
	public void pause() {}
	@Override
	public void resume() {}
	@Override
	public void hide() {dispose();}
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
 