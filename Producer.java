package hearthlandsoptimizer;

/**
 * A building that produce {@link Resource}.
 * 
 * @author Amélia @SlayZeKyriarchy
 *
 */
public class Producer extends Building {
    
    private ResourceMultiset consumption;
    private ResourceMultiset production;
    
    private String name;
    private int    loadsPerYear;
    private int    localStaff;
    private int    totalStaff;
    
    private ProducerMultiset completeChain;
    
    
}
