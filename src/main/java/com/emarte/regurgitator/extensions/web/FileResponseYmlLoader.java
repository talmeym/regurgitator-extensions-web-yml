package com.emarte.regurgitator.extensions.web;

import com.emarte.regurgitator.core.*;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.SOURCE;
import static com.emarte.regurgitator.core.YmlConfigUtil.*;
import static com.emarte.regurgitator.extensions.web.WebConfigConstants.PATH_PREFIX;

public class FileResponseYmlLoader implements YmlLoader<FileResponse> {
	private static Log log = Log.getLog(FileResponseYmlLoader.class);

	@Override
	public FileResponse load(Yaml yaml, Set<Object> allIds) throws RegurgitatorException {
		String id = loadId(yaml, allIds);
		String source = loadMandatoryStr(yaml, SOURCE);
		String pathPrefix = loadOptionalStr(yaml, PATH_PREFIX);

		ContextLocation location = source != null ? new ContextLocation(source) : null;
		log.debug("Loaded file response '" + id + "'");
		return new FileResponse(id, new ValueSource(location, null), pathPrefix);
	}
}
