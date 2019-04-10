package hearthlandsoptimizer;

import java.util.Map;

/**
 * Implementation of a Multiset interface for {@link Map}.
 * 
 * @author Amélia @SlayZeKyriarchy
 * @param <K> the type of elements maintained by this multiset.
 *
 */
public interface Multiset<K> extends Map<K, Integer> {
    
    
    /**
     * Adds the specified count of {@code element} to the multiset. 
     * 
     * @param element The key to add.
     * @param count The amount to add.
     */
    public default void add(K element, Integer count) {
        Integer previousCount = this.get(element);
        
        if(previousCount == null) {
            this.put(element, count);
        } else {
            this.put(element, count + previousCount);
        }
    }
    
    /**
     * Adds all members of the specified multiset to this multiset.
     * 
     * @param ms The multiset to add.
     */
    public default void addAll(Multiset<K> ms) {
        for(Map.Entry<K, Integer> entry : ms.entrySet()) {
            add(entry.getKey(), entry.getValue());
        }
    }
    
}
