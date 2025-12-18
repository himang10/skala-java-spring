package com.skala.springbootsample.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.skala.springbootsample.domain.Region;
import com.skala.springbootsample.repo.RegionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionService {

    private final RegionRepository regionRepository;

    public List<Region> findAll() {
        return regionRepository.findAll();
    }

    public Region findById(Long id) {
        return regionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Region not found: " + id));
    }

    @Transactional
    public Region create(Region region) {
        if (regionRepository.findByName(region.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Region name already exists: " + region.getName());
        }
        return regionRepository.save(region);
    }

    @Transactional
    public Region update(Long id, Region updated) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Region not found: " + id));

        // 이름 변경 시 중복 체크
        if (!region.getName().equals(updated.getName()) &&
                regionRepository.findByName(updated.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Region name already exists: " + updated.getName());
        }

        region.setName(updated.getName());
        return regionRepository.save(region);
    }

    @Transactional
    public void delete(Long id) {
        if (!regionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Region not found: " + id);
        }
        regionRepository.deleteById(id);
    }
}

