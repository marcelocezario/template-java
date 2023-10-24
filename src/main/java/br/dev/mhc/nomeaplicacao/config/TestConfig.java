package br.dev.mhc.nomeaplicacao.config;

import br.dev.mhc.nomeaplicacao.services.db.DBSeedServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfig {

    private final DBSeedServiceTest dbService;

    @Autowired
    public TestConfig(DBSeedServiceTest dbService) {
        this.dbService = dbService;
    }

    @Bean
    public boolean instantiateDataBase() {
        dbService.databaseSeeding();
        return true;
    }
}
