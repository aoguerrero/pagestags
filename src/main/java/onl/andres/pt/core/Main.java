package onl.andres.pt.core;

import onl.andres.mvcly.core.Application;
import onl.andres.mvcly.ctrl.BaseController;
import onl.andres.mvcly.ctrl.StaticController;
import onl.andres.pt.ctrl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static onl.andres.pt.core.AppParameters.*;

public class Main {

  private static Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws Exception {

    logger.info(
        "Accepted JVM parameters: '{}', '{}', '{}'",
        PAGES_PATH.getName(),
        USERNAME.getName(),
        PASSWORD.getName());

    logger.info("Pages path: {}", PAGES_PATH.get());

    System.setProperty(SESSION_ID.getName(), UUID.randomUUID().toString());

    PagesTagsCtx ctx = new PagesTagsCtx();

    Map<String, BaseController> controllers = new HashMap<>();

    final String ROOT_PATH = "/";

    controllers.put("/", new HomeTemplateCtrl("home.vm", ctx));

    controllers.put("/login", new LoginTemplateCtrl("login.vm", ctx));

    controllers.put("/login/validate", new LoginValidateFormCtrl(ROOT_PATH));

    controllers.put("/logout", new LogoutRedirectCtrl(ROOT_PATH));

    controllers.put("/files/.*", new StaticController(""));

    controllers.put("/favicon\\.ico", new StaticController("favicon.ico"));

    controllers.put("/pages/list(/.*)?", new PageListTemplateCtrl("list.vm", ctx));

    controllers.put("/pages/(.*)/view", new PageViewTemplateCtrl("view.vm", ctx));

    controllers.put("/pages/new/(.*)", new PageNewTemplateCtrl("new.vm", ctx));

    PageSaveFormCtrl pageSaveFormCtrl = new PageSaveFormCtrl("/pages/{id}/view", ctx);
    controllers.put("/pages/save", pageSaveFormCtrl);
    controllers.put("/pages/(.*)/save", pageSaveFormCtrl);

    controllers.put("/pages/(.*)/edit", new PageViewTemplateCtrl("edit.vm", ctx));

    controllers.put("/pages/(.*)/delete/confirmation",
            new PageDeleteConfirmationTemplateCtrl("delete_confirmation.vm", ctx));
    controllers.put("/pages/(.*)/delete", new PageDeleteRedirectCtrl("/pages/list", ctx));

    new Application().start(controllers);
  }
}
