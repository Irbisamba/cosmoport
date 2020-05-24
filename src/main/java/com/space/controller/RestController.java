package com.space.controller;

import com.space.model.Ship;
import com.space.request.FindShipRequest;
import com.space.request.ShipRequest;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rest")
@ResponseBody
public class RestController {

    private ShipService shipService;

    @Autowired
    public RestController(ShipService shipService) {
        this.shipService = shipService;
    }

    @PostMapping("/ships")
    @ResponseBody
    public Ship createShip(@RequestBody ShipRequest request) {
       return shipService.createShip(request);
    }

    @GetMapping("/ships")
    public List<Ship> getShipsList(FindShipRequest request) {
        List<Ship> filteredShips = shipService.getShipsList(request);
        return shipService.filteredShips(filteredShips, request.getOrder(),
                request.getPageNumber(), request.getPageSize());
    }

    @GetMapping("/ships/count")
    public Integer getShipsCount(@RequestParam(required = false) Boolean isUsed, FindShipRequest request) {
        List<Ship> filteredShips = shipService.getShipsList(request);
        return filteredShips.size();
    }

    @GetMapping("/ships/{id}")
    public Ship getShip(@RequestParam(required = true) Integer id) {

        return null;
    }
}
