package com.guoanjia.business.utils.qiniu;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Created by cuiss on 2017/3/21.
 */
public class CdnUtil {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(CdnUtil.class);

    //设置好账号的ACCESS_KEY和SECRET_KEY
    private static final String ACCESS_KEY = "HrfSUQLnauuNKiErBrP_lBGlPBTfWQlqZqhP76-a";
    private static final String SECRET_KEY = "nvmXyeEhKRCA2o7LAthnM37uy-vfdAmRsfCr7yLt";

    //要上传的空间
    private static final String bucketname = "guoanjia";
    /**
     * 文件上传到cdn上，当前用的是七牛的cdn加速
     * @param newfilename
     * @param dirPath
     * @return
     */
    public static boolean cdnUploadFile(String newfilename, File dirPath){
        //第二种方式: 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);
        //创建上传对象
        UploadManager uploadManager = new UploadManager(c);
        //密钥配置
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String token  =  auth.uploadToken(bucketname);
        Response res = null;
        try {
            res = uploadManager.put(dirPath, newfilename, token);
        } catch (QiniuException e) {
            e.printStackTrace();
            return false;
        }
        if(res.statusCode==200){
            return true;
        }else {
            logger.error("qiniu:error 七牛上传文件出错！");
            return false;
        }
    }
}
