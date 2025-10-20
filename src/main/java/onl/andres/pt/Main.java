package onl.andres.pt;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.Map;
import onl.andres.mvcly.Application;
import onl.andres.mvcly.ctrl.BaseController;
import onl.andres.mvcly.ctrl.StaticController;
import onl.andres.pt.ctrl.HomeTemplateCtrl;
import onl.andres.pt.ctrl.LoginTemplateCtrl;
import onl.andres.pt.ctrl.LoginValidateFrmCtrl;
import onl.andres.pt.ctrl.LogoutRedirectCtrl;
import onl.andres.pt.ctrl.PageDeleteConfirmationTemplateCtrl;
import onl.andres.pt.ctrl.PageDeleteRedirectCtrl;
import onl.andres.pt.ctrl.PageListTemplateCtrl;
import onl.andres.pt.ctrl.PageNewTemplateCtrl;
import onl.andres.pt.ctrl.PageSaveFormCtrl;
import onl.andres.pt.ctrl.PageViewTemplateCtrl;
import onl.andres.pt.db.PagesCache;
import onl.andres.pt.db.PagesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static onl.andres.pt.AppParameters.*;

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

    final String ROOT_PATH = "/";

    controllers.put("/", new HomeTemplateCtrl("home.vm", pagesCache));

    controllers.put("/login", new LoginTemplateCtrl("login.vm"));

    controllers.put("/login/validate", new LoginValidateFrmCtrl(ROOT_PATH));

    controllers.put("/logout", new LogoutRedirectCtrl(ROOT_PATH));

    controllers.put("/files/.*", new StaticController(""));

    controllers.put("/favicon\\.ico", new StaticController("favicon.ico"));

    controllers.put("/pages/list(/.*)?", new PageListTemplateCtrl("list.vm", pagesCache));

    controllers.put("/pages/(.*)/view", new PageViewTemplateCtrl("view.vm"));

    controllers.put("/pages/new/(.*)", new PageNewTemplateCtrl("new.vm"));

    PageSaveFormCtrl pageSaveFormCtrl = new PageSaveFormCtrl("/pages/{id}/view", pagesCache);
    controllers.put("/pages/save", pageSaveFormCtrl);
    controllers.put("/pages/(.*)/save", pageSaveFormCtrl);

    controllers.put("/pages/(.*)/edit", new PageViewTemplateCtrl("edit.vm"));

    controllers.put(
        "/pages/(.*)/delete/confirmation",
        new PageDeleteConfirmationTemplateCtrl("delete_confirmation.vm"));
    controllers.put("/pages/(.*)/delete", new PageDeleteRedirectCtrl("/pages/list", pagesCache));

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
