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

package com.github.asherli0103.core.jpa.service.impl;

import com.github.asherli0103.core.jpa.domain.BaseRepository;
import com.github.asherli0103.core.jpa.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 通用业务层
 *
 * @author AsherLi0103
 * @version 1.0.00
 */
@Service
@Transactional
public abstract class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BaseRepository<T, ID> baseRepository;

    @Autowired
    public BaseServiceImpl(BaseRepository<T, ID> baseRepository) {
        this.baseRepository = baseRepository;
    }

    @Override
    public <S extends T> S save(S s) {
        return baseRepository.save(s);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return baseRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public T findOne(Specification<T> specification) {

        return baseRepository.findOne(specification);
    }

    @Override
    @Transactional(readOnly = true)
    public T findOne(Example<T> example) {
        return baseRepository.findOne(example);
    }

    @Override
    public boolean exists(Example<T> example) {
        return baseRepository.exists(example);
    }

    @Override
    public boolean exists(ID id) {
        return baseRepository.exists(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll(Specification<T> specification) {
        return baseRepository.findAll(specification);
    }

    @Override
    public void delete(T t) {
        baseRepository.delete(t);
    }


    @Override
    public Long count(Example<T> example) {
        return baseRepository.count(example);
    }

    @Override
    public <S extends T> List<T> batchSave(Iterable<T> iterable) {
        return baseRepository.save(iterable);
    }

}
