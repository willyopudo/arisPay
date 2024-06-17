package org.arispay.utils;

import org.springframework.stereotype.Service;

@Service
public class GlobalHelpers {
    public boolean isNotBlank(Object obj) {
        if (obj == null) return false;
        if (obj instanceof String) {
            return !((String) obj).isEmpty();
        }
        return true;
    }
}
