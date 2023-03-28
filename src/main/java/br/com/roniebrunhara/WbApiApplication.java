package br.com.roniebrunhara;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@SpringBootApplication
public class WbApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WbApiApplication.class, args);
	}
	
	@Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/webapi");
        dataSource.setUsername("root");
        dataSource.setPassword("");
        return dataSource;

    }

}
