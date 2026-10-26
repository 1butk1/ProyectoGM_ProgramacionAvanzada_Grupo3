package com.mygdx.game.logica;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;


public final class Progreso {

    private static final String ARCHIVO = "ProyectoGM_progreso";
    private static final String CLAVE_NIVEL_MAXIMO = "nivelMaximoHistoria";

    private Progreso() { }
    public static int getNivelMaximo() {
        return Gdx.app.getPreferences(ARCHIVO).getInteger(CLAVE_NIVEL_MAXIMO, 0);
    }
    public static void desbloquear(int indiceNivel) {
        Preferences prefs = Gdx.app.getPreferences(ARCHIVO);
        if (indiceNivel > prefs.getInteger(CLAVE_NIVEL_MAXIMO, 0)) {
            prefs.putInteger(CLAVE_NIVEL_MAXIMO, indiceNivel);
            prefs.flush();
        }
    }

    public static void reiniciar() {
        Preferences prefs = Gdx.app.getPreferences(ARCHIVO);
        prefs.putInteger(CLAVE_NIVEL_MAXIMO, 0);
        prefs.flush();
    }
}