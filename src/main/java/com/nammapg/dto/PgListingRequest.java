package com.nammapg.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PgListingRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String city;

    @NotBlank
    private String area;

    private String metroLine;

    @NotBlank
    private String nearestMetro;

    private Double distanceFromMetro;

    private String address;

    @NotNull
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

    @NotBlank
    private String contactNumber;

    private Double latitude;
    private Double longitude;
}
