package net.dinglezz.torgrays_trials.entity;

public class Effect {
    public final String name;
    public final int duration;
    public final Image image;
    public int time;

    public Effect(String name, int duration, Image image) {
        this.name = name;
        this.duration = duration;
        this.image = image;
        time = duration;
    }
}
