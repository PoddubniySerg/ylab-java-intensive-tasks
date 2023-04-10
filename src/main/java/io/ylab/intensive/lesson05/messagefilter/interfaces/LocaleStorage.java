package io.ylab.intensive.lesson05.messagefilter.interfaces;

import java.io.IOException;
import java.util.List;

public interface LocaleStorage {

    List<String> getFilters() throws IOException;
}
