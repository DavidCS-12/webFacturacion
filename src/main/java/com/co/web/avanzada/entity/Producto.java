package com.co.web.avanzada.entity;
import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The persistent class for the producto database table.
 * 
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="producto")
@NamedQuery(name="Producto.findAll", query="SELECT p FROM Producto p")
public class Producto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="codigo_producto")
	private int codigoProducto;

	private String descripcion;

	private String nombre;

	@Column(name="precio_compra")
	private double precioCompra;

	@Column(name="precio_venta")
	private double precioVenta;

	@Column(name="url_foto")
	private String urlFoto;

	//bi-directional many-to-one association to Categoria
	@ManyToOne
	@JoinColumn(name="categoria_fk")
	private Categoria categoria;

	//bi-directional many-to-one association to Proveedor
	@ManyToOne
	@JoinColumn(name="proveedor_fk")
	private Proveedor proveedor;
}