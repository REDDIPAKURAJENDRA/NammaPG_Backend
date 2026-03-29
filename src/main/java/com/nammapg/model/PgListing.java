package com.nammapg.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "pg_listings")
public class PgListing {

    @Id
    private String id;

    private String name;
    private String city;
    private String area;
    private String metroLine;
    private String nearestMetro;
    private Double distanceFromMetro;
    private String address;

    private Double rent;
    private Double deposit;
    private List<String> sharingTypes;

    private String gender; // MALE, FEMALE, ANY
    private Boolean foodAvailable;
    private Boolean acAvailable;
    private Boolean wifiAvailable;
    private Boolean laundryAvailable;
    private Boolean parkingAvailable;

    private String description;
    private List<String> images;
    private String contactNumber;

    @Builder.Default
    private Boolean verified = false;

    @Builder.Default
    private Boolean available = true;

    private Double latitude;
    private Double longitude;

    private String ownerId;

    @Builder.Default
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    public enum ApprovalStatus {
        PENDING, APPROVED, REJECTED
    }
}
