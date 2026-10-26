package com.mygdx.game.builder;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.personaje.Tarro;
import com.mygdx.game.template.CaidaAgua;
import com.mygdx.game.template.CaidaAguaNegra;
import com.mygdx.game.template.CaidaAguaToxica;
import com.mygdx.game.template.CaidaBuitre;
import com.mygdx.game.template.CaidaEscudo;
import com.mygdx.game.template.CaidaGotaDorada;
import com.mygdx.game.template.CaidaObject;
import com.mygdx.game.template.CaidaRadioactiva;


public class VisionLimitada {

    private static final int TAM_MASCARA = 128;     
    private static final float ESCALA = 3.2f;       
    private static final float ALFA_MAX = 0.98f;
    private static final float ANCHO_PANTALLA = 800f;
    private static final float ALTO_PANTALLA = 480f;

    private final Texture mascara;
    private final Texture pixel;
    private final Texture halo;
    private float tiempo = 0f;

    public VisionLimitada() {
        mascara = crearMascara();
        pixel = crearPixel();
        halo = crearHalo();
    }

    private Texture crearMascara() {
        Pixmap p = new Pixmap(TAM_MASCARA, TAM_MASCARA, Pixmap.Format.RGBA8888);
        p.setBlending(Pixmap.Blending.None);
        float c = (TAM_MASCARA - 1) / 2f;
        for (int y = 0; y < TAM_MASCARA; y++) {
            for (int x = 0; x < TAM_MASCARA; x++) {
                float d = (float) Math.sqrt((x - c) * (x - c) + (y - c) * (y - c)) / (TAM_MASCARA / 2f);
                float a = MathUtils.clamp((d - 0.30f) / 0.62f, 0f, 1f);   
                a = a * a * (3f - 2f * a);
                a = Math.round(a * 7f) / 7f;                               
                p.drawPixel(x, y, Color.rgba8888(0f, 0f, 0f, a * ALFA_MAX));
            }
        }
        Texture t = new Texture(p);
        t.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        p.dispose();
        return t;
    }

    private Texture crearPixel() {
        Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        p.setColor(Color.WHITE);
        p.fill();
        Texture t = new Texture(p);
        p.dispose();
        return t;
    }

    private Texture crearHalo() {
        int n = 64;
        Pixmap p = new Pixmap(n, n, Pixmap.Format.RGBA8888);
        p.setBlending(Pixmap.Blending.None);
        float c = (n - 1) / 2f;
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                float d = (float) Math.sqrt((x - c) * (x - c) + (y - c) * (y - c)) / (n / 2f);
                float a = MathUtils.clamp(1f - d, 0f, 1f);
                a = Math.round(a * a * 6f) / 6f;
                p.drawPixel(x, y, Color.rgba8888(1f, 1f, 1f, a));
            }
        }
        Texture t = new Texture(p);
        t.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        p.dispose();
        return t;
    }

    public void dibujar(SpriteBatch batch, Tarro tarro, Array<CaidaObject> gotas, float delta) {
        tiempo += delta;

        Rectangle b = tarro.getBounds();
        float cx = b.x + b.width / 2f;
        float cy = b.y + b.height / 2f + 8f;
        float parpadeo = 1f + 0.025f * MathUtils.sin(tiempo * 8.3f) + 0.015f * MathUtils.sin(tiempo * 21.7f);
        float lado = TAM_MASCARA * ESCALA * parpadeo;
        float x0 = cx - lado / 2f;
        float y0 = cy - lado / 2f;
        float x1 = x0 + lado;
        float y1 = y0 + lado;

        batch.setColor(0f, 0f, 0f, ALFA_MAX);
        if (y0 > 0f) batch.draw(pixel, 0f, 0f, ANCHO_PANTALLA, y0);
        if (y1 < ALTO_PANTALLA) batch.draw(pixel, 0f, y1, ANCHO_PANTALLA, ALTO_PANTALLA - y1);
        if (x0 > 0f) batch.draw(pixel, 0f, y0, x0, lado);
        if (x1 < ANCHO_PANTALLA) batch.draw(pixel, x1, y0, ANCHO_PANTALLA - x1, lado);

        batch.setColor(1f, 1f, 1f, 1f);
        batch.draw(mascara, x0, y0, lado, lado);

        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        for (int i = 0; i < gotas.size; i++) {
            CaidaObject g = gotas.get(i);
            if (!g.isActive() || !aplicarTinte(batch, g)) continue;
            Rectangle r = g.getBounds();
            batch.draw(halo, r.x + r.width / 2f - 64f, r.y + r.height / 2f - 64f, 128f, 128f);
        }
        if (!tarro.hasBucket()) {                                       
            batch.setColor(0.7f, 0.9f, 1f, 0.55f);
            Rectangle r = tarro.getBucketBounds();
            batch.draw(halo, r.x + r.width / 2f - 48f, r.y + r.height / 2f - 48f, 96f, 96f);
        }
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setColor(1f, 1f, 1f, 1f);
    }

    private boolean aplicarTinte(SpriteBatch batch, CaidaObject g) {
        if (g instanceof CaidaAgua)            batch.setColor(0.35f, 0.65f, 1.00f, 0.55f);
        else if (g instanceof CaidaRadioactiva) batch.setColor(0.55f, 1.00f, 0.25f, 0.65f);
        else if (g instanceof CaidaGotaDorada)  batch.setColor(1.00f, 0.80f, 0.30f, 0.60f);
        else if (g instanceof CaidaEscudo)      batch.setColor(0.30f, 0.90f, 0.70f, 0.50f);
        else if (g instanceof CaidaAguaToxica)  batch.setColor(1.00f, 0.25f, 0.20f, 0.30f);
        else if (g instanceof CaidaAguaNegra)   batch.setColor(0.60f, 0.40f, 0.90f, 0.25f);
        else if (g instanceof CaidaBuitre)      batch.setColor(1.00f, 0.30f, 0.20f, 0.35f);
        else return false;
        return true;
    }

    public void dispose() {
        mascara.dispose();
        pixel.dispose();
        halo.dispose();
    }
}