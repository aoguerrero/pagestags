package com.pagestags.cntr;

import java.util.HashMap;
import java.util.Map;

import com.pagestags.thinmvc.cntr.TemplateController;

public class LoginTmplCntr extends TemplateController {

	public LoginTmplCntr(String path) {
		super(path);
	}

	public Map<String, Object> getContext() {

		Map<String, Object> data = new HashMap<>();
		data.put("base_path", basePath);

		return data;
	}

}
