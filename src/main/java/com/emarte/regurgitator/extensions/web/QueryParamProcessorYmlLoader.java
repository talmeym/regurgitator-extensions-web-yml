/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.extensions.web;

import com.emarte.regurgitator.core.*;

import java.util.Set;

import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.YmlConfigUtil.loadMandatoryStr;
import static com.emarte.regurgitator.extensions.web.ExtensionsWebConfigConstants.KEY;

public class QueryParamProcessorYmlLoader implements YmlLoader<QueryParamProcessor> {
    private static final Log log = getLog(QueryParamProcessorYmlLoader.class);

    @Override
    public QueryParamProcessor load(Yaml yaml, Set<Object> allIds) throws RegurgitatorException {
        log.debug("Loaded QueryParamProcessor");
        return new QueryParamProcessor(loadMandatoryStr(yaml, KEY));
    }
}
