package com.mygdx.game;
import com.badlogic.gdx.graphics.Texture;
public class CaidaAgua extends CaidaObject implements EfectoCaida {
	private static final int PUNTOS = 10;
	 
    public CaidaAgua(float x, float y, float velocidad, Texture textura) {
        super(x, y, 64f, 64f, velocidad, textura);
    }
 
    @Override
    public void applyEffect(Tarro tarro, GameManager gestor) {
        gestor.addScore(PUNTOS);
    }
}
 