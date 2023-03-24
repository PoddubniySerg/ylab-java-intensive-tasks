package lesson04.persistentmap;

import lesson04.DbUtil;

import javax.sql.DataSource;
import java.sql.SQLException;

public class PersistenceMapTest {
    public static void main(String[] args) throws SQLException {
        DataSource dataSource = initDb();
        PersistentMap persistentMap = new PersistentMapImpl(dataSource);
        // Написать код демонстрации работы
        final String firstMap = "first_map";
        final String secondMap = "second_map";
        final String firstKey = "_first_key";
        final String secondKey = "_second_key";
        addMap(firstMap, persistentMap);
        addMap(secondMap, persistentMap);
        System.out.println("2й список ключей после добавления элементов: " + persistentMap.getKeys());
        System.out.println("Значение по 2му ключу 2й мапы: " + persistentMap.get(secondMap + secondKey));
        persistentMap.remove(secondMap + secondKey);
        System.out.println("2й список ключей после удаления одного из элементов: " + persistentMap.getKeys());
        persistentMap.clear();
        System.out.println("2й список ключей после очистки 2го списка: " + persistentMap.getKeys());
        persistentMap.init(firstMap);
        System.out.println("Содержит ли 1я коллекция ключ " + firstMap + firstKey + ": " + persistentMap.containsKey(firstMap + firstKey));
    }

    public static DataSource initDb() throws SQLException {
        String createMapTable = ""
                + "drop table if exists persistent_map; "
                + "CREATE TABLE if not exists persistent_map (\n"
                + "   map_name varchar,\n"
                + "   KEY varchar,\n"
                + "   value varchar\n"
                + ");";
        DataSource dataSource = DbUtil.buildDataSource();
        DbUtil.applyDdl(createMapTable, dataSource);
        return dataSource;
    }

    private static void addMap(String name, PersistentMap map) throws SQLException {
        final String firstKey = "_first_key";
        final String secondKey = "_second_key";
        final String firstValue = "_first_value";
        final String secondValue = "_second_value";
        map.init(name);
        map.put(name + firstKey, name + firstValue);
        map.put(name + secondKey, name + secondValue);
    }
}
