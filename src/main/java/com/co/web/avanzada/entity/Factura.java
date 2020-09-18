package com.co.web.avanzada.entity;
import java.io.Serializable;
import javax.persistence.*;

import lombok.Data;


/**
 * The persistent class for the factura database table.
 * 
 */
@Entity
@Data
@NamedQuery(name="Factura.findAll", query="SELECT f FROM Factura f")
public class Factura implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_factura")
	private int idFactura;

	private int cantidad;

	@Column(name="producto_fk")
	private int productoFk;

	//bi-directional many-to-one association to DetalleFactura
	@ManyToOne
	@JoinColumn(name="detalle_factura_fk")
	private DetalleFactura detalleFactura;

	public Factura() {
	}
}