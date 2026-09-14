package com.tpvgallega.backend.infrastructure.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class PedidoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void flujoCompletoDeUnPedidoAtravesDelContratoOpenApi() throws Exception {
        String cuerpoCrear = """
                {
                  "mesa": 5,
                  "lineas": [
                    {"nombreProducto": "Pulpo a feira", "cantidad": 2, "tipoProducto": "COMIDA"}
                  ]
                }
                """;

        String respuesta = mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpoCrear))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mesa").value(5))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"))
                .andExpect(jsonPath("$.lineas[0].nombreProducto").value("Pulpo a feira"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = JsonPath.parse(respuesta).read("$.id", Long.class);

        mockMvc.perform(get("/api/pedidos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));

        mockMvc.perform(patch("/api/pedidos/{id}/estado", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"estado\": \"PREPARANDO\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("PREPARANDO"));

        mockMvc.perform(patch("/api/pedidos/{id}/estado", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"estado\": \"ENTREGADO\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crearPedidoConMesaInvalidaDevuelve400() throws Exception {
        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mesa\": 0, \"lineas\": [{\"nombreProducto\": \"X\", \"cantidad\": 1, \"tipoProducto\": \"COMIDA\"}]}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crearPedidoSinLineasDevuelve400() throws Exception {
        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mesa\": 3, \"lineas\": []}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void obtenerPedidoInexistenteDevuelve404() throws Exception {
        mockMvc.perform(get("/api/pedidos/{id}", 999999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").exists());
    }
}
