package com.mygdx.game.template;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.logica.*;
import com.mygdx.game.personaje.*;

public class CaidaAguaNegra extends CaidaObject implements EfectoCaida {
	private static final float DURACION_LENTITUD = 3f;
    private static final float AMPLITUD_VAIVEN = 40f; 
    private float tiempo = 0f;
 
    public CaidaAguaNegra(float x, float y, float velocidad, Texture textura) {
        super(x, y, 48f, 48f, velocidad, textura);
    }
 
    @Override
    protected void updateSpecialBehavior(float delta) {
        tiempo += delta;
        area.x += (float) Math.sin(tiempo * 4f) * AMPLITUD_VAIVEN * delta;
        if (area.x < 0) area.x = 0;
        if (area.x > 800f - area.width) area.x = 800f - area.width;
    }
 
    @Override
    public void applyEffect(Tarro tarro, GameManager gestor) {tarro.applySlow(DURACION_LENTITUD);}
}
 
