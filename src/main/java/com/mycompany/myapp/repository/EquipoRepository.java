package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Equipo;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Equipo entity.
 */
public interface EquipoRepository extends JpaRepository<Equipo,Long> {

}
