package com.mygdx.game.builder;

public class LevelConfig {
    private final String nombre;
    private final String rutaFondo;
    private final float velocidadGotas;
    private final float intervaloAparicion;
    private final int puntajeRequerido;
    private final boolean saltoHabilitado;

    private LevelConfig(Builder constructor) {
        this.nombre = constructor.nombre;
        this.rutaFondo = constructor.rutaFondo;
        this.velocidadGotas = constructor.velocidadGotas;
        this.intervaloAparicion = constructor.intervaloAparicion;
        this.puntajeRequerido = constructor.puntajeRequerido;
        this.saltoHabilitado = constructor.saltoHabilitado;
    }

    public String getName() { return nombre; }
    public String getBackgroundPath() { return rutaFondo; }
    public float getDropSpeed() { return velocidadGotas; }
    public float getSpawnInterval() { return intervaloAparicion; }
    public int getScoreRequired() { return puntajeRequerido; }
    public boolean isJumpEnabled() { return saltoHabilitado; }

    public static class Builder {
        private String nombre;
        private String rutaFondo;
        private float velocidadGotas;
        private float intervaloAparicion;
        private int puntajeRequerido;
        private boolean saltoHabilitado;

        public Builder setName(String nombre) { this.nombre = nombre; return this; }
        public Builder setBackgroundPath(String ruta) { this.rutaFondo = ruta; return this; }
        public Builder setDropSpeed(float velocidad) { this.velocidadGotas = velocidad; return this; }
        public Builder setSpawnInterval(float intervalo) { this.intervaloAparicion = intervalo; return this; }
        public Builder setScoreRequired(int puntaje) { this.puntajeRequerido = puntaje; return this; }
        public Builder setJumpEnabled(boolean v) { this.saltoHabilitado = v; return this; }

        public LevelConfig build() { return new LevelConfig(this); }
    }
}