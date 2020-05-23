package com.space.controller;

import com.space.model.Ship;
import com.space.request.ShipRequest;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
