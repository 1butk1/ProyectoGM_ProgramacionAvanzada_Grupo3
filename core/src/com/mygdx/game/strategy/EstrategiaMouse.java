package com.mygdx.game.strategy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class EstrategiaMouse implements EstrategiaControles {

	private final OrthographicCamera camara;
    private final Vector3 toque = new Vector3();
 
    public EstrategiaMouse(OrthographicCamera camara) {this.camara = camara;}
 
    @Override
    public void movimiento(Rectangle area, float velocidad, float delta) {
        toque.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camara.unproject(toque);
 
        float objetivo = toque.x - area.width / 2f;
        float pasoMaximo = velocidad * delta;
        float diferencia = objetivo - area.x;
 
        if (Math.abs(diferencia) <= pasoMaximo) {
            area.x = objetivo;
        } else {
            area.x += Math.signum(diferencia) * pasoMaximo;
        }
    }
}
