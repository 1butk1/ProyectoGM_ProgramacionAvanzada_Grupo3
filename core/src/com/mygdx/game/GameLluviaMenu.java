package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.pantallas.MainMenuScreen;

public class GameLluviaMenu extends Game {

	private SpriteBatch batch;
	private BitmapFont font;
	private int higherScore;
	private int higheScoreInfinito;
	private float volumenMusica = 0.6f;
	private float volumenEfectos = 1f;
	private Preferences prefs;
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		prefs = Gdx.app.getPreferences("salvationrain");
		volumenMusica = prefs.getFloat("musica", 0.6f);
		volumenEfectos = prefs.getFloat("efectos", 1f);
		this.setScreen(new MainMenuScreen(this));
		
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
		font.dispose();
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public BitmapFont getFont() {
		return font;
	}
	public int getHigherScoreInfinito() { return higheScoreInfinito; }
	public void setHigherScoreInfinito(int v) { higheScoreInfinito = v; }
	public int getHigherScore() {return higherScore;}
	public void setHigherScore(int higherScore) {this.higherScore = higherScore;}
	
	public float getVolumenMusica() { return volumenMusica; }
	public float getVolumenEfectos() { return volumenEfectos; }
	public void setVolumenMusica(float v) { volumenMusica = MathUtils.clamp(v, 0f, 1f); }
	public void setVolumenEfectos(float v) { volumenEfectos = MathUtils.clamp(v, 0f, 1f); }
	public void guardarVolumenes() {
	    prefs.putFloat("musica", volumenMusica);
	    prefs.putFloat("efectos", volumenEfectos);
	    prefs.flush();
	}

}