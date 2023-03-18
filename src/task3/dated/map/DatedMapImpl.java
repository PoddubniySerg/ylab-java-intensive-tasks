package task3.dated.map;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DatedMapImpl implements DatedMap {

    private final Map<String, DatedValue> values = new HashMap<>();

    @Override
    public void put(String key, String value) {
        values.put(key, new DatedValue(value, new Date()));
    }

    @Override
    public String get(String key) {
        return values.get(key).getValue();
    }

    @Override
    public boolean containsKey(String key) {
        return values.containsKey(key);
    }

    @Override
    public void remove(String key) {
        values.remove(key);
    }

    @Override
    public Set<String> keySet() {
        return values.keySet();
    }

    @Override
    public Date getKeyLastInsertionDate(String key) {
        return values.get(key).getDate();
    }
}
