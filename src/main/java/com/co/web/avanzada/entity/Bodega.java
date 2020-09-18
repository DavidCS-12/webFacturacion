package com.co.web.avanzada.entity;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;

/**
 * The persistent class for the bodega database table.
 * 
 */
@Entity
@Data
@NamedQuery(name="Bodega.findAll", query="SELECT b FROM Bodega b")
public class Bodega implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_bodega")
	private int idBodega;


	private String direccion;

	//bi-directional many-to-one association to Municipio
	@ManyToOne
	@JoinColumn(name="municipio_bodega_fk")
	private Municipio municipio;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="vendedor_fk")
	private Usuario usuario;

	public Bodega() {
	}
}