var tabla;
var tablaAfiliado;
var vehiculos;

$(document).ready(function(){
	

		tabla = craarTabla(tabla, '#tblVehiculos', true);		
		loadData(tabla);

		
	//Limpia mensajes de validacion del form en el hidden.
	//Unicamente cuando se oculta el modal haciendo click en algún lugar de la pantalla.	
	 $("#modal-entidad-2").on('hide.bs.modal', function (e) {		 
			 limpiarFormulario();		  
	 });
	 
	 // le digo al form la accion a ejecutar	 
	 $('#frmVehiculo').on('submit', function (e) {		 
		    procesarAccion();		 
	 });
	 

});


function salir(){
	limpiarFormulario();
	$('#modal-entidad-2').modal('hide');
	$('body').removeClass('modal-open');
	$('.modal-backdrop').remove();
}

function limpiarFormulario(){
	$('#frmVehiculo')[0].reset();
	$('#frmVehiculo').validator('destroy').validator();
	
	//limpia campos
	$("#accion-form").val('POST');
	$("#modal-entidad-2-title").html('Nuevo Vehiculo');
	$("#btnProcesar").html('Guardar');
	$("#form-group-id").hide();
	$("#txtId").val("").attr('readonly', 'readonly');
	$("#txtVersion").val("").attr('readonly', 'readonly').hide();
	$("#txtMarca").val("").attr('readonly', false);
	$("#txtModelo").val("").attr('readonly', false);	
	$("#txtAnio").val("").attr('readonly', false);
	$("#txtColor").val("").attr('readonly', false);	
}


	// carga y recarga de la tabla para ABM de Vehiculos.
	function loadData(tblElement){

		tblElement.clear();
				
		$.ajax({
		    type: "GET",
		    url: _path + "api/vehiculos/listar",
			beforeSend: function(xhr){xhr.setRequestHeader('X-CSRF-TOKEN', _csrf );	modalWaitShow();}
		})
		.done( function(vehiculos){
			
					
			modalWaitHide();
       
				for(i=0;i<vehiculos.length;i++){
			    	        				        
			        	tblElement.row.add([
			        		vehiculos[i].id,
			        		vehiculos[i].marca,
			        		vehiculos[i].modelo,
			        		vehiculos[i].anio,
			        		vehiculos[i].color,			        		
				         		'<div class="text-center">' + 
				                	'<a onclick="cargar(\''+vehiculos[i].id+'\', \'PUT\')" href="#"><i class="fa fa-pencil"></i></a>' + 
				                	'    ' +
				                	'<a onclick="cargar(\''+vehiculos[i].id+'\', \'DELETE\')" href="#"><i class="fa fa-trash"></i></a>' + 
				                '</div>'
				             ]);
			        	
			        	
					}
	
			        tblElement.draw();
				})
				
		.fail(function(data){
			modalError("La tabla no pudo cargarse correctamente.", data)
		});
	}
	

function procesarAccion(){
	var servicio ={};
	servicio["id"] 			= $("#txtId").val();
	servicio["marca"] 		= $("#txtMarca").val();
	servicio["modelo"] 		= $("#txtModelo").val();	
	servicio["anio"]		=$("#txtAnio").val();		
	servicio["color"]		= $("#txtColor").val();	
	servicio["version"]		= $("#txtVersion").val();
	

	var modo = $("#accion-form").val();
	var urlPostfijo   = '';
	if(modo=='PUT')
		urlPostfijo   = '/' + servicio["id"];
	if(modo=='DELETE')
		urlPostfijo   = '/' + servicio["id"] + '?version=' + servicio["version"];
	
	$.ajax({
	    type: modo,
	    url: _path + "api/vehiculos" + urlPostfijo,
	    data: JSON.stringify(servicio),
	    dataTYpe: 'json',
	    contentType: 'application/json',
		beforeSend: function(xhr){
			xhr.setRequestHeader('X-CSRF-TOKEN', _csrf );
			modalWaitShow();
		}
	}).done(function(data){
		salir();
		modalSuccess("Operación realizada correctamente.");		    
		loadData(tabla);		
	})
	.fail(function(data){
		modalError("La operación no pudo realizarse correctamente.", data)
	});
}

//falta modificar
function cargar(id, accion){	
	
	$("#form-group-id").show();
	$("#txtId").val("").attr('readonly', 'readonly');
	$("#txtVersion").val("").attr('readonly', 'readonly').hide();

	if(accion=='DELETE'){
		$("#btnProcesar").html('Eliminar');
		$("#txtMarca").attr('readonly', 'readonly');
		$("#txtModelo").attr('readonly', 'readonly');		
		$("#txtAnio").attr('readonly', 'readonly');
		$("#txtColor").attr('readonly', 'readonly');

		//Se quitan validaciones en acción de eliminar.
		$('#frmVehiculo').validator('destroy');
		
		$("#modal-entidad-2-title").html('Eliminar Vehiculo');	
		
	}
	if(accion=='PUT'){	
		$("#modal-entidad-2-title").html('Editar Vehiculo');	
	}
	
	$.ajax({
	    type: "GET",
	    url: _path + "api/vehiculos/"+id,
		beforeSend: function(xhr){xhr.setRequestHeader('X-CSRF-TOKEN', _csrf );	}
	}).done( function(data){
		$("#accion-form").val(accion);
		$("#txtId").val(data.id);
		$("#txtVersion").val(data.version);
		$("#txtMarca").val(data.marca);
		$("#txtModelo").val(data.modelo);		
		$("#txtAnio").val(data.anio);
		$("#txtColor").val(data.color);			
	});
	$('#modal-entidad-2').modal('show');	
}

function showVehiculoModal(){
	$('#modal-entidad-2').modal('show');
}




function craarTabla(tblElement, tblId, esAbmVehiculo) {
	
	var centerWidth = [0,1,2,3,4];
	
	
	tblElement = $(tblId).DataTable({
		'language'   : {'url'   : _path + 'js/plugins/datatables.es.json'},		        	
		'columnDefs' : [	{ className: 'text-left',  	'targets': [0, 1, 2, 3,4] }, 
							{ className: 'text-center', 'targets': centerWidth }, 
							{ 'width': '8%', 'targets': centerWidth }, 
							{ 'width': '8%', 'targets': [1] }, 
							{ "visible": false, "targets": 0 }
					   ],
	     'order'	 : [[1, 'asc']],
		    dom: 'lfrtBip',

		    buttons: [
		              {
		                  extend:    'copyHtml5',
		                  text:      '<i class="fa fa-files-o" style="color:blue;"></i>',
		                  titleAttr: 'Copiar'
		              },
		              {
		                  extend:    'excelHtml5',
		                  text:      '<i class="fa fa-file-excel-o" style="color:green;"></i>',
		                  titleAttr: 'Descargar como Excel'
		              },
		              {
		                  extend:    'csvHtml5',
		                  text:      '<i class="fa fa-file-text-o" style="color:green;"></i>',
		                  titleAttr: 'Descargar como CSV'
		              },
		              {
		                  extend:    'pdfHtml5',
		                  text:      '<i class="fa fa-file-pdf-o" style="color:red;"></i>',
		                  titleAttr: 'Descargar como PDF'
		              }
		          ]
		});
	
	
	$(tblId).on('click', 'tr', function () {
		var data = tblElement.row(this).data();
		localStorage.setItem('VehiculoId', data[0]);	
	});
	
	return tblElement;
}

