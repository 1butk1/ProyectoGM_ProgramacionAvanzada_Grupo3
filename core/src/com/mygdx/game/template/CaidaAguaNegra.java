package com.mygdx.game.template;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.logica.GameManager;
import com.mygdx.game.personaje.Tarro;
 
public class CaidaAguaNegra extends CaidaObject {
 
    private static final float DURACION_LENTITUD = 3f;
    private static final float AMPLITUD_VAIVEN = 40f;
    private final Texture texturaCharco;
    private float tiempo = 0f;
 
    public CaidaAguaNegra(float x, float y, float velocidad, Texture textura, Texture texturaCharco) {
        super(x, y, 48f, 48f, velocidad, textura);
        this.texturaCharco = texturaCharco;
    }
    @Override
    protected void updateSpecialBehavior(float delta) {
        tiempo += delta;
        desplazar((float) Math.sin(tiempo * 4f) * AMPLITUD_VAIVEN * delta, 0f);
        mantenerEnPantalla();
    }
    @Override
    protected boolean debeAterrizar() {return true;}
    @Override
    protected void alAterrizar() {generarMarca(new CharcoNegro(centroX(), texturaCharco));}
    @Override
    public void colorVision(Color destino) {destino.set(0.60f, 0.40f, 0.90f, 0.25f);}
    @Override
    public void applyEffect(Tarro tarro, GameManager gestor) {tarro.applySlow(DURACION_LENTITUD);}
}
 