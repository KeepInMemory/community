package com.it.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class CommunityUtil {
    //生成字符串
    public static String generateUUID() {
        //UUID.randomUUID().toString()里有横杠,不需要的话就替换成空字符串
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /*
    MD5加密
    MD5只能加密不能解密,黑客都有简单字符串对应的MD5库
    我们将密码+随机字符串 这样黑客的库里就没有这样的简单密码
    随机字符串记录在salt字段里
    */
    public static String md5(String key) {
        //key为null "" "   "都返回true
        if(StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
