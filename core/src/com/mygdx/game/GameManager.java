package com.mygdx.game;

public class GameManager {
	private static final int PUNTOS_POR_HITO = 100;
	 
	private final int initialLives;
	private GameState state;
	private int score;
	private int lives;
 
	// control de la gota dorada (agregado para las clases de gotas)
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
 
	public void finishGame() {
		state = GameState.TERMINADO;
	}
 
	public void addScore(int points) {
		if (state == GameState.JUGANDO) {
			score += points;
		}
	}
 
	/** Lo llama ToxicDrop (CaidaAguaToxica) cuando el jugador recibe una gota toxica. */
	public void registerContaminatedHit() {
		golpesContaminadosDesdeHito++;
	}
 
	/**
	 * Lo llama Lluvia antes de crear una gota dorada.
	 * Solo se permite al alcanzar un hito de puntos (cada 100) sin haber
	 * recibido gotas toxicas desde el hito anterior.
	 */
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
 
	public GameState getState() {
		return state;
	}
 
	public int getScore() {
		return score;
	}
 
	public int getLives() {
		return lives;
	}
 
	public boolean isPlaying() {
		return state == GameState.JUGANDO;
	}
 
	public boolean isPaused() {
		return state == GameState.PAUSADO;
	}
 
	public boolean isGameOver() {
		return state == GameState.TERMINADO;
	}
}
 