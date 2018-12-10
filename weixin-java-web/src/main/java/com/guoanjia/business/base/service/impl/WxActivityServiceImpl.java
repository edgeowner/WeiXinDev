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

package com.guoanjia.business.base.service.impl;

import com.github.asherli0103.core.entity.AjaxJson;
import com.github.asherli0103.core.jpa.criteria.Criteria;
import com.github.asherli0103.core.jpa.criteria.Restrictions;
import com.github.asherli0103.core.jpa.domain.BaseRepository;
import com.github.asherli0103.core.jpa.service.impl.BaseServiceImpl;
import com.github.asherli0103.utils.ImageUtil;
import com.github.asherli0103.utils.tools.UUID;
import com.guoanjia.business.base.entity.WxActivity;
import com.guoanjia.business.base.entity.WxUser;
import com.guoanjia.business.base.entity.jpa.WxActivityRepository;
import com.guoanjia.business.poster.entity.jpa.WxUserRelationRepository;
import com.guoanjia.business.base.entity.jpa.WxUserRepository;
import com.guoanjia.business.base.service.WxActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;


/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@Service
@Transactional
public class WxActivityServiceImpl extends BaseServiceImpl<WxActivity, String> implements WxActivityService {

    private final WxActivityRepository wxActivityRepository;
    private final WxUserRepository wxUserRepository;

    @Autowired
    public WxActivityServiceImpl(BaseRepository<WxActivity, String> baseRepository, WxActivityRepository wxActivityRepository, WxUserRelationRepository wxUserRelationRepository, WxUserRepository wxUserRepository) {
        super(baseRepository);
        this.wxActivityRepository = wxActivityRepository;
        this.wxUserRepository = wxUserRepository;
    }

    /**
     * 根据条件查询活动信息
     *
     * @param wxActivity 根据条件查询活动信息
     * @return
     */
    @Override
    public AjaxJson findActivityByCondition(WxActivity wxActivity) {
        AjaxJson ajaxJson = new AjaxJson();
        Example<WxActivity> wxActivityExample = Example.of(wxActivity);
        WxActivity wxActivity1 = findOne(wxActivityExample);
        if (null == wxActivity1) {
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("活动不存在");
            return ajaxJson;
        }
        ajaxJson.setSuccess(true);
        ajaxJson.setMsg("活动存在");
        ajaxJson.setData(wxActivity1);
        return ajaxJson;
    }

    @Override
    public AjaxJson checkActivityByCondition(WxActivity wxActivity) {
        AjaxJson ajaxJson = new AjaxJson();
        Criteria<WxActivity> wxActivityCriteria = new Criteria<>();
        if (null != wxActivity.getId()) {
            wxActivityCriteria.add(Restrictions.eq("id", wxActivity.getId(), true));
        }
        if (null != wxActivity.getActName()) {
            wxActivityCriteria.add(Restrictions.eq("actName", wxActivity.getActName(), true));
        }
        if (null != wxActivity.getActStartTime()) {
            wxActivityCriteria.add(Restrictions.lt("actStartTime", wxActivity.getActStartTime(), true));
            wxActivityCriteria.add(Restrictions.gt("actEndTime", wxActivity.getActStartTime(), true));
        }
        List<WxActivity> wxActivities = findAll(wxActivityCriteria);
        if (null != wxActivities && !wxActivities.isEmpty() && wxActivities.size() != 0) {
            ajaxJson.setSuccess(true);
            ajaxJson.setMsg("活动存在");
            ajaxJson.setData(wxActivities.get(0));
            return ajaxJson;
        }
        ajaxJson.setSuccess(false);
        ajaxJson.setMsg("活动不存在");
        return ajaxJson;
    }

    @Override
    public AjaxJson existsActivityByCondition(WxActivity wxActivity) {
        AjaxJson ajaxJson = new AjaxJson();
        Example<WxActivity> wxActivityExample = Example.of(wxActivity);
        if (exists(wxActivityExample)) {
            ajaxJson.setSuccess(true);
            ajaxJson.setMsg("活动存在");
            return ajaxJson;
        }
        ajaxJson.setSuccess(false);
        ajaxJson.setMsg("活动不存在");
        return ajaxJson;
    }

    @Override
    public AjaxJson createPoster(String photo, File qrcode, String nickName, boolean islocal, String uId) {
        AjaxJson ajaxJson = new AjaxJson();
        BufferedImage backImg = null;
        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStream configInputStream = classLoader.getResourceAsStream("static/image/share.jpg");
        backImg = ImageUtil.loadImageLocal(configInputStream);

        BufferedImage photoImg = null;
        if (islocal) {
            photoImg = ImageUtil.loadImageLocal(photo);
        } else {
            String fileName = "";
            String OS = System.getProperty("os.name").toLowerCase();
            photoImg = ImageUtil.loadImageUrl(photo);
            if (OS.contains("linux")) {
                fileName = File.separator + "usr" + File.separator + "local" + File.separator + "kuaima" + File.separator + UUID.generate() + ".png";
                ImageUtil.writeImageFile(fileName, photoImg);
            } else {
                fileName = "d:" + File.separator + UUID.generate() + ".png";
                ImageUtil.writeImageFile(fileName, photoImg);
            }
            Criteria<WxUser> wxUserCriteria = new Criteria<>();
            wxUserCriteria.add(Restrictions.eq("id", uId, true));
            WxUser one = wxUserRepository.findOne(wxUserCriteria);
            wxUserRepository.save(one.setuHeadimgLocal(fileName));
        }
        BufferedImage qucodeImg = ImageUtil.loadImageLocal(qrcode);
        if (photoImg == null) {
            InputStream configInputStream1 = classLoader.getResourceAsStream("static/image/aa.jpg");
            photoImg = ImageUtil.loadImageLocal(configInputStream1);
        }
        BufferedImage temp = ImageUtil.superpositionImage(backImg, photoImg, 297, 396, 129, 129, true);
        temp = ImageUtil.superpositionImage(temp, qucodeImg, 263, 855, 216, 216, true);
        temp = ImageUtil.modifyImage(temp, nickName, 310, 557, "微软雅黑", Font.BOLD, 20, Color.white, Color.white);

        File tempFile = ImageUtil.writeImageTempFile(UUID.generate(), temp, "png");

        ajaxJson.setData(tempFile);
        return ajaxJson;
    }


    @Override
    public void saveActivity() {
        WxActivity wxActivity = new WxActivity();
        wxActivity.setActName("aaaa");
        wxActivity.setActBackimg("ddd");
        wxActivity.setActEndCondition("10000");
        wxActivity.setActEndTime(new Date());
        wxActivity.setActMaxAmount(11d);
        wxActivity.setActMinAmount(12d);
        wxActivity.setActRule("dddd");
        save(wxActivity);
    }


}
