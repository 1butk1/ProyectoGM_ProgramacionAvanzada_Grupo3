package com.mygdx.game.template;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.logica.*;
import com.mygdx.game.personaje.*;

public class CaidaEscudo extends CaidaObject implements EfectoCaida {
	public CaidaEscudo(float x, float y, float velocidad, Texture textura) {
        super(x, y, 48f, 48f, velocidad, textura);
    }
 
    @Override
    public void applyEffect(Tarro tarro, GameManager gestor) {
        tarro.activateShield();
    }
}
	 