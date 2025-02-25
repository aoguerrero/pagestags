package onl.andres.pt;

import static onl.andres.pt.PTParameters.PAGES_PATH;
import static onl.andres.pt.PTParameters.PASSWORD;
import static onl.andres.pt.PTParameters.SESSION_ID;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import onl.andres.pt.cntr.HomeTmplCntr;
import onl.andres.pt.cntr.LoginTmplCntr;
import onl.andres.pt.cntr.LoginValidateFrmCntr;
import onl.andres.pt.cntr.LogoutRdrcCntr;
import onl.andres.pt.cntr.PageDeleteConfirmationTmplCntr;
import onl.andres.pt.cntr.PageDeleteRdrcCntr;
import onl.andres.pt.cntr.PageListTmplCntr;
import onl.andres.pt.cntr.PageNewTmplCntr;
import onl.andres.pt.cntr.PageSaveFrmCntr;
import onl.andres.pt.cntr.PageViewTmplCntr;
import onl.andres.pt.db.PagesRepository;
import onl.andres.thinmvc.Application;
import onl.andres.thinmvc.cntr.BaseController;
import onl.andres.thinmvc.cntr.StaticController;

public class Main {

	private static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws Exception {

		logger.info("Accepted JVM parameters: '{}', '{}'", PAGES_PATH.getName(), PASSWORD.getName());

		System.setProperty(SESSION_ID.getName(), getSessionId());

		PagesRepository pagesRepo = new PagesRepository();
		pagesRepo.scanPages();

		Map<String, BaseController> controllers = new HashMap<>();

		controllers.put("/", new HomeTmplCntr("file://templates/home.vm"));

		controllers.put("/login", new LoginTmplCntr("file://templates/login.vm"));

		controllers.put("/login/validate", new LoginValidateFrmCntr("/pages/list"));

		controllers.put("/logout", new LogoutRdrcCntr("/pages/list"));

		controllers.put("/files/.*", new StaticController("file://files/"));

		controllers.put("/favicon\\.ico", new StaticController("file://files/favicon.ico"));

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
