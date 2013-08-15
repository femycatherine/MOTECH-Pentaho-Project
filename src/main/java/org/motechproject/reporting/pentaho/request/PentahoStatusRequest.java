package org.motechproject.reporting.pentaho.request;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class PentahoStatusRequest extends PentahoRequest {

    @Override
    public String toQueryString() {
        List<String> queryParams = new ArrayList<>();

        if (xml != null) {
            queryParams.add(concat("xml", xml));
        }

        return StringUtils.join(queryParams, "&");
    }
}
