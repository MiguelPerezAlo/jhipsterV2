package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Estadio;
import com.mycompany.myapp.repository.EstadioRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Estadio.
 */
@RestController
@RequestMapping("/api")
public class EstadioResource {

    private final Logger log = LoggerFactory.getLogger(EstadioResource.class);

    @Inject
    private EstadioRepository estadioRepository;

    /**
     * POST  /estadios -> Create a new estadio.
     */
    @RequestMapping(value = "/estadios",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Estadio> create(@RequestBody Estadio estadio) throws URISyntaxException {
        log.debug("REST request to save Estadio : {}", estadio);
        if (estadio.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new estadio cannot already have an ID").body(null);
        }
        Estadio result = estadioRepository.save(estadio);
        return ResponseEntity.created(new URI("/api/estadios/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("estadio", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /estadios -> Updates an existing estadio.
     */
    @RequestMapping(value = "/estadios",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Estadio> update(@RequestBody Estadio estadio) throws URISyntaxException {
        log.debug("REST request to update Estadio : {}", estadio);
        if (estadio.getId() == null) {
            return create(estadio);
        }
        Estadio result = estadioRepository.save(estadio);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("estadio", estadio.getId().toString()))
                .body(result);
    }

    /**
     * GET  /estadios -> get all the estadios.
     */
    @RequestMapping(value = "/estadios",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Estadio> getAll() {
        log.debug("REST request to get all Estadios");
        return estadioRepository.findAll();
    }

    /**
     * GET  /estadios/:id -> get the "id" estadio.
     */
    @RequestMapping(value = "/estadios/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Estadio> get(@PathVariable Long id) {
        log.debug("REST request to get Estadio : {}", id);
        return Optional.ofNullable(estadioRepository.findOne(id))
            .map(estadio -> new ResponseEntity<>(
                estadio,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /estadios/:id -> delete the "id" estadio.
     */
    @RequestMapping(value = "/estadios/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Estadio : {}", id);
        estadioRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("estadio", id.toString())).build();
    }
}
