package com.pagestags;

import static com.pagestags.Constants.PAGES_PATH;
import static com.pagestags.Constants.PASSWORD;
import static com.pagestags.Constants.SESSION_ID;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pagestags.cntr.HomeTmplCntr;
import com.pagestags.cntr.LoginValidateFrmCntr;
import com.pagestags.cntr.LogoutRdrcCntr;
import com.pagestags.cntr.PageDeleteConfirmationTmplCntr;
import com.pagestags.cntr.PageDeleteRdrcCntr;
import com.pagestags.cntr.PageListTmplCntr;
import com.pagestags.cntr.PageSaveFrmCntr;
import com.pagestags.cntr.PageViewTmplCntr;
import com.pagestags.db.PagesRepository;

import onl.andres.thinmvc.Application;
import onl.andres.thinmvc.cntr.BaseController;
import onl.andres.thinmvc.cntr.StaticController;

public class Main {

	private static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws Exception {

		logger.info("Accepted JVM parameters: 'pages_path', 'password'");
		logger.info("Current directory: {}", System.getProperty("user.dir"));

		System.setProperty(SESSION_ID, getSessionId());

		if (System.getProperty(PASSWORD) == null)
			System.setProperty(PASSWORD, "secret");

		if (System.getProperty(PAGES_PATH) == null)
			System.setProperty(PAGES_PATH, "pages");

		PagesRepository pagesRepo = new PagesRepository();
		pagesRepo.scanPages();

		Map<String, BaseController> controllers = new HashMap<>();

		final String PAGES_LIST = "/pages/list";

		controllers.put("/", new HomeTmplCntr("file://templates/home.vm"));

		controllers.put("/login", new StaticController("file://templates/login.html"));

		controllers.put("/login/validate", new LoginValidateFrmCntr(PAGES_LIST));

		controllers.put("/logout", new LogoutRdrcCntr(PAGES_LIST));

		controllers.put("/strapdown/([^/]*)$", new StaticController("file://files/strapdown/{file}"));
		controllers.put("/strapdown/themes/([^/]*)$", new StaticController("file://files/strapdown/themes/{file}"));


		controllers.put("/(favicon\\.ico)", new StaticController("file://files/favicon.ico"));

		PageListTmplCntr pageListTmplCntr = new PageListTmplCntr("file://templates/list.vm");
		controllers.put(PAGES_LIST, pageListTmplCntr);
		controllers.put(PAGES_LIST + "/(.*)", pageListTmplCntr);

		controllers.put("/pages/(.*)/view", new PageViewTmplCntr("file://templates/view.vm"));

		controllers.put("/pages/new", new StaticController("file://templates/new.html"));

		PageSaveFrmCntr pageSaveFrmCntr = new PageSaveFrmCntr("/pages/{id}/view");
		controllers.put("/pages/save", pageSaveFrmCntr);
		controllers.put("/pages/(.*)/save", pageSaveFrmCntr);

		controllers.put("/pages/(.*)/edit", new PageViewTmplCntr("file://templates/edit.vm"));

		controllers.put("/pages/(.*)/delete/confirmation",
				new PageDeleteConfirmationTmplCntr("file://templates/delete_confirmation.vm"));
		controllers.put("/pages/(.*)/delete", new PageDeleteRdrcCntr(PAGES_LIST));

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