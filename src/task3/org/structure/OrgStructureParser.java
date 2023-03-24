package task3.org.structure;

import java.io.File;
import java.io.IOException;

public interface OrgStructureParser {

    Employee parseStructure(File csvFile) throws IOException;
}
