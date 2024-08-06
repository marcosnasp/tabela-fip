package br.com.alura.tabelafip;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.alura.tabelafip.principal.Principal;

@SpringBootApplication
public class TabelaFipApplication implements CommandLineRunner {

	@Autowired
	private Principal principal;

	public static void main(String[] args) {
		SpringApplication.run(TabelaFipApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		principal.exibeMenu();
	}

}
