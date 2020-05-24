package com.space.service;

import com.space.exception.BadRequestException;
import com.space.model.Ship;
import com.space.repository.ShipRepository;
import com.space.request.ShipRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Objects;

@Service
public class ShipService {

    private ShipRepository shipRepository;

    @Autowired
    public ShipService(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    public Ship createShip(@NonNull ShipRequest request) {
        Ship ship = new Ship();
        if(Objects.nonNull(request.getName()) && !request.getName().trim().isEmpty() && request.getName().length()<=50) {
            ship.setName(request.getName());
        } else {
            throw new BadRequestException();
        }
        if(!request.getPlanet().trim().isEmpty() && request.getPlanet().length()<=50) {
            ship.setPlanet(request.getPlanet());
        } else {
            throw new BadRequestException();
        }
        if(request.getProdDate()>=0) {
            Date date = new Date(request.getProdDate());
            if (date.getYear()+1900 >= 2800 && date.getYear()+1900 <= 3019) {
                ship.setProdDate(date);
            } else {
                throw  new BadRequestException();
            }
        } else {
            throw new BadRequestException();
        }
        if(request.getUsed() == null || request.getUsed() == false) {
            ship.setUsed(false);
        } else {
            ship.setUsed(true);
        }
        if(request.getSpeed() != null && request.getSpeed()>0) {
            double speedValue = round(request.getSpeed());
            if (speedValue >= 0.01d && speedValue <= 0.99d) {
                ship.setSpeed(speedValue);
            } else {
                throw new BadRequestException();
            }
        } else throw new BadRequestException();

        if(request.getCrewSize()>0 && request.getCrewSize()<10000) {
            ship.setCrewSize(request.getCrewSize());
        }
        else {
            throw new BadRequestException();
        }
        Double rating = round(countRating(ship));
        ship.setRating(rating);
        ship.setShipType(request.getShipType());
        return shipRepository.save(ship);
    }

    private double round(double value) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private double countRating(Ship ship){
        double k = 1d;
        if(ship.getUsed())
            k = 0.5d;
        double v = ship.getSpeed();
        double year = ship.getProdDate().getYear()+1900;
        return (80*v*k)/(3019-year+1);
    }
}
