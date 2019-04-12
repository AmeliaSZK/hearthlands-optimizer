package hearthlandsoptimizer;

class Utils {

    /**
     * Brings to uppercase the first letter in the specified string. Empty
     * strings allowed.
     * 
     * @param string The string for which to put the first letter in uppercase.
     * @return The {@code string}, with its first letter in uppercase.
     */
    public static String capitalizeFirstLetter(String string) {
        String returnedString = "";
        
        if (string.isEmpty() == false) {
            String firstLetter = string.substring(0, 1).toUpperCase();
            // String.toUpperCase used because it is locale-sensitive.
            
            String tail = string.length() > 1 ? string.substring(1) : "";
            
            returnedString = firstLetter + tail;
        }
        
        return returnedString;
    }
}
