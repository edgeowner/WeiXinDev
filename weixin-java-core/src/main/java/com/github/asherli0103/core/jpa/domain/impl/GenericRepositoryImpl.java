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

package com.github.asherli0103.core.jpa.domain.impl;

import com.github.asherli0103.core.jpa.QueryResult;
import com.github.asherli0103.core.jpa.criteria.Functions;
import com.github.asherli0103.core.jpa.domain.GenericRepository;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.util.Assert;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * 通用数据调用实现类
 *
 * @author AsherLi
 * @version V1.0.00
 */
//@SuppressWarnings("SpringJavaAutowiringInspection")
@NoRepositoryBean  // 必须的
public class GenericRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements GenericRepository<T, ID> {

    private static final Logger logger = LoggerFactory.getLogger(GenericRepositoryImpl.class);
    private final EntityManager em;
    private final JpaEntityInformation<T, ?> jpaEntityInformation;

//    public Session getSession() {
//getSession().;
//
//    }

    /**
     * 构造函数
     *
     * @param jpaEntityInformation
     * @param em
     */
    public GenericRepositoryImpl(JpaEntityInformation<T, ?> jpaEntityInformation, EntityManager em) {
        super(jpaEntityInformation, em);
        this.em = em;
        this.jpaEntityInformation = jpaEntityInformation;
    }


    /**
     * 构造函数
     *
     * @param domainClass
     * @param em
     */
    public GenericRepositoryImpl(Class<T> domainClass, EntityManager em) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
    }

    protected Class<T> getDomainClass() {
        return jpaEntityInformation.getJavaType();
    }

    @Override
    public void setQueryParams(Query query, Object[] queryParams) {

        if (null != queryParams && queryParams.length != 0) {
            for (int i = 0; i < queryParams.length; i++) {
                query.setParameter(i + 1, queryParams[i]);
            }
        }
    }

    @Override
    public String buildOrderby(LinkedHashMap<String, String> orderby) {
        StringBuilder orderbyql = new StringBuilder("");
        if (orderby != null && orderby.size() > 0) {
            orderbyql.append(" order by ");
            for (String key : orderby.keySet()) {
                orderbyql.append("o.").append(key).append(" ").append(orderby.get(key)).append(",");
            }
            orderbyql.deleteCharAt(orderbyql.length() - 1);
        }
        return orderbyql.toString();
    }

    @Override
    public String getEntityName(Class<T> entityClass) {
        String entityname = entityClass.getSimpleName();
        Entity entity = entityClass.getAnnotation(Entity.class);
        if (!"".equals(entity.name())) {
            entityname = entity.name();
        }
        return entityname;
    }

    @Override
    public QueryResult<T> getScrollDataByJpql(Class<T> entityClass, String whereJpql, Object[] queryParams,
                                              LinkedHashMap<String, String> orderby, Pageable pageable) {

        QueryResult<T> qr = new QueryResult<T>();
        String entityname = getEntityName(entityClass);
        String sql = "select o from " + entityname + " o ";
        String sqlWhere = whereJpql == null ? "" : "where " + whereJpql;
        Query query = em.createQuery(sql + sqlWhere + buildOrderby(orderby));

        setQueryParams(query, queryParams);
        if (pageable.getPageNumber() != -1 && pageable.getPageSize() != -1) {
            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());
        }
        qr.setResultList(query.getResultList());

        query = em.createQuery("select count(" + getCountField(entityClass) + ") from " + entityname + " o " + sqlWhere);
        setQueryParams(query, queryParams);
        qr.setTotalRecord((Long) query.getSingleResult());

        return qr;
    }

    @Override
    public QueryResult<T> getScrollDataBySql(String sql, Object[] queryParams, Pageable pageable) {
        //查询记录数
        QueryResult<T> qr = new QueryResult<T>();
        Query query = em.createNativeQuery(sql);
        setQueryParams(query, queryParams);
        if (pageable.getPageNumber() != -1 && pageable.getPageSize() != -1) {
            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());
        }
        qr.setResultList(query.getResultList());

        //
        String from = getFromClause(sql);
        //查询总记录数
        query = em.createQuery("select count(*) " + from);
        setQueryParams(query, queryParams);
        qr.setTotalRecord((Long) query.getSingleResult());
        return qr;
    }


    @Override
    @SuppressWarnings(value = {"unchecked"})
    public <E> E functionQuery(Specification<T> spec, Class<E> returnType, Sort sort, List<Functions> functionses) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        Class domainClass = this.getDomainClass();
        CriteriaQuery query = criteriaBuilder.createQuery(returnType);
        Root root = this.applySpecificationToCriteria(spec, domainClass, query);

