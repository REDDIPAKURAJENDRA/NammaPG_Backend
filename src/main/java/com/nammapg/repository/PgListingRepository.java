package com.nammapg.repository;

import com.nammapg.model.PgListing;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PgListingRepository extends MongoRepository<PgListing, String> {
    List<PgListing> findByOwnerId(String ownerId);
}
