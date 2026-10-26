package com.mygdx.game.strategy;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class ControlMouse implements EstrategiaControles {
	   @Override
	    public void movimiento(Rectangle area, float velocidad, float delta) {
	        float mouseX = Gdx.input.getX() * 800f / Gdx.graphics.getWidth();
	        area.x = mouseX - area.width / 2f;
	    }
	}