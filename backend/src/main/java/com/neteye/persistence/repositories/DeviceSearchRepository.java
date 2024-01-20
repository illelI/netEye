package com.neteye.persistence.repositories;

import com.neteye.persistence.entities.Device;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.CriteriaDefinition;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class DeviceSearchRepository {
    private final CassandraTemplate cassandraTemplate;

    public DeviceSearchRepository(CassandraTemplate cassandraTemplate) {
        this.cassandraTemplate = cassandraTemplate;
    }

    //TODO pagination correction & query creation improvement
    public Page<Device> findDevicesByRequestedCriteria(Map<String, String> criteria, Pageable pageable) {
        List<CriteriaDefinition> criteriaDefinitions = new ArrayList<>();

        for (String key : criteria.keySet()) {
            criteriaDefinitions.add(Criteria.where(key).is(criteria.get(key)));
        }

        CassandraPageRequest cassandraPageRequest = CassandraPageRequest.of(pageable, null);
        Query query = Query.query(criteriaDefinitions).pageRequest(cassandraPageRequest).withAllowFiltering();
        Slice<Device> slice = cassandraTemplate.slice(query, Device.class);
        return new PageImpl<>(slice.getContent(), pageable, slice.getSize());
    }
}
