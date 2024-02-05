package com.neteye.persistence.repositories;

import com.neteye.persistence.entities.Device;
import com.neteye.persistence.entities.PortInfo.PortInfo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DeviceSearchRepository {

    private final EntityManager entityManager;

    public DeviceSearchRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    //TODO pagination correction & query creation improvement
    public Page<Device> findDevicesByRequestedCriteria(Map<String, String> criteria, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Device> query = criteriaBuilder.createQuery(Device.class);
        Root<Device> root = query.from(Device.class);

        Predicate predicate = createPredicate(criteriaBuilder, root, criteria);

        query.where(predicate);

        List<Device> resultList = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(resultList, pageable, countResults(criteria));
    }

    private long countResults(Map<String, String> criteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Device> root = countQuery.from(Device.class);

        countQuery.select(criteriaBuilder.count(root));

        Predicate predicate = createPredicate(criteriaBuilder, root, criteria);

        countQuery.where(predicate);

        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private Predicate createPredicate(CriteriaBuilder criteriaBuilder, Root<Device> root ,Map<String, String> criteria) {
        Predicate predicate = criteriaBuilder.conjunction();
        Join<Device, PortInfo> openedPortsJoin = root.join("openedPorts", JoinType.LEFT);

        for (Map.Entry<String, String> entry : criteria.entrySet()) {
            String columnName = entry.getKey();
            String value = entry.getValue();


            switch (columnName) {
                case "port" -> predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(openedPortsJoin.get("primaryKey").get(columnName), value));
                case "info" -> predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(openedPortsJoin.get(columnName), "%" + value + "%"));
                case "appName", "appVersion" -> predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(openedPortsJoin.get(columnName), value));
                default ->
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(columnName), value));
            }
        }

        return predicate;
    }
}
