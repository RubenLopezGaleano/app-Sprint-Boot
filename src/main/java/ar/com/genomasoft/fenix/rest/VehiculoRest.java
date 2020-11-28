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

import ar.com.genomasoft.fenix.model.Vehiculo;
import ar.com.genomasoft.fenix.reports.PdfGeneratorUtil;
import ar.com.genomasoft.fenix.service.VehiculoService;
import ar.com.genomasoft.jproject.core.daos.ConditionEntry;
import ar.com.genomasoft.jproject.core.daos.ConditionSimple;
import ar.com.genomasoft.jproject.core.daos.SearchOption;
import ar.com.genomasoft.jproject.webutils.webservices.BaseClientAuditedEntityWebService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api("Vehiculos - Servicio web REST")
@RequestMapping(path = "/api/vehiculos")
public class VehiculoRest extends BaseClientAuditedEntityWebService<Vehiculo, VehiculoService> {
	
	@Autowired
	PdfGeneratorUtil pdfGenaratorUtil;

	@Autowired
	VehiculoService vservice;
	
	
	@GetMapping(path="/listar")
	@ApiOperation(value = "Listas de vehiculos")
	public @ResponseBody Collection<Vehiculo> listar() throws Exception {
		return this.service.findAll();
	}
	
	@GetMapping(path="/pdf", produces = {MediaType.APPLICATION_PDF_VALUE})
	@ApiOperation(value = "Listado de vehiculos.")
	public @ResponseBody byte[] getPdf() throws Exception {
		 Map<Object,Object> data = new HashMap<Object,Object>();
	    data.put("titulo","Vehiculos");
	    data.put("personas", service.findAll());
	    byte[] reporte = pdfGenaratorUtil.createPdf("/pdf/vehiculos",data); 
	    return reporte;
	}
	
	
	
	@GetMapping(path="/validar/{vehiculo}")
	public @ResponseBody Collection<Vehiculo> validar(
			@PathVariable("vehiculo") String vehiculo
			) throws Exception {
		
			Vehiculo v = new Gson().fromJson(vehiculo,Vehiculo.class);

			List<ConditionEntry> condiciones=new ArrayList<ConditionEntry>();		

			condiciones.add(new ConditionSimple("marca",SearchOption.EQUAL, v.getMarca()));		
			condiciones.add(new ConditionSimple("modelo",SearchOption.EQUAL,v.getModelo()));			
			
			Collection<Vehiculo> vehiculos = vservice.findByFilters(condiciones);
			
			if(vehiculos.size()>0){
			System.out.println("usuario no disponible")	;
			
			}else {
				v.setMarca("");
				v.setCreatedTime(new Date());
				v.setCreatedByUser(1);
				
				vservice.save(v);
			System.out.println("usuario creado correctamente");
			}
			return vehiculos;

    }

	
	
	
 }



  
