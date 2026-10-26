package com.mygdx.game.template;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.logica.*;
import com.mygdx.game.personaje.*;

public class CaidaGotaDorada extends CaidaObject implements EfectoCaida {
	  	private static final int PUNTOS = 50;
	    private static final float MULTIPLICADOR_VELOCIDAD = 1.5f;
	 
	    public CaidaGotaDorada(float x, float y, float velocidad, Texture textura) {
	        super(x, y, 48f, 48f, velocidad, textura);
	    }
	 
	    @Override
	    protected void updateMovement(float delta) {
	        area.y -= velocidad * MULTIPLICADOR_VELOCIDAD * delta;
	    }
	 
	    @Override
	    public void applyEffect(Tarro tarro, GameManager gestor) {
	    	tarro.addLife();
	        gestor.addScore(PUNTOS);
	    }
	}