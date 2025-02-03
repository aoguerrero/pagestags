package com.pagestags;

import static com.pagestags.PagestagsParameters.PAGES_PATH;
import static com.pagestags.PagestagsParameters.PASSWORD;
import static com.pagestags.PagestagsParameters.SESSION_ID;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pagestags.cntr.HomeTmplCntr;
import com.pagestags.cntr.LoginTmplCntr;
import com.pagestags.cntr.LoginValidateFrmCntr;
import com.pagestags.cntr.LogoutRdrcCntr;
import com.pagestags.cntr.PageDeleteConfirmationTmplCntr;
import com.pagestags.cntr.PageDeleteRdrcCntr;
import com.pagestags.cntr.PageListTmplCntr;
import com.pagestags.cntr.PageNewTmplCntr;
import com.pagestags.cntr.PageSaveFrmCntr;
import com.pagestags.cntr.PageViewTmplCntr;
import com.pagestags.db.PagesRepository;
import com.pagestags.thinmvc.Application;
import com.pagestags.thinmvc.cntr.BaseController;
import com.pagestags.thinmvc.cntr.StaticController;

public class Main {

	private static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws Exception {

		logger.info("Accepted JVM parameters: '{}', '{}'", PAGES_PATH.getName(), PASSWORD.getName());
		logger.info("Current directory: {}", System.getProperty("user.dir"));

		System.setProperty(SESSION_ID.getName(), getSessionId());

		PagesRepository pagesRepo = new PagesRepository();
		pagesRepo.scanPages();

		Map<String, BaseController> controllers = new HashMap<>();

		controllers.put("/", new HomeTmplCntr("file://templates/home.vm"));

		controllers.put("/login", new LoginTmplCntr("file://templates/login.vm"));

		controllers.put("/login/validate", new LoginValidateFrmCntr("/pages/list"));

		controllers.put("/logout", new LogoutRdrcCntr("/pages/list"));

		controllers.put("/files/(.*)", new StaticController("file://files/{file}"));

		controllers.put("/(favicon\\.ico)", new StaticController("file://files/favicon.ico"));

		PageListTmplCntr pageListTmplCntr = new PageListTmplCntr("file://templates/list.vm");
		controllers.put("/pages/list", pageListTmplCntr);
		controllers.put("/pages/list/(.*)", pageListTmplCntr);

		controllers.put("/pages/(.*)/view", new PageViewTmplCntr("file://templates/view.vm"));

		controllers.put("/pages/new/(.*)", new PageNewTmplCntr("file://templates/new.vm"));

		PageSaveFrmCntr pageSaveFrmCntr = new PageSaveFrmCntr("/pages/{id}/view");
		controllers.put("/pages/save", pageSaveFrmCntr);
		controllers.put("/pages/(.*)/save", pageSaveFrmCntr);

		controllers.put("/pages/(.*)/edit", new PageViewTmplCntr("file://templates/edit.vm"));

		controllers.put("/pages/(.*)/delete/confirmation",
				new PageDeleteConfirmationTmplCntr("file://templates/delete_confirmation.vm"));
		controllers.put("/pages/(.*)/delete", new PageDeleteRdrcCntr("/pages/list"));

		Application.start(controllers);
	}

	public static String getSessionId() {
		SecureRandom secureRandom = new SecureRandom();
		Encoder encoder = Base64.getUrlEncoder().withoutPadding();
		byte[] buffer = new byte[32];
		secureRandom.nextBytes(buffer);
		return encoder.encodeToString(buffer);
	}
}
