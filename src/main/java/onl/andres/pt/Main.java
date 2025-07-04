package onl.andres.pt;

import static onl.andres.pt.AppParameters.PAGES_PATH;
import static onl.andres.pt.AppParameters.PASSWORD;
import static onl.andres.pt.AppParameters.SESSION_ID;
import static onl.andres.pt.AppParameters.USERNAME;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.Map;
import onl.andres.mvcly.Application;
import onl.andres.mvcly.cntr.BaseController;
import onl.andres.mvcly.cntr.StaticController;
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
import onl.andres.pt.db.PagesCache;
import onl.andres.pt.db.PagesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

  private static Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws Exception {

    logger.info(
        "Accepted JVM parameters: '{}', '{}', '{}'",
        PAGES_PATH.getName(),
        USERNAME.getName(),
        PASSWORD.getName());

    System.setProperty(SESSION_ID.getName(), getSessionId());

    PagesCache pagesCache = new PagesCache();
    pagesCache.addPages(PagesScanner.scanPages());

    Map<String, BaseController> controllers = new HashMap<>();

    final String PAGES_LIST = "/pages/list";
    final String PAGES_ROOT = "/";

    controllers.put("/", new HomeTmplCntr("file://templates/home.vm", pagesCache));

    controllers.put("/login", new LoginTmplCntr("file://templates/login.vm"));

    controllers.put("/login/validate", new LoginValidateFrmCntr(PAGES_ROOT));

    controllers.put("/logout", new LogoutRdrcCntr(PAGES_ROOT));

    controllers.put("/files/.*", new StaticController("file://files/"));

    controllers.put("/favicon\\.ico", new StaticController("file://files/favicon.ico"));

    PageListTmplCntr pageListTmplCntr =
        new PageListTmplCntr("file://templates/list.vm", pagesCache);
    controllers.put(PAGES_LIST, pageListTmplCntr);
    controllers.put(PAGES_LIST + "/(.*)", pageListTmplCntr);

    controllers.put("/pages/(.*)/view", new PageViewTmplCntr("file://templates/view.vm"));

    controllers.put("/pages/new/(.*)", new PageNewTmplCntr("file://templates/new.vm"));

    PageSaveFrmCntr pageSaveFrmCntr = new PageSaveFrmCntr("/pages/{id}/view", pagesCache);
    controllers.put("/pages/save", pageSaveFrmCntr);
    controllers.put("/pages/(.*)/save", pageSaveFrmCntr);

    controllers.put("/pages/(.*)/edit", new PageViewTmplCntr("file://templates/edit.vm"));

    controllers.put(
        "/pages/(.*)/delete/confirmation",
        new PageDeleteConfirmationTmplCntr("file://templates/delete_confirmation.vm"));
    controllers.put("/pages/(.*)/delete", new PageDeleteRdrcCntr(PAGES_LIST, pagesCache));

    new Application().start(controllers);
  }

  public static String getSessionId() {
    SecureRandom secureRandom = new SecureRandom();
    Encoder encoder = Base64.getUrlEncoder().withoutPadding();
    byte[] buffer = new byte[32];
    secureRandom.nextBytes(buffer);
    return encoder.encodeToString(buffer);
  }
}
