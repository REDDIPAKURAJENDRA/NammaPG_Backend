package com.nammapg.service;

import com.nammapg.dto.PgListingRequest;
import com.nammapg.dto.PgListingResponse;
import com.nammapg.model.PgListing;
import com.nammapg.model.User;
import com.nammapg.repository.PgListingRepository;
import com.nammapg.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PgListingService {

    private final PgListingRepository pgListingRepository;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    // ======== PUBLIC ========

    public List<PgListingResponse> searchPgs(String city, String area, String nearestMetro,
                                              Double maxRent, String sharingType, String gender,
                                              Boolean foodAvailable, Boolean acAvailable,
                                              Boolean verified, Boolean available, String sortBy) {
        Query query = new Query();

        // Only show approved and available listings to public
        query.addCriteria(Criteria.where("approvalStatus").is(PgListing.ApprovalStatus.APPROVED.name()));
        query.addCriteria(Criteria.where("available").is(true));

        if (city != null && !city.isEmpty())
            query.addCriteria(Criteria.where("city").regex(city, "i"));
        if (area != null && !area.isEmpty())
            query.addCriteria(Criteria.where("area").regex(area, "i"));
        if (nearestMetro != null && !nearestMetro.isEmpty())
            query.addCriteria(Criteria.where("nearestMetro").regex(nearestMetro, "i"));
        if (maxRent != null)
            query.addCriteria(Criteria.where("rent").lte(maxRent));
        if (sharingType != null && !sharingType.isEmpty())
            query.addCriteria(Criteria.where("sharingTypes").in(sharingType));
        if (gender != null && !gender.isEmpty())
            query.addCriteria(Criteria.where("gender").regex(gender, "i"));
        if (foodAvailable != null)
            query.addCriteria(Criteria.where("foodAvailable").is(foodAvailable));
        if (acAvailable != null)
            query.addCriteria(Criteria.where("acAvailable").is(acAvailable));
        if (verified != null)
            query.addCriteria(Criteria.where("verified").is(verified));

        if (sortBy != null) {
            switch (sortBy) {
                case "rent" -> query.with(Sort.by(Sort.Direction.ASC, "rent"));
                case "distanceFromMetro" -> query.with(Sort.by(Sort.Direction.ASC, "distanceFromMetro"));
                case "verified" -> query.with(Sort.by(Sort.Direction.DESC, "verified"));
                default -> query.with(Sort.by(Sort.Direction.DESC, "createdAt"));
            }
        } else {
            query.with(Sort.by(Sort.Direction.DESC, "createdAt"));
        }

        List<PgListing> listings = mongoTemplate.find(query, PgListing.class);
        return listings.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public PgListingResponse getPgById(String id) {
        PgListing pg = pgListingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PG listing not found"));
        return toResponse(pg);
    }

    // ======== OWNER ========

    public PgListingResponse createPg(PgListingRequest request, String ownerId) {
        PgListing pg = PgListing.builder()
                .name(request.getName())
                .city(request.getCity())
                .area(request.getArea())
                .metroLine(request.getMetroLine())
                .nearestMetro(request.getNearestMetro())
                .distanceFromMetro(request.getDistanceFromMetro())
                .address(request.getAddress())
                .rent(request.getRent())
                .deposit(request.getDeposit())
                .sharingTypes(request.getSharingTypes())
                .gender(request.getGender())
                .foodAvailable(request.getFoodAvailable())
                .acAvailable(request.getAcAvailable())
                .wifiAvailable(request.getWifiAvailable())
                .laundryAvailable(request.getLaundryAvailable())
                .parkingAvailable(request.getParkingAvailable())
                .description(request.getDescription())
                .images(request.getImages())
                .customAmenities(request.getCustomAmenities())
                .contactNumber(request.getContactNumber())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .ownerId(ownerId)
                .approvalStatus(PgListing.ApprovalStatus.PENDING)
                .verified(false)
                .available(true)
                .build();

        pg = pgListingRepository.save(pg);
        return toResponse(pg);
    }

    public List<PgListingResponse> getOwnerPgs(String ownerId) {
        return pgListingRepository.findByOwnerId(ownerId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public PgListingResponse updateOwnerPg(String pgId, PgListingRequest request, String ownerId) {
        PgListing pg = pgListingRepository.findById(pgId)
                .orElseThrow(() -> new RuntimeException("PG listing not found"));

        if (!pg.getOwnerId().equals(ownerId)) {
            throw new RuntimeException("You can only edit your own listings");
        }

        updatePgFields(pg, request);
        pg.setUpdatedAt(LocalDateTime.now());
        pg = pgListingRepository.save(pg);
        return toResponse(pg);
    }

    public void deleteOwnerPg(String pgId, String ownerId) {
        PgListing pg = pgListingRepository.findById(pgId)
                .orElseThrow(() -> new RuntimeException("PG listing not found"));

        if (!pg.getOwnerId().equals(ownerId)) {
            throw new RuntimeException("You can only delete your own listings");
        }
        pgListingRepository.delete(pg);
    }

    // ======== ADMIN ========

    public List<PgListingResponse> getAllPgs() {
        return pgListingRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public PgListingResponse adminUpdatePg(String pgId, PgListingRequest request) {
        PgListing pg = pgListingRepository.findById(pgId)
                .orElseThrow(() -> new RuntimeException("PG listing not found"));

        updatePgFields(pg, request);
        pg.setUpdatedAt(LocalDateTime.now());
        pg = pgListingRepository.save(pg);
        return toResponse(pg);
    }

    public void adminDeletePg(String pgId) {
        pgListingRepository.deleteById(pgId);
    }

    public PgListingResponse verifyPg(String pgId, boolean verified) {
        PgListing pg = pgListingRepository.findById(pgId)
                .orElseThrow(() -> new RuntimeException("PG listing not found"));
        pg.setVerified(verified);
        pg.setUpdatedAt(LocalDateTime.now());
        pg = pgListingRepository.save(pg);
        return toResponse(pg);
    }

    public PgListingResponse updateApprovalStatus(String pgId, String status) {
        PgListing pg = pgListingRepository.findById(pgId)
                .orElseThrow(() -> new RuntimeException("PG listing not found"));
        pg.setApprovalStatus(PgListing.ApprovalStatus.valueOf(status.toUpperCase()));
        pg.setUpdatedAt(LocalDateTime.now());
        pg = pgListingRepository.save(pg);
        return toResponse(pg);
    }

    // ======== HELPERS ========

    private void updatePgFields(PgListing pg, PgListingRequest req) {
        if (req.getName() != null) pg.setName(req.getName());
        if (req.getCity() != null) pg.setCity(req.getCity());
        if (req.getArea() != null) pg.setArea(req.getArea());
        if (req.getMetroLine() != null) pg.setMetroLine(req.getMetroLine());
        if (req.getNearestMetro() != null) pg.setNearestMetro(req.getNearestMetro());
        if (req.getDistanceFromMetro() != null) pg.setDistanceFromMetro(req.getDistanceFromMetro());
        if (req.getAddress() != null) pg.setAddress(req.getAddress());
        if (req.getRent() != null) pg.setRent(req.getRent());
        if (req.getDeposit() != null) pg.setDeposit(req.getDeposit());
        if (req.getSharingTypes() != null) pg.setSharingTypes(req.getSharingTypes());
        if (req.getGender() != null) pg.setGender(req.getGender());
        if (req.getFoodAvailable() != null) pg.setFoodAvailable(req.getFoodAvailable());
        if (req.getAcAvailable() != null) pg.setAcAvailable(req.getAcAvailable());
        if (req.getWifiAvailable() != null) pg.setWifiAvailable(req.getWifiAvailable());
        if (req.getLaundryAvailable() != null) pg.setLaundryAvailable(req.getLaundryAvailable());
        if (req.getParkingAvailable() != null) pg.setParkingAvailable(req.getParkingAvailable());
        if (req.getDescription() != null) pg.setDescription(req.getDescription());
        if (req.getImages() != null) pg.setImages(req.getImages());
        if (req.getCustomAmenities() != null) pg.setCustomAmenities(req.getCustomAmenities());
        if (req.getContactNumber() != null) pg.setContactNumber(req.getContactNumber());
        if (req.getLatitude() != null) pg.setLatitude(req.getLatitude());
        if (req.getLongitude() != null) pg.setLongitude(req.getLongitude());
    }

    private PgListingResponse toResponse(PgListing pg) {
        String ownerName = null;
        if (pg.getOwnerId() != null) {
            Optional<User> owner = userRepository.findById(pg.getOwnerId());
            ownerName = owner.map(User::getName).orElse(null);
        }

        return PgListingResponse.builder()
                .id(pg.getId())
                .name(pg.getName())
                .city(pg.getCity())
                .area(pg.getArea())
                .metroLine(pg.getMetroLine())
                .nearestMetro(pg.getNearestMetro())
                .distanceFromMetro(pg.getDistanceFromMetro())
                .address(pg.getAddress())
                .rent(pg.getRent())
                .deposit(pg.getDeposit())
                .sharingTypes(pg.getSharingTypes())
                .gender(pg.getGender())
                .foodAvailable(pg.getFoodAvailable())
                .acAvailable(pg.getAcAvailable())
                .wifiAvailable(pg.getWifiAvailable())
                .laundryAvailable(pg.getLaundryAvailable())
                .parkingAvailable(pg.getParkingAvailable())
                .description(pg.getDescription())
                .images(pg.getImages())
                .customAmenities(pg.getCustomAmenities())
                .contactNumber(pg.getContactNumber())
                .verified(pg.getVerified())
                .available(pg.getAvailable())
                .latitude(pg.getLatitude())
                .longitude(pg.getLongitude())
                .ownerId(pg.getOwnerId())
                .ownerName(ownerName)
                .approvalStatus(pg.getApprovalStatus() != null ? pg.getApprovalStatus().name() : null)
                .createdAt(pg.getCreatedAt())
                .updatedAt(pg.getUpdatedAt())
                .build();
    }
}
