package com.mygdx.game.modos;

import com.mygdx.game.GameLluviaMenu;

public class ModoInfinito implements ModoJuego {

    private static final float AUMENTO_POR_VUELTA = 0.12f;
    private static final float TOPE = 1.6f;

    private static final String[] PRIMERA_VUELTA = {
        "RESISTE LO M\u00c1S QUE PUEDAS.",
        "No te detengas. El grupo cuenta contigo.",
        "Cada gota que atrapas es un d\u00eda m\u00e1s.",
        "Aqu\u00ed nada es seguro. Mant\u00e9n la calma.",
        "Sin luz, solo queda avanzar."
    };
    private static final String[] VUELTAS = {
        "La lluvia vuelve a empezar. Aguanta.",
        "M\u00e1s fuerte. M\u00e1s r\u00e1pido. Sigue en pie.",
        "Nadie lleg\u00f3 tan lejos. No pares ahora.",
        "Ya no se trata de sobrevivir. Se trata de resistir."
    };

    @Override public String getNombre() { return "Infinito"; }
    @Override public boolean esCiclico() { return true; }
    @Override public boolean haGanado(int puntaje) { return false; }

    @Override
    public float getMultiplicadorDificultad(int vuelta) {
        return Math.min(TOPE, 1f + AUMENTO_POR_VUELTA * vuelta);
    }

    @Override
    public String getMensajeZona(int indiceNivel, int vuelta) {
        if (vuelta <= 0) {
            return (indiceNivel >= 0 && indiceNivel < PRIMERA_VUELTA.length) ? PRIMERA_VUELTA[indiceNivel] : null;
        }
        return VUELTAS[(vuelta - 1 + Math.max(0, indiceNivel)) % VUELTAS.length];
    }

    @Override public int getHighScore(GameLluviaMenu game) { return game.getHigherScoreInfinito(); }
    @Override public void setHighScore(GameLluviaMenu game, int p) { game.setHigherScoreInfinito(p); }
}