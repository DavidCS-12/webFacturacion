package com.co.web.avanzada.entity;

import java.io.Serializable;
import javax.persistence.*;

import lombok.Data;


/**
 * The persistent class for the proveedor database table.
 * 
 */
@Entity
@Data
@NamedQuery(name="Proveedor.findAll", query="SELECT p FROM Proveedor p")
public class Proveedor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="NIT")
	private int nit;

	private String correo;

	private String direccion;

	@Column(name="nombre_proveedor")
	private String nombreProveedor;

	private String telefono;
	//bi-directional many-to-one association to Municipio
	@ManyToOne
	@JoinColumn(name="municipio_fk")
	private Municipio municipio;

	public Proveedor() {
	}
}