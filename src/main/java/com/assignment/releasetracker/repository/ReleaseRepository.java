package com.assignment.releasetracker.repository;

import com.assignment.releasetracker.entity.Release;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JpaRepository interface for Release entity
 */
public interface ReleaseRepository extends JpaRepository<Release, Long> {

    List<Release> findAllByStatusOrIssuesName(String status, String name);

}
