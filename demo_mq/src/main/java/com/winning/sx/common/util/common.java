package com.winning.sx.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by smq on 16/6/30.
 */
public class common {
    //返回默认属性文件的属性值
    public static String GetConfigValue(String propName) throws IOException {
        String  propValue="";
        InputStream path1 =Thread.currentThread().getContextClassLoader().getResourceAsStream("datasource1-context.properties");
        Properties pro = new Properties();//属性集合对象
        pro.load(path1);
        propValue = pro.getProperty(propName);
        return  propValue;
    }
}
