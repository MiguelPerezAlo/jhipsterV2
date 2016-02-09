package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Socio;
import com.mycompany.myapp.repository.SocioRepository;

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
 * Test class for the SocioResource REST controller.
 *
 * @see SocioResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SocioResourceTest {

    private static final String DEFAULT_NOMBRE = "SAMPLE_TEXT";
    private static final String UPDATED_NOMBRE = "UPDATED_TEXT";

    private static final LocalDate DEFAULT_NACIMIENTO = new LocalDate(0L);
    private static final LocalDate UPDATED_NACIMIENTO = new LocalDate();
    private static final String DEFAULT_TIPO = "SAMPLE_TEXT";
    private static final String UPDATED_TIPO = "UPDATED_TEXT";

    private static final Double DEFAULT_CUOTA = 1D;
    private static final Double UPDATED_CUOTA = 2D;

    @Inject
    private SocioRepository socioRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restSocioMockMvc;

    private Socio socio;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SocioResource socioResource = new SocioResource();
        ReflectionTestUtils.setField(socioResource, "socioRepository", socioRepository);
        this.restSocioMockMvc = MockMvcBuilders.standaloneSetup(socioResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        socio = new Socio();
        socio.setNombre(DEFAULT_NOMBRE);
        socio.setNacimiento(DEFAULT_NACIMIENTO);
        socio.setTipo(DEFAULT_TIPO);
        socio.setCuota(DEFAULT_CUOTA);
    }

    @Test
    @Transactional
    public void createSocio() throws Exception {
        int databaseSizeBeforeCreate = socioRepository.findAll().size();

        // Create the Socio

        restSocioMockMvc.perform(post("/api/socios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(socio)))
                .andExpect(status().isCreated());

        // Validate the Socio in the database
        List<Socio> socios = socioRepository.findAll();
        assertThat(socios).hasSize(databaseSizeBeforeCreate + 1);
        Socio testSocio = socios.get(socios.size() - 1);
        assertThat(testSocio.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testSocio.getNacimiento()).isEqualTo(DEFAULT_NACIMIENTO);
        assertThat(testSocio.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testSocio.getCuota()).isEqualTo(DEFAULT_CUOTA);
    }

    @Test
    @Transactional
    public void getAllSocios() throws Exception {
        // Initialize the database
        socioRepository.saveAndFlush(socio);

        // Get all the socios
        restSocioMockMvc.perform(get("/api/socios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(socio.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].nacimiento").value(hasItem(DEFAULT_NACIMIENTO.toString())))
                .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
                .andExpect(jsonPath("$.[*].cuota").value(hasItem(DEFAULT_CUOTA.doubleValue())));
    }

    @Test
    @Transactional
    public void getSocio() throws Exception {
        // Initialize the database
        socioRepository.saveAndFlush(socio);

        // Get the socio
        restSocioMockMvc.perform(get("/api/socios/{id}", socio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(socio.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.nacimiento").value(DEFAULT_NACIMIENTO.toString()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.cuota").value(DEFAULT_CUOTA.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSocio() throws Exception {
        // Get the socio
        restSocioMockMvc.perform(get("/api/socios/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSocio() throws Exception {
        // Initialize the database
        socioRepository.saveAndFlush(socio);

		int databaseSizeBeforeUpdate = socioRepository.findAll().size();

        // Update the socio
        socio.setNombre(UPDATED_NOMBRE);
        socio.setNacimiento(UPDATED_NACIMIENTO);
        socio.setTipo(UPDATED_TIPO);
        socio.setCuota(UPDATED_CUOTA);
        

        restSocioMockMvc.perform(put("/api/socios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(socio)))
                .andExpect(status().isOk());

        // Validate the Socio in the database
        List<Socio> socios = socioRepository.findAll();
        assertThat(socios).hasSize(databaseSizeBeforeUpdate);
        Socio testSocio = socios.get(socios.size() - 1);
        assertThat(testSocio.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testSocio.getNacimiento()).isEqualTo(UPDATED_NACIMIENTO);
        assertThat(testSocio.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testSocio.getCuota()).isEqualTo(UPDATED_CUOTA);
    }

    @Test
    @Transactional
    public void deleteSocio() throws Exception {
        // Initialize the database
        socioRepository.saveAndFlush(socio);

		int databaseSizeBeforeDelete = socioRepository.findAll().size();

        // Get the socio
        restSocioMockMvc.perform(delete("/api/socios/{id}", socio.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Socio> socios = socioRepository.findAll();
        assertThat(socios).hasSize(databaseSizeBeforeDelete - 1);
    }
}
