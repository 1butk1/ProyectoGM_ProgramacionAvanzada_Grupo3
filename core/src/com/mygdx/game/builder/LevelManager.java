package com.mygdx.game.builder;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.modos.ModoJuego;

public class LevelManager {

    private static final int[] UMBRALES_VUELTA = {100, 250, 400, 550, 700};
    private static final int PUNTOS_POR_VUELTA = 700;
    private static final float INTERVALO_MINIMO = 0.3f;
    private final Array<LevelConfig> niveles;
    private final ModoJuego modo;
    private int indiceNivelActual;
    private int vuelta;

    public LevelManager(ModoJuego modo) {
        this(modo, 0);
    }

    public LevelManager(ModoJuego modo, int nivelInicial) {
        this.modo = modo;
        niveles = new Array<>();
        indiceNivelActual = 0;
        vuelta = 0;

        niveles.add(new LevelConfig.Builder()
                .setName("Bosque").setBackgroundPath("fondos/bosque.png")
                .setDropSpeed(170f).setSpawnInterval(1.2f)
                .setScoreRequired(150).setGoldenDropThreshold(30)
                .setJumpEnabled(true).build());

        niveles.add(new LevelConfig.Builder()
                .setName("Pueblo").setBackgroundPath("fondos/pueblo.png")
                .setDropSpeed(50f).setSpawnInterval(0.9f)
                .setScoreRequired(350).setGoldenDropThreshold(50)
                .setJumpEnabled(true).build());

        
        niveles.add(new LevelConfig.Builder()
                .setName("Ciudad").setBackgroundPath("fondos/ciudad.png")
                .setDropSpeed(300f).setSpawnInterval(0.6f)
                .setScoreRequired(600).setGoldenDropThreshold(80)
                .setJumpEnabled(true).build());
        
        niveles.add(new LevelConfig.Builder()
                .setName("Planta").setBackgroundPath("fondos/planta.png")
                .setDropSpeed(300f).setSpawnInterval(0.6f)
                .setScoreRequired(900).setGoldenDropThreshold(80)
                .setJumpEnabled(true).build());
        
        
        niveles.add(new LevelConfig.Builder()
                .setName("Mina").setBackgroundPath("fondos/mina.png")
                .setDropSpeed(300f).setSpawnInterval(0.6f)
                .setScoreRequired(1300).setGoldenDropThreshold(80).setJumpEnabled(true).build());

        if (!modo.esCiclico()) {
            indiceNivelActual = Math.max(0, Math.min(nivelInicial, niveles.size - 1));
        }
    }

    // Devuelve true si el puntaje hizo cambiar de nivel. 
    public boolean updateForScore(int puntaje) {
        if (modo.esCiclico()) {
            int umbral = vuelta * PUNTOS_POR_VUELTA + UMBRALES_VUELTA[indiceNivelActual];
            if (puntaje >= umbral) {
                indiceNivelActual++;
                if (indiceNivelActual >= niveles.size) {
                    indiceNivelActual = 0;
                    vuelta++;
                }
                return true;
            }
            return false;
        }

        boolean esUltimoNivel = indiceNivelActual >= niveles.size - 1;
        if (esUltimoNivel) return false;
        if (puntaje >= niveles.get(indiceNivelActual).getScoreRequired()) {
            indiceNivelActual++;
            return true;
        }
        return false;
    }

    // Config del nivel actual
    public LevelConfig getCurrentConfig() {
        LevelConfig base = niveles.get(indiceNivelActual);
        float m = modo.getMultiplicadorDificultad(vuelta);
        if (m == 1f) return base;
        return new LevelConfig.Builder()
                .setName(base.getName())
                .setBackgroundPath(base.getBackgroundPath())
                .setDropSpeed(base.getDropSpeed() * m)
                .setSpawnInterval(Math.max(INTERVALO_MINIMO, base.getSpawnInterval() / m))
                .setScoreRequired(base.getScoreRequired())
                .setGoldenDropThreshold(base.getGoldenDropThreshold())
                .setJumpEnabled(base.isJumpEnabled())
                .build();
    }

    public int getCurrentLevel() { return indiceNivelActual + 1; }
    public int getIndiceNivel() { return indiceNivelActual; }
    public int getVuelta() { return vuelta; }

    public int getCantidadNiveles() { return niveles.size; }

    public LevelConfig getConfigNivel(int indice) { return niveles.get(indice); }
    public int getPuntajeInicialNivel(int indice) {
        return indice <= 0 ? 0 : niveles.get(indice - 1).getScoreRequired();
    }
}