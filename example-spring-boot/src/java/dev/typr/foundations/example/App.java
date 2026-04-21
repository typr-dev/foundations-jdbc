package dev.typr.foundations.example;

import dev.typr.foundations.AnalyzableScanner;
import dev.typr.foundations.QueryChecker;
import dev.typr.foundations.TransactorJdbc;
import dev.typr.foundations.connect.ConnectionSource;
import dev.typr.foundations.connect.DuckDbConfig;
import dev.typr.foundations.spring.SpringTransactor;
import javax.sql.DataSource;
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
  DataSource dataSource() {
    return ConnectionSource.of(DuckDbConfig.inMemory().build());
  }

  @Bean
  TransactorJdbc transactor(DataSource dataSource) {
    return SpringTransactor.create(dataSource);
  }

  @Bean
  CommandLineRunner demo(TodoRepository todos, TransactorJdbc tx) {
    return args -> {
      todos.createSchema();

      var buy = todos.create("Buy milk");
      var write = todos.create("Write code");
      var read = todos.create("Read the docs");
      System.out.println("Created: " + buy);
      System.out.println("Created: " + write);
      System.out.println("Created: " + read);

      var completed = todos.createAndComplete("Walk the dog");
      System.out.println("Created and completed: " + completed);

      System.out.println("\nAll todos:");
      for (var todo : todos.findAll()) {
        var mark = todo.done() ? "x" : " ";
        System.out.printf("  [%s] #%d %s%n", mark, todo.id(), todo.title());
      }

      System.out.println("\nQuery analysis:");
      var analyzables = AnalyzableScanner.scan("dev.typr.foundations.example");
      QueryChecker checker = () -> tx;
      checker.checkAll(analyzables);
      System.out.println("  All " + analyzables.size() + " queries passed analysis.");
    };
  }
}
