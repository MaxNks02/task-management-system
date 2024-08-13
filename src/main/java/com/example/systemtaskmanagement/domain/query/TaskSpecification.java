package com.example.systemtaskmanagement.domain.query;

import com.example.systemtaskmanagement.domain.dao.model.Task;
import com.example.systemtaskmanagement.domain.dao.model.enums.TaskPriority;
import com.example.systemtaskmanagement.domain.dao.model.enums.TaskStatus;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TaskSpecification {

    public static Specification<Task> filterAndSort(
            TaskStatus status,
            TaskPriority priority,
            String sortBy,
            String sortDirection) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();


            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }


            if (priority != null) {
                predicates.add(criteriaBuilder.equal(root.get("priority"), priority));
            }


            if (sortBy != null && !sortBy.isEmpty()) {
                Path<String> sortByField = root.get(sortBy);
                if ("desc".equalsIgnoreCase(sortDirection)) {
                    Objects.requireNonNull(query).orderBy(criteriaBuilder.desc(sortByField));
                } else {
                    Objects.requireNonNull(query).orderBy(criteriaBuilder.asc(sortByField));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
