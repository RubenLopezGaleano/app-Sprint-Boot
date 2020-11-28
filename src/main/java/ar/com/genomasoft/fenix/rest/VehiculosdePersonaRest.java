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

import ar.com.genomasoft.fenix.model.VehiculosdePersona;
import ar.com.genomasoft.fenix.reports.PdfGeneratorUtil;
import ar.com.genomasoft.fenix.service.VehiculosdePersonaService;
import ar.com.genomasoft.jproject.core.daos.ConditionEntry;
import ar.com.genomasoft.jproject.core.daos.ConditionSimple;
import ar.com.genomasoft.jproject.core.daos.SearchOption;
import ar.com.genomasoft.jproject.webutils.webservices.BaseClientAuditedEntityWebService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api("Vehiculos de Persona - Servicio web REST")
@RequestMapping(path = "/api/VehiculosdePersona")
public class VehiculosdePersonaRest extends BaseClientAuditedEntityWebService<VehiculosdePersona, VehiculosdePersonaService> {
	
	@Autowired
	PdfGeneratorUtil pdfGenaratorUtil;

	@Autowired
	VehiculosdePersonaService vpservice;
	
	
	@GetMapping(path="/listar")
	@ApiOperation(value = "Listas de Vehiculos de Persona")
	public @ResponseBody Collection<VehiculosdePersona> listar() throws Exception {
		return this.service.findAll();
	}
	
	@GetMapping(path="/pdf", produces = {MediaType.APPLICATION_PDF_VALUE})
	@ApiOperation(value = "Listado de Vehiculos de Persona.")
	public @ResponseBody byte[] getPdf() throws Exception {
		 Map<Object,Object> data = new HashMap<Object,Object>();
	    data.put("titulo","VehiculosdePersona");
	    data.put("VehiculosdePersona", service.findAll());
	    byte[] reporte = pdfGenaratorUtil.createPdf("/pdf/VehiculosdePersona",data); 
	    return reporte;
	}
	
	
	
	@GetMapping(path="/validar/{VehiculosdePersona}")
	public @ResponseBody Collection<VehiculosdePersona> validar(
			@PathVariable("VehiculosdePersona") String VehiculosdePersona
			) throws Exception {
		
			VehiculosdePersona VP = new Gson().fromJson(VehiculosdePersona, VehiculosdePersona.class);

			List<ConditionEntry> condiciones=new ArrayList<ConditionEntry>();		

			condiciones.add(new ConditionSimple("personaid",SearchOption.EQUAL, VP.getpersonaid()));		
			condiciones.add(new ConditionSimple("vehiculoid",SearchOption.EQUAL, VP.getvehiculoid()));
			
			
			Collection<VehiculosdePersona> vehpersonas = vpservice.findByFilters(condiciones);
			
			if(vehpersonas.size()>0){
			System.out.println("usuario no disponible")	;
			
			}else {				
				VP.setCreatedTime(new Date());
				VP.setCreatedByUser(1);
				
				vpservice.save(VP);
			System.out.println("usuario creado correctamente");
			}
			return vehpersonas;

    }

	
		
	
 }



  
