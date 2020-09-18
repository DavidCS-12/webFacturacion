package com.co.web.avanzada.entity;
import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

import lombok.Data;


/**
 * The persistent class for the usuario database table.
 * 
 */
@Entity
@Data
@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int dni;
	 
	private String nombre;
    
	private String apellido;
	
	private String telefono;
	
	private String email;
	
	private String password;
	
	private String direccion;
	
	@ManyToOne
	@JoinColumn(name="municipio_fk")
	private Municipio municipio;
	
	@Column
	private boolean enabled;

	public Usuario() {
	}
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "authorities_users", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "authority_id"))
	private Set<Authority> authority;
	
}