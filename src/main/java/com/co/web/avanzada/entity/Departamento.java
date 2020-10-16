package com.co.web.avanzada.entity;
import java.io.Serializable;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The persistent class for the departamento database table.
 * 
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="departamento")
@NamedQuery(name="Departamento.findAll", query="SELECT d FROM Departamento d")
public class Departamento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_departamento")
	private int idDepartamento;

	private String nombre;

	//bi-directional many-to-one association to Pai
	@ManyToOne
	@JoinColumn(name="pais_fk")
	private Pai pai;
}