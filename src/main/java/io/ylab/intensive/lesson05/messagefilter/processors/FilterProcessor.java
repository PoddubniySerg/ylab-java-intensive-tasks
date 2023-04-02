package io.ylab.intensive.lesson05.messagefilter.processors;

import io.ylab.intensive.lesson05.messagefilter.clients.FilleReader;
import io.ylab.intensive.lesson05.messagefilter.clients.FilterDbClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Component
public class FilterProcessor implements InitializingBean {

    private final FilleReader filleReader;
    private final FilterDbClient filterDbClient;

    public FilterProcessor(FilleReader filleReader, FilterDbClient filterDbClient) {
        this.filleReader = filleReader;
        this.filterDbClient = filterDbClient;
    }

    public void prepareFilter() throws IOException, SQLException {
        final List<String> filters = filleReader.getFilters();
        filterDbClient.save(filters);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        prepareFilter();
    }
}
