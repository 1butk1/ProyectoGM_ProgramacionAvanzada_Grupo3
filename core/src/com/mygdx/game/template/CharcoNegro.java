package com.mygdx.game.template;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.personaje.Tarro;

public class CharcoNegro extends MarcaSuelo {

    private static final float ANCHO = 76f;
    private static final float ALTO = 20f;
    private static final float Y = 14f;
    private static final float DURACION = 6f;
    private static final float SALPICADURA = 0.35f;
    private static final float SECADO = 1.0f;
    private static final float PEGAJOSO = 0.25f;

    public CharcoNegro(float centroX, Texture textura) { super(centroX, ANCHO, ALTO, Y, DURACION, textura);}
    @Override
    protected void aplicarEfecto(float delta, Tarro tarro) {
        if (pisando(tarro)) {
            tarro.applySlow(PEGAJOSO);
        }
    }
    @Override
    protected float getAlfa() {
        float t = getTiempo();
        float brillo = 0.92f + 0.08f * MathUtils.sin(t * 4f);
        return Math.min(1f, (DURACION - t) / SECADO) * brillo;
    }
    @Override
    protected float getEscala() {
        float t = getTiempo();
        return t >= SALPICADURA ? 1f : 0.4f + 0.6f * (t / SALPICADURA);
    }
}