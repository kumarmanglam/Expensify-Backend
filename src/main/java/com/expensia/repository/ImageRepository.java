package com.expensia.repository;

import com.expensia.entity.Image;
import com.expensia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByUser(User user);

    boolean existsByUser(User user);
}
