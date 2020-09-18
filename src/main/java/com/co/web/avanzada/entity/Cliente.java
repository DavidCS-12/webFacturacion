package com.co.web.avanzada.entity;
import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;


/**
 * The persistent class for the cliente database table.
 * 
 */
@Entity
@Data
@NamedQuery(name="Cliente.findAll", query="SELECT c FROM Cliente c")
public class Cliente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int dni;
	
	private String apellido;

	private String direccion;

	private String email;

	private String nombre;

	private String telefono;

	@ManyToOne
	@JoinColumn(name="municipio_cliente_fk")
	private Municipio municipio;

	public Cliente() {
	}
}