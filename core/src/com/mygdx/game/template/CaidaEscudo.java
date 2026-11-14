package com.mygdx.game.template;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.logica.*;
import com.mygdx.game.personaje.*;

public class CaidaEscudo extends CaidaObject implements EfectoCaida {
	public CaidaEscudo(float x, float y, float velocidad, Texture textura) {super(x, y, 48f, 48f, velocidad, textura);}
	@Override
	public void colorVision(Color destino) {
	    destino.set(0.4f, 0.9f, 1f, 0.45f);   
	}
    @Override
    public void applyEffect(Tarro tarro, GameManager gestor) {tarro.activateShield();}
}
	 