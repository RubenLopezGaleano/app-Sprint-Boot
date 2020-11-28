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

/** VehiculosdePersona
 * @author David Schwarz (juandavidschwarz@gmail.com)
 *
 */
@Entity
@ApiModel(	value="VehiculosdePersona", 
			description="Entidad Persona del sistema Fénix.", 
			reference="La persona es una entidad del sistema.", 
			parent=BaseAuditedEntity.class)
@Table(name = "USR_VEHICULOSDEPERSONA")
@Where(clause="DELETED_TIME IS NULL")//Así es como filtra la entidad para mostrar en la web.
@SQLDelete(sql="UPDATE USR_VEHICULOSDEPERSONA SET DELETED_TIME = CURRENT_TIMESTAMP WHERE ID = ? AND VERSION = ?") // Lo que ejecuta al momento de deletear una persona. El campo version evita la concurrencia (cada vez que se modifica se actualiza la version y no deja modificarle a otro que tiene la version anterior)
public class VehiculosdePersona extends BaseClientAuditedEntity {
	
	private String personaid;
	private String vehiculoid;	

	public VehiculosdePersona() {

	}
	
	@Override // 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique=true, nullable=false)
	@ApiModelProperty(value="Clave Primaria de la Persona", required=false, position=0)
	public Integer getId() {
		return super.id;
	}

	/**
	 * @return the personaid
	 */
	@Column(name = "PERSONAID", nullable=false)
	@ApiModelProperty(value="ID de la persona.", required=true, position=1)
	public String getpersonaid() {
		return personaid;
	}

	/**
	 * @param personaid the codigo to set
	 */
	public void setpersonaid(String personaid) {
		this.personaid = personaid;
	}
	
	/**
	 * @return the vehiculoid
	 */
	@Column(name = "VEHICULOID", nullable=false)
	@ApiModelProperty(value="vehiculoid de la persona.", required=true, position=2)
	public String getvehiculoid() {
		return vehiculoid;
	}

	/**
	 * @param vehiculoid the codigo to set
	 */
	public void setvehiculoid(String vehiculoid) {
		this.vehiculoid= vehiculoid;
	}

	

	
}