package com.mygdx.game.personaje;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.logica.GameManager;
import com.mygdx.game.strategy.EstrategiaControles;
import com.mygdx.game.logica.GameSettings;
public class Tarro {

    private static final float ANCHO_PANTALLA = 800f;
    private static final float VELOCIDAD_BASE = 400f;
    private static final float FACTOR_LENTITUD = 0.4f;
    private static final int FRAMES_HERIDO = 50;
    private static final float TIEMPO_LIMITE_BALDE = 6f;
    private static final float TIEMPO_BLOQUEO = 0.5f;
    private static final float FACTOR_SIN_BALDE = 1.2f;
    private static final float GRAVEDAD = 1400f;
    private static final float FRICCION = 450f;
    private static final float ANCHO_BALDE = 40f;
    private static final float ALTO_BALDE = 28f;
    private static final float SUELO_BALDE = 22f;
    private static final float SUELO_Y = 20f;
    private static final float VEL_SALTO = 620f;
    private static final float GRAVEDAD_SALTO = 1500f;
    private final GameManager gestor;   
    private boolean saltoHabilitado = false;
    private float velY = 0f;
    private boolean enSuelo = true;
    private final Rectangle area;
    private float velocidadMovimiento;
    private boolean escudoActivo;
    private float temporizadorLentitud;
    private EstrategiaControles estrategiaControl;
    private final Texture textura;            
    private final Texture texturaSinBalde;    
    private final Texture texturaBaldeCaido;  
    private final Sound sonidoHerido;
    private boolean herido;
    private int temporizadorHerido;
    private boolean tieneBalde;
    private final Rectangle areaBalde;
    private float baldeVx;
    private float baldeVy;
    private float tiempoSinBalde;

    public Tarro(Texture textura, Texture texturaSinBalde, Texture texturaBaldeCaido,
                 EstrategiaControles estrategiaControl, Sound sonidoHerido, GameManager gestor) {
        this.textura = textura;
        this.texturaSinBalde = texturaSinBalde;
        this.texturaBaldeCaido = texturaBaldeCaido;
        this.estrategiaControl = estrategiaControl;
        this.sonidoHerido = sonidoHerido;
        this.gestor = gestor;
        this.area = new Rectangle(ANCHO_PANTALLA / 2f - 32f, SUELO_Y, 64f, 64f);
        this.areaBalde = new Rectangle(0, SUELO_BALDE, ANCHO_BALDE, ALTO_BALDE);
        this.velocidadMovimiento = VELOCIDAD_BASE;
        this.escudoActivo = false;
        this.temporizadorLentitud = 0f;
        this.herido = false;
        this.tieneBalde = true;
    }

   
    public void updateMovement(float delta) {
        if (!tieneBalde) actualizarBaldeCaido(delta);
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
        actualizarSalto(delta);
        if (!tieneBalde) actualizarRecuperacion(delta);
    }

   

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
            baldeVy = (Math.abs(baldeVy) > 150f) ? -baldeVy * 0.35f : 0f; 
        }

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
            tieneBalde = true;              
            return;
        }

        if (tiempoSinBalde >= TIEMPO_LIMITE_BALDE) {
            
            tieneBalde = true;
            gestor.perderVida();
            iniciarAnimacionHerido();
        }
    }

    private void iniciarAnimacionHerido() {
        herido = true;
        temporizadorHerido = FRAMES_HERIDO;
        sonidoHerido.play(GameSettings.getInstance().getVolumenEfectosEfectivo());
    }

    public void setControlStrategy(EstrategiaControles estrategia) {
        this.estrategiaControl = estrategia;
    }
        public void receiveDamage() {
        if (herido) return;	
        if (escudoActivo) {
            escudoActivo = false;
            return;
        }
        gestor.perderVida();
        iniciarAnimacionHerido();
    }

    public void golpeDeBuitre() {
        if (!tieneBalde) return;          
        if (escudoActivo) {
            escudoActivo = false;
            return;
        }
        iniciarAnimacionHerido();
        soltarBalde();
    }

    public boolean addLife() {
        return gestor.ganarVida();
    }

    public void setSaltoHabilitado(boolean v) {
        saltoHabilitado = v;
        if (!v) { area.y = SUELO_Y; velY = 0f; enSuelo = true; }
    }

    public boolean isOnGround() { return area.y <= SUELO_Y + 10f; }

    private void actualizarSalto(float delta) {
        if (!saltoHabilitado) return;
        
        if (enSuelo && estrategiaControl.quiereSaltar()) {
            velY = VEL_SALTO;
            enSuelo = false;
        }
        if (!enSuelo) {
            velY -= GRAVEDAD_SALTO * delta;
            area.y += velY * delta;
            if (area.y <= SUELO_Y) {
                area.y = SUELO_Y;
                velY = 0f;
                enSuelo = true;
            }
        }
    }

    public void activateShield() { escudoActivo = true; }
    public void applySlow(float duracion) { temporizadorLentitud = Math.max(temporizadorLentitud, duracion); }
    public Rectangle getBounds() { return new Rectangle(area); }
    public Rectangle getBucketBounds() { return new Rectangle(areaBalde); }
    public boolean hasShield() { return escudoActivo; }
    public boolean isHurt() { return herido; }
    public boolean hasBucket() { return tieneBalde; }

    public float getBucketTimeLeft() {
        return tieneBalde ? 0f : Math.max(0f, TIEMPO_LIMITE_BALDE - tiempoSinBalde);
    }

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

        float x = area.x;
        float y = area.y;
        if (herido) {
            float progreso = temporizadorHerido / (float) FRAMES_HERIDO;
            float intensidad = 6f * progreso;
            x += MathUtils.random(-intensidad, intensidad);
            y += MathUtils.random(-intensidad, intensidad);
            if ((temporizadorHerido / 5) % 2 == 0) {
                batch.setColor(1f, 0.25f, 0.25f, 1f);
            }

            temporizadorHerido--;
            if (temporizadorHerido <= 0) herido = false;
        }
        Texture personaje = tieneBalde ? textura : texturaSinBalde;
        batch.draw(personaje, x, y, area.width, area.height);
        batch.setColor(1f, 1f, 1f, 1f);
    }

    public void dispose() {
        textura.dispose();
        texturaSinBalde.dispose();
        texturaBaldeCaido.dispose();
        sonidoHerido.dispose();
    }
}