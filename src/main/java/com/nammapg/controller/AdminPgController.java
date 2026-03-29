package com.nammapg.controller;

import com.nammapg.dto.PgListingRequest;
import com.nammapg.dto.PgListingResponse;
import com.nammapg.service.PgListingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/pgs")
@RequiredArgsConstructor
public class AdminPgController {

    private final PgListingService pgListingService;

    @GetMapping
    public ResponseEntity<List<PgListingResponse>> getAllPgs() {
        return ResponseEntity.ok(pgListingService.getAllPgs());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PgListingResponse> updatePg(
            @PathVariable String id,
            @Valid @RequestBody PgListingRequest request) {
        return ResponseEntity.ok(pgListingService.adminUpdatePg(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePg(@PathVariable String id) {
        pgListingService.adminDeletePg(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/verify")
    public ResponseEntity<PgListingResponse> verifyPg(
            @PathVariable String id,
            @RequestBody Map<String, Boolean> body) {
        return ResponseEntity.ok(pgListingService.verifyPg(id, body.getOrDefault("verified", false)));
    }

    @PutMapping("/{id}/approval-status")
    public ResponseEntity<PgListingResponse> updateApprovalStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(pgListingService.updateApprovalStatus(id, body.get("status")));
    }
}
