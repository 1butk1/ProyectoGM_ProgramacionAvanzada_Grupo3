package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.mygdx.game.pantallas.MainMenuScreen;
import com.mygdx.game.logica.GameSettings;

public class GameLluviaMenu extends Game {

	private SpriteBatch batch;
	private BitmapFont font;
	private int higherScore;
	private int higheScoreInfinito;
	private Preferences prefs;
	
	@Override
	public void create() {
	    batch = new SpriteBatch();
	    font = new BitmapFont();
	    prefs = Gdx.app.getPreferences("salvationrain");
	    GameSettings settings = GameSettings.getInstance();
	    settings.setVolumenMusica(prefs.getFloat("musica", 0.6f));
	    settings.setVolumenEfectos(prefs.getFloat("efectos", 1f));
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

	public void guardarVolumenes() {
	    GameSettings settings = GameSettings.getInstance();
	    prefs.putFloat("musica", settings.getVolumenMusica());
	    prefs.putFloat("efectos", settings.getVolumenEfectos());
	    prefs.flush();
	}
}