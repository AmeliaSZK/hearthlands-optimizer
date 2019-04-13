package hearthlandsoptimizer;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A building that produce {@link Resource}.
 * 
 * @author Amélia @SlayZeKyriarchy
 *
 */
public class Producer extends Building {
    
    private static final int MAX_RECURSIVE_CALLS    = 1;
    private static final int UNCOMPUTED_TOTAL_STAFF = -1;
    
    private int              loadsPerYear;
    private ResourceMultiset consumption;
    private ResourceMultiset production;
    
    private ProducerMultiset completeChain;
    
    private int nbOfRecursiveCalls;
    
    private Producer(ProducerBuilder builder) {
        this.name = builder.name;
        this.loadsPerYear = builder.loadsPerYear;
        this.localStaff = builder.localStaff;
        
        totalStaff = UNCOMPUTED_TOTAL_STAFF;
        completeChain = new ProducerMultiset();
    }
    
    protected Producer(List<String> specs) {
        commonParser(specs);
        particularParser(specs);
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
    
    @Override
    public int getTotalStaff() {
        if (isTotalStaffComputed() == false) {
            this.computeTotalStaff();
        }
        
        return totalStaff;
    }
    
    private Map<Resource, Float> getProduction() {
        return Map.copyOf(production);
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
            for (Entry<Resource, Float> supply : consumption.entrySet()) {
                Resource resourceNeeded  = supply.getKey();
                Float    suppliesNeeded  = supply.getValue();
                Producer supplier        = null;
                Float    suppliersNeeded = 0f;
                
                for (Building building : allBuildings) {
                    if (building instanceof Producer) {
                        Producer producer = (Producer) building;
                        if (producer.isProducing(resourceNeeded)) {
                            supplier = producer;
                            Float suppliesProduced = supplier.getProduction()
                                    .get(resourceNeeded);
                            suppliersNeeded = suppliesNeeded / suppliesProduced;
                        }
                    }
                }
                
                assert supplier != null : String.format(
                        "No supplier of %s found for %s.", resourceNeeded,
                        this.name);
                
                completeChain.addAll(supplier.getCompleteChain(), suppliersNeeded);
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
        
        for (Entry<Producer, Float> supplier : completeChain.entrySet()) {
            cumulatedStaff += supplier.getKey().getLocalStaff()
                    * supplier.getValue();
        }
        
        totalStaff = cumulatedStaff;
        
    }
    
    @Override
    protected void particularParser(List<String> particularSpecs) {
        this.loadsPerYear = Integer.valueOf(particularSpecs.get(LOADS_COLUMN));
        
        consumption = new ResourceMultiset();
        parseResources(consumption, particularSpecs.get(CONSUMPTION_COLUMN));
        production = new ResourceMultiset();
        parseResources(production, particularSpecs.get(PRODUCTION_COLUMN));
        
        totalStaff = UNCOMPUTED_TOTAL_STAFF;
        completeChain = new ProducerMultiset();
    }
    
    private void parseResources(ResourceMultiset direction,
            String specifications) {
        if (!specifications.isBlank()) {
            String[] specs = specifications.split(" +");
            assert specs.length
                    % 2 == 0 : "Needs an even number of fields in Consumption and Production columns.";
            
            // Notice the i=i+2 instead of i++
            for (int i = 0; i < specs.length - 1; i = i + 2) {
                Float    count    = Float.valueOf(specs[i]) * loadsPerYear;
                Resource resource = Resource.valueOf(specs[i + 1]);
                direction.add(resource, count);
            } // End for all pairs of fields
        } // End if specifications aren't empty
    } // End parseResources
    
    @Override
    protected String particularToString() {
        String result = "";
        
        result += loadsPerYear;
        
        return result;
    }
}
