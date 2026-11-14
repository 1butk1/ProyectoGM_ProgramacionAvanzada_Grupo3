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
import com.mygdx.game.logica.GameSettings;
import com.mygdx.game.ui.Boton;
import com.mygdx.game.ui.Visual;

public class MainMenuScreen implements Screen {

	private static final String RUTA_FONDO = "fondos/menu.png";
	private static final Color COLOR_POLVO = new Color(0.86f, 0.84f, 0.76f, 0.5f);

	private final Sound sonidoMenu;
	private final GameLluviaMenu game;
	private final SpriteBatch batch;
	private final BitmapFont font;
	private final OrthographicCamera camera;
	private final ShapeRenderer shapeRenderer;
	private final Vector3 puntero;
	private final Boton botonJugar;
	private final Boton botonSalir;
	private final Boton botonOpciones;
	private final Visual visual;
	private Texture fondo;

	public MainMenuScreen(final GameLluviaMenu game) {
		this.game = game;
		this.batch = game.getBatch();
		this.font = game.getFont();
		this.visual = new Visual(font);
		this.camera = new OrthographicCamera();
		this.sonidoMenu = Gdx.audio.newSound(Gdx.files.internal("sonidomenu.mp3"));
		this.camera.setToOrtho(false, 800, 480);
		this.shapeRenderer = new ShapeRenderer();
		this.puntero = new Vector3();
		this.botonJugar = new Boton("Jugar (ENTER)", 300, 220, 200, 50);
		this.botonOpciones = new Boton("Opciones (O)", 300, 150, 200, 50);
		this.botonSalir = new Boton("Salir (ESC)", 300, 80, 200, 50);

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
		boolean sobreJugar = botonJugar.contiene(puntero.x, puntero.y);
		boolean sobreSalir = botonSalir.contiene(puntero.x, puntero.y);
		boolean sobreOpciones = botonOpciones.contiene(puntero.x, puntero.y);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		if (fondo != null) visual.fondo(batch, fondo);
		visual.lluvia(batch, delta);
		visual.polvo(batch, delta, COLOR_POLVO);
		visual.vineta(batch);
		batch.end();
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		botonJugar.dibujarFondo(shapeRenderer, sobreJugar);
		botonOpciones.dibujarFondo(shapeRenderer, sobreOpciones);
		botonSalir.dibujarFondo(shapeRenderer, sobreSalir);
		shapeRenderer.end();
		batch.begin();
		visual.titulo(batch, "Salvation Rain", 410f, 3.5f, Visual.HUESO);
		visual.texto(batch, "A Little Hope...", 362f, 1.3f, Visual.MUSGO);
		botonJugar.dibujarTexto(batch, font, sobreJugar);
		botonOpciones.dibujarTexto(batch, font, sobreOpciones);
		botonSalir.dibujarTexto(batch, font, sobreSalir);
		font.setColor(Color.WHITE);
		batch.end();
		boolean click = Gdx.input.justTouched();
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || (click && sobreJugar)) {
			game.setScreen(new SeleccionModo(game));
			return;
		}
	
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || (click && sobreSalir)) {Gdx.app.exit();}
		if (Gdx.input.isKeyJustPressed(Input.Keys.O) || (click && sobreOpciones)) {
			game.setScreen(new OpcionesScreen(game));
			return;
		}
	}

	@Override
	public void show() { sonidoMenu.play(0.8f * GameSettings.getInstance().getVolumenMusicaEfectivo()); }
	@Override public void resize(int width, int height) { }
	@Override public void pause() { }
	@Override public void resume() { }
	@Override
	public void hide() {dispose();}
	
	@Override
	public void dispose() {
		shapeRenderer.dispose();
		visual.dispose();
		if (fondo != null) {
			fondo.dispose();
			fondo = null;
		}
		sonidoMenu.dispose();
	}
}