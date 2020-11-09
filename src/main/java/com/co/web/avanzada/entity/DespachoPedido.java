package com.co.web.avanzada.entity;
import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The persistent class for the despacho_pedido database table.
 * 
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="despacho_pedido")
@NamedQuery(name="DespachoPedido.findAll", query="SELECT d FROM DespachoPedido d")
public class DespachoPedido implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_despacho")
	private int idDespacho;

	//bi-directional many-to-one association to Cliente
	@ManyToOne
	@JoinColumn(name="cliente_despacho_fk")
	private Usuario cliente;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="vendedor_despacho_fk")
	private Usuario vendedor;
	
	@Column(name="estado")
	private boolean estado;
	
}