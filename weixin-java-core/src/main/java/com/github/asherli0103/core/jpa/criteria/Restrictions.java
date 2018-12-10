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

import com.github.asherli0103.core.jpa.criteria.expression.*;
import com.github.asherli0103.utils.ObjectUtil;
import org.hibernate.criterion.MatchMode;

import java.util.Collection;
import java.util.List;


/**
 * 条件语句拼接器
 *
 * @author AsherLi
 * @version V1.0.00
 */
public class Restrictions {

    /**
     * 等于
     *
     * @param fieldName
     * @param value
     * @param ignoreNull
     * @return
     */
    public static SimpleExpression eq(String fieldName, Object value, boolean ignoreNull) {
        if (ObjectUtil.isEmpty(value) && ignoreNull) {
            return null;
        }
        return new SimpleExpression(fieldName, value, Criterion.Operator.EQ);
    }

    /**
     * 不等于
     *
     * @param fieldName
     * @param value
     * @param ignoreNull
     * @return
     */
    public static SimpleExpression ne(String fieldName, Object value, boolean ignoreNull) {
        if (ObjectUtil.isEmpty(value)) return null;
        return new SimpleExpression(fieldName, value, Criterion.Operator.NE);
    }

    /**
     * 模糊匹配
     *
     * @param fieldName
     * @param value
     * @param ignoreNull
     * @return
     */
    public static SimpleExpression like(String fieldName, String value, boolean ignoreNull) {
        if (ObjectUtil.isEmpty(value)) return null;
        return new SimpleExpression(fieldName, value, Criterion.Operator.LIKE);
    }

    /**
     * @param fieldName
     * @param value
     * @param matchMode
     * @param ignoreNull
     * @return
     */
    public static SimpleExpression like(String fieldName, String value,
                                        MatchMode matchMode, boolean ignoreNull) {
        if (ObjectUtil.isEmpty(value)) return null;
        return null;
    }

    /**
     * 大于
     *
     * @param fieldName
     * @param value
     * @param ignoreNull
     * @return
     */
    public static SimpleExpression gt(String fieldName, Object value, boolean ignoreNull) {
        if (ObjectUtil.isEmpty(value)) return null;
        return new SimpleExpression(fieldName, value, Criterion.Operator.GT);
    }

    /**
     * 小于
     *
     * @param fieldName
     * @param value
     * @param ignoreNull
     * @return
     */
    public static SimpleExpression lt(String fieldName, Object value, boolean ignoreNull) {
        if (ObjectUtil.isEmpty(value)) return null;
        return new SimpleExpression(fieldName, value, Criterion.Operator.LT);
    }

    /**
     * 大于等于
     *
     * @param fieldName
     * @param value
     * @param ignoreNull
     * @return
     */
    public static SimpleExpression lte(String fieldName, Object value, boolean ignoreNull) {
        if (ObjectUtil.isEmpty(value)) return null;
        return new SimpleExpression(fieldName, value, Criterion.Operator.GTE);
    }

    /**
     * 小于等于
     *
     * @param fieldName
     * @param value
     * @param ignoreNull
     * @return
     */
    public static SimpleExpression gte(String fieldName, Object value, boolean ignoreNull) {
        if (ObjectUtil.isEmpty(value)) return null;
        return new SimpleExpression(fieldName, value, Criterion.Operator.LTE);
    }

    /**
     * 并且
     *
     * @param criterions
     * @return
     */
    public static LogicalExpression and(Criterion... criterions) {
        return new LogicalExpression(criterions, Criterion.Operator.AND);
    }

    /**
     * 或者
     *
     * @param criterions
     * @return
     */
    public static LogicalExpression or(Criterion... criterions) {
        return new LogicalExpression(criterions, Criterion.Operator.OR);
    }

    /**
     * Between and
     *
     * @param fieldName
     * @param value1
     * @param value2
     * @param ignoreNull
     * @return
     */
    public static BetweenExpression between(String fieldName, Object value1, Object value2, boolean ignoreNull) {
        if (ObjectUtil.isEmpty(value1) || ObjectUtil.isEmpty(value2)) return null;
        return new BetweenExpression(fieldName, value1, value2, Criterion.Operator.BETWEEN);
    }

    /**
     * isNull
     *
     * @param fieldName
     * @return
     */
    public static NullExpression isNull(String fieldName) {
        return new NullExpression(fieldName, Criterion.Operator.ISNULL);
    }

    /**
     * isNotNull
     *
     * @param fieldName
     * @return
     */
    public static NotNullExpression isNotNull(String fieldName) {
        return new NotNullExpression(fieldName, Criterion.Operator.NOTNULL);
    }

    /**
     * join
     *
     * @param fieldName
     * @return
     */
    public static JoinExpression join(List<String> tableName, String fieldName, Class clazz, Object value, boolean ignoreNull) {
        if (ObjectUtil.isEmpty(value) || !ignoreNull) return null;
        return new JoinExpression(tableName, fieldName, clazz, value, Criterion.Operator.JOIN);
    }

    /**
     * 包含于
     *
     * @param fieldName
     * @param value
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static LogicalExpression in(String fieldName, Collection value, boolean ignoreNull) {
        if (ignoreNull && (value == null || value.isEmpty())) {
            return null;
        }
        SimpleExpression[] ses = new SimpleExpression[value.size()];
        int i = 0;
        for (Object obj : value) {
            ses[i] = new SimpleExpression(fieldName, obj, Criterion.Operator.EQ);
            i++;
        }
        return new LogicalExpression(ses, Criterion.Operator.OR);
    }


//    Criteria<Event> c = new Criteria<Event>();
//c.add(Restrictions.like("code", searchParam.getCode(), true));
//        c.add(Restrictions.eq("level", searchParam.getLevel(), false));
//        c.add(Restrictions.eq("mainStatus", searchParam.getMainStatus(), true));
//        c.add(Restrictions.eq("flowStatus", searchParam.getFlowStatus(), true));
//        c.add(Restrictions.eq("createUser.userName", searchParam.getCreateUser(), true));
//        c.add(Restrictions.lte("submitTime", searchParam.getStartSubmitTime(), true));
//        c.add(Restrictions.gte("submitTime", searchParam.getEndSubmitTime(), true));
//        c.add(Restrictions.eq("needFollow", searchParam.getIsfollow(), true));
//        c.add(Restrictions.ne("flowStatus", CaseConstants.CASE_STATUS_DRAFT, true));
//        c.add(Restrictions.in("solveTeam.code",teamCodes, true));
//eventDao.findAll(c);

}
