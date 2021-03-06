package com.co.web.avanzada.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.co.web.avanzada.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	// Necesario para evitar que la seguridad se aplique a los resources
	// Como los css, imagenes y javascripts
	String[] resources = new String[] {"/css/**", "/images/**","/webfonts/**","/img/**", "/js/**"};
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests().antMatchers(resources).permitAll().antMatchers("/login","/usuario/upload","/ajax/productos","/ajax/municipios","/ajax/listarProductoCategoria","/listarProductosBodega","/ajax/departamentos","/","/addusuario","/add_usuario").permitAll()
				.antMatchers("/addCategoria").hasAnyRole("ADMIN")
				.antMatchers("/add_categoria").hasAnyRole("ADMIN")
				.antMatchers("/editCategoria/**").hasAnyRole("ADMIN")
				.antMatchers("/updateCategoria/**").hasAnyRole("ADMIN")
				.antMatchers("/deleteCategoria/**").hasAnyRole("ADMIN")
				.antMatchers("/listarcategorias").hasAnyRole("ADMIN")
				.antMatchers("/addproveedor").hasAnyRole("ADMIN")
				.antMatchers("/add_proveedor").hasAnyRole("ADMIN")
				.antMatchers("/editProveedor/**").hasAnyRole("ADMIN")
				.antMatchers("/updateProveedor/**").hasAnyRole("ADMIN")
				.antMatchers("/deleteProveedor/**").hasAnyRole("ADMIN")
				.antMatchers("/listarProveedor").hasAnyRole("ADMIN")
				.antMatchers("/addproducto").hasAnyRole("ADMIN")
				.antMatchers("/add_producto").hasAnyRole("ADMIN")
				.antMatchers("/editProducto/**").hasAnyRole("ADMIN")
				.antMatchers("/updateProducto/**").hasAnyRole("ADMIN")
				.antMatchers("/deleteProducto/**").hasAnyRole("ADMIN")
				.antMatchers("/listarProducto").hasAnyRole("ADMIN")
				.antMatchers("/addpai").hasAnyRole("ADMIN")
				.antMatchers("/add_pai").hasAnyRole("ADMIN")
				.antMatchers("/editPai/**").hasAnyRole("ADMIN")
				.antMatchers("/updatePai/**").hasAnyRole("ADMIN")
				.antMatchers("/deletePai/**").hasAnyRole("ADMIN")
				.antMatchers("/listarPaises").hasAnyRole("ADMIN")
				.antMatchers("/addDepartamento").hasAnyRole("ADMIN")
				.antMatchers("/add_departamento").hasAnyRole("ADMIN")
				.antMatchers("/editDepartamento/**").hasAnyRole("ADMIN")
				.antMatchers("/updateDepartamento/**").hasAnyRole("ADMIN")
				.antMatchers("/deleteDepartamento/**").hasAnyRole("ADMIN")
				.antMatchers("/listarDepartamentos").hasAnyRole("ADMIN")
				.antMatchers("/addMunicipio").hasAnyRole("ADMIN")
				.antMatchers("/add_municipio").hasAnyRole("ADMIN")
				.antMatchers("/editMunicipio/**").hasAnyRole("ADMIN")
				.antMatchers("/updateMunicipio/**").hasAnyRole("ADMIN")
				.antMatchers("/deleteMunicipio/**").hasAnyRole("ADMIN")
				.antMatchers("/listarMunicipios").hasAnyRole("ADMIN")
				.antMatchers("/addbodega/**").hasAnyRole("ADMIN", "VENDEDOR")
				.antMatchers("/add_bodega").hasAnyRole("ADMIN", "VENDEDOR")
				.antMatchers("/editBodega/**").hasAnyRole("ADMIN", "VENDEDOR")
				.antMatchers("/updateBodega/**").hasAnyRole("ADMIN", "VENDEDOR")
				.antMatchers("/deleteBodega/**").hasAnyRole("ADMIN", "VENDEDOR")
				.antMatchers("/listarBodega").hasAnyRole("ADMIN", "VENDEDOR")
				.antMatchers("/listarInventario/**").hasAnyRole("VENDEDOR","ADMIN")
				.antMatchers("/addInventario/**").hasAnyRole("VENDEDOR")
				.antMatchers("/add_inventario/**").hasAnyRole("VENDEDOR")
				.antMatchers("/editInventario/**").hasAnyRole("VENDEDOR")
				.antMatchers("/updateInventario/**").hasAnyRole("VENDEDOR")
				.antMatchers("/deleteInventario/**").hasAnyRole("VENDEDOR")
				.antMatchers("/listarFacturas/**").hasAnyRole("VENDEDOR","CLIENTE")
				.antMatchers("/addFactura/**").hasAnyRole("VENDEDOR")
				.antMatchers("/add_factura/**").hasAnyRole("VENDEDOR")
				.antMatchers("/editFactura/**").hasAnyRole("VENDEDOR","CLIENTE")
				.antMatchers("/updateFactura/**").hasAnyRole("VENDEDOR","CLIENTE")
				.antMatchers("/deleteFactura/**").hasAnyRole("VENDEDOR","CLIENTE")
				.antMatchers("/listarDetalles/**").hasAnyRole("VENDEDOR","CLIENTE")
				.antMatchers("/addDetalle/**").hasAnyRole("VENDEDOR","CLIENTE")
				.antMatchers("/add_detalle/**").hasAnyRole("VENDEDOR","CLIENTE")
				.antMatchers("/editDetalle/**").hasAnyRole("VENDEDOR","CLIENTE")
				.antMatchers("/updateDetalle/**").hasAnyRole("VENDEDOR","CLIENTE")
				.antMatchers("/deleteDetalle/**").hasAnyRole("VENDEDOR","CLIENTE")
				.antMatchers("/listarDespachos/**").hasAnyRole("VENDEDOR")
				.antMatchers("/addDespacho/**").hasAnyRole("VENDEDOR")
				.antMatchers("/add_despacho/**").hasAnyRole("VENDEDOR")
				.antMatchers("/editDespacho/**").hasAnyRole("VENDEDOR")
				.antMatchers("/updateDespacho/**").hasAnyRole("VENDEDOR")
				.antMatchers("/deleteDespacho/**").hasAnyRole("VENDEDOR")
				.anyRequest().authenticated().and().formLogin()
				.loginPage("/login").permitAll().defaultSuccessUrl("/", true).failureUrl("/login?error=true")
				.usernameParameter("email").passwordParameter("password").and().logout().permitAll()
				.logoutSuccessUrl("/exit");
	}

	BCryptPasswordEncoder bCryptPasswordEncoder;

	// Crea el encriptador de contraseñas
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		bCryptPasswordEncoder = new BCryptPasswordEncoder(4);
//El numero 4 representa que tan fuerte quieres la encriptacion.
//Se puede en un rango entre 4 y 31. 
//Si no pones un numero el programa utilizara uno aleatoriamente cada vez
//que inicies la aplicacion, por lo cual tus contrasenas encriptadas no funcionaran bien
		return bCryptPasswordEncoder;
	}

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	// Registra el service para usuarios y el encriptador de contrasena
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		// Setting Service to find User in the database.
		// And Setting PassswordEncoder
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
}