
package im.firat.reversistadium.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class GameController {



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    private HttpServletRequest  request;
    private HttpServletResponse response;



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public GameController(HttpServletRequest request, HttpServletResponse response) {

        this.request  = request;
        this.response = response;
    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public String showIndex() {

        request.setAttribute("contextPath", request.getContextPath());

        return "index.jsp";
    }
}
