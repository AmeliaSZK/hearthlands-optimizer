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
     * Adds the specified count of {@code keyType} to the multiset. 
     * 
     * @param keyType The key to add.
     * @param count The amount to add.
     */
    public default void add(K keyType, Integer count) {
        Integer previousCount = this.get(keyType);
        
        if(previousCount == null) {
            this.put(keyType, count);
        } else {
            this.put(keyType, count + previousCount);
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
