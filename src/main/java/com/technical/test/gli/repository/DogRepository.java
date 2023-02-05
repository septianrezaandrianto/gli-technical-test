package com.technical.test.gli.repository;

import com.technical.test.gli.entity.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {
    @Query(value = "SELECT * FROM Dog d WHERE d.DOG_NAME = :dogName", nativeQuery = true)
    Dog findByDogName(@Param("dogName") String dogName);
}
