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

package com.guoanjia.business.lanfest.service;

import com.github.asherli0103.core.entity.AjaxJson;
import com.github.asherli0103.core.jpa.service.BaseService;
import com.guoanjia.business.lanfest.entity.Guess;
import com.guoanjia.business.lanfest.entity.GuessUser;
import redis.clients.jedis.JedisPool;

public interface GuessService extends BaseService<Guess,String> {
    void listGuess(JedisPool jedisPool);

    AjaxJson loginGuess(String phone);

}
