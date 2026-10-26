package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public final class Visual {
	public static final float ANCHO = 800f;
	public static final float ALTO = 480f;
 
	public static final Color SANGRE = Color.valueOf("B3262B");
	public static final Color HUESO = Color.valueOf("D8D2B8");
	public static final Color MUSGO = Color.valueOf("7C8F6B");
	public static final Color SELECCION = Color.valueOf("E0B341");
	public static final Color APAGADO = Color.valueOf("8A8A7A");
 
	private static final int GOTAS = 140;
	private static final int POLVO = 45;
 
	private final BitmapFont font;
	private final GlyphLayout layout = new GlyphLayout();
	private final Texture pixel;
	private final float[] gx = new float[GOTAS];
	private final float[] gy = new float[GOTAS];
	private final float[] gv = new float[GOTAS];
	private final float[] px = new float[POLVO];
	private final float[] py = new float[POLVO];
	private final float[] pv = new float[POLVO];
	private float tiempo;
	private float centroX = ANCHO / 2f;
 
	public Visual(BitmapFont font) {
		this.font = font;
		Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pm.setColor(Color.WHITE);
		pm.fill();
		pixel = new Texture(pm);
		pm.dispose();
 
		for (int i = 0; i < GOTAS; i++) {
			gx[i] = MathUtils.random(0f, ANCHO);
			gy[i] = MathUtils.random(0f, ALTO);
			gv[i] = MathUtils.random(380f, 650f);
		}
		for (int i = 0; i < POLVO; i++) {
			px[i] = MathUtils.random(0f, ANCHO);
			py[i] = MathUtils.random(0f, ALTO);
			pv[i] = MathUtils.random(4f, 14f);
		}
	}
 
	public void setCentroX(float centroX) {
		this.centroX = centroX;
	}

	public void polvo(SpriteBatch batch, float delta, Color color) {
		tiempo += delta;
		for (int i = 0; i < POLVO; i++) {
			py[i] += pv[i] * delta;
			px[i] += MathUtils.sin(tiempo * 0.6f + i) * 10f * delta;
			if (py[i] > ALTO + 4f) {
				py[i] = -4f;
				px[i] = MathUtils.random(0f, ANCHO);
			}
			float a = color.a * (0.5f + 0.5f * MathUtils.sin(tiempo * 2f + i * 1.7f));
			batch.setColor(color.r, color.g, color.b, a);
			batch.draw(pixel, px[i], py[i], 2f, 2f);
		}
		batch.setColor(Color.WHITE);
	}
 
	// Fondo a pantalla completa.
	public void fondo(SpriteBatch batch, Texture textura) {
		batch.setColor(Color.WHITE);
		batch.draw(textura, 0, 0, ANCHO, ALTO);
	}
 
	public void velo(SpriteBatch batch, float alpha) {
		batch.setColor(0f, 0f, 0.03f, alpha);
		batch.draw(pixel, 0, 0, ANCHO, ALTO);
		batch.setColor(Color.WHITE);
	}
 
	public void vineta(SpriteBatch batch) {
		batch.setColor(0f, 0f, 0f, 0.35f);
		batch.draw(pixel, 0, 0, ANCHO, 40);
		batch.draw(pixel, 0, ALTO - 40, ANCHO, 40);
		batch.setColor(Color.WHITE);
	}
 
	public void lluvia(SpriteBatch batch, float delta) {
		tiempo += delta;
		batch.setColor(0.65f, 0.75f, 0.85f, 0.28f);
		for (int i = 0; i < GOTAS; i++) {
			gy[i] -= gv[i] * delta;
			gx[i] -= 40f * delta;
			if (gy[i] < -12f || gx[i] < -4f) {
				gy[i] = ALTO + MathUtils.random(0f, 40f);
				gx[i] = MathUtils.random(0f, ANCHO + 60f);
			}
			batch.draw(pixel, gx[i], gy[i], 1.5f, 11f);
		}
		batch.setColor(Color.WHITE);
	}
 
	// Titulo grande 
	public void titulo(SpriteBatch batch, String texto, float y, float escala, Color color) {
		escribirCentrado(batch, texto, y + 3f, escala, Color.BLACK, centroX + 3f);
		escribirCentrado(batch, texto, y, escala, color, centroX);
	}
 
	// Texto secundario 
	public void texto(SpriteBatch batch, String texto, float y, float escala, Color color) {
		escribirCentrado(batch, texto, y, escala, color, centroX);
	}
 
	// Opcion de menu: la seleccionada parpadea suavemente y lleva marcadores
	public void opcion(SpriteBatch batch, String texto, float y, boolean seleccionada) {
		if (seleccionada) {
			float pulso = 0.75f + 0.25f * MathUtils.sin(tiempo * 6f);
			Color c = new Color(SELECCION).mul(pulso, pulso, pulso, 1f);
			escribirCentrado(batch, "> " + texto + " <", y, 1.4f, c, centroX);
		} else {
			escribirCentrado(batch, texto, y, 1.2f, APAGADO, centroX);
		}
	}
 
	private void escribirCentrado(SpriteBatch batch, String texto, float y, float escala, Color color, float cx) {
		float escalaPrevia = font.getData().scaleX;
		Color colorPrevio = new Color(font.getColor());
		font.getData().setScale(escala);
		font.setColor(color);
		layout.setText(font, texto);
		font.draw(batch, layout, cx - layout.width / 2f, y);
		font.getData().setScale(escalaPrevia);
		font.setColor(colorPrevio);
	}
 
	public void dispose() {
		pixel.dispose();
	}
}
 