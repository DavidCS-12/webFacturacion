package com.co.web.avanzada.entity;
import java.io.Serializable;
import javax.persistence.*;

import lombok.Data;

import java.util.Date;

/**
 * The persistent class for the detalle_factura database table.
 * 
 */
@Entity
@Data
@Table(name="detalle_factura")
@NamedQuery(name="DetalleFactura.findAll", query="SELECT d FROM DetalleFactura d")
public class DetalleFactura implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_detalle_factura")
	private int idDetalleFactura;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_venta")
	private Date fechaVenta;

	@Column(name="valor_compra")
	private double valorCompra;

	//bi-directional many-to-one association to DespachoPedido
	@ManyToOne
	@JoinColumn(name="despacho_pedido_fk")
	private DespachoPedido despachoPedido;


	public DetalleFactura() {
	}
}