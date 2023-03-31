package lesson04.filesort;

import lesson04.DbUtil;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class FileSorterTest {
    public static void main(String[] args) throws SQLException, IOException {
        final long start = System.currentTimeMillis();
//        File dataFile = new Generator().generate("data.txt", 2_000_000);
        DataSource dataSource = initDb();
        File data = new File("data.txt");
        FileSorter fileSorter = new FileSortImpl(dataSource);
        File res = fileSorter.sort(data);
        System.out.println((System.currentTimeMillis() - start) / 1000);
    }

    public static DataSource initDb() throws SQLException {
        String createSortTable = ""
                + "drop table if exists numbers;"
                + "CREATE TABLE if not exists numbers (\n"
                + "\tval bigint\n"
                + ");";
        DataSource dataSource = DbUtil.buildDataSource();
        DbUtil.applyDdl(createSortTable, dataSource);
        return dataSource;
    }
}
