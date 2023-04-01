package io.ylab.intensive.lesson04.movie;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class MovieLoaderImpl implements MovieLoader {

    private final DataSource dataSource;

    public MovieLoaderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void loadData(File file) {
        // РЕАЛИЗАЦИЮ ПИШЕМ ТУТ
        try {
            final List<Movie> movies = readFile(file);
            loadToDb(movies);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Movie> readFile(File file) throws IOException {
        final List<Movie> movies = new ArrayList<>();
        try (final FileInputStream inputStream = new FileInputStream(file);
             final Scanner scanner = new Scanner(inputStream, "cp1251")) {
            final String delimiter = ";";
            while (scanner.hasNext()) {
                final String[] values = scanner.nextLine().split(delimiter);
                final Integer year = getInt(values[0]);
                if (year == null) {
                    continue;
                }
                final Movie movie = new Movie();
                movie.setYear(year);
                movie.setLength(getInt(values[1]));
                setValue(values[2], movie::setTitle);
                setValue(values[3], movie::setSubject);
                setValue(values[4], movie::setActors);
                setValue(values[5], movie::setActress);
                setValue(values[6], movie::setDirector);
                movie.setPopularity(getInt(values[7]));
                movie.setAwards(values[8].equalsIgnoreCase("Yes"));
                movies.add(movie);
            }
        }
        return movies;
    }

    private Integer getInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void setValue(String value, Consumer<String> fun) {
        if (!value.isEmpty()) {
            fun.accept(value);
        }
    }

    private void loadToDb(List<Movie> movies) throws SQLException {
        final String query = "insert into movie (year, length, title, subject, actors, actress, director, popularity, awards) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement = connection.prepareStatement(query)) {
            for (Movie movie : movies) {
                statement.setInt(1, movie.getYear());
                setParam(statement, 2, movie.getLength(), Types.INTEGER);
                setParam(statement, 3, movie.getTitle(), Types.VARCHAR);
                setParam(statement, 4, movie.getSubject(), Types.VARCHAR);
                setParam(statement, 5, movie.getActors(), Types.VARCHAR);
                setParam(statement, 6, movie.getActress(), Types.VARCHAR);
                setParam(statement, 7, movie.getDirector(), Types.VARCHAR);
                setParam(statement, 8, movie.getPopularity(), Types.INTEGER);
                setParam(statement, 9, movie.getAwards(), Types.BOOLEAN);
                statement.execute();
            }
        }
    }

    private <T> void setParam(PreparedStatement statement, int parameterIndex, T param, int type) throws SQLException {
        if (param == null) {
            statement.setNull(parameterIndex, type);
        } else {
            if (type == Types.INTEGER) {
                statement.setInt(parameterIndex, (Integer) param);
            } else if (type == Types.BOOLEAN) {
                statement.setBoolean(parameterIndex, (Boolean) param);
            } else {
                statement.setString(parameterIndex, (String) param);
            }
        }
    }
}
