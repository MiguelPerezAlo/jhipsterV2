package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Entrenador;
import com.mycompany.myapp.repository.EntrenadorRepository;

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
 * Test class for the EntrenadorResource REST controller.
 *
 * @see EntrenadorResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EntrenadorResourceTest {

    private static final String DEFAULT_NOMBRE = "SAMPLE_TEXT";
    private static final String UPDATED_NOMBRE = "UPDATED_TEXT";

    private static final LocalDate DEFAULT_NACIMIENTO = new LocalDate(0L);
    private static final LocalDate UPDATED_NACIMIENTO = new LocalDate();
    private static final String DEFAULT_NACIONALIDAD = "SAMPLE_TEXT";
    private static final String UPDATED_NACIONALIDAD = "UPDATED_TEXT";

    private static final Integer DEFAULT_PARTIDOS = 1;
    private static final Integer UPDATED_PARTIDOS = 2;

    @Inject
    private EntrenadorRepository entrenadorRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restEntrenadorMockMvc;

    private Entrenador entrenador;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EntrenadorResource entrenadorResource = new EntrenadorResource();
        ReflectionTestUtils.setField(entrenadorResource, "entrenadorRepository", entrenadorRepository);
        this.restEntrenadorMockMvc = MockMvcBuilders.standaloneSetup(entrenadorResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        entrenador = new Entrenador();
        entrenador.setNombre(DEFAULT_NOMBRE);
        entrenador.setNacimiento(DEFAULT_NACIMIENTO);
        entrenador.setNacionalidad(DEFAULT_NACIONALIDAD);
        entrenador.setPartidos(DEFAULT_PARTIDOS);
    }

    @Test
    @Transactional
    public void createEntrenador() throws Exception {
        int databaseSizeBeforeCreate = entrenadorRepository.findAll().size();

        // Create the Entrenador

        restEntrenadorMockMvc.perform(post("/api/entrenadors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(entrenador)))
                .andExpect(status().isCreated());

        // Validate the Entrenador in the database
        List<Entrenador> entrenadors = entrenadorRepository.findAll();
        assertThat(entrenadors).hasSize(databaseSizeBeforeCreate + 1);
        Entrenador testEntrenador = entrenadors.get(entrenadors.size() - 1);
        assertThat(testEntrenador.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEntrenador.getNacimiento()).isEqualTo(DEFAULT_NACIMIENTO);
        assertThat(testEntrenador.getNacionalidad()).isEqualTo(DEFAULT_NACIONALIDAD);
        assertThat(testEntrenador.getPartidos()).isEqualTo(DEFAULT_PARTIDOS);
    }

    @Test
    @Transactional
    public void getAllEntrenadors() throws Exception {
        // Initialize the database
        entrenadorRepository.saveAndFlush(entrenador);

        // Get all the entrenadors
        restEntrenadorMockMvc.perform(get("/api/entrenadors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(entrenador.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].nacimiento").value(hasItem(DEFAULT_NACIMIENTO.toString())))
                .andExpect(jsonPath("$.[*].nacionalidad").value(hasItem(DEFAULT_NACIONALIDAD.toString())))
                .andExpect(jsonPath("$.[*].partidos").value(hasItem(DEFAULT_PARTIDOS)));
    }

    @Test
    @Transactional
    public void getEntrenador() throws Exception {
        // Initialize the database
        entrenadorRepository.saveAndFlush(entrenador);

        // Get the entrenador
        restEntrenadorMockMvc.perform(get("/api/entrenadors/{id}", entrenador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(entrenador.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.nacimiento").value(DEFAULT_NACIMIENTO.toString()))
            .andExpect(jsonPath("$.nacionalidad").value(DEFAULT_NACIONALIDAD.toString()))
            .andExpect(jsonPath("$.partidos").value(DEFAULT_PARTIDOS));
    }

    @Test
    @Transactional
    public void getNonExistingEntrenador() throws Exception {
        // Get the entrenador
        restEntrenadorMockMvc.perform(get("/api/entrenadors/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntrenador() throws Exception {
        // Initialize the database
        entrenadorRepository.saveAndFlush(entrenador);

		int databaseSizeBeforeUpdate = entrenadorRepository.findAll().size();

        // Update the entrenador
        entrenador.setNombre(UPDATED_NOMBRE);
        entrenador.setNacimiento(UPDATED_NACIMIENTO);
        entrenador.setNacionalidad(UPDATED_NACIONALIDAD);
        entrenador.setPartidos(UPDATED_PARTIDOS);
        

        restEntrenadorMockMvc.perform(put("/api/entrenadors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(entrenador)))
                .andExpect(status().isOk());

        // Validate the Entrenador in the database
        List<Entrenador> entrenadors = entrenadorRepository.findAll();
        assertThat(entrenadors).hasSize(databaseSizeBeforeUpdate);
        Entrenador testEntrenador = entrenadors.get(entrenadors.size() - 1);
        assertThat(testEntrenador.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEntrenador.getNacimiento()).isEqualTo(UPDATED_NACIMIENTO);
        assertThat(testEntrenador.getNacionalidad()).isEqualTo(UPDATED_NACIONALIDAD);
        assertThat(testEntrenador.getPartidos()).isEqualTo(UPDATED_PARTIDOS);
    }

    @Test
    @Transactional
    public void deleteEntrenador() throws Exception {
        // Initialize the database
        entrenadorRepository.saveAndFlush(entrenador);

		int databaseSizeBeforeDelete = entrenadorRepository.findAll().size();

        // Get the entrenador
        restEntrenadorMockMvc.perform(delete("/api/entrenadors/{id}", entrenador.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Entrenador> entrenadors = entrenadorRepository.findAll();
        assertThat(entrenadors).hasSize(databaseSizeBeforeDelete - 1);
    }
}
