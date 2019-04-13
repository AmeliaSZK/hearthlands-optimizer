package hearthlandsoptimizer;

import java.util.List;

/**
 * A catch-all for buildings not yet implemented in this Java program.
 * 
 * @author Amélia @SlayZeKyriarchy
 *
 */
public class OtherBuilding extends Building {
    
    protected OtherBuilding(List<String> specs) {
        commonParser(specs);
        particularParser(specs);
    }
    
    @Override
    protected void particularParser(List<String> specs) {}

    @Override
    protected String particularToString() {
        return "";
    }
    
}
