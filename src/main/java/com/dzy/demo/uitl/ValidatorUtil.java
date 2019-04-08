package com.dzy.demo.uitl;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {
    private static Logger log= LoggerFactory.getLogger(ValidatorUtil.class);
    private static final Pattern mobile_pattern=Pattern.compile("1\\d{10}");

    public static boolean isMobile(String src){
        if(StringUtils.isEmpty(src)){
            return false;
        }
        Matcher m=mobile_pattern.matcher(src);
        log.info("isMatcher:"+m.matches());
        return m.matches();
    }
}
