package com.nammapg.controller;

import com.nammapg.dto.PgListingResponse;
import com.nammapg.service.PgListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pgs")
@RequiredArgsConstructor
public class PublicPgController {

    private final PgListingService pgListingService;

    @GetMapping
    public ResponseEntity<List<PgListingResponse>> searchPgs(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String nearestMetro,
            @RequestParam(required = false) Double maxRent,
            @RequestParam(required = false) String sharingType,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Boolean foodAvailable,
            @RequestParam(required = false) Boolean acAvailable,
            @RequestParam(required = false) Boolean verified,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) String sortBy) {
        return ResponseEntity.ok(pgListingService.searchPgs(
                city, area, nearestMetro, maxRent, sharingType,
                gender, foodAvailable, acAvailable, verified, available, sortBy));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PgListingResponse> getPgById(@PathVariable String id) {
        return ResponseEntity.ok(pgListingService.getPgById(id));
    }
}
