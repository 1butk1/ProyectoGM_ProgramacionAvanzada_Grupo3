package com.mygdx.game.template;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.logica.GameManager;
import com.mygdx.game.personaje.Tarro;
 
public class CaidaGotaDorada extends CaidaObject {
 
    private static final int PUNTOS = 50;
    private static final float MULTIPLICADOR_VELOCIDAD = 1.5f;
    public CaidaGotaDorada(float x, float y, float velocidad, Texture textura) {super(x, y, 48f, 48f, velocidad, textura);}
    @Override
    protected void updateMovement(float delta) {desplazar(0f, -getVelocidad() * MULTIPLICADOR_VELOCIDAD * delta);}
    @Override
    public boolean requiereBalde() {return true;}
    @Override
    public boolean reproduceSonidoAlAtrapar() {return true; }
    @Override
    public void colorVision(Color destino) {destino.set(1.00f, 0.80f, 0.30f, 0.60f);}
    @Override
    public void applyEffect(Tarro tarro, GameManager gestor) {
        gestor.ganarVida();        
        gestor.addScore(PUNTOS);
    }
}
 
