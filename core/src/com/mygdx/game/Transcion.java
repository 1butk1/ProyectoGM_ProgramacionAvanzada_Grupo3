package com.mygdx.game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Transcion {

	private static final float ANCHO = 800f;
	private static final float ALTO = 480f;
 
	private static final float DUR_BAJAR = 0.8f;
	private static final float DUR_TITULO = 1.4f;
	private static final float DUR_SUBIR = 1.0f;
 
	private static final float ZOOM_BAJAR = 1.06f;   // zoom al que llega mientras oscurece
	private static final float ZOOM_REVELAR = 1.10f; // zoom desde el que aparece la nueva escena
	private static final float ALTO_BARRAS = 56f;
 
	private enum Fase { NINGUNA, BAJAR, TITULO, SUBIR }
 
	private final Texture pixel;
	private final GlyphLayout layout = new GlyphLayout();
 
	private Fase fase = Fase.NINGUNA;
	private float t;
	private String titulo = "";
	private String subtitulo = "";
	private Runnable alOscurecer;
 
	public Transcion() {
		Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pm.setColor(Color.WHITE);
		pm.fill();
		pixel = new Texture(pm);
		pm.dispose();
	}
 
	/**
	 * @param alOscurecer se ejecuta una sola vez cuando la pantalla esta totalmente negra;
	 *                    aqui hay que cambiar fondo, LevelConfig, limpiar gotas, etc.
	 */
	public void iniciar(String titulo, String subtitulo, Runnable alOscurecer) {
		if (fase != Fase.NINGUNA) return;
		this.titulo = titulo;
		this.subtitulo = subtitulo == null ? "" : subtitulo;
		this.alOscurecer = alOscurecer;
		this.fase = Fase.BAJAR;
		this.t = 0f;
	}
 
	/** True durante toda la transicion (para dibujar fondo con zoom y overlay). */
	public boolean estaActiva() {
		return fase != Fase.NINGUNA;
	}
 
	/**
	 * True mientras el juego debe estar congelado: al oscurecer y en el titulo.
	 * Al subir el brillo el jugador ya puede moverse mientras la escena se revela.
	 */
	public boolean bloqueaJuego() {
		return fase == Fase.BAJAR || fase == Fase.TITULO;
	}
 
	public void actualizar(float delta) {
		if (fase == Fase.NINGUNA) return;
		t += delta;
		switch (fase) {
		case BAJAR:
			if (t >= DUR_BAJAR) {
				fase = Fase.TITULO;
				t = 0f;
				if (alOscurecer != null) alOscurecer.run();
			}
			break;
		case TITULO:
			if (t >= DUR_TITULO) {
				fase = Fase.SUBIR;
				t = 0f;
			}
			break;
		case SUBIR:
			if (t >= DUR_SUBIR) {
				fase = Fase.NINGUNA;
				t = 0f;
			}
			break;
		default:
			break;
		}
	}
 
	// ---- curvas ----
 
	private static float suave(float p) {
		p = MathUtils.clamp(p, 0f, 1f);
		return p * p * (3f - 2f * p);
	}
 
	private static float salida(float p) {
		p = MathUtils.clamp(p, 0f, 1f);
		float q = 1f - p;
		return 1f - q * q * q;
	}
 
	/** 0 = escena visible, 1 = negro total. */
	private float oscuridad() {
		switch (fase) {
		case BAJAR:  return suave(t / DUR_BAJAR);
		case TITULO: return 1f;
		case SUBIR:  return 1f - suave(t / DUR_SUBIR);
		default:     return 0f;
		}
	}
 
	private float zoom() {
		switch (fase) {
		case BAJAR:  return 1f + (ZOOM_BAJAR - 1f) * suave(t / DUR_BAJAR);
		case TITULO: return ZOOM_REVELAR;
		case SUBIR:  return ZOOM_REVELAR - (ZOOM_REVELAR - 1f) * salida(t / DUR_SUBIR);
		default:     return 1f;
		}
	}
 
	private float barras() {
		switch (fase) {
		case BAJAR:  return ALTO_BARRAS * suave(t / DUR_BAJAR);
		case TITULO: return ALTO_BARRAS;
		case SUBIR:  return ALTO_BARRAS * (1f - suave(t / DUR_SUBIR));
		default:     return 0f;
		}
	}
 
	/** Dibuja el fondo a pantalla completa aplicando el zoom de la transicion (centrado). */
	public void dibujarFondo(SpriteBatch batch, Texture fondo) {
		float z = zoom();
		float w = ANCHO * z;
		float h = ALTO * z;
		batch.setColor(Color.WHITE);
		batch.draw(fondo, (ANCHO - w) / 2f, (ALTO - h) / 2f, w, h);
	}
 
	/** Oscurecimiento, barras de cine y titulo. Llamar al final, dentro de batch.begin()/end(). */
	public void dibujarOverlay(SpriteBatch batch, BitmapFont font) {
		if (fase == Fase.NINGUNA) return;
 
		float osc = oscuridad();
		batch.setColor(0f, 0f, 0.02f, osc);
		batch.draw(pixel, 0, 0, ANCHO, ALTO);
 
		float b = barras();
		batch.setColor(0f, 0f, 0f, 1f);
		batch.draw(pixel, 0, 0, ANCHO, b);
		batch.draw(pixel, 0, ALTO - b, ANCHO, b);
		batch.setColor(Color.WHITE);
 
		if (fase == Fase.TITULO) {
			float a = 1f;
			if (t < 0.35f) a = t / 0.35f;
			else if (t > DUR_TITULO - 0.35f) a = (DUR_TITULO - t) / 0.35f;
			a = MathUtils.clamp(a, 0f, 1f);
 
			Color c1 = new Color(Visual.HUESO);
			c1.a = a;
			escribirCentrado(batch, font, titulo, 270f, 2.6f, c1);
			if (!subtitulo.isEmpty()) {
				Color c2 = new Color(Visual.MUSGO);
				c2.a = a;
				escribirCentrado(batch, font, subtitulo, 225f, 1.1f, c2);
			}
		}
	}
 
	private void escribirCentrado(SpriteBatch batch, BitmapFont font, String texto, float y, float escala, Color color) {
		float escalaPrevia = font.getData().scaleX;
		Color colorPrevio = new Color(font.getColor());
		font.getData().setScale(escala);
		font.setColor(color);
		layout.setText(font, texto);
		font.draw(batch, layout, ANCHO / 2f - layout.width / 2f, y);
		font.getData().setScale(escalaPrevia);
		font.setColor(colorPrevio);
	}
 
	public void dispose() {
		pixel.dispose();
	}
}
 