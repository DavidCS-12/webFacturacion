package com.co.web.avanzada.entity;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;


/**
 * The persistent class for the categoria database table.
 * 
 */
@Entity
@Data
@NamedQuery(name="Categoria.findAll", query="SELECT c FROM Categoria c")
public class Categoria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_categoria")
	private int idCategoria;

	private String nombre;
	
	public Categoria() {
	}

}