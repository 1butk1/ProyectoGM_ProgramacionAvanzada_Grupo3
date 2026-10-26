package com.mygdx.game.logica;

public class GameManager {
	private static final int PUNTOS_POR_HITO = 100;
	 
	private final int initialLives;
	private GameState state;
	private int score;
	private int lives;
	private int proximoHito;
	private int golpesContaminadosDesdeHito;
 
	public GameManager(int initialLives) {
		this.initialLives = initialLives;
		startNewGame();
	}
	public void startNewGame() {
		state = GameState.JUGANDO;
		score = 0;
		lives = initialLives;
		proximoHito = PUNTOS_POR_HITO;
		golpesContaminadosDesdeHito = 0;
	}
	public void pause() {
		if (state == GameState.JUGANDO) {
			state = GameState.PAUSADO;
		}
	}
	public void resume() {
		if (state == GameState.PAUSADO) {
			state = GameState.JUGANDO;
		}
	}
	public void finishGame() {state = GameState.TERMINADO;}
	public void addScore(int points) {
		if (state == GameState.JUGANDO) {
			score += points;
		}
	}
	public void registerContaminatedHit() {golpesContaminadosDesdeHito++;}
	public boolean canSpawnGoldenDrop() {
		if (score < proximoHito) {
			return false;
		}
		boolean permitido = (golpesContaminadosDesdeHito == 0);
		proximoHito += PUNTOS_POR_HITO;
		golpesContaminadosDesdeHito = 0;
		return permitido;
	}
	public void sincronizar(int newScore, int newLives) {
		if (state != GameState.JUGANDO) {
			return;
		}
		score = newScore;
		lives = Math.max(0, newLives);
		if (lives == 0) {
			finishGame();
		}
	}
	public GameState getState() {return state;}
	public int getScore() {return score;}
	public int getLives() {return lives;}
	public boolean isPlaying() {return state == GameState.JUGANDO;}
	public boolean isPaused() {return state == GameState.PAUSADO;}
	public boolean isGameOver() {return state == GameState.TERMINADO;}
}
 