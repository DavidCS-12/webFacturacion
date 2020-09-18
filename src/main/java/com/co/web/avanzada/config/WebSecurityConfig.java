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
	String[] resources = new String[] { "/include/**", "/css/**", "/icons/**", "/images/**", "/js/**", "/layer/**" };
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests().antMatchers(resources).permitAll().antMatchers("/usuario/upload","/ajax/municipios","/ajax/departamentos","/ajax/subcategorias","/","/addusuario","/add_usuario").permitAll()
				.antMatchers("/admin").hasAnyRole("ADMIN")
				.antMatchers("/admind/addCategoria").hasAnyRole("ADMIN")
				.antMatchers("/admind/add_categoria").hasAnyRole("ADMIN")
				.antMatchers("/admind/editCategoria/**").hasAnyRole("ADMIN")
				.antMatchers("/admind/updateCategoria/**").hasAnyRole("ADMIN")
				.antMatchers("/admind/deleteCategoria/**").hasAnyRole("ADMIN")
				.antMatchers("/admind/listarcategorias").hasAnyRole("ADMIN")
				.antMatchers("/admind/addproveedor").hasAnyRole("ADMIN")
				.antMatchers("/admind/add_proveedor").hasAnyRole("ADMIN")
				.antMatchers("/admind/editProveedor/**").hasAnyRole("ADMIN")
				.antMatchers("/admind/updateProveedor/**").hasAnyRole("ADMIN")
				.antMatchers("/admind/deleteProveedor/**").hasAnyRole("ADMIN")
				.antMatchers("/admind/listarProveedor").hasAnyRole("ADMIN")
				.antMatchers("/admind/addproducto").hasAnyRole("ADMIN")
				.antMatchers("/admind/add_producto").hasAnyRole("ADMIN")
				.antMatchers("/admind/editProducto/**").hasAnyRole("ADMIN")
				.antMatchers("/admind/updateProducto/**").hasAnyRole("ADMIN")
				.antMatchers("/admind/deleteProducto/**").hasAnyRole("ADMIN")
				.antMatchers("/admind/listarProducto").hasAnyRole("ADMIN")
				.antMatchers("/admind/addpai").hasAnyRole("ADMIN")
				.antMatchers("/admind/add_pai").hasAnyRole("ADMIN")
				.antMatchers("/admind/editPai/**").hasAnyRole("ADMIN")
				.antMatchers("/admind/updatePai/**").hasAnyRole("ADMIN")
				.antMatchers("/admind/deletePai/**").hasAnyRole("ADMIN")
				.antMatchers("/admind/listarPaises").hasAnyRole("ADMIN")
				.antMatchers("/admind/addDepartamento").hasAnyRole("ADMIN")
				.antMatchers("/admind/add_departamento").hasAnyRole("ADMIN")
				.antMatchers("/admind/editDepartamento/**").hasAnyRole("ADMIN")
				.antMatchers("/admind/updateDepartamento/**").hasAnyRole("ADMIN")
				.antMatchers("/admind/deleteDepartamento/**").hasAnyRole("ADMIN")
				.antMatchers("/admind/listarDepartamentos").hasAnyRole("ADMIN")
				.antMatchers("/admind/addMunicipio").hasAnyRole("ADMIN")
				.antMatchers("/admind/add_municipio").hasAnyRole("ADMIN")
				.antMatchers("/admind/editMunicipio/**").hasAnyRole("ADMIN")
				.antMatchers("/admind/updateMunicipio/**").hasAnyRole("ADMIN")
				.antMatchers("/admind/deleteMunicipio/**").hasAnyRole("ADMIN")
				.antMatchers("/admind/listarMunicipios").hasAnyRole("ADMIN")
				.anyRequest().authenticated().and().formLogin()
				.loginPage("/").permitAll().defaultSuccessUrl("/admin").failureUrl("/?error=true")
				.usernameParameter("email").passwordParameter("password");
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