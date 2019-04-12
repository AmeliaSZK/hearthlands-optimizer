package hearthlandsoptimizer;

/**
 * A catch-all for buildings not yet implemented in this Java program.
 * 
 * @author Amélia @SlayZeKyriarchy
 *
 */
public class OtherBuilding extends Building {
    
    protected OtherBuilding(String[] specs) {
        commonParser(specs);
        particularParser(specs);
    }
    
    @Override
    protected void particularParser(String[] particularSpecs) {}
    
}
