package ar.com.genomasoft.fenix.rest;
	
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import ar.com.genomasoft.fenix.model.Persona;
import ar.com.genomasoft.fenix.reports.PdfGeneratorUtil;
import ar.com.genomasoft.fenix.service.PersonaService;
import ar.com.genomasoft.jproject.core.daos.ConditionEntry;
import ar.com.genomasoft.jproject.core.daos.ConditionSimple;
import ar.com.genomasoft.jproject.core.daos.SearchOption;
import ar.com.genomasoft.jproject.webutils.webservices.BaseClientAuditedEntityWebService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api("Personas - Servicio web REST")
@RequestMapping(path = "/api/personas")
public class PersonaRest extends BaseClientAuditedEntityWebService<Persona, PersonaService> {
	
	@Autowired
	PdfGeneratorUtil pdfGenaratorUtil;

	@Autowired
	PersonaService pservice;
	
	
	@GetMapping(path="/listar")
	@ApiOperation(value = "Listas de personas")
	public @ResponseBody Collection<Persona> listar() throws Exception {
		return this.service.findAll();
	}
	
	@GetMapping(path="/pdf", produces = {MediaType.APPLICATION_PDF_VALUE})
	@ApiOperation(value = "Listado de personas.")
	public @ResponseBody byte[] getPdf() throws Exception {
		 Map<Object,Object> data = new HashMap<Object,Object>();
	    data.put("titulo","Personas");
	    data.put("personas", service.findAll());
	    byte[] reporte = pdfGenaratorUtil.createPdf("/pdf/personas",data); 
	    return reporte;
	}
	
	
	
	@GetMapping(path="/validar/{persona}")
	public @ResponseBody Collection<Persona> validar(
			@PathVariable("persona") String persona
			) throws Exception {
		
			Persona P= new Gson().fromJson(persona,Persona.class);

			List<ConditionEntry> condiciones=new ArrayList<ConditionEntry>();		

			condiciones.add(new ConditionSimple("documento",SearchOption.EQUAL,P.getDocumento()));		
			condiciones.add(new ConditionSimple("nombre",SearchOption.EQUAL,P.getNombre()));
			condiciones.add(new ConditionSimple("nombre",SearchOption.EQUAL,P.getApellido()));
			
			Collection<Persona> personas = pservice.findByFilters(condiciones);
			
			if(personas.size()>0){
			System.out.println("usuario no disponible")	;
			
			}else {
				P.setDireccion("");
				P.setCreatedTime(new Date());
				P.setCreatedByUser(1);
				
				pservice.save(P);
			System.out.println("usuario creado correctamente");
			}
			return personas;

    }

	
	@GetMapping(path = "/guardar2/{nombre}/{apellido}/{nrodoc}/{domicilio}/{edad}")
	@Transactional
	public @ResponseBody void guardar2 (
			@PathVariable("nombre") String nom,
			@PathVariable("apellido") String ape,
			@PathVariable("nrodoc") String dni,
			@PathVariable("domicilio") String dom,
			@PathVariable("edad") Integer ed
			) throws Exception{
		
		
		
		Persona p = new Persona();
				
		p.setNombre(nom);
		p.setApellido(ape);		
		p.setDocumento(dni);
		p.setDireccion(dom);
		p.setEdad(ed);
		
		p.setCreatedByUser(1);
		
		pservice.save(p);		
		
	}
	
	@GetMapping(path = "/guardarPersonalizado/{nombre}/{apellido}/{nrodoc}")
	@Transactional
	public @ResponseBody void guardarPersonalizado (
			@PathVariable("nombre") String nom,
			@PathVariable("apellido") String ape,
			@PathVariable("nrodoc") String dni
		    ) throws Exception{
		
		
		
		Persona p = new Persona();
				
		p.setNombre(nom);
		p.setApellido(ape);		
		p.setDocumento(dni);
		
		p.setCreatedByUser(1);
		
		pservice. save(p);		
		
	}
		

	@GetMapping(path="/Filtrar/{let}")
	@ApiOperation(value = "Listas de personas")
	public @ResponseBody Collection<Persona> Filtrar(
			@PathVariable("let") String letra
			) throws Exception {
		
		
		List <ConditionEntry> condiciones = new ArrayList <ConditionEntry>();
		
		ConditionSimple condicion1 = new ConditionSimple("nombre", SearchOption.BEGIN,letra);
		
		condiciones.add(condicion1);
		
		List<Persona> personas = (List<Persona>) this.pservice.findByFilters(condiciones);
		
		
		
		return personas;
	}
 }



  
