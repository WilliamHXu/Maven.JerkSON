package io.zipcoder;

import io.zipcoder.utils.FileReader;
import io.zipcoder.utils.Item;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class GroceryReporter {
    private final String originalFileText;

    public GroceryReporter(String jerksonFileName) {
        this.originalFileText = FileReader.readFile(jerksonFileName);
    }

    public LinkedHashMap<String, LinkedHashMap> mapMaker(){
        ItemParser itemParser = new ItemParser();
        List<Item> itemList = itemParser.parseItemList(originalFileText);
        LinkedHashMap<String, LinkedHashMap> result = new LinkedHashMap<>();
        for(Item item : itemList){
            boolean inserted = false;
            String name = item.getName();
            Set<String> itemNames = result.keySet();
            for(String key : itemNames){
                if(name.equals(key)){
                    // Get in the map
                    putInPriceMap(item, result.get(key));
                    inserted = true;
                }
            }
            if(!inserted){
                LinkedHashMap<Double, Integer> priceMap = new LinkedHashMap<>();
                priceMap.put(item.getPrice(), 1);
                result.put(item.getName(), priceMap);
            }
        }
        return result;
    }

    private void putInPriceMap(Item item, LinkedHashMap<Double, Integer> priceMap) {
        boolean inserted = false;
        Set<Double> prices = priceMap.keySet();
        for (Double price : prices){
            if (price.equals(item.getPrice())){
                Integer count = priceMap.get(price);
                priceMap.put(price, ++count);
                inserted = true;
            }
        }
        if(!inserted){
            priceMap.put(item.getPrice(), 1);
        }
    }

    @Override
    public String toString() {
        LinkedHashMap<String, LinkedHashMap> fullMap = mapMaker();
        Set<String> itemNames = fullMap.keySet();
        String result = "";
        Integer numberOfEntries = 0;
        for(String name : itemNames){
            //result += makeItemString(name, fullMap.get(name));
            Integer total = 0;
            LinkedHashMap<Double, Integer> priceOccurrences = fullMap.get(name);
            Set<Double> prices = priceOccurrences.keySet();
            String priceString = "";
            for (Double price : prices){
                priceString += String.format("Price:%7.2f        seen: %d time%s", price, priceOccurrences.get(price), plural(priceOccurrences.get(price))) + "\n";
                priceString += "-------------        -------------\n";
                total += priceOccurrences.get(price);
            }
            result += String.format("name:%8s        seen: %d time%s", capitalize(name), total, plural(total)) + "\n" +
                "=============        =============\n";
            result += priceString + "\n";
            numberOfEntries += total;
        }
        String[] temp = originalFileText.split("##");
        Integer numberOfErrors = temp.length - numberOfEntries;
        result += "Errors               seen: " + numberOfErrors + " times\n";
        return result;
    }

    private String capitalize(String name) {
        if (name.length() > 0){
            return "" + ((Character) name.charAt(0)).toString().toUpperCase() + name.substring(1);
        }
        return "";
    }

    private String plural(Integer integer) {
        if (integer > 1){
            return "s";
        }
        return "";
    }
}
