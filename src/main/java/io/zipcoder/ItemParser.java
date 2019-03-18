package io.zipcoder;

import io.zipcoder.utils.Item;
import io.zipcoder.utils.ItemParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemParser {
    public List<Item> parseItemList(String valueToParse){
        String[] items = valueToParse.split("##");
        List<Item> result = new ArrayList<>();
        for (String itemJSON: items) {
            String matchString = itemJSON + "##";
            try {
                result.add(parseSingleItem(matchString));
            }
            catch (ItemParseException e){
            }
        }
        return result;
    }

    public Item parseSingleItem(String singleItem) throws ItemParseException {
        String regex = makeRegex();
        // Pattern and Matcher
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(singleItem);
        if (matcher.matches()) {
            String name = matcher.group(2).toLowerCase();
            // Trying to catch this silly co0kie;
            if(name.equals("co0kies")){
                name = "cookies";
            }
            Double price = Double.valueOf(matcher.group(4));
            String type = matcher.group(6).toLowerCase();
            String expiration = matcher.group(8).toLowerCase();
            return new Item(name, price, type, expiration);
        }
        throw new ItemParseException();
    }

    private static String makeRegex() {
        // Group
//        1 : name
        String nameRegex = "(\\w+)";
//        2 : separator
        String separator = "[:@^*%;!]";
//        3 : name value
        String nameValue = "(\\w+)";
//        4 : price
        String priceRegex = "(\\w+)";
//        5 : separator
        String separator2 = separator;
//        6 : dollars
        String dollars = "(\\d+\\.\\d{0,2})";
//        7 : type
        String typeRegex = "(\\w+)";
//        8 : separator
        String separator3 = separator;
//        9 : type value
        String typeValue = "(\\w+)";
//        10: expiration
        String expirationRegex = "(\\w+)";
//        11: separator
        String separator4 = separator;
//        12: date
        String date = "(\\d{1,2}/\\d{1,2}/\\d{4})";
        // Build regex string
        return nameRegex + separator + nameValue + separator +
                priceRegex + separator2 + dollars + separator +
                typeRegex + separator3 + typeValue + separator +
                expirationRegex + separator4 + date + "##";
    }
}
