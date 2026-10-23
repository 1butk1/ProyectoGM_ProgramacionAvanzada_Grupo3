package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class Lluvia {
	 
    private static final float SCREEN_WIDTH = 800f;
    private static final float SCREEN_HEIGHT = 480f;
    private static final float DROP_SIZE = 64f;
 
    private Array<FallingObject> drops;
    private float spawnTimer;
    private float spawnInterval;
 
    private LevelConfig currentConfig;
    private GameManager lastManager;
 
    private Texture waterTexture;
    private Texture toxicTexture;
    private Texture shieldTexture;
    private Texture goldenTexture;
    private Texture blackTexture;
 
    private Sound dropSound;
    private Music rainMusic;
 
    /**
     * Las texturas drop.png y dropBad.png son las originales del proyecto.
     * Las otras tres deben agregarse a assets/ (puedes copiar drop.png y teñirla).
     */
    public Lluvia(Sound dropSound, Music rainMusic) {
        this.dropSound = dropSound;
        this.rainMusic = rainMusic;
 
        drops = new Array<FallingObject>();
        spawnTimer = 0f;
        spawnInterval = 1f;
 
        waterTexture = new Texture("drop.png");
        toxicTexture = new Texture("dropBad.png");
        shieldTexture = new Texture("dropShield.png");
        goldenTexture = new Texture("dropGolden.png");
        blackTexture = new Texture("dropBlack.png");
    }
 
    /** Inicia la musica de fondo (llamar en show() de GameScreen). */
    public void startMusic() {
        rainMusic.setLooping(true);
        rainMusic.play();
    }
 
    public void stopMusic() {
        rainMusic.stop();
    }
 
    /** GameScreen la llama cuando cambia de nivel (con LevelManager.getCurrentConfig()). */
    public void setLevelConfig(LevelConfig config) {
        this.currentConfig = config;
        this.spawnInterval = config.getSpawnInterval();
    }
 
    public void update(float delta, Tarro tarro, GameManager manager) {
        this.lastManager = manager;
        if (currentConfig == null) return;
 
        // Generacion
        spawnTimer += delta;
        if (spawnTimer >= spawnInterval) {
            spawnTimer = 0f;
            spawnDrop(currentConfig);
        }
 
        // Actualizacion y colisiones (recorrido inverso para poder eliminar)
        for (int i = drops.size - 1; i >= 0; i--) {
            FallingObject drop = drops.get(i);
            drop.update(delta);
 
            if (drop.isActive() && drop.getBounds().overlaps(tarro.getBounds())) {
                ((DropEffect) drop).applyEffect(tarro, manager);
                if (drop instanceof WaterDrop || drop instanceof GoldenDrop) {
                    dropSound.play();
                }
                drop.deactivate();
            }
 
            if (!drop.isActive()) {
                drops.removeIndex(i);
            }
        }
    }
 
    public void spawnDrop(LevelConfig config) {
        float x = MathUtils.random(0f, SCREEN_WIDTH - DROP_SIZE);
        float y = SCREEN_HEIGHT;
        float speed = config.getDropSpeed();
 
        float roll = MathUtils.random(100f);
        FallingObject drop;
 
        if (roll < 50f) {
            drop = new WaterDrop(x, y, speed, waterTexture);
        } else if (roll < 75f) {
            drop = new ToxicDrop(x, y, speed, toxicTexture);
        } else if (roll < 83f) {
            drop = new ShieldDrop(x, y, speed, shieldTexture);
        } else if (roll < 91f) {
            drop = new BlackDrop(x, y, speed, blackTexture);
        } else if (lastManager != null && lastManager.canSpawnGoldenDrop()) {
            drop = new GoldenDrop(x, y, speed, goldenTexture);
        } else {
            drop = new WaterDrop(x, y, speed, waterTexture);
        }
 
        drops.add(drop);
    }
 
    public void render(SpriteBatch batch) {
        for (FallingObject drop : drops) {
            drop.draw(batch);
        }
    }
 
    /** Elimina todas las gotas (util al reiniciar la partida). */
    public void clear() {
        for (FallingObject drop : drops) {
            drop.dispose();
        }
        drops.clear();
        spawnTimer = 0f;
    }
 
    public void dispose() {
        clear();
        waterTexture.dispose();
        toxicTexture.dispose();
        shieldTexture.dispose();
        goldenTexture.dispose();
        blackTexture.dispose();
        dropSound.dispose();
        rainMusic.dispose();
    }
}
 
