package com.mygdx.game.logica;

public class GameManager {
	
	private static final int PUNTOS_POR_HITO_DEFECTO = 100;
	private static final int MAX_VIDAS = 10;
	private final int initialLives;
	private GameState state;
	private int score;
	private int lives;
	private int puntosPorHito = PUNTOS_POR_HITO_DEFECTO;
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
		proximoHito = puntosPorHito;
		golpesContaminadosDesdeHito = 0;
	}

	public void pause() {if (state == GameState.JUGANDO) {state = GameState.PAUSADO;}}
	public void resume() {if (state == GameState.PAUSADO) {state = GameState.JUGANDO;}}
	public void finishGame() { state = GameState.TERMINADO; }
	public void addScore(int points) {
		if (state == GameState.JUGANDO) {score += points;}
	}

	public void perderVida() {if (state == GameState.JUGANDO && lives > 0) {lives--;}}
	public boolean ganarVida() {
		if (state != GameState.JUGANDO || lives >= MAX_VIDAS) {
			return false;
		}
		lives++;
		return true;
	}
	public void setPuntosPorHito(int puntos) {
		if (puntos <= 0) return;
		this.puntosPorHito = puntos;
		this.proximoHito = (score / puntos + 1) * puntos;
	}
	
	public void registerContaminatedHit() { golpesContaminadosDesdeHito++; }
	public boolean canSpawnGoldenDrop() {
		if (score < proximoHito) {
			return false;
		}
		boolean permitido = (golpesContaminadosDesdeHito == 0);
		proximoHito += puntosPorHito;
		golpesContaminadosDesdeHito = 0;
		return permitido;
	}
	public GameState getState() { return state; }
	public int getScore() { return score; }
	public int getLives() { return lives; }
	public boolean isPlaying() { return state == GameState.JUGANDO; }
	public boolean isPaused() { return state == GameState.PAUSADO; }
	public boolean isGameOver() { return state == GameState.TERMINADO; }
}
