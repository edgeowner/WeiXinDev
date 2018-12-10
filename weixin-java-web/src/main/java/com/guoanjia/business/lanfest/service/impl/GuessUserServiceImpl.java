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

package com.guoanjia.business.lanfest.service.impl;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.github.asherli0103.core.jpa.domain.BaseRepository;
import com.github.asherli0103.core.jpa.service.impl.BaseServiceImpl;
import com.github.asherli0103.utils.tools.UUID;
import com.guoanjia.business.lanfest.entity.GuessUser;
import com.guoanjia.business.lanfest.entity.jpa.GuessUserRepository;
import com.guoanjia.business.lanfest.service.GuessUserService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("guessUserService")
@Transactional
public class GuessUserServiceImpl extends BaseServiceImpl<GuessUser,String> implements GuessUserService {

    private final GuessUserRepository guessUserRepository;

    public GuessUserServiceImpl(BaseRepository<GuessUser, String> baseRepository, GuessUserRepository guessUserRepository) {
        super(baseRepository);
        this.guessUserRepository = guessUserRepository;
    }



    @Override
    public boolean doSaveGuess(String name, String phone, String score, String company) {
        List<GuessUser> guessUserEntityList = guessUserRepository.findAll(Example.of(new GuessUser().setPhone(phone)));
        GuessUser entity = new GuessUser();
        try {
            if (guessUserEntityList.size() > 0) {
                entity = guessUserEntityList.get(0);
                String maxscore = entity.getMaxscore();
                Integer guesstimes = entity.getTotaltimes();
                if (Integer.parseInt(maxscore) < Integer.parseInt(score)) {
                    maxscore = score;//最高分
                }
                entity.setMaxscore(maxscore);
                entity.setFinallcsore(score);
                entity.setTotaltimes(guesstimes + 1);
                entity.setUpdatetime(new Date());
                guessUserRepository.save(entity);
                //更新
            } else {
                //新增
                entity.setId(UUID.generate());
                entity.setCompany(company);
                entity.setName(name);
                entity.setPhone(phone);
                entity.setTotaltimes(1);//总次数
                entity.setMaxscore(score);
                entity.setFinallcsore(score);
                entity.setCreattime(new Date());
                entity.setUpdatetime(new Date());
                guessUserRepository.save(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}