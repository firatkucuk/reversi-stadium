
package im.firat.reversistadium.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class GameController {



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    private final HttpServletRequest  request;
    private final HttpServletResponse response;



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public GameController(HttpServletRequest request, HttpServletResponse response) {

        this.request  = request;
        this.response = response;
    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    /**
     * Main index page controller
     *
     * @return  returns main index page
     */
    public String showIndex() {

        request.setAttribute("contextPath", request.getContextPath());

        return "index.jsp";
    }
}
