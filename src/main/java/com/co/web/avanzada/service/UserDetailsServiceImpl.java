package com.co.web.avanzada.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.co.web.avanzada.repository.IUsuarioRepo;
import com.co.web.avanzada.entity.Authority;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    IUsuarioRepo userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
     //Buscar el usuario con el repositorio y si no existe lanzar una exepcion
    	com.co.web.avanzada.entity.Usuario	appUser = 
                 userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No existe el usuario"));
		
    //Mapear nuestra lista de Authority con la de spring security 
    List grantList = new ArrayList();
    for(Authority authority: appUser.getAuthority()) {
        // ROLE_CLIENTE, ROLE_ADMIN, ROLE_VENDEDOR
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getAuthority());
            grantList.add(grantedAuthority);
    }
		
    //Crear El objeto UserDetails que va a ir en sesion y retornarlo.
    UserDetails user = (UserDetails) new User(appUser.getEmail(), appUser.getPassword(), grantList);
         return user;
    }
}