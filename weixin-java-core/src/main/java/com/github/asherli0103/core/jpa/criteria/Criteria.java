/*
 * Copyright  (c) 2017. By AsherLi0103
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


package com.github.asherli0103.core.jpa.criteria;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * SQl 条件拼接
 *
 * @author AsherLi
 * @version V1.0.00
 */
public class Criteria<T> implements Specification<T> {

    private List<Criterion> criterions = new ArrayList<>();

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (!criterions.isEmpty()) {
            List<Predicate> predicates = new ArrayList<>();
            for (Criterion c : criterions) {
                predicates.add(c.toPredicate(root, criteriaQuery, criteriaBuilder));
            }
            // 将所有条件用 and 联合起来
            if (predicates.size() > 0) {
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }
        return criteriaBuilder.conjunction();
    }

    /**
     * 增加简单条件表达式
     *
     */
    public void add(Criterion criterion) {
        if (criterion != null) {
            criterions.add(criterion);
        }
    }
}
