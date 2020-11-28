package ar.com.genomasoft.fenix;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ar.com.genomasoft.fenix.config.Application;
import ar.com.genomasoft.fenix.model.Persona;
import ar.com.genomasoft.fenix.service.PersonaService;
import ar.com.genomasoft.jproject.core.exception.BaseException;

@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
@Transactional
@ContextConfiguration(classes = {Application.class})
public class ApplicationTests {

	@Autowired
	PersonaService pservice;
	
	ArrayList<Persona> ListaPersonas = new ArrayList<Persona>();
	
	@Test
	public void contextLoads() throws BaseException {	
		
		Persona per = new Persona();
		
		per.setNombre("Gonzalo");		
		per.setApellido("Vecchi");
		per.setEdad(28);
		
		//per = pservice.save(per);
		ListaPersonas.add(per);
				
		Persona per2 = new Persona();
		
		per2.setNombre("Andres");		
		per2.setApellido("Perez");
		per2.setEdad(25);
		
		ListaPersonas.add(per2);
		
		Persona per3 = new Persona();
		
		per3.setNombre("Juan");		
		per3.setApellido("Gonzalez");
		per3.setEdad(26);
		
		ListaPersonas.add(per3);
		
		Persona per4 = new Persona();
		
		per4.setNombre("Luis");		
		per4.setApellido("Serafini");
		per4.setEdad(29);
		
		ListaPersonas.add(per4);
	}
	
	@Test
	public void recorrer() throws BaseException {
		
		Persona pmayor = new Persona();
		pmayor.setEdad(-1);
		
		for (Persona persona : ListaPersonas) {
			if (persona.getEdad() > pmayor.getEdad()) {
				pmayor = persona;				
			}			
		}
		
		System.out.println(pmayor.getNombre());
		
	}

}
