package fr.miage.depenses;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(DepenseController.class)
@Import(DepenseService.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DepenseControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
private CategorieClient categorieClient;

    @Test
    void listeVideAuDemarrage() throws Exception {
        mvc.perform(get("/depenses"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void creeUneDepensePuisLaRetrouveDansLaListe() throws Exception {
        String json = """
                {
                    "montant": 25.50,
                    "categorieId": 1,
                    "date": "2026-11-03"
                }
                """;

        mvc.perform(post("/depenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.montant").value(25.5))
                .andExpect(jsonPath("$.categorieId").value(1))
                .andExpect(jsonPath("$.date").value("2026-11-03"));

        mvc.perform(get("/depenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].montant").value(25.5))
                .andExpect(jsonPath("$[0].categorieId").value(1))
                .andExpect(jsonPath("$[0].date").value("2026-11-03"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        """
        {"montant":-10,"categorieId":1,"date":"2026-11-03"}
        """,
        """
        {"montant":0,"categorieId":1,"date":"2026-11-03"}
        """,
        """
        {"categorieId":1,"date":"2026-11-03"}
        """,
        """
        {"montant":25.5,"categorieId":0,"date":"2026-11-03"}
        """,
        """
        {"montant":25.5,"date":"2026-11-03"}
        """,
        """
        {"montant":25.5,"categorieId":1}
        """,
        """
        {"montant":25.5,"categorieId":1,"date":"pas-une-date"}
        """,
        "{"
    })
    void refuseUneRequeteInvalideSansEnregistrer(String json)
            throws Exception {

        mvc.perform(post("/depenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        mvc.perform(get("/depenses"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}