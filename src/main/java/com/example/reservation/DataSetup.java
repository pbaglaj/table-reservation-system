package com.example.reservation;

import com.example.reservation.model.RestaurantTable;
import com.example.reservation.repository.TableRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataSetup {

    @Bean
    public CommandLineRunner commandLineRunner(TableRepository repository) {
        return args -> {
          if (repository.count() == 0) {
              repository.saveAll(List.of(
                      new RestaurantTable(null, 1, 2, true),
                      new RestaurantTable(null, 2, 4, true),
                      new RestaurantTable(null, 3, 6, false)
              ));
              System.out.println("Created startup tables: " + repository.count());
          }
        };
    }
}
