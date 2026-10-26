package com.mygdx.game.builder;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.personaje.Tarro;
 
/*
 Charco radiactivo que deja una gota al caer al suelo.P
 Despues quita una vida UNA sola vez a quien este encima (respeta el escudo).
 */
public class MarcaRadioactiva extends MarcaSuelo {
 
    private static final float ANCHO = 56f;
    private static final float ALTO = 18f;
    private static final float Y = 16f;
    private static final float AVISO = 0.6f;        // segundos de parpadeo sin dano
    private static final float DURACION = 3.5f;     // vida total de la marca
    private static final float DESVANECER = 0.6f;   // fundido final
 
    private boolean danoAplicado = false;
 
    public MarcaRadioactiva(float centroX, Texture textura) {
        super(centroX, ANCHO, ALTO, Y, DURACION, textura);
    }
 
    @Override
    protected void aplicarEfecto(float delta, Tarro tarro) {
        if (danoAplicado || tiempo < AVISO) return;
        if (pisando(tarro)) {
            tarro.receiveDamage();
            danoAplicado = true;
        }
    }
 
    @Override
    protected float getAlfa() {
        if (tiempo < AVISO) {
            return ((int) (tiempo * 14f) % 2 == 0) ? 0.9f : 0.25f;          
        }
        float alfa = 0.85f + 0.15f * MathUtils.sin(tiempo * 10f);            
        alfa = Math.min(alfa, (DURACION - tiempo) / DESVANECER);             
        return danoAplicado ? alfa * 0.4f : alfa;                            
    }
}
 