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

package com.github.asherli0103.core.jpa.service;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public interface BaseService<T, ID extends Serializable> {


    <S extends T> S save(S s);

    List<T> findAll();

    T findOne(Specification<T> specification);

    T findOne(Example<T> example);

    boolean exists(Example<T> example);

    boolean exists(ID id);

    List<T> findAll(Specification<T> specification);


    void delete(T t);

    Long count(Example<T> example);

    <S extends T> List<T> batchSave(Iterable<T> iterable);
}
