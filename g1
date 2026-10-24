package com.mygdx.game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
 
public class Boton {
	private final String texto;
	private final Rectangle area;
 
	public Boton(String texto, float x, float y, float ancho, float alto) {
		this.texto = texto;
		this.area = new Rectangle(x, y, ancho, alto);
	}
 
	public boolean contiene(float x, float y) {
		return area.contains(x, y);
	}
 
	/** Boton oscuro con borde fino; se aclara un poco al pasar el mouse. */
	public void dibujarFondo(ShapeRenderer shapeRenderer, boolean resaltado) {
		if (resaltado) {
			shapeRenderer.setColor(0.62f, 0.66f, 0.58f, 1f);   // borde
		} else {
			shapeRenderer.setColor(0.30f, 0.34f, 0.31f, 1f);
		}
		shapeRenderer.rect(area.x - 2, area.y - 2, area.width + 4, area.height + 4);
 
		if (resaltado) {
			shapeRenderer.setColor(0.20f, 0.24f, 0.22f, 1f);   // relleno
		} else {
			shapeRenderer.setColor(0.09f, 0.11f, 0.11f, 1f);
		}
		shapeRenderer.rect(area.x, area.y, area.width, area.height);
	}
 
	public void dibujarTexto(SpriteBatch batch, BitmapFont font, boolean resaltado) {
		font.getData().setScale(1.3f);
		font.setColor(resaltado ? new Color(0.94f, 0.88f, 0.66f, 1f) : new Color(0.78f, 0.80f, 0.76f, 1f));
		GlyphLayout layout = new GlyphLayout(font, texto);
		float x = area.x + (area.width - layout.width) / 2;
		float y = area.y + (area.height + layout.height) / 2;
		font.draw(batch, layout, x, y);
		font.setColor(Color.WHITE);
		font.getData().setScale(1f);
	}
 
	public static void dibujarTextoCentrado(SpriteBatch batch, BitmapFont font, String texto, float escala, float y, float anchoPantalla) {
		font.getData().setScale(escala);
		GlyphLayout layout = new GlyphLayout(font, texto);
		font.draw(batch, layout, (anchoPantalla - layout.width) / 2, y);
		font.getData().setScale(1f);
	}
}
 