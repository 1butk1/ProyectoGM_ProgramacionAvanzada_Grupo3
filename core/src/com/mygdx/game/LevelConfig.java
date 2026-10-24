package com.mygdx.game;

public class LevelConfig {
    private final String nombre;
    private final String rutaFondo;
    private final float velocidadGotas;
    private final float intervaloAparicion;
    private final int puntajeRequerido;
    private final int umbralGotaDorada;

    private LevelConfig(Builder constructor) {
        this.nombre = constructor.nombre;
        this.rutaFondo = constructor.rutaFondo;
        this.velocidadGotas = constructor.velocidadGotas;
        this.intervaloAparicion = constructor.intervaloAparicion;
        this.puntajeRequerido = constructor.puntajeRequerido;
        this.umbralGotaDorada = constructor.umbralGotaDorada;
    }

    public String getName() { return nombre; }
    public String getBackgroundPath() { return rutaFondo; }
    public float getDropSpeed() { return velocidadGotas; }
    public float getSpawnInterval() { return intervaloAparicion; }
    public int getScoreRequired() { return puntajeRequerido; }
    public int getGoldenDropThreshold() { return umbralGotaDorada; }

    public static class Builder {
        private String nombre;
        private String rutaFondo;
        private float velocidadGotas;
        private float intervaloAparicion;
        private int puntajeRequerido;
        private int umbralGotaDorada;

        public Builder setName(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder setBackgroundPath(String ruta) {
            this.rutaFondo = ruta;
            return this;
        }

        public Builder setDropSpeed(float velocidad) {
            this.velocidadGotas = velocidad;
            return this;
        }

        public Builder setSpawnInterval(float intervalo) {
            this.intervaloAparicion = intervalo;
            return this;
        }

        public Builder setScoreRequired(int puntaje) {
            this.puntajeRequerido = puntaje;
            return this;
        }

        public Builder setGoldenDropThreshold(int umbral) {
            this.umbralGotaDorada = umbral;
            return this;
        }

        public LevelConfig build() {
            return new LevelConfig(this);
        }
    }
}