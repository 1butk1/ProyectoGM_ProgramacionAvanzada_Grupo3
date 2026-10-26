package com.mygdx.game.modos;

import com.mygdx.game.GameLluviaMenu;

public interface ModoJuego {
	   String getNombre();
	    boolean esCiclico();                          
	    boolean haGanado(int puntaje);                
	    float getMultiplicadorDificultad(int vuelta); 
	    String getMensajeZona(int indiceNivel, int vuelta); 
	    int getHighScore(GameLluviaMenu game);
	    void setHighScore(GameLluviaMenu game, int puntaje);
	}


