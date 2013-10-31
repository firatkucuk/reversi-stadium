
package im.firat.reversistadium.filters;

import im.firat.reversistadium.controllers.GameController;
import im.firat.reversistadium.controllers.RestController;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@WebFilter(urlPatterns = { "/", "/status", "/status/", "/start", "/start/", "/cancel/*", "/move/*" })
public class DispatcherFilter implements Filter {



    //~ --- [STATIC FIELDS/INITIALIZERS] -------------------------------------------------------------------------------

    private static final String VIEW_PREFIX = "/WEB-INF/views/";



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public DispatcherFilter() {

    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    @Override
    public void destroy() {

    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException,
        IOException {

        HttpServletRequest  httpRequest  = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String              path         = httpRequest.getServletPath();
        String              method       = httpRequest.getMethod();
        String              template     = null;

        if (path.equals("/")) { // -------------------------------------------------------------------------------------

            if (method.equalsIgnoreCase("GET")) {
                GameController controller = new GameController(httpRequest, httpResponse);
                template = controller.showIndex();
            } else {
                httpResponse.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
        } else if (path.equals("/status") || path.equals("/status/")) { // ---------------------------------------------

            if (method.equalsIgnoreCase("GET")) {
                RestController controller = new RestController(httpRequest, httpResponse);
                controller.showGameStatus();
            } else {
                httpResponse.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
        } else if (path.equals("/start") || path.equals("/start/")) { // -----------------------------------------------

            if (method.equalsIgnoreCase("POST")) {
                RestController controller = new RestController(httpRequest, httpResponse);
                controller.startGame();
            } else {
                httpResponse.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
        } else if (path.matches("/cancel/([a-zA-Z0-9]+/?)")) { // ------------------------------------------------------

            if (method.equalsIgnoreCase("DELETE")) {
                String         cancellationCode = path.replaceAll("/cancel/", "").replaceAll("/.*", "");
                RestController controller       = new RestController(httpRequest, httpResponse);

                controller.cancelGame(cancellationCode);
            } else {
                httpResponse.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
        } else if (path.matches("/move/([a-zA-Z0-9]+/[a-h][1-8]/?)")) { // ---------------------------------------------

            if (method.equalsIgnoreCase("PUT")) {
                String         temp       = path.replaceAll("/move/", "");
                String         authCode   = temp.replaceAll("/.*", "");
                String         move       = temp.replaceAll(".*/", "");
                RestController controller = new RestController(httpRequest, httpResponse);

                controller.movePiece(authCode, move);
            } else {
                httpResponse.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
        } else { // ----------------------------------------------------------------------------------------------------
            httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
        }        // end if-else

        if (template != null) {
            RequestDispatcher view = request.getRequestDispatcher(VIEW_PREFIX + template);
            view.forward(request, response);
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
}
