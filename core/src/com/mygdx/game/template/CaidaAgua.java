package com.mygdx.game.template;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.logica.*;
import com.mygdx.game.personaje.*;
public class CaidaAgua extends CaidaObject implements EfectoCaida {
	private static final int PUNTOS = 10;
    public CaidaAgua(float x, float y, float velocidad, Texture textura) {super(x, y, 48f, 48f, velocidad, textura);}
    @Override
    public void colorVision(Color destino) {
        destino.set(0.3f, 0.6f, 1f, 0.45f);   
    }
    public void applyEffect(Tarro tarro, GameManager gestor) {gestor.addScore(PUNTOS);}
}
 