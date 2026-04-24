package hooks;

import base.Baseclass;
import io.cucumber.java.Before;
import utils.ConfigReader;

public class Hooks extends Baseclass {

    @Before
    public void beforeScenario() {
        ConfigReader.loadConfig();
        setup();
    }
}