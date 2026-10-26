package com.mygdx.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.GameLluviaMenu;
import com.mygdx.game.ui.Boton;

public class CinematicaScreen implements Screen {

	 public interface Siguiente {
	        Screen crear();
	    }

	    private static final float FADE = 0.9f;    
	    private static final float MINIMO = 1.5f;  
	    private static final float AUTO = 7f;      

	    private final GameLluviaMenu game;
	    private final SpriteBatch batch;
	    private final BitmapFont font;
	    private final OrthographicCamera camera;
	    private final String[] rutas;
	    private final String[][] textos;
	    private final Siguiente siguiente;
	    private final Texture pixel;
	    private Texture imagen;
	    private int indice = 0;
	    private float t = 0f;
	    private float tSalida = 0f;
	    private boolean saliendo = false;

	    public CinematicaScreen(GameLluviaMenu game, String[] rutas, String[][] textos, Siguiente siguiente) {
	        this.game = game;
	        this.batch = game.getBatch();
	        this.font = game.getFont();
	        this.camera = new OrthographicCamera();
	        this.camera.setToOrtho(false, 800, 480);
	        this.rutas = rutas;
	        this.textos = textos;
	        this.siguiente = siguiente;

	        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
	        pm.setColor(Color.WHITE);
	        pm.fill();
	        pixel = new Texture(pm);
	        pm.dispose();

	        cargarImagen();
	    }

	    // ---- las dos cinematicas del juego ----
	    // Antes del manantial: el camino y el descubrimiento. 
	    public static Screen intro(final GameLluviaMenu game, final int puntaje) {
	        return new CinematicaScreen(game,
	                new String[] {
	                    "fondos/cine_planta.png",
	                    "fondos/cine_mina_entrada.png",
	                    "fondos/cine_paraiso.png"
	                },
	                
	                new String[][] {
	                    { "Dejas atr\u00e1s la planta, todav\u00eda brillando en verde bajo la lluvia.",
	                      "Dicen que en una vieja mina a\u00fan queda agua limpia." },
	                    { "La galer\u00eda parece no tener fin, pero al fondo hay una luz.",
	                      "Una luz azul que no es de ninguna l\u00e1mpara." },
	                    { "Bajo la tierra, donde nada deber\u00eda sobrevivir, crece un bosque.",
	                      "Cada gota que cuidaste te trajo hasta aqu\u00ed." }
	                },
	                new Siguiente() {
	                    @Override public Screen crear() { return epilogo(game); }
	                });
	    }

	    // Despues del manantial: un cierre antes de volver al menu. 
	    public static Screen epilogo(final GameLluviaMenu game) {
	        return new CinematicaScreen(game,
	                new String[] { "fondos/cine_luz.png", "fondos/cine_salida.png" },
	                new String[][] {
	                    { "El agua corre limpia, y por fin alguien puede compartirla.",
	                      "Pero la lluvia sigue ah\u00ed fuera. Ma\u00f1ana habr\u00e1 que volver." },
	                    { "Afuera, alguien mantiene una fogata encendida.",
	                      "Esta vez no vuelves con las manos vac\u00edas." }
	                },
	                new Siguiente() {
	                    @Override public Screen crear() { return new MainMenuScreen(game); }
	                });
	    }
	    private void cargarImagen() {
	        if (imagen != null) { imagen.dispose(); imagen = null; }
	        if (indice < rutas.length && Gdx.files.internal(rutas[indice]).exists()) {
	            imagen = new Texture(Gdx.files.internal(rutas[indice]));
	        }
	    }
	    private void texto(String s, float y, float a) {
	        font.setColor(0f, 0f, 0f, a * 0.85f);
	        Boton.dibujarTextoCentrado(batch, font, s, 1.2f, y - 2, camera.viewportWidth + 4);
	        font.setColor(0.86f, 0.84f, 0.76f, a);
	        Boton.dibujarTextoCentrado(batch, font, s, 1.2f, y, camera.viewportWidth);
	    }

	    @Override
	    public void render(float delta) {
	        t += delta;
	        if (saliendo) tSalida += delta;
	        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
	            game.setScreen(siguiente.crear());
	            return;
	        }
	        if (!saliendo) {
	            boolean pidio = Gdx.input.justTouched()
	                    || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)
	                    || Gdx.input.isKeyJustPressed(Input.Keys.ENTER);
	            if ((t >= MINIMO && pidio) || t >= AUTO) saliendo = true;
	        } else if (tSalida >= FADE) {
	            indice++;
	            if (indice >= rutas.length) {
	                game.setScreen(siguiente.crear());
	                return;
	            }
	            cargarImagen();
	            t = 0f;
	            tSalida = 0f;
	            saliendo = false;
	        }
	        float alfa = saliendo ? 1f - tSalida / FADE : MathUtils.clamp(t / FADE, 0f, 1f);
	        float alfaTexto = saliendo ? alfa : alfa * MathUtils.clamp((t - 0.8f) / 1f, 0f, 1f);

	        ScreenUtils.clear(0, 0, 0, 1);
	        camera.update();
	        batch.setProjectionMatrix(camera.combined);
	        batch.begin();
	        batch.setColor(1f, 1f, 1f, alfa);
	        if (imagen != null) batch.draw(imagen, 0, 0, 800, 480);

	        batch.setColor(0f, 0f, 0f, 0.55f * alfaTexto);
	        batch.draw(pixel, 0, 25, 800, 95);
	        batch.setColor(Color.WHITE);

	        String[] lineas = textos[indice];
	        for (int i = 0; i < lineas.length; i++) {
	            texto(lineas[i], 90 - 30 * i, alfaTexto);
	        }

	        font.setColor(0.62f, 0.68f, 0.64f, alfaTexto * 0.8f);
	        font.getData().setScale(0.9f);
	        font.draw(batch, "ENTER / clic: continuar   ESC: saltar", 470, 18);
	        font.getData().setScale(1f);

	        font.setColor(Color.WHITE);
	        batch.end();
	    }

	    @Override public void show() { }
	    @Override public void resize(int width, int height) { }
	    @Override public void pause() { }
	    @Override public void resume() { }
	    @Override public void hide() { dispose(); }

	    @Override
	    public void dispose() {
	        if (imagen != null) { imagen.dispose(); imagen = null; }
	        pixel.dispose();
	    }
	}
