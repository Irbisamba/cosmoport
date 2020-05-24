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
    public Integer getShipsCount(FindShipRequest request) {
        List<Ship> filteredShips = shipService.getShipsList(request);
        return filteredShips.size();
    }

    @GetMapping("/ships/{id}")
    public Ship getShip(@PathVariable Integer id) {
        return shipService.getShip(id);
    }

    @PostMapping("/ships/{id}")
    @ResponseBody
    public Ship updateShip(@PathVariable Integer id, @RequestBody ShipRequest request) {
        return shipService.updateShip(id, request);
    }

    @DeleteMapping("/ships/{id}")
    @ResponseBody
    public void deleteShip(@PathVariable Integer id) {
        shipService.deleteShip(id);
    }
}
