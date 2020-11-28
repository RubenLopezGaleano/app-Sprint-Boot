var tabla;
var tablaAfiliado;
var personas;

$(document).ready(function(){

		tabla = craarTabla(tabla, '#tblPersonas', true);		
		loadData(tabla);

		
	//Limpia mensajes de validacion del form en el hidden.
	//Unicamente cuando se oculta el modal haciendo click en algún lugar de la pantalla.	
	 $("#modal-entidad").on('hide.bs.modal', function (e) {		 
			 limpiarFormulario();		  
	 });
	 
	 // le digo al form la accion a ejecutar	 
	 $('#frmPersona').on('submit', function (e) {		 
		    procesarAccion();		 
	 });
	 

});


function salir(){
	limpiarFormulario();
	$('#modal-entidad').modal('hide');
	$('body').removeClass('modal-open');
	$('.modal-backdrop').remove();
}

function limpiarFormulario(){
	$('#frmPersona')[0].reset();
	$('#frmPersona').validator('destroy').validator();
	
	//limpia campos
	$("#accion-form").val('POST');
	$("#modal-entidad-title").html('Nueva Persona');
	$("#btnProcesar").html('Guardar');
	$("#form-group-id").hide();
	$("#txtId").val("").attr('readonly', 'readonly');
	$("#txtVersion").val("").attr('readonly', 'readonly').hide();
	$("#txtNombre").val("").attr('readonly', false);
	$("#txtEdad").val("").attr('readonly', false);	
	$("#txtFechanacimiento").val("").attr('readonly', false);
	$("#txtNroDocumento").val("").attr('readonly', false);
	$("#txtApellido").val("").attr('readonly', false);
	$("#txtEmail").val("").attr('readonly', false);
}


	// carga y recarga de la tabla para ABM de Personas.
	function loadData(tblElement){

		tblElement.clear();
				
		$.ajax({
		    type: "GET",
		    url: _path + "api/personas/listar",
			beforeSend: function(xhr){xhr.setRequestHeader('X-CSRF-TOKEN', _csrf );	modalWaitShow();}
		})
		.done( function(personas){
			
					
			modalWaitHide();
       
				for(i=0;i<personas.length;i++){
			    	        				        
			        	tblElement.row.add([
			        		personas[i].id,
			        		personas[i].nombre,
			        		personas[i].apellido,
			        		personas[i].documento,
			        		personas[i].direccion,
			        		personas[i].fecha,
			        		personas[i].edad,
				         		'<div class="text-center">' + 
				                	'<a onclick="cargar(\''+personas[i].id+'\', \'PUT\')" href="#"><i class="fa fa-pencil"></i></a>' + 
				                	'    ' +
				                	'<a onclick="cargar(\''+personas[i].id+'\', \'DELETE\')" href="#"><i class="fa fa-trash"></i></a>' + 
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
	servicio["id"] 				= $("#txtId").val();
	servicio["nombre"] 			= $("#txtNombre").val();
	servicio["apellido"] 		= $("#txtApellido").val();	
	servicio["documento"]		=$("#txtNroDocumento").val();
	servicio["direccion"]		=$("#txtdireccion").val();
	servicio["fecha"]			=$("#txtFechanacimiento").val();
	servicio["edad"]			=$("#txtEdad").val();	
	servicio["version"]			= $("#txtVersion").val();	
	servicio["email"] 			= $("#txtEmail").val();

	var modo = $("#accion-form").val();
	var urlPostfijo   = '';
	if(modo=='PUT')
		urlPostfijo   = '/' + servicio["id"];
	if(modo=='DELETE')
		urlPostfijo   = '/' + servicio["id"] + '?version=' + servicio["version"];
	
	$.ajax({
	    type: modo,
	    url: _path + "api/personas" + urlPostfijo,
	    data: JSON.stringify(servicio),
	    dataTYpe: 'json',
	    contentType: 'application/json',
		beforeSend: function(xhr){
			xhr.setRequestHeader('X-CSRF-TOKEN', _csrf );
			modalWaitShow();
		}
	}).done(function(){
		salir();
		modalSuccess("Operación realizada correctamente.");		    
		loadData(tabla);		
	})
	.fail(function(data){
		modalError("La operación no pudo realizarse correctamente.", data)
	});
}

function cargar(id, accion){	
	
	$("#form-group-id").show();
	$("#txtId").val("").attr('readonly', 'readonly');
	$("#txtVersion").val("").attr('readonly', 'readonly').hide();

	if(accion=='DELETE'){
		$("#btnProcesar").html('Eliminar');
		$("#txtNombre").attr('readonly', 'readonly');
		$("#txtApellido").attr('readonly', 'readonly');		
		$("#txtNroDocumento").attr('readonly', 'readonly');
		$("#txtdireccion").attr('readonly', 'readonly');
		$("#txtFechanacimiento").attr('readonly', 'readonly');		
		$("#txtEdad").attr('readonly', 'readonly');		
		$("#txtEmail").attr('readonly', 'readonly');

		//Se quitan validaciones en acción de eliminar.
		$('#frmPersona').validator('destroy');
		
		$("#modal-entidad-title").html('Eliminar Persona');	
		
	}
	if(accion=='PUT'){	
		$("#modal-entidad-title").html('Editar Persona');	
	}
	
	$.ajax({
	    type: "GET",
	    url: _path + "api/personas/"+id,
		beforeSend: function(xhr){xhr.setRequestHeader('X-CSRF-TOKEN', _csrf );	}
	}).done( function(data){
		$("#accion-form").val(accion);
		$("#txtId").val(data.id);
		$("#txtVersion").val(data.version);
		$("#txtNombre").val(data.nombre);
		$("#txtApellido").val(data.apellido);		
		$("#txtNroDocumento").val(data.documento);
		$("#txtdireccion").val(data.direccion);
		$("#txtFechanacimiento").val(data.fecha);
		$("#txtEdad").val(data.edad);		
		$("#txtEmail").val(data.email);				
	});
	$('#modal-entidad').modal('show');	
}

function showPersonaModal(){
	$('#modal-entidad').modal('show');
}




function craarTabla(tblElement, tblId, esAbmPersona) {
	
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
		localStorage.setItem('personaId', data[0]);	
	});
	
	return tblElement;
}


function Guardar2(){
		
	var nom = $('#txtNombre').val();
	var ape = $('#txtApellido').val();
	var dni = $('#txtNroDocumento').val();	
	var domicilio = $('#txtdireccion').val();
	var edad = $('#txtEdad').val();
	
	$.ajax({
		type : 'GET',
		url : _path + "api/personas/guardar2/" + nom + "/" + ape + "/" + dni+ "/" + domicilio + "/" + edad,
		beforeSend: function(xhr){xhr.setRequestHeader('X-CSRF-TOKEN', _csrf );	}		
	}).done(function(){
		//OK
		modalSuccess("Operación realizada correctamente.");		
		loadData(tabla); // Recarga la grilla
		salir(); // cierra el formulario
	}).fail(function(error){
		//ERROR
	});
	
}
function GuardarPersonalizado(){
	
	var nom = $('#txtNombre').val();
	var ape = $('#txtApellido').val();
	var dni = $('#txtNroDocumento').val();	
	var domicilio = $('#txtdireccion').val();
	var edad = $('#txtEdad').val();
	
	
	alert(nom +" "+ ape +" "+ dni)
	
	$.ajax({
		type : 'GET',
		url : _path + "api/personas/guardarPersonalizado/" + nom + "/" + ape + "/" + dni,
		beforeSend: function(xhr){xhr.setRequestHeader('X-CSRF-TOKEN', _csrf );	}		
	}).done(function(){
		//OK
		modalSuccess("Operación realizada correctamente.");		
		loadData(tabla); // Recarga la grilla
		salir(); // cierra el formulario
	}).fail(function(error){
		//ERROR
	});
	
}

function NombresCon(){
	
	var letra = $('#txtlet').val();
	
	tabla.clear();
	
	$.ajax({
	    type: "GET",
	    url: _path + "api/personas/Filtrar/" + letra,
		beforeSend: function(xhr){xhr.setRequestHeader('X-CSRF-TOKEN', _csrf );	modalWaitShow();}
	})
	.done( function(personas){
		
				
		modalWaitHide();
   
			for(i=0;i<personas.length;i++){
		    	        				        
		        	tabla.row.add([
		        		personas[i].id,
		        		personas[i].nombre,
		        		personas[i].apellido,
		        		personas[i].documento,
		        		personas[i].direccion,
		        		personas[i].fecha,
		        		personas[i].edad,
			         		'<div class="text-center">' + 
			                	'<a onclick="cargar(\''+personas[i].id+'\', \'PUT\')" href="#"><i class="fa fa-pencil"></i></a>' + 
			                	'    ' +
			                	'<a onclick="cargar(\''+personas[i].id+'\', \'DELETE\')" href="#"><i class="fa fa-trash"></i></a>' + 
			                '</div>'
			             ]);
		        	
		        	
				}

		        tabla.draw();
			})
			
	.fail(function(data){
		modalError("La tabla no pudo cargarse correctamente.", data)
		
		
	});
	
	
	
	
}
	
	
	
	
	
	
	
