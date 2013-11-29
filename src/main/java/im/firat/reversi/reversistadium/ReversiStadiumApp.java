
package im.firat.reversi.reversistadium;


import im.firat.reversi.reversistadium.controllers.GameController;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;



public final class ReversiStadiumApp extends ResourceConfig {



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public ReversiStadiumApp() {

        packages(GameController.class.getPackage().getName());
        register(JacksonFeature.class);
    }
}
