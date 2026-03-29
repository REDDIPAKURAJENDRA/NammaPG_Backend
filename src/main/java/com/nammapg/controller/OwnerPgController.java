package com.nammapg.controller;

import com.nammapg.dto.PgListingRequest;
import com.nammapg.dto.PgListingResponse;
import com.nammapg.service.PgListingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner/pgs")
@RequiredArgsConstructor
public class OwnerPgController {

    private final PgListingService pgListingService;

    @PostMapping
    public ResponseEntity<PgListingResponse> createPg(
            @Valid @RequestBody PgListingRequest request,
            HttpServletRequest httpRequest) {
        String ownerId = (String) httpRequest.getAttribute("userId");
        return ResponseEntity.ok(pgListingService.createPg(request, ownerId));
    }

    @GetMapping
    public ResponseEntity<List<PgListingResponse>> getMyPgs(HttpServletRequest httpRequest) {
        String ownerId = (String) httpRequest.getAttribute("userId");
        return ResponseEntity.ok(pgListingService.getOwnerPgs(ownerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PgListingResponse> updatePg(
            @PathVariable String id,
            @Valid @RequestBody PgListingRequest request,
            HttpServletRequest httpRequest) {
        String ownerId = (String) httpRequest.getAttribute("userId");
        return ResponseEntity.ok(pgListingService.updateOwnerPg(id, request, ownerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePg(
            @PathVariable String id,
            HttpServletRequest httpRequest) {
        String ownerId = (String) httpRequest.getAttribute("userId");
        pgListingService.deleteOwnerPg(id, ownerId);
        return ResponseEntity.noContent().build();
    }
}
