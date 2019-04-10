package hearthlandsoptimizer;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A building that produce {@link Resource}.
 * 
 * @author Amélia @SlayZeKyriarchy
 *
 */
public class Producer extends Building {
    private static final int MAX_RECURSIVE_CALLS    = 1;
    private static final int UNCOMPUTED_TOTAL_STAFF = -1;
    
    private ResourceMultiset consumption;
    private ResourceMultiset production;
    
    private String name;
    private int    loadsPerYear;
    private int    localStaff;
    
    private int              totalStaff;
    private ProducerMultiset completeChain;
    
    private Set<Producer> culture;
    
    private int nbOfRecursiveCalls;
    
    private Producer(ProducerBuilder builder) {
        this.name = builder.name;
        this.loadsPerYear = builder.loadsPerYear;
        this.localStaff = builder.localStaff;
        
        totalStaff = UNCOMPUTED_TOTAL_STAFF;
        completeChain = new ProducerMultiset();
    }
    
    public static class ProducerBuilder {
        // Required parameters
        private String name;
        private int    loadsPerYear;
        private int    localStaff;
        
        // Optional parameters
        private ResourceMultiset consumption;
        private ResourceMultiset production;
        
        /**
         * Basic constructor. Will throw an AssertionError if {@code produces}
         * isn't called before {@code build()}.
         * 
         * @param name         Name of the building.
         * @param loadsPerYear Loads produced per year.
         * @param localStaff   Staff needed for this producer, and only this
         *                     producer.
         */
        public ProducerBuilder(String name, int loadsPerYear, int localStaff) {
            this.name = name;
            this.loadsPerYear = loadsPerYear;
            this.localStaff = localStaff;
            
            this.consumption = new ResourceMultiset();
            this.production = new ResourceMultiset();
        }
        
        public ProducerBuilder consumes(Resource resType, int count) {
            this.consumption.add(resType, count);
            return this;
        }
        
        public ProducerBuilder produces(Resource resType, int count) {
            this.production.add(resType, count);
            return this;
        }
        
        /**
         * @return A Producer.
         * @throws AssertionError if nothing is produced.
         */
        public Producer build() {
            assert !production.isEmpty() : "A Producer must produce something.";
            return new Producer(this);
        }
        
    } // End class ProducerBuilder
    
    public boolean isTotalStaffComputed() {
        return totalStaff != UNCOMPUTED_TOTAL_STAFF;
    }
    
    public boolean isCompleteChainComputed() {
        return !completeChain.isEmpty();
        /*
         * A complete chain must include this instance, so an empty chain cannot
         * have been computed yet.
         */
    }
    
    public boolean isProducing(Resource resource) {
        return production.containsKey(resource);
    }
    
    public void resetDependencies() {
        totalStaff = UNCOMPUTED_TOTAL_STAFF;
        completeChain.clear();
    }
    
    public void setCulture(Collection<Producer> availableProducers) {
        /*
         * TODO Make a proper Culture class.
         */
        culture = Set.copyOf(availableProducers);
        resetDependencies();
    }
    
    public int getLocalStaff() {
        return localStaff;
    }
    
    public int getTotalStaff() {
        if (isTotalStaffComputed() == false) {
            this.computeTotalStaff();
        }
        
        return totalStaff;
    }
    
    public ProducerMultiset getCompleteChain() {
        if (isCompleteChainComputed() == false) {
            this.computeCompleteChain();
        }
        
        return completeChain;
    }
    
    /**
     * Not yet correct, as it doesn't take into account the required amount of
     * resources.
     * 
     * @throws AssertError if there is a loop in the production chain.
     */
    public void computeCompleteChain() {
        assert culture != null : "Culture must be set before computing chain.";
        nbOfRecursiveCalls++;
        assert nbOfRecursiveCalls <= MAX_RECURSIVE_CALLS : String.format(
                "There was a loop when computing chain for %s!", this.name);
        
        if (!consumption.isEmpty()) {
            /*
             * An empty consumption is the halt condition for this recursive
             * exercize. computeCompleteChain() calls getCompleteChain(), which
             * in turn will call computeCompleteChain() if the chain hasn't yet
             * been computed.
             * 
             * This recursive loop should finish as long as there is no cycle in
             * the production scheme. Ie, if A needs B, B needs C, and C needs
             * A, there is a cycle.
             * 
             * To guard against this, nbOfRecursiveCalls and MAX_RECURSIVE_CALLS
             * were introduced. nbOfRecursiveCalls will increment every time
             * this method is called, and checked against MAX_RECURSIVE_CALLS.
             * If the count is more than the max, then an AssertError will be
             * thrown. The count is set back to 0 at the end of this method.
             */
            for (Entry<Resource, Integer> entry : consumption.entrySet()) {
                Resource neededResource = entry.getKey();
                Producer supplier       = null;
                
                for (Producer producer : culture) {
                    if (producer.isProducing(neededResource)) {
                        supplier = producer;
                    }
                }
                
                assert supplier != null : String.format(
                        "No supplier of %s found for %s.", neededResource,
                        this.name);
                
                completeChain.addAll(supplier.getCompleteChain());
            }
        }
        
        completeChain.add(this, 1);
        nbOfRecursiveCalls = 0;
        
    }
    
    public void computeTotalStaff() {
        if (isCompleteChainComputed() == false) {
            computeCompleteChain();
        }
        
        int cumulatedStaff = 0;
        /*
         * This instance is included in completeChain, so we initialize to 0.
         */
        
        for (Entry<Producer, Integer> entry : completeChain.entrySet()) {
            cumulatedStaff += entry.getKey().getLocalStaff() * entry.getValue();
        }
        
    }
    
}
