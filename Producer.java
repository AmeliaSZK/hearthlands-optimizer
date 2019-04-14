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
    private static final int INT_UNCOMPUTED_TOTAL_STAFF = 0;
    private static final Float UNCOMPUTED_TOTAL_STAFF = 0f;
    
    private int              loadsPerYear;
    private ResourceMultiset consumption;
    private ResourceMultiset production;
    
    private BuildingMultiset completeChain;
    
    private int nbOfRecursiveCalls;
    
    protected Producer(List<String> specs) {
        commonParser(specs);
        particularParser(specs);
    }
    
    @Override
    protected void particularParser(List<String> particularSpecs) {
        this.loadsPerYear = Integer.valueOf(particularSpecs.get(LOADS_COLUMN));
        
        consumption = new ResourceMultiset();
        parseResources(consumption, particularSpecs.get(CONSUMPTION_COLUMN));
        production = new ResourceMultiset();
        parseResources(production, particularSpecs.get(PRODUCTION_COLUMN));
                
        totalStaff = INT_UNCOMPUTED_TOTAL_STAFF;
        completeChain = new BuildingMultiset();
        
        for(Culture culture : cultures) {
            allChains.get(culture).setTotalStaff(UNCOMPUTED_TOTAL_STAFF);
            allChains.get(culture).getChain().clear();
        }
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
                
                completeChain.addAll(supplier.getCompleteChain(),
                        suppliersNeeded);
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
        
        for (Entry<Building, Float> supplierEntry : completeChain.entrySet()) {
            /*
             * Here, we sum the localStaff values of each supplier instead of
             * their totalStaff because we are going through every supplier in
             * the chain.
             * 
             * We would need the totalStaff if we were only going through this
             * Producer's direct suppliers, not this suppliers' suppliers and so
             * on.
             */
            
            Building supplier           = supplierEntry.getKey();
            Float    suppliersNeeded    = supplierEntry.getValue();
            
            cumulatedStaff += supplier.getLocalStaff() * suppliersNeeded;
        }
        
        totalStaff = cumulatedStaff;
        
    }
    
    public BuildingMultiset getCompleteChain() {
        if (isCompleteChainComputed() == false) {
            this.computeCompleteChain();
        }
        
        return completeChain;
    }
    
    private Map<Resource, Float> getProduction() {
        return Map.copyOf(production);
    }
    
    @Override
    public int getTotalStaff() {
        if (isTotalStaffComputed() == false) {
            this.computeTotalStaff();
        }
        
        return totalStaff;
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
    
    public boolean isTotalStaffComputed() {
        return totalStaff != INT_UNCOMPUTED_TOTAL_STAFF;
    }
    
    @Override
    protected String particularToString() {
        String result = "";
        
        result += loadsPerYear;
        
        return result;
    }
}
