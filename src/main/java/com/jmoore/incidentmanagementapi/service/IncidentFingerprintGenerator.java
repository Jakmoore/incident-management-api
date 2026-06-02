package com.jmoore.incidentmanagementapi.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class IncidentFingerprintGenerator {

    public String generate(String toEncode) {
        return DigestUtils.sha256Hex(toEncode);
    }
}
