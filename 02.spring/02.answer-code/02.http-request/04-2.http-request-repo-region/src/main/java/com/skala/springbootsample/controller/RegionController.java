package com.skala.springbootsample.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.skala.springbootsample.domain.Region;
import com.skala.springbootsample.service.RegionService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    // 전체 조회
    @GetMapping("/regions")
    public ResponseEntity<List<Region>> list() {
        return ResponseEntity.ok(regionService.findAll());
    }

    // 단건 조회
    @GetMapping("/regions/{id}")
    public ResponseEntity<Region> get(@PathVariable Long id) {
        return ResponseEntity.ok(regionService.findById(id));
    }

    // 생성
    @PostMapping("/regions")
    public ResponseEntity<Region> create(@RequestBody Region region) {
        Region created = regionService.create(region);
        return ResponseEntity
                .created(URI.create("/api/regions/" + created.getId()))
                .body(created);
    }

    // 수정
    @PutMapping("/regions/{id}")
    public ResponseEntity<Region> update(@PathVariable Long id, @RequestBody Region updated) {
        return ResponseEntity.ok(regionService.update(id, updated));
    }

    // 삭제
    @DeleteMapping("/regions/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        regionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

