package io.ylab.intensive.lesson03.dated.map;

public class DatedMapTest {

    public static void main(String[] args) {
        final DatedMap map = new DatedMapImpl();
        map.put("First key", "First value");
        System.out.println("Fist key's value: " + map.get("First key"));
        System.out.println("Empty key's value (not exist in the map): " + map.get(""));
        System.out.println("Map contains 'First key': " + map.containsKey("First key"));
        System.out.println("Map contains 'Second key': " + map.containsKey("Second key"));
        map.put("Second key", "Second value");
        System.out.println("Keys after added 'Second key': " + map.keySet());
        map.remove("Second key");
        System.out.println("Keys after removed 'Second key': " + map.keySet());
        System.out.println("Date of the first value added" + map.getKeyLastInsertionDate("First key"));
    }
}
