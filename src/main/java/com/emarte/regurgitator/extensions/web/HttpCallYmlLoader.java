/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.extensions.web;

import com.emarte.regurgitator.core.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.STEPS;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.YmlConfigUtil.*;
import static com.emarte.regurgitator.extensions.web.WebConfigConstants.*;
import static java.lang.Integer.parseInt;

public class HttpCallYmlLoader implements YmlLoader<Step> {
    private static final Log log = getLog(HttpCallYmlLoader.class);
    private static final YmlLoaderUtil<YmlLoader<Step>> loaderUtil = new YmlLoaderUtil<YmlLoader<Step>>();

    @Override
    public Step load(Yaml yaml, Set<Object> allIds) throws RegurgitatorException {
        String id = loadId(yaml, allIds);
        List<Step> steps = new ArrayList<Step>();
        List stepYamls = (List) yaml.get(STEPS);

        if(stepYamls != null) {
            for (Object obj : stepYamls) {
                Yaml stepYaml = new Yaml((Map) obj);
                steps.add(loaderUtil.deriveLoader(stepYaml).load(stepYaml, allIds));
            }
        }

        String username = loadOptionalStr(yaml, USERNAME);
        String password = loadOptionalStr(yaml, PASSWORD);

        if((username == null && password != null) || (username != null && password == null)) {
            throw new RegurgitatorException("Both username and password (or neither) required");
        }

        log.debug("Loaded HttpCall '{}'", id);
        return new HttpCall(id, new HttpMessageProxy(new HttpClientWrapper(loadMandatoryStr(yaml, HOST), parseInt(loadMandatoryStr(yaml, PORT)), username, password)), steps);
    }
}