//        List<Selection> selections = new ArrayList<>();
        for (Functions functions : functionses) {
            switch (functions.getFunctionName()) {
                case AVG:
//                    selections.add(builder.avg(root.get(functions.getFieldName())));
                    query.select(criteriaBuilder.avg(root.get(functions.getFieldName())));
                    break;
                case MAX:
//                    selections.add(builder.max(root.get(functions.getFieldName())));
                    query.select(criteriaBuilder.max(root.get(functions.getFieldName())));
                    break;
                case MIN:
//                    selections.add(builder.min(root.get(functions.getFieldName())));
                    query.select(criteriaBuilder.min(root.get(functions.getFieldName())));
                    break;
                case COUNT:
//                    selections.add(builder.count(root.get(functions.getFieldName())));
                    query.select(criteriaBuilder.count(root.get(functions.getFieldName())));
                    break;
                case SUM:
                    query.select(criteriaBuilder.sum(root.get(functions.getFieldName())));
                    //selections.add(builder.sum(root.get(functions.getFieldName())));
                    break;
                default:
                    return null;
            }
        }


        if (sort != null) {
            query.orderBy(QueryUtils.toOrders(sort, root, criteriaBuilder));
        }

        if (em.createQuery(query) == null) {
            try {
                return returnType.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return (E) this.em.createQuery(query).getSingleResult();
    }


    @Override
    public T findOneWithLock(ID id) {
        return super.findOne(id);
    }

    @Override
    public T findOneWithLock(Specification<T> specification) {
        return super.findOne(specification);
    }

    private <S, U extends T> Root<U> applySpecificationToCriteria(Specification<U> spec, Class<U> domainClass, CriteriaQuery<S> query) {
        Assert.notNull(query);
        Assert.notNull(domainClass);
        Root root = query.from(domainClass);
        if (spec == null) {
            return root;
        } else {
            CriteriaBuilder builder = this.em.getCriteriaBuilder();
            Predicate predicate = spec.toPredicate(root, query, builder);
            if (predicate != null) {
                query.where(predicate);
            }

            return root;
        }
    }

    private String getCountField(Class<T> clazz) {

        String out = "o";
        try {
            PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
            for (PropertyDescriptor propertydesc : propertyDescriptors) {
                Method method = propertydesc.getReadMethod();
                if (method != null && method.isAnnotationPresent(EmbeddedId.class)) {
                    PropertyDescriptor[] ps = Introspector.getBeanInfo(propertydesc.getPropertyType()).getPropertyDescriptors();
                    out = "o." + propertydesc.getName() + "." + (!ps[1].getName().equals("class") ? ps[1].getName() : ps[0].getName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }


    /**
     * 从sql中找出from子句
     *
     * @param sql
     * @return
     */
    private String getFromClause(String sql) {
        String sql2 = sql.toLowerCase();
        int index = sql2.indexOf(" from ");
        if (index < 0) {
            return null;
        } else {
            int i1 = sql2.lastIndexOf(" order by ");
            int i2 = sql2.lastIndexOf(" group by ");

            if (i1 >= 0 && i2 >= 0) {
                return sql.substring(index, i1 > i2 ? i2 : i1);
            } else if (i1 >= 0) {
                return sql.substring(index, i1);
            } else if (i2 >= 0) {
                return sql.substring(index, i2);
            } else {
                return sql.substring(index);
            }
        }
    }

}