package dev.typr.foundations.example;

import dev.typr.foundations.Transactor;
import dev.typr.foundations.connect.DuckDbConfig;
import dev.typr.foundations.connect.SingleConnectionDataSource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    Transactor transactor() {
        var ds = SingleConnectionDataSource.create(DuckDbConfig.inMemory().build());
        return ds.transactor(Transactor.autoCommitStrategy());
    }

    @Bean
    CommandLineRunner demo(TodoRepository todos) {
        return args -> {
            todos.createSchema();

            var buy = todos.create("Buy milk");
            var write = todos.create("Write code");
            var read = todos.create("Read the docs");
            System.out.println("Created: " + buy);
            System.out.println("Created: " + write);
            System.out.println("Created: " + read);

            todos.setDone(buy.id(), true);

            System.out.println("\nAll todos:");
            for (var todo : todos.findAll()) {
                var mark = todo.done() ? "x" : " ";
                System.out.printf("  [%s] #%d %s%n", mark, todo.id(), todo.title());
            }

            System.out.println("\nQuery analysis:");
            todos.analyzeQueries();
        };
    }
}
