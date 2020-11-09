package com.co.web.avanzada.entity;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The persistent class for the factura database table.
 * 
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@NamedQuery(name="Factura.findAll", query="SELECT f FROM Factura f")
public class Factura implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_factura")
	private int idFactura;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_venta")
	private Date fechaVenta;

	@Column(name="valor_compra")
	private double valorCompra;
	
	@Column(name="valor_compra_iva")
	private double valorCompraIva;

	//bi-directional many-to-one association to DespachoPedido
	@ManyToOne
	@JoinColumn(name="despacho_pedido_fk")
	private DespachoPedido despachoPedido;
}