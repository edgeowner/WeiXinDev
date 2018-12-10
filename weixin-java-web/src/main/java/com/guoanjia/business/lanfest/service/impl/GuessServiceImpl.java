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

import com.github.asherli0103.core.entity.AjaxJson;
import com.github.asherli0103.core.jpa.criteria.Criteria;
import com.github.asherli0103.core.jpa.criteria.Restrictions;
import com.github.asherli0103.core.jpa.domain.BaseRepository;
import com.github.asherli0103.core.jpa.service.impl.BaseServiceImpl;
import com.github.asherli0103.utils.JacksonUtil;
import com.guoanjia.business.lanfest.entity.Guess;
import com.guoanjia.business.lanfest.entity.GuessUser;
import com.guoanjia.business.lanfest.entity.jpa.GuessRepository;
import com.guoanjia.business.lanfest.entity.jpa.GuessUserRepository;
import com.guoanjia.business.lanfest.entity.model.GuessModel;
import com.guoanjia.business.lanfest.service.GuessService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class GuessServiceImpl extends BaseServiceImpl<Guess, String> implements GuessService {

    private final GuessRepository guessRepository;

    private final GuessUserRepository guessUserRepository;

    public GuessServiceImpl(BaseRepository<Guess, String> baseRepository, GuessRepository guessRepository, GuessUserRepository guessUserRepository) {
        super(baseRepository);
        this.guessRepository = guessRepository;
        this.guessUserRepository = guessUserRepository;
    }

    @Override
    public void listGuess(JedisPool jedisPool){
        List<Guess> guesses = guessRepository.findAll();
        if (guesses.size() > 0) {
            int j=0;
            for (Guess guess : guesses) {
                String keyboard = geneChar(guess.getResult());
                GuessModel guessModel = new GuessModel().setId(Integer.valueOf(guess.getId()))
                        .setGuess(guess.getGuess()).setKeyboard(keyboard).setResult(guess.getResult())
                        .setTitle(guess.getTitle());
                try(Jedis jedis = jedisPool.getResource()) {
                    jedis.set("guessModel" + j, JacksonUtil.toJSONString(guessModel));
                }
                j++;
            }

        }
    }


    @Override
    public AjaxJson loginGuess(String phone) {
        AjaxJson ajaxJson = new AjaxJson();

        Map<String, Object> map = new LinkedHashMap<>();

        Sort.Order order = new Sort.Order("RAND()");
        Sort sort = new Sort(order);
        Pageable pageable = new PageRequest(0, 15, sort);
        List<Guess> guesses = guessRepository.findAll(pageable).getContent();

        Criteria<GuessUser> guessUserCriteria = new Criteria<>();
        guessUserCriteria.add(Restrictions.eq("phone", phone, true));
        GuessUser guessUser = guessUserRepository.findOne(guessUserCriteria);
        if (Objects.isNull(guessUser)) {
            map.put("userinfo", null);
        }
        map.put("userinfo", guessUser);
        if (guesses.size() > 0) {
            List<GuessModel> guessModels = new ArrayList<>();
            for (Guess guess : guesses) {
                String keyboard = geneChar(guess.getResult());
                GuessModel guessModel = new GuessModel().setId(Integer.valueOf(guess.getId()))
                        .setGuess(guess.getGuess()).setKeyboard(keyboard).setResult(guess.getResult())
                        .setTitle(guess.getTitle());
                guessModels.add(guessModel);
            }
            map.put("guessinfo", guessModels);
        }

        Long maxCount = guessUserRepository.count(Example.of(new GuessUser().setMaxscore("15")));
        if (maxCount >= 400) {
            ajaxJson.setCode(201);
            ajaxJson.setMsg("很遗憾,奖品已发送完毕。");
            ajaxJson.setSuccess(false);
            return ajaxJson;
        }

        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String systemtime = df.format(new Date());
        String starttime = "20170210";
        int result = systemtime.compareTo(starttime);
        if (result < 0) {
            ajaxJson.setCode(201);
            ajaxJson.setMsg("活动尚未开始，请耐心等待。");
            ajaxJson.setSuccess(false);
            return ajaxJson;
        }
        ajaxJson.setCode(200);
        ajaxJson.setObj(map);
        ajaxJson.setSuccess(true);
        ajaxJson.setMsg("猜灯谜登录成功");
        return ajaxJson;
    }

    private String geneChar(String result) {
        StringBuffer str = new StringBuffer();
        ArrayList<Character> charList = new ArrayList<>();
        char[] resultArr = result.toCharArray();
        //答案
        for (char aResultArr : resultArr) {
            charList.add(aResultArr);
        }
        int length = 18 - charList.size();
        //随机汉字
        for (int i = 0; i < length; i++) {
            //System.out.print(getRandomChar() + "  ");
            charList.add(getRandomChar());
        }
        Collections.shuffle(charList);
        //拼接字符串
        for (Object aCharList : charList) {
            str = str.append(aCharList);
        }
        return str.toString();
    }

    private static char getRandomChar() {
        String str = "";
        int hightPos; //
        int lowPos;

        Random random = new Random();
        hightPos = (176 + Math.abs(random.nextInt(39)));
        lowPos = (161 + Math.abs(random.nextInt(93)));

        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(hightPos)).byteValue();
        b[1] = (Integer.valueOf(lowPos)).byteValue();

        try {
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("错误");
        }
        return str.charAt(0);
    }

}