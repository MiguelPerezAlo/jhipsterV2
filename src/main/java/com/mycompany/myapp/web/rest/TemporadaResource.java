package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Temporada;
import com.mycompany.myapp.repository.TemporadaRepository;
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
 * REST controller for managing Temporada.
 */
@RestController
@RequestMapping("/api")
public class TemporadaResource {

    private final Logger log = LoggerFactory.getLogger(TemporadaResource.class);

    @Inject
    private TemporadaRepository temporadaRepository;

    /**
     * POST  /temporadas -> Create a new temporada.
     */
    @RequestMapping(value = "/temporadas",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Temporada> create(@RequestBody Temporada temporada) throws URISyntaxException {
        log.debug("REST request to save Temporada : {}", temporada);
        if (temporada.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new temporada cannot already have an ID").body(null);
        }
        Temporada result = temporadaRepository.save(temporada);
        return ResponseEntity.created(new URI("/api/temporadas/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("temporada", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /temporadas -> Updates an existing temporada.
     */
    @RequestMapping(value = "/temporadas",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Temporada> update(@RequestBody Temporada temporada) throws URISyntaxException {
        log.debug("REST request to update Temporada : {}", temporada);
        if (temporada.getId() == null) {
            return create(temporada);
        }
        Temporada result = temporadaRepository.save(temporada);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("temporada", temporada.getId().toString()))
                .body(result);
    }

    /**
     * GET  /temporadas -> get all the temporadas.
     */
    @RequestMapping(value = "/temporadas",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Temporada> getAll() {
        log.debug("REST request to get all Temporadas");
        return temporadaRepository.findAllWithEagerRelationships();
    }

    /**
     * GET  /temporadas/:id -> get the "id" temporada.
     */
    @RequestMapping(value = "/temporadas/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Temporada> get(@PathVariable Long id) {
        log.debug("REST request to get Temporada : {}", id);
        return Optional.ofNullable(temporadaRepository.findOneWithEagerRelationships(id))
            .map(temporada -> new ResponseEntity<>(
                temporada,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /temporadas/:id -> delete the "id" temporada.
     */
    @RequestMapping(value = "/temporadas/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Temporada : {}", id);
        temporadaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("temporada", id.toString())).build();
    }
}
