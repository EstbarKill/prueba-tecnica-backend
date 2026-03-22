package com.company.prueba_tecnica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación.
 * Punto de entrada para el arranque de Spring Boot.
 *
 * @SpringBootApplication activa:
 *   - Escaneo automático de componentes (@ComponentScan)
 *   - Auto-configuración de Spring (@EnableAutoConfiguration)
 *   - Configuración de la clase actual como fuente de beans (@Configuration)
 */
@SpringBootApplication
public class PruebaTecnicaApplication {

	/**
	 * Método main — arranca el contexto de Spring Boot.
	 *
	 * @param args argumentos de línea de comandos (no utilizados)
	 */
	public static void main(String[] args) {
		SpringApplication.run(PruebaTecnicaApplication.class, args);
	}

}
