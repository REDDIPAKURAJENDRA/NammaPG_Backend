package com.nammapg.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PgListingResponse {
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
    private String gender;
    private Boolean foodAvailable;
    private Boolean acAvailable;
    private Boolean wifiAvailable;
    private Boolean laundryAvailable;
    private Boolean parkingAvailable;
    private String description;
    private List<String> images;
    private List<String> customAmenities;
    private String contactNumber;
    private Boolean verified;
    private Boolean available;
    private Double latitude;
    private Double longitude;
    private String ownerId;
    private String ownerName;
    private String approvalStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
