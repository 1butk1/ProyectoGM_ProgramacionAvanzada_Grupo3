package com.mygdx.game.template;
import com.mygdx.game.logica.*;
import com.mygdx.game.personaje.*;
import com.badlogic.gdx.graphics.Texture;

public class CaidaAguaToxica extends CaidaObject implements EfectoCaida {
	 public CaidaAguaToxica(float x, float y, float velocidad, Texture textura) {
	        super(x, y, 48f, 48f, velocidad, textura);
	    }
	 
	    @Override
	    public void applyEffect(Tarro tarro, GameManager gestor) {
	        tarro.receiveDamage();
	        gestor.registerContaminatedHit();
	    }
	}

