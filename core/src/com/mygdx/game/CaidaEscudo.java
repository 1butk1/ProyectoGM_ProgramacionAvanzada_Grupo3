package com.mygdx.game;
import com.badlogic.gdx.graphics.Texture;

public class CaidaEscudo extends CaidaObject implements EfectoCaida {
	 public CaidaEscudo(float x, float y, float velocidad, Texture textura) {
	        super(x, y, 64f, 64f, velocidad, textura);
	    }
	 
	    @Override
	    public void applyEffect(Tarro tarro, GameManager gestor) {
	        tarro.activateShield();
	    }
	}
	 