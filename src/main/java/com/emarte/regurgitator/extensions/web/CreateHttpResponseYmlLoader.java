/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.extensions.web;

import com.emarte.regurgitator.core.*;

import java.util.Set;

import static com.emarte.regurgitator.core.YmlConfigUtil.loadOptionalStr;
import static com.emarte.regurgitator.extensions.web.ExtensionsWebConfigConstants.CONTENT_TYPE;
import static com.emarte.regurgitator.extensions.web.ExtensionsWebConfigConstants.STATUS_CODE;

public class CreateHttpResponseYmlLoader implements YmlLoader<CreateHttpResponse> {
    private final CreateResponseYmlLoader responseJsonLoader = new CreateResponseYmlLoader();

    @Override
    public CreateHttpResponse load(Yaml yaml, Set<Object> allIds) throws RegurgitatorException {
        CreateResponse response = (CreateResponse) responseJsonLoader.load(yaml, allIds);
        String statusCodeStr = loadOptionalStr(yaml, STATUS_CODE);
        long statusCode = statusCodeStr != null ? Long.parseLong(statusCodeStr) : -1L;
        String contentType = loadOptionalStr(yaml, CONTENT_TYPE);
        return new CreateHttpResponse(response, statusCode, contentType);
    }
}
