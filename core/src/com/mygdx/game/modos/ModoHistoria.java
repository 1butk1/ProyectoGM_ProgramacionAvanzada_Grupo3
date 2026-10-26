package com.mygdx.game.modos;

import com.mygdx.game.GameLluviaMenu;

public class ModoHistoria implements ModoJuego {

	public static final int META = 450;

	private static final String[] MENSAJES = {
		    "La lluvia no ha parado desde hace a\u00f1os. Cada gota cuenta.",                 
		    "Las casas est\u00e1n vac\u00edas. Alguien tuvo que llegar hasta aqu\u00ed.",     
		    "La ciudad est\u00e1 a oscuras. Las tuber\u00edas llevan hasta la planta.",       
		    "Aqu\u00ed naci\u00f3 la contaminaci\u00f3n. Lo que brilla en el suelo no perdona.", 
		    "Se apagan las luces. Se oye agua corriendo... y algo brilla al fondo."           
		};

    @Override public String getNombre() { return "Historia"; }
    @Override public boolean esCiclico() { return false; }
    @Override public boolean haGanado(int puntaje) { return puntaje >= META; }
    @Override public float getMultiplicadorDificultad(int vuelta) { return 1f; }

    @Override
    public String getMensajeZona(int indiceNivel, int vuelta) {
        return (indiceNivel >= 0 && indiceNivel < MENSAJES.length) ? MENSAJES[indiceNivel] : null;
    }

    @Override public int getHighScore(GameLluviaMenu game) { return game.getHigherScore(); }
    @Override public void setHighScore(GameLluviaMenu game, int p) { game.setHigherScore(p); }
}
