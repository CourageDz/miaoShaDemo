package com.dzy.demo.validator;

import com.dzy.demo.uitl.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.rmi.CORBA.Util;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

public class IsMobileVolidator implements ConstraintValidator<IsMobile,String> {
    private boolean required=false;

    public void initialize(IsMobile constraintAnnotation) {
        required=constraintAnnotation.required();

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(required){
            return ValidatorUtil.isMobile(s);
        }else{
            if(StringUtils.isEmpty(s)){
                return false;
            }else {
                return ValidatorUtil.isMobile(s);
            }
        }
    }
}
