package com.co.web.avanzada.entity;
import java.io.Serializable;
import javax.persistence.*;

import lombok.Data;


/**
 * The persistent class for the inventario database table.
 * 
 */
@Entity
@Data
@NamedQuery(name="Inventario.findAll", query="SELECT i FROM Inventario i")
public class Inventario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_inventario")
	private int idInventario;

	private int cantidad;

	@Column(name="producto_fk")
	private int productoFk;

	//bi-directional many-to-one association to Bodega
	@ManyToOne
	@JoinColumn(name="bodega_fk")
	private Bodega bodega;

	public Inventario() {
	}
}