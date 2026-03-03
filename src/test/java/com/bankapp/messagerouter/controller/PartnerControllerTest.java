package com.bankapp.messagerouter.controller;

import com.bankapp.messagerouter.entity.Partner;
import com.bankapp.messagerouter.entity.PartnerDirection;
import com.bankapp.messagerouter.entity.PartnerType;
import com.bankapp.messagerouter.entity.ProcessedFlowType;
import com.bankapp.messagerouter.service.PartnerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.security.enabled=false")
@AutoConfigureMockMvc
public class PartnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartnerService partnerService;

    @Autowired
    private ObjectMapper objectMapper;

    // ---------------------- GET ALL PARTNERS ----------------------
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllPartners() throws Exception {
        Partner partner = new Partner();
        partner.setAlias("Test Partner");

        when(partnerService.getAllPartners())
                .thenReturn(Collections.singletonList(partner));

        mockMvc.perform(get("/api/partners"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].alias").value("Test Partner"));
    }

    // ---------------------- ADD PARTNER ----------------------
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testAddPartner() throws Exception {
        Partner partner = new Partner();
        partner.setAlias("New Partner");
        partner.setType(PartnerType.MESSAGE);
        partner.setDirection(PartnerDirection.INBOUND);
        partner.setProcessedFlowType(ProcessedFlowType.MESSAGE);
        partner.setDescription("Valid Description");

        when(partnerService.savePartner(any(Partner.class))).thenReturn(partner);

        mockMvc.perform(post("/api/partners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partner)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.alias").value("New Partner"))
                .andExpect(jsonPath("$.type").value("MESSAGE"))
                .andExpect(jsonPath("$.direction").value("INBOUND"))
                .andExpect(jsonPath("$.processedFlowType").value("MESSAGE"))
                .andExpect(jsonPath("$.description").value("Valid Description"));
    }

    // ---------------------- DELETE PARTNER ----------------------
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void deletePartner_shouldReturnNoContent_whenSuccessful() throws Exception {
        Long partnerId = 1L;
        Partner partner = new Partner();
        partner.setId(partnerId);

        when(partnerService.findPartnerById(partnerId)).thenReturn(Optional.of(partner));
        doNothing().when(partnerService).deletePartner(partnerId);

        mockMvc.perform(delete("/api/partners/{id}", partnerId))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void deletePartner_shouldReturnNotFound_whenPartnerNotFound() throws Exception {
        Long partnerId = 1L;

        when(partnerService.findPartnerById(partnerId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/partners/{id}", partnerId))
                .andExpect(status().isNotFound());
    }

    // ---------------------- GET PARTNER BY ID ----------------------
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void getPartnerById_shouldReturnPartner_whenFound() throws Exception {
        Long partnerId = 1L;
        Partner partner = new Partner();
        partner.setId(partnerId);
        partner.setAlias("Partner 1");

        when(partnerService.findPartnerById(partnerId)).thenReturn(Optional.of(partner));

        mockMvc.perform(get("/api/partners/{id}", partnerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.alias").value("Partner 1"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void getPartnerById_shouldReturnNotFound_whenPartnerNotFound() throws Exception {
        Long partnerId = 1L;

        when(partnerService.findPartnerById(partnerId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/partners/{id}", partnerId))
                .andExpect(status().isNotFound());
    }
}