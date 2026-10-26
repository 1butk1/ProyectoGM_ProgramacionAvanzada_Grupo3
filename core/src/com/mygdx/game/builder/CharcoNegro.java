package com.mygdx.game.builder;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.personaje.Tarro;

/*
 Charco de petroleo que deja una gota negra al caer al suelo.
  Mientras pisas el charco vas mas lento (se pega un instante al salir).
 */
public class CharcoNegro extends MarcaSuelo {

    private static final float ANCHO = 76f;
    private static final float ALTO = 20f;
    private static final float Y = 14f;
    private static final float DURACION = 6f;        
    private static final float SALPICADURA = 0.35f;  
    private static final float SECADO = 1.0f;       
    private static final float PEGAJOSO = 0.25f;     

    public CharcoNegro(float centroX, Texture textura) {
        super(centroX, ANCHO, ALTO, Y, DURACION, textura);
    }

    @Override
    protected void aplicarEfecto(float delta, Tarro tarro) {
        if (pisando(tarro)) {
            tarro.applySlow(PEGAJOSO);
        }
    }

    @Override
    protected float getAlfa() {
        float brillo = 0.92f + 0.08f * MathUtils.sin(tiempo * 4f);
        return Math.min(1f, (DURACION - tiempo) / SECADO) * brillo;
    }

    @Override
    protected float getEscala() {
        return tiempo >= SALPICADURA ? 1f : 0.4f + 0.6f * (tiempo / SALPICADURA);
    }
}
