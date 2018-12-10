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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Sql条件拼接接口及条件枚举
 *
 * @author AsherLi
 * @version V1.0.00
 */
public interface Criterion {
    Predicate toPredicate(Root<?> root, CriteriaQuery<?> query,
                          CriteriaBuilder builder);


    enum Operator {
        EQ, NE, LIKE, GT, LT, GTE, LTE, AND, OR, BETWEEN, ISNULL, NOTNULL, JOIN
    }
}
