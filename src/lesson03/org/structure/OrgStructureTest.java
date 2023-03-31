package lesson03.org.structure;

import java.io.File;
import java.io.IOException;

public class OrgStructureTest {

    public static void main(String[] args) {
        try {
            OrgStructureParser parser = new OrgStructureParserImpl();
            File file = new File("./src/task3/org/structure/file.csv");
            final Employee boss = parser.parseStructure(file);
            System.out.println("Босс:");
            System.out.println("id " + boss.getId());
            System.out.println("bossId " + boss.getBossId());
            System.out.println(boss.getName());
            System.out.println(boss.getPosition());
            System.out.println("Subordinate:");
            boss.getSubordinate().forEach(e -> System.out.println(e.getName()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
