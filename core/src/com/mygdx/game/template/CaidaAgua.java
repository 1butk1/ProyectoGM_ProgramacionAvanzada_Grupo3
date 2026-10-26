package com.mygdx.game.template;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.logica.*;
import com.mygdx.game.personaje.*;
public class CaidaAgua extends CaidaObject implements EfectoCaida {
	private static final int PUNTOS = 10;
	 
    public CaidaAgua(float x, float y, float velocidad, Texture textura) {
        super(x, y, 48f, 48f, velocidad, textura);
    }
 
    @Override
    public void applyEffect(Tarro tarro, GameManager gestor) {
        gestor.addScore(PUNTOS);
    }
}
 