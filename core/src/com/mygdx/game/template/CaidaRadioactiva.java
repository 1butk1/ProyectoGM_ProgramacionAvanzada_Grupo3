package com.mygdx.game.template;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.logica.GameManager;
import com.mygdx.game.personaje.Tarro;
 
public class CaidaRadioactiva extends CaidaObject {
 
    private final Texture texturaMarca;

    public CaidaRadioactiva(float x, float y, float velocidad, Texture textura, Texture texturaMarca) {
        super(x, y, 48f, 48f, velocidad, textura);
        this.texturaMarca = texturaMarca;
    }
 
    @Override
    protected boolean debeAterrizar() {
        return true;
    }

    @Override
    protected void alAterrizar() {
        generarMarca(new MarcaRadioactiva(centroX(), texturaMarca));
    }
 
    @Override
    public void colorVision(Color destino) {
        destino.set(0.55f, 1.00f, 0.25f, 0.65f);
    }
 
    @Override
    public void applyEffect(Tarro tarro, GameManager gestor) {
        tarro.receiveDamage();
        gestor.registerContaminatedHit();
    }
}
 
 