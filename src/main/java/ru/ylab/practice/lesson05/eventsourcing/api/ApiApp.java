package ru.ylab.practice.lesson05.eventsourcing.api;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApiApp {
    public static void main(String[] args) throws Exception {
        // Тут пишем создание PersonApi, запуск и демонстрацию работы
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        applicationContext.start();
        PersonApi personApi = applicationContext.getBean(PersonApi.class);
        // пишем взаимодействие с PersonApi
        final Printer printer = applicationContext.getBean(Printer.class);
        personApi.savePerson(0L, "Kirill", "And", "Mifodiy");
        personApi.savePerson(1L, "Vasiliy", "Ivanov", null);
        personApi.savePerson(2L, "Petr", "Petrov", "Petrovich");
        personApi.savePerson(3L, "Aleksey", "Gagarin", "Alekseevich");
        personApi.savePerson(4L, "Irina", "Suchova", "Gennadievna");
        personApi.deletePerson(0L);
        personApi.deletePerson(0L);
        printer.print(personApi.findPerson(0L));
        printer.print(personApi.findPerson(3L));
        printer.print(personApi.findAll());
    }
}
