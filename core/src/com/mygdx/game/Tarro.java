package com.mygdx.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Tarro {

    private static final float ANCHO_PANTALLA = 800f;
    private static final float VELOCIDAD_BASE = 400f;
    private static final float FACTOR_LENTITUD = 0.4f;
    private static final int VIDAS_INICIALES = 3;
    private static final int FRAMES_HERIDO = 50;

    // ---- Mecanica del balde caido (ajusta estos valores para el balance) ----
    private static final float PROBABILIDAD_SOLTAR = 0.5f;   // 0 = nunca, 1 = siempre que lo golpeen
    private static final float TIEMPO_LIMITE_BALDE = 6f;     // segundos para recogerlo
    private static final float TIEMPO_BLOQUEO = 0.5f;        // no se puede recoger justo al soltarlo
    private static final float FACTOR_SIN_BALDE = 1.2f;      // sin el peso, corre un poco mas rapido
    private static final float GRAVEDAD = 1400f;
    private static final float FRICCION = 450f;
    private static final float ANCHO_BALDE = 40f;
    private static final float ALTO_BALDE = 28f;
    private static final float SUELO_BALDE = 22f;

    private Rectangle area;
    private int vidas;
    private float velocidadMovimiento;
    private boolean escudoActivo;
    private float temporizadorLentitud;
    private EstrategiaControles estrategiaControl;
    private Texture textura;            // personaje CON balde
    private Texture texturaSinBalde;    // personaje sin balde
    private Texture texturaBaldeCaido;  // balde tirado en el suelo
    private Sound sonidoHerido;

    // animacion de herido
    private boolean herido;
    private int temporizadorHerido;

    // estado del balde
    private boolean tieneBalde;
    private Rectangle areaBalde;
    private float baldeVx;
    private float baldeVy;
    private float tiempoSinBalde;

    public Tarro(Texture textura, Texture texturaSinBalde, Texture texturaBaldeCaido,
                 EstrategiaControles estrategiaControl, Sound sonidoHerido) {
        this.textura = textura;
        this.texturaSinBalde = texturaSinBalde;
        this.texturaBaldeCaido = texturaBaldeCaido;
        this.estrategiaControl = estrategiaControl;
        this.sonidoHerido = sonidoHerido;
        this.area = new Rectangle(ANCHO_PANTALLA / 2f - 32f, 20f, 64f, 64f);
        this.areaBalde = new Rectangle(0, SUELO_BALDE, ANCHO_BALDE, ALTO_BALDE);
        this.vidas = VIDAS_INICIALES;
        this.velocidadMovimiento = VELOCIDAD_BASE;
        this.escudoActivo = false;
        this.temporizadorLentitud = 0f;
        this.herido = false;
        this.tieneBalde = true;
    }

    /** Actualiza movimiento, temporizadores y la fisica del balde. */
    public void updateMovement(float delta) {
        // El balde sigue cayendo/rodando aunque el jugador este aturdido
        if (!tieneBalde) actualizarBaldeCaido(delta);

        // Durante la animacion de herido el jugador no se mueve
        if (herido) return;

        float velocidad = velocidadMovimiento;
        if (!tieneBalde) velocidad *= FACTOR_SIN_BALDE;
        if (temporizadorLentitud > 0f) {
            velocidad *= FACTOR_LENTITUD;
            temporizadorLentitud -= delta;
            if (temporizadorLentitud < 0f) temporizadorLentitud = 0f;
        }

        estrategiaControl.movimiento(area, velocidad, delta);

        if (area.x < 0) area.x = 0;
        if (area.x > ANCHO_PANTALLA - area.width) area.x = ANCHO_PANTALLA - area.width;

        if (!tieneBalde) actualizarRecuperacion(delta);
    }

    // ---------------- balde caido ----------------

    private void soltarBalde() {
        tieneBalde = false;
        tiempoSinBalde = 0f;
        areaBalde.setPosition(area.x + 12f, area.y + 64f);
        float direccion = MathUtils.randomBoolean() ? 1f : -1f;
        baldeVx = direccion * MathUtils.random(160f, 280f);
        baldeVy = MathUtils.random(330f, 430f);
    }

    private void actualizarBaldeCaido(float delta) {
        baldeVy -= GRAVEDAD * delta;
        areaBalde.y += baldeVy * delta;

        if (areaBalde.y <= SUELO_BALDE) {
            areaBalde.y = SUELO_BALDE;
            baldeVy = (Math.abs(baldeVy) > 150f) ? -baldeVy * 0.35f : 0f;   // rebote
        }

        // friccion cuando esta en el suelo
        if (areaBalde.y <= SUELO_BALDE + 0.01f && baldeVx != 0f) {
            float reduccion = FRICCION * delta;
            if (Math.abs(baldeVx) <= reduccion) baldeVx = 0f;
            else baldeVx -= Math.signum(baldeVx) * reduccion;
        }

        areaBalde.x += baldeVx * delta;
        if (areaBalde.x < 0) { areaBalde.x = 0; baldeVx = -baldeVx * 0.4f; }
        if (areaBalde.x > ANCHO_PANTALLA - areaBalde.width) {
            areaBalde.x = ANCHO_PANTALLA - areaBalde.width;
            baldeVx = -baldeVx * 0.4f;
        }
    }

    private void actualizarRecuperacion(float delta) {
        tiempoSinBalde += delta;

        if (tiempoSinBalde >= TIEMPO_BLOQUEO && area.overlaps(areaBalde)) {
            tieneBalde = true;              // lo recogio a tiempo
            return;
        }

        if (tiempoSinBalde >= TIEMPO_LIMITE_BALDE) {
            // No llego a tiempo: pierde una vida y recupera un balde nuevo
            tieneBalde = true;
            if (vidas > 0) vidas--;
            herido = true;
            temporizadorHerido = FRAMES_HERIDO;
            sonidoHerido.play();
        }
    }

    // ---------------- API publica ----------------

    public void setControlStrategy(EstrategiaControles estrategia) {
        this.estrategiaControl = estrategia;
    }

    /** Recibe dano: si hay escudo lo consume, si no pierde una vida y puede soltar el balde. */
    public void receiveDamage() {
        if (escudoActivo) {
            escudoActivo = false;
            return;
        }
        if (vidas > 0) vidas--;
        herido = true;
        temporizadorHerido = FRAMES_HERIDO;
        sonidoHerido.play();

        if (vidas > 0 && tieneBalde && MathUtils.randomBoolean(PROBABILIDAD_SOLTAR)) {
            soltarBalde();
        }
    }

    public void activateShield() { escudoActivo = true; }

    public void applySlow(float duracion) { temporizadorLentitud = duracion; }

    public Rectangle getBounds() { return area; }

    public boolean hasShield() { return escudoActivo; }

    public int getLives() { return vidas; }

    public boolean isGameOver() { return vidas <= 0; }

    public boolean isHurt() { return herido; }

    /** Solo puede atrapar gotas mientras lleva el balde. */
    public boolean hasBucket() { return tieneBalde; }

    /** Segundos que le quedan para recoger el balde (0 si lo lleva). */
    public float getBucketTimeLeft() {
        return tieneBalde ? 0f : Math.max(0f, TIEMPO_LIMITE_BALDE - tiempoSinBalde);
    }

    public Rectangle getBucketBounds() { return areaBalde; }

    /** Dibuja el balde caido (parpadea al acabarse el tiempo) y al personaje. */
    public void draw(SpriteBatch batch) {
        if (!tieneBalde) {
            boolean visible = getBucketTimeLeft() > 2f || ((int) (tiempoSinBalde * 8f) % 2 == 0);
            if (visible) {
                batch.draw(texturaBaldeCaido, areaBalde.x, areaBalde.y, areaBalde.width, areaBalde.height);
            }
        }

        if (temporizadorLentitud > 0f) {
            batch.setColor(0.5f, 0.5f, 0.5f, 1f);
        } else if (escudoActivo) {
            batch.setColor(0.4f, 0.8f, 1f, 1f);
        }

        float y = area.y;
        if (herido) {
            y += MathUtils.random(-5, 5);
            temporizadorHerido--;
            if (temporizadorHerido <= 0) herido = false;
        }
        Texture personaje = tieneBalde ? textura : texturaSinBalde;
        batch.draw(personaje, area.x, y, area.width, area.height);
        batch.setColor(1f, 1f, 1f, 1f);
    }

    public void dispose() {
        textura.dispose();
        texturaSinBalde.dispose();
        texturaBaldeCaido.dispose();
        sonidoHerido.dispose();
    }
}