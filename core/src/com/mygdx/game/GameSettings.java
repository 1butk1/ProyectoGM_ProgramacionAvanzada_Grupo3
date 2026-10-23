package com.mygdx.game;

public class GameSettings {
    private static final GameSettings INSTANCE = new GameSettings();

    private EstrategiaControles modoControl;
    private boolean sonidoActivado;

    private GameSettings() {
        sonidoActivado = true;
        
    }

    public static GameSettings getInstance() {
        return INSTANCE;
    }

    public EstrategiaControles getControlMode() {
        return modoControl;
    }

    public void SetControlMode(EstrategiaControles modo) {
        this.modoControl = modo;
    }

    public Boolean isSoundEnable() {
        return sonidoActivado;
    }

    public void SetSoundEnable(Boolean activado) {
        this.sonidoActivado = activado;
    }
}