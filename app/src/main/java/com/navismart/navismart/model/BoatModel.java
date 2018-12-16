package com.navismart.navismart.model;

public class BoatModel {

    private String name = "default";
    private String id = "default";
    private float length = 0;
    private float beam = 0;
    private String type = "default";

    public BoatModel() {
    }

    public BoatModel(String name, String id, float length, float beam, String type) {
        this.name = name;
        this.id = id;
        this.length = length;
        this.beam = beam;
        this.type = type;
    }

    public float getBeam() {
        return beam;
    }

    public void setBeam(float beam) {
        this.beam = beam;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
