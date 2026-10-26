package com.mygdx.game.ui;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameHud{
    private final SpriteBatch lote;
    private final BitmapFont fuente;

    public GameHud(SpriteBatch lote) {
        this.lote = lote;
        this.fuente = new BitmapFont();
        this.fuente.getData().setScale(1.5f);
    }

    public void render(int puntaje, int vidas, int nivel, boolean escudo) {
        float alto = Gdx.graphics.getHeight();
        float ancho = Gdx.graphics.getWidth();

        fuente.draw(lote, "Puntos: " + puntaje, 10, alto - 10);
        fuente.draw(lote, "Vidas: " + vidas, 10, alto - 40);
        fuente.draw(lote, "Nivel: " + nivel, ancho - 150, alto - 10);
        if (escudo) {
            fuente.draw(lote, "ESCUDO", ancho - 150, alto - 40);
        }
        
    }

    public void dispose() {
        fuente.dispose();
    }
}