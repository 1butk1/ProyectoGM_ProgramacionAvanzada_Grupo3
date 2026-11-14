package com.mygdx.game.template;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;


public interface Visible {
    boolean isActive();
    Rectangle getBounds();
    void colorVision(Color destino);
}
