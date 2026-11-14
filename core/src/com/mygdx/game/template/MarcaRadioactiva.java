package com.mygdx.game.template;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.personaje.Tarro;

public class MarcaRadioactiva extends MarcaSuelo {

    private static final float ANCHO = 56f;
    private static final float ALTO = 18f;
    private static final float Y = 16f;
    private static final float AVISO = 0.6f;        
    private static final float DURACION = 3.5f;     
    private static final float DESVANECER = 0.6f;   
    private boolean danoAplicado = false;

    public MarcaRadioactiva(float centroX, Texture textura) { super(centroX, ANCHO, ALTO, Y, DURACION, textura);}
    @Override
    protected void aplicarEfecto(float delta, Tarro tarro) {
        if (danoAplicado || getTiempo() < AVISO) return;
        if (pisando(tarro)) {
            tarro.receiveDamage();
            danoAplicado = true;
        }
    }

    @Override
    protected float getAlfa() {
        float t = getTiempo();
        if (t < AVISO) {
            return ((int) (t * 14f) % 2 == 0) ? 0.9f : 0.25f;
        }
        float alfa = 0.85f + 0.15f * MathUtils.sin(t * 10f);
        alfa = Math.min(alfa, (DURACION - t) / DESVANECER);
        return danoAplicado ? alfa * 0.4f : alfa;
    }
}