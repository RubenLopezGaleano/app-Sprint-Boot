package ar.com.genomasoft.fenix.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import ar.com.genomasoft.jproject.core.entities.BaseAuditedEntity;
import ar.com.genomasoft.jproject.core.entities.BaseClientAuditedEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/** Vehiculo
 * @author Gonzalo Vecchi
 *
 */
@Entity
@ApiModel(	value="Vehiculo", 
			description="Entidad Vehiculo del sistema Fénix.", 
			reference="El vehiculo es una entidad del sistema.", 
			parent=BaseAuditedEntity.class)
@Table(name = "USR_VEHICULO")
@Where(clause="DELETED_TIME IS NULL")//Así es como filtra la entidad para mostrar en la web.
@SQLDelete(sql="UPDATE USR_VEHICULO SET DELETED_TIME = CURRENT_TIMESTAMP WHERE ID = ? AND VERSION = ?") // Lo que ejecuta al momento de deletear un vehiculo. El campo version evita la concurrencia (cada vez que se modifica se actualiza la version y no deja modificarle a otro que tiene la version anterior)
public class Vehiculo extends BaseClientAuditedEntity {

	
	private String marca;
	private String modelo;	
	private Integer anio;
	private String color;	

	public Vehiculo() {

	}
	
	@Override // 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique=true, nullable=false)
	@ApiModelProperty(value="Clave Primaria del vehiculo", required=false, position=0)
	public Integer getId() {
		return super.id;
	}


	@Column(name = "MARCA", nullable=false)
	@ApiModelProperty(value="Marca del vehiculo.", required=true, position=1)
	public String getMarca() {
		return marca;
	}
	
	public void setMarca(String marca) {
		this.marca = marca;
	}
	

	@Column(name = "MODELO", nullable=false)
	@ApiModelProperty(value="Modelo del vehiculo.", required=true, position=2)
	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo.toUpperCase();
	}


	@Column(name = "ANIO", nullable=false)
	@ApiModelProperty(value="ANIO del vehiculo.", required=true, position=4)
	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	

	@Column(name = "COLOR", nullable=false)
	@ApiModelProperty(value="Color del vehiculo.", required=true, position=25)
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	

	
	/*METODOS TRANSIENT, NO ASOCIADOS A NINGUN CAMPO DE LA BASE*/
	/*@Transient
    public String getNombreApellidoDni() {
    	String s = (nombre != null? nombre  + " ": "") +
    			(apellido != null? apellido  + " ": "") +
    			(documento != null? "(" + documento  + ") ": "");
    	
    	return s;
	}*/
	
	
}