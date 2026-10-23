package com.mygdx.game;
import com.badlogic.gdx.graphics.Texture;

public class CaidaGotaDorada extends CaidaObject implements EfectoCaida {
	  	private static final int PUNTOS = 50;
	    private static final float MULTIPLICADOR_VELOCIDAD = 1.5f;
	 
	    public CaidaGotaDorada(float x, float y, float velocidad, Texture textura) {
	        super(x, y, 64f, 64f, velocidad, textura);
	    }
	 
	    /** Polimorfismo: la gota dorada cae mas rapido que las demas. */
	    @Override
	    protected void updateMovement(float delta) {
	        area.y -= velocidad * MULTIPLICADOR_VELOCIDAD * delta;
	    }
	 
	    @Override
	    public void applyEffect(Tarro tarro, GameManager gestor) {
	        gestor.addScore(PUNTOS);
	    }
	}