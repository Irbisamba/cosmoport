package com.space.service;

import com.space.controller.ShipOrder;
import com.space.exception.BadRequestException;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import com.space.request.FindShipRequest;
import com.space.request.ShipRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

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

    public List<Ship> getShipsList(FindShipRequest request) {
        List<Ship> shipList = shipRepository.findAll();
        //System.out.println(shipList.toString());
        String name = request.getName();
        if (name != null && !name.trim().isEmpty()) {
            shipList = shipList.stream()
                    .filter(ship -> ship.getName().contains(name))
                    .collect(Collectors.toList());
        }
        String planet = request.getPlanet();
        if (planet != null && !planet.trim().isEmpty()) {
            shipList = shipList.stream()
                    .filter(ship -> ship.getPlanet().contains(planet))
                    .collect(Collectors.toList());
        }
        ShipType shipType = request.getShipType();
        if (shipType != null) {
            shipList = shipList.stream()
                    .filter(ship -> ship.getShipType().equals(shipType))
                    .collect(Collectors.toList());
        }
        Long after = request.getAfter();
        if (after != null) {
            shipList = shipList.stream()
                    .filter(ship -> ship.getProdDate().getTime()>=after)
                    .collect(Collectors.toList());
        }
        Long before = request.getBefore();
        if (before != null) {
            shipList = shipList.stream()
                    .filter(ship -> ship.getProdDate().getTime()<=before)
                    .collect(Collectors.toList());
        }
        System.out.println(request);
        System.out.println(request.getUsed());
        if (request.getUsed() != null) {
            System.out.println("isUsed = ");
            shipList = shipList.stream()
                    .filter(ship -> ship.getUsed().equals(request.getUsed()))
                    .collect(Collectors.toList());
        }
        Double minSpeed = request.getMinSpeed();
        if (minSpeed != null) {
            shipList = shipList.stream()
                    .filter(ship -> ship.getSpeed() >= minSpeed)
                    .collect(Collectors.toList());
        }
        Double maxSpeed = request.getMaxSpeed();
        if (maxSpeed != null) {
            shipList = shipList.stream()
                    .filter(ship -> ship.getSpeed() <= maxSpeed)
                    .collect(Collectors.toList());
        }
        Integer minCrewSize = request.getMinCrewSize();
        if (minCrewSize != null) {
            shipList = shipList.stream()
                    .filter(ship -> ship.getCrewSize() >= minCrewSize)
                    .collect(Collectors.toList());
        }
        Integer maxCrewSize = request.getMaxCrewSize();
        if (maxCrewSize != null) {
            shipList = shipList.stream()
                    .filter(ship -> ship.getCrewSize() <= maxCrewSize)
                    .collect(Collectors.toList());
        }
        Double minRating = request.getMinRating();
        if (minRating != null) {
            shipList = shipList.stream()
                    .filter(ship -> ship.getRating() >= minRating)
                    .collect(Collectors.toList());
        }
        Double maxRating = request.getMaxRating();
        if (maxRating != null) {
            shipList = shipList.stream()
                    .filter(ship -> ship.getRating() <= maxRating)
                    .collect(Collectors.toList());
        }
        return shipList;
    }

    public List<Ship> filteredShips(final List<Ship> shipList, ShipOrder order, Integer pageNumber, Integer pageSize) {
        pageNumber = pageNumber == null ? 0 : pageNumber;
        pageSize = pageSize == null ? 3 : pageSize;

        return shipList.stream()
                .sorted(getComparator(order))
                .skip(pageNumber * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }
    private Comparator<Ship> getComparator(ShipOrder order) {
        if (order == null) {
            return Comparator.comparing(Ship::getId);
        }

        Comparator<Ship> comparator = null;
        switch (order.getFieldName()) {
            case "id":
                comparator = Comparator.comparing(Ship::getId);
                break;
            case "speed":
                comparator = Comparator.comparing(Ship::getSpeed);
                break;
            case "prodDate":
                comparator = Comparator.comparing(Ship::getProdDate);
                break;
            case "rating":
                comparator = Comparator.comparing(Ship::getRating);
        }

        return comparator;
    }

//    public Ship getShip(Integer id) {
//        shipRepository.getOne(id);
//    }

}
