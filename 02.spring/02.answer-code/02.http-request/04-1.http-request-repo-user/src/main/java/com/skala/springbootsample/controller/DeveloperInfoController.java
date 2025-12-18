package com.skala.springbootsample.controller;

import com.skala.springbootsample.config.DeveloperProperties;
import com.skala.springbootsample.dto.DeveloperInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DeveloperInfoController {

    private final DeveloperProperties props;

    public DeveloperInfoController(DeveloperProperties props) {
        this.props = props;
    }

    @GetMapping("/developer-info")
    public DeveloperInfo info() {
        var owner = props.getOwner();
        var team = props.getTeam();

        return new DeveloperInfo (owner, team);
    }
}
