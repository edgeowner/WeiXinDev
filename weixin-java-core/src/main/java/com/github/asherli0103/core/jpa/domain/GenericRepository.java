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


package com.github.asherli0103.core.jpa.domain;

import com.github.asherli0103.core.jpa.QueryResult;
import com.github.asherli0103.core.jpa.criteria.Functions;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.LockModeType;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 通用数据调用接口
 *
 * @author AsherLi
 * @version V1.0.00
 */
@NoRepositoryBean
public interface GenericRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    /**
     * 设置query的参数
     *
     * @param query       查询对象
     * @param queryParams 参数
     */
    void setQueryParams(Query query, Object[] queryParams);


    /**
     * 组装ORDER BY 语句
     *
     * @param orderby
     * @return
     */
    String buildOrderby(LinkedHashMap<String, String> orderby);


    /**
     * 获取实体名
     *
     * @param entityClass
     * @return
     */
    String getEntityName(Class<T> entityClass);


    /**
     * jpql语句查询
     *
     * @param entityClass
     * @param whereJpql
     * @param queryParams
     * @param orderby
     * @param pageable
     * @return
     */
    QueryResult<T> getScrollDataByJpql(Class<T> entityClass, String whereJpql, Object[] queryParams, LinkedHashMap<String, String> orderby, Pageable pageable);


    /**
     * sql语句查询
     *
     * @param sql
     * @param queryParams
     * @param pageable
     * @return
     */
    QueryResult<T> getScrollDataBySql(String sql, Object[] queryParams, Pageable pageable);

    @SuppressWarnings(value = {"unchecked"})
    <E> E functionQuery(Specification<T> spec, Class<E> returnType, Sort sort, List<Functions> functions);

    /**
     * 查找数据 select for update
     *
     * @param id
     * @return
     */
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    T findOneWithLock(ID id);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    T findOneWithLock(Specification<T> specification);
}
