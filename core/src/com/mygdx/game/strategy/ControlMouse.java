package com.mygdx.game.strategy;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class ControlMouse implements EstrategiaControles {
	private static final float ANCHO_PANTALLA = 800f;

    @Override
    public void movimiento(Rectangle area, float velocidad, float delta) {
        float mouseX = Gdx.input.getX() * ANCHO_PANTALLA / Gdx.graphics.getWidth();
        float objetivo = mouseX - area.width / 2f;
        float maxPaso = velocidad * delta;
        float diferencia = objetivo - area.x;
        area.x += MathUtils.clamp(diferencia, -maxPaso, maxPaso);
    }

    @Override
    public boolean quiereSaltar() {
        return Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)
            || Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
    }
    
}