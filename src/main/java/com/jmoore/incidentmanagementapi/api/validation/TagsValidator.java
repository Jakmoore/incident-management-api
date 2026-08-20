package com.jmoore.incidentmanagementapi.api.validation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class TagsValidator {

    public boolean isValid(String[] tags) {
        if (tags == null || tags.length == 0) {
            return false;
        }

        for (String tag : tags) {
            if (StringUtils.isBlank(tag)) {
                return false;
            }
        }

        return true;
    }
}
