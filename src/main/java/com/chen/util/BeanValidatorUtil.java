package com.chen.util;

import com.chen.exception.ValidateException;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * BeanValidatorUtil
 * @Author LeifChen
 * @Date 2018-04-11
 */
public class BeanValidatorUtil {

    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    /**
     * 校验对象
     * @param t
     * @param groups
     * @param <T>
     * @return
     */
    public static <T> Map<String, String> validateObject(T t, Class... groups) {
        Validator validator = validatorFactory.getValidator();
        Set validateResult = validator.validate(t, groups);
        if (validateResult.isEmpty()) {
            return Collections.emptyMap();
        } else {
            LinkedHashMap<String, String> errors = Maps.newLinkedHashMap();
            for (Object object : validateResult) {
                ConstraintViolation violation = (ConstraintViolation) object;
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }

            return errors;
        }
    }

    /**
     * 校验对象列表
     * @param collection
     * @return
     */
    public static Map<String, String> validateList(Collection<?> collection) {
        Preconditions.checkNotNull(collection);
        Iterator iterator = collection.iterator();
        Map<String, String> errors;

        do {
            if (!iterator.hasNext()) {
                return Collections.emptyMap();
            }
            Object object = iterator.next();
            errors = validateObject(object);
        } while (errors.isEmpty());

        return errors;
    }

    /**
     * 校验方法
     * @param first
     * @param objects
     * @return
     */
    public static Map<String, String> validate(Object first, Object... objects) {
        if (objects != null && objects.length > 0) {
            return validateList(Lists.asList(first, objects));
        } else {
            return validateObject(first);
        }
    }

    /**
     * 校验对象
     * @param param
     * @throws ValidateException
     */
    public static void check(Object param) throws ValidateException {
        Map<String, String> map = BeanValidatorUtil.validate(param);
        if (MapUtils.isNotEmpty(map)) {
            throw new ValidateException(map.toString());
        }
    }
}
