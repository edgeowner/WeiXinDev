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
import java.util.List;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public class JoinExpression implements Criterion {
    private final List<String> tableNames;
    private final String propertyName;
    private final Class clazz;
    private final Object value;
    private Operator operator;      //计算符

    public JoinExpression(List<String> tableNames, String propertyName, Class clazz, Object value, Operator operator) {
        this.tableNames = tableNames;
        this.propertyName = propertyName;
        this.clazz = clazz;
        this.value = value;
        this.operator = operator;
    }

    public List<String> getTableNames() {
        return tableNames;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Class getClazz() {
        return clazz;
    }

    public Object getValue() {
        return value;
    }

    public Criterion.Operator getOperator() {
        return operator;
    }

    public JoinExpression setOperator(Operator operator) {
        this.operator = operator;
        return this;
    }

    @Override
    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        try {
            Expression expression = null;
            for (String tableName : tableNames) {
                root.join(tableName);
            }
            expression = root.get(propertyName);
            switch (operator) {
                case JOIN:
                    return builder.equal(expression, value);
                default:
                    return null;
            }
        } catch (Exception e) {
        }
        return null;
    }

}
