package com.example.demo.controller;

import com.example.demo.entity.Substation;
import com.example.demo.repository.SubstationRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.*;

@RestController
@RequestMapping("/api/substation")
public class SubstationController {

    private final SubstationRepository substationRepository;

    public SubstationController(SubstationRepository substationRepository) {
        this.substationRepository = substationRepository;
    }

    @PostMapping
    public ResponseEntity<Substation> create(@RequestBody Substation substation) {
        Substation saved = substationRepository.save(substation);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Substation>> getAll() {
        return ResponseEntity.ok(substationRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Substation> getById(@PathVariable long id) {
        return substationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Substation> update(@PathVariable Long id, @RequestBody Substation updated) {
        return substationRepository.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    existing.setCode(updated.getCode());
                    existing.setLocation(updated.getLocation());
                    Substation saved = substationRepository.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!substationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        substationRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
