package io.ylab.intensive.lesson03.org.structure;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class OrgStructureParserImpl implements OrgStructureParser {

    @Override
    public Employee parseStructure(File csvFile) throws IOException {

        final Map<Long, Employee> employeeMap = readFile(csvFile);
        return getBoss(employeeMap);
    }

    private Map<Long, Employee> readFile(File file) throws IOException {

        final Map<Long, Employee> employeeMap = new HashMap<>();

        try (final InputStream is = new FileInputStream(file);
             final Scanner scanner = new Scanner(is)) {

            while (scanner.hasNext()) {
                final Employee e = new Employee();
                final String[] values = scanner.nextLine().split(";");
                e.setId(parseStringToLongId(values[0]));
                e.setBossId(parseStringToLongId(values[1]));
                e.setName(values[2]);
                e.setPosition(values[3]);
                employeeMap.put(e.getId(), e);
            }
        }
        return employeeMap;
    }

    private Employee getBoss(Map<Long, Employee> map) {

        Employee bigBoss = null;

        for (Employee e : map.values()) {
            final Long bossId = e.getBossId();
            if (bossId == null) {
                bigBoss = e;
            } else {
                final Employee boss = map.get(e.getBossId());
                e.setBoss(boss);
                boss.getSubordinate().add(e);
            }
        }
        return bigBoss;
    }

    private Long parseStringToLongId(String id) {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
