package com.mygdx.game;

import com.badlogic.gdx.utils.Array;

public class LevelManager {
    private Array<LevelConfig> niveles;
    private int indiceNivelActual;

    public LevelManager() {
        niveles = new Array<>();
        indiceNivelActual = 0;

        niveles.add(new LevelConfig.Builder()
                .setName("Ciudad")
                .setBackgroundPath("fondos/ciudad.png")
                .setDropSpeed(150f)
                .setSpawnInterval(1.2f)
                .setScoreRequired(100)
                .setGoldenDropThreshold(30)
                .build());

        niveles.add(new LevelConfig.Builder()
                .setName("Pueblo")
                .setBackgroundPath("fondos/pueblo.png")
                .setDropSpeed(220f)
                .setSpawnInterval(0.9f)
                .setScoreRequired(250)
                .setGoldenDropThreshold(50)
                .build());

        // Último nivel: no hay siguiente, por eso el puntaje requerido es inalcanzable
        niveles.add(new LevelConfig.Builder()
                .setName("Bosque")
                .setBackgroundPath("fondos/bosque.png")
                .setDropSpeed(300f)
                .setSpawnInterval(0.6f)
                .setScoreRequired(Integer.MAX_VALUE)
                .setGoldenDropThreshold(80)
                .build());
    }

    /** Devuelve true si el puntaje hizo subir de nivel. */
    public boolean updateForScore(int puntaje) {
        boolean esUltimoNivel = indiceNivelActual >= niveles.size - 1;
        if (esUltimoNivel) {
            return false;
        }
        if (puntaje >= niveles.get(indiceNivelActual).getScoreRequired()) {
            indiceNivelActual++;
            return true;
        }
        return false;
    }

    public LevelConfig getCurrentConfig() {
        return niveles.get(indiceNivelActual);
    }

    /** Número de nivel para mostrar al jugador (empieza en 1). */
    public int getCurrentLevel() {
        return indiceNivelActual + 1;
    }
}