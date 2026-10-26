package com.mygdx.game.template;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.logica.*;
import com.mygdx.game.personaje.*;

public class CaidaRadioactiva extends CaidaObject implements EfectoCaida {

    private static final float ALTURA_SUELO = 24f;
    private boolean aterrizada = false;

    public CaidaRadioactiva(float x, float y, float velocidad, Texture textura) {
        super(x, y, 48f, 48f, velocidad, textura);
    }

    @Override
    protected void updateSpecialBehavior(float delta) {
        if (!aterrizada && area.y <= ALTURA_SUELO) {
            area.y = ALTURA_SUELO;
            aterrizada = true;
        }
    }

    public boolean isAterrizada() {
        return aterrizada;
    }

    @Override
    public void applyEffect(Tarro tarro, GameManager gestor) {
        tarro.receiveDamage();
        gestor.registerContaminatedHit();
    }
}
