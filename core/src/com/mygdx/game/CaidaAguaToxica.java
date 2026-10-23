package com.mygdx.game;
import com.badlogic.gdx.graphics.Texture;

public class CaidaAguaToxica extends CaidaObject implements EfectoCaida {
	 public CaidaAguaToxica(float x, float y, float velocidad, Texture textura) {
	        super(x, y, 64f, 64f, velocidad, textura);
	    }
	 
	    @Override
	    public void applyEffect(Tarro tarro, GameManager gestor) {
	        tarro.receiveDamage();
	        gestor.registerContaminatedHit();
	    }
	}
}
