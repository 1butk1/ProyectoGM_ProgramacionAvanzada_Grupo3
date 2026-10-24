package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

/**
 * Strategy: define como se mueve el balde.
 * Permite cambiar los controles sin modificar la clase Tarro.
 */
public interface ControlStrategy {
    /**
     * Mueve el rectangulo del balde.
     * @param bounds area del balde (se modifica su x)
     * @param speed  velocidad efectiva en pixeles por segundo (ya considera ralentizacion)
     * @param delta  tiempo transcurrido desde el ultimo frame
     */
    void move(Rectangle bounds, float speed, float delta);
}
