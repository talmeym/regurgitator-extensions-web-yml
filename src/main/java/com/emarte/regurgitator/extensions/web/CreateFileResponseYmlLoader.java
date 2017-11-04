/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.extensions.web;

import com.emarte.regurgitator.core.*;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.SOURCE;
import static com.emarte.regurgitator.core.YmlConfigUtil.*;
import static com.emarte.regurgitator.extensions.web.ExtensionsWebConfigConstants.PATH_PREFIX;

public class CreateFileResponseYmlLoader implements YmlLoader<CreateFileResponse> {
    private static final Log log = Log.getLog(CreateFileResponseYmlLoader.class);

    @Override
    public CreateFileResponse load(Yaml yaml, Set<Object> allIds) throws RegurgitatorException {
        String id = loadId(yaml, allIds);
        String source = loadMandatoryStr(yaml, SOURCE);
        String pathPrefix = loadOptionalStr(yaml, PATH_PREFIX);
        ContextLocation location = source != null ? new ContextLocation(source) : null;
        log.debug("Loaded file response '{}'", id);
        return new CreateFileResponse(id, new ValueSource(location, null), pathPrefix);
    }
}
