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

package com.github.asherli0103.core.jpa.criteria.expression;


import com.github.asherli0103.core.jpa.criteria.Criterion;

import javax.persistence.criteria.*;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public class NullExpression implements Criterion {

    private final String propertyName;

    private final Operator operator;

    public NullExpression(String propertyName, Operator operator) {
        this.propertyName = propertyName;
        this.operator = operator;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Operator getOperator() {
        return operator;
    }

    @Override
    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Path expression = root.get(propertyName);
        switch (operator) {
            case ISNULL:
                return builder.isNull(expression);
            default:
                return null;
        }
    }


}
