
package com.game.fish.model;

import jakarta.persistence.*;

@Entity
public class Fish {

    @Version
    private int version;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(length = 1024)
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public Double getsWeight() {
        return sWeight;
    }

    public void setsWeight(Double sWeight) {
        this.sWeight = sWeight;
    }

    public Double getaWeight() {
        return aWeight;
    }

    public void setaWeight(Double aWeight) {
        this.aWeight = aWeight;
    }

    public Double getbWeight() {
        return bWeight;
    }

    public void setbWeight(Double bWeight) {
        this.bWeight = bWeight;
    }

    public Double getcWeight() {
        return cWeight;
    }

    public void setcWeight(Double cWeight) {
        this.cWeight = cWeight;
    }

    public Double getMean() {
        return mean;
    }

    public void setMean(Double mean) {
        this.mean = mean;
    }

    public Double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(Double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Column(nullable = false)
    private double probability;

    @Column
    private Double sWeight;

    @Column
    private Double aWeight;

    @Column
    private Double bWeight;

    @Column
    private Double cWeight;

    @Column(nullable = false)
    private double mean;

    @Column(nullable = false)
    private double standardDeviation;

    @Column(nullable = false)
    private boolean status = true;

    // Getters and Setters
}
