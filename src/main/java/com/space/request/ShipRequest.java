package com.space.request;

import com.space.model.ShipType;
import org.springframework.lang.NonNull;

import java.util.Date;

public class ShipRequest {

    @NonNull
    private String name;
    @NonNull
    private String planet;
    @NonNull
    private ShipType shipType;
    @NonNull
    private Long prodDate;
    private Boolean isUsed;
    @NonNull
    private Double speed;
    @NonNull
    private Integer crewSize;

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getPlanet() {
        return planet;
    }

    public void setPlanet(@NonNull String planet) {
        this.planet = planet;
    }

    @NonNull
    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(@NonNull ShipType shipType) {
        this.shipType = shipType;
    }

    @NonNull
    public Long getProdDate() {
        return prodDate;
    }

    public void setProdDate(@NonNull Long prodDate) {
        this.prodDate = prodDate;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    @NonNull
    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(@NonNull Double speed) {
        this.speed = speed;
    }

    @NonNull
    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(@NonNull Integer crewSize) {
        this.crewSize = crewSize;
    }
}
