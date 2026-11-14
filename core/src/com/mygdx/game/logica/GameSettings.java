package com.mygdx.game.logica;

import com.badlogic.gdx.math.MathUtils;

public final class GameSettings {

    private static final GameSettings INSTANCE = new GameSettings();
    private boolean sonidoActivado = true;
    private float volumenMusica = 0.6f;
    private float volumenEfectos = 1f;
    private GameSettings() { }
    public static GameSettings getInstance() {return INSTANCE;}
    public float getVolumenMusicaEfectivo() {return sonidoActivado ? volumenMusica : 0f;}
    public float getVolumenEfectosEfectivo() {return sonidoActivado ? volumenEfectos : 0f;}
    public boolean isSoundEnabled() { return sonidoActivado; }
    public void setSoundEnabled(boolean activado) { this.sonidoActivado = activado; }
    public float getVolumenMusica() { return volumenMusica; }
    public float getVolumenEfectos() { return volumenEfectos; }
    public void setVolumenMusica(float v) { volumenMusica = MathUtils.clamp(v, 0f, 1f); }
    public void setVolumenEfectos(float v) { volumenEfectos = MathUtils.clamp(v, 0f, 1f); }
}