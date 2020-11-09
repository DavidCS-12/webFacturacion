package com.co.web.avanzada.entity;
import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * The persistent class for the detalle_factura database table.
 * 
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="detalle_factura")
@NamedQuery(name="DetalleFactura.findAll", query="SELECT d FROM DetalleFactura d")
public class DetalleFactura implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_detalle")
	private int idDetalle;

	@Column(name="cantidad")
	private int cantidad;
	
	@ManyToOne
	@JoinColumn(name="producto_fk")
	private Producto producto;

	//bi-directional many-to-one association to DetalleFactura
	@ManyToOne
	@JoinColumn(name="detalle_factura_fk")
	private Factura factura;

}