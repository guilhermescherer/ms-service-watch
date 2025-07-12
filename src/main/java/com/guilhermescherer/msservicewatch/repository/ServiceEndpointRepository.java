package com.guilhermescherer.msservicewatch.repository;

import com.guilhermescherer.msservicewatch.model.ServiceEndpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceEndpointRepository extends JpaRepository<ServiceEndpoint, Long> {

    @Query(value = """
            SELECT * FROM service_endpoint
            WHERE active = true
              AND (
                  checked_at IS NULL OR
                  (now() - checked_at) > (check_interval * interval '1 minute')
              )
            ORDER BY checked_at NULLS FIRST, id
            LIMIT 2
            FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    List<ServiceEndpoint> findServiceEndpointsToVerify();
}
