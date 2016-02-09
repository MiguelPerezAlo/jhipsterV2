package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Estadio;
import com.mycompany.myapp.repository.EstadioRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the EstadioResource REST controller.
 *
 * @see EstadioResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EstadioResourceTest {

    private static final String DEFAULT_NOMBRE = "SAMPLE_TEXT";
    private static final String UPDATED_NOMBRE = "UPDATED_TEXT";

    private static final LocalDate DEFAULT_CREACION = new LocalDate(0L);
    private static final LocalDate UPDATED_CREACION = new LocalDate();

    private static final Integer DEFAULT_AFORO = 1;
    private static final Integer UPDATED_AFORO = 2;

    @Inject
    private EstadioRepository estadioRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restEstadioMockMvc;

    private Estadio estadio;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EstadioResource estadioResource = new EstadioResource();
        ReflectionTestUtils.setField(estadioResource, "estadioRepository", estadioRepository);
        this.restEstadioMockMvc = MockMvcBuilders.standaloneSetup(estadioResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        estadio = new Estadio();
        estadio.setNombre(DEFAULT_NOMBRE);
        estadio.setCreacion(DEFAULT_CREACION);
        estadio.setAforo(DEFAULT_AFORO);
    }

    @Test
    @Transactional
    public void createEstadio() throws Exception {
        int databaseSizeBeforeCreate = estadioRepository.findAll().size();

        // Create the Estadio

        restEstadioMockMvc.perform(post("/api/estadios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estadio)))
                .andExpect(status().isCreated());

        // Validate the Estadio in the database
        List<Estadio> estadios = estadioRepository.findAll();
        assertThat(estadios).hasSize(databaseSizeBeforeCreate + 1);
        Estadio testEstadio = estadios.get(estadios.size() - 1);
        assertThat(testEstadio.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEstadio.getCreacion()).isEqualTo(DEFAULT_CREACION);
        assertThat(testEstadio.getAforo()).isEqualTo(DEFAULT_AFORO);
    }

    @Test
    @Transactional
    public void getAllEstadios() throws Exception {
        // Initialize the database
        estadioRepository.saveAndFlush(estadio);

        // Get all the estadios
        restEstadioMockMvc.perform(get("/api/estadios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(estadio.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].creacion").value(hasItem(DEFAULT_CREACION.toString())))
                .andExpect(jsonPath("$.[*].aforo").value(hasItem(DEFAULT_AFORO)));
    }

    @Test
    @Transactional
    public void getEstadio() throws Exception {
        // Initialize the database
        estadioRepository.saveAndFlush(estadio);

        // Get the estadio
        restEstadioMockMvc.perform(get("/api/estadios/{id}", estadio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(estadio.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.creacion").value(DEFAULT_CREACION.toString()))
            .andExpect(jsonPath("$.aforo").value(DEFAULT_AFORO));
    }

    @Test
    @Transactional
    public void getNonExistingEstadio() throws Exception {
        // Get the estadio
        restEstadioMockMvc.perform(get("/api/estadios/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEstadio() throws Exception {
        // Initialize the database
        estadioRepository.saveAndFlush(estadio);

		int databaseSizeBeforeUpdate = estadioRepository.findAll().size();

        // Update the estadio
        estadio.setNombre(UPDATED_NOMBRE);
        estadio.setCreacion(UPDATED_CREACION);
        estadio.setAforo(UPDATED_AFORO);
        

        restEstadioMockMvc.perform(put("/api/estadios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estadio)))
                .andExpect(status().isOk());

        // Validate the Estadio in the database
        List<Estadio> estadios = estadioRepository.findAll();
        assertThat(estadios).hasSize(databaseSizeBeforeUpdate);
        Estadio testEstadio = estadios.get(estadios.size() - 1);
        assertThat(testEstadio.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEstadio.getCreacion()).isEqualTo(UPDATED_CREACION);
        assertThat(testEstadio.getAforo()).isEqualTo(UPDATED_AFORO);
    }

    @Test
    @Transactional
    public void deleteEstadio() throws Exception {
        // Initialize the database
        estadioRepository.saveAndFlush(estadio);

		int databaseSizeBeforeDelete = estadioRepository.findAll().size();

        // Get the estadio
        restEstadioMockMvc.perform(delete("/api/estadios/{id}", estadio.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Estadio> estadios = estadioRepository.findAll();
        assertThat(estadios).hasSize(databaseSizeBeforeDelete - 1);
    }
}
