package z.ivan.controller;

import z.ivan.model.dao.config.DbInitializer;
import z.ivan.service.impl.AccountService;
import z.ivan.service.impl.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RichestServlet extends HttpServlet {

    private static final AccountService ACCOUNT_SERVICE = AccountService.getInstance();
    private static final UserService USER_SERVICE = UserService.getInstance();


    @Override
    public void init() throws ServletException {
        super.init();
        DbInitializer.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String richestName = USER_SERVICE.getRichest();
        Long total = ACCOUNT_SERVICE.getTotal();

        req.setAttribute("name", richestName);
        req.setAttribute("total", total);

        RequestDispatcher view = req.getRequestDispatcher("index.jsp");
        view.forward(req, resp);
    }
}
