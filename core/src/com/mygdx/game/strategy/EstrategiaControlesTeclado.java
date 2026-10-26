package com.mygdx.game.strategy;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

public class EstrategiaControlesTeclado implements EstrategiaControles {

	@Override
    public void movimiento(Rectangle area, float velocidad, float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            area.x -= velocidad * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            area.x += velocidad * delta;
        }
    }
}

