package com.co.web.avanzada.entity;
import java.io.Serializable;
import javax.persistence.*;

import lombok.Data;


/**
 * The persistent class for the municipio database table.
 * 
 */
@Entity
@Data
@NamedQuery(name="Municipio.findAll", query="SELECT m FROM Municipio m")
public class Municipio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_municipio")
	private int idMunicipio;

	private String nombre;

	@ManyToOne
	@JoinColumn(name="departamento_fk")
	private Departamento departamento;

	public Municipio() {
	}
}