package com.bankapp.messagerouter.controller;

import com.bankapp.messagerouter.entity.Partner;
import com.bankapp.messagerouter.service.PartnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/partners")
@CrossOrigin(origins = "*")
@Slf4j
public class PartnerController {

    private final PartnerService partnerService;

    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Partner>> getAllPartners() {
        log.info("Fetching all partners");
        List<Partner> partners = partnerService.getAllPartners();
        return ResponseEntity.ok(partners);
    }

    @GetMapping("/paged")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<Partner>> getPartnersPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "alias") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Page<Partner> partners = partnerService.getPartnersPaginated(page, size, sortBy, direction);
        return ResponseEntity.ok(partners);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Partner> getPartnerById(@PathVariable Long id) {
        log.info("Fetching partner with id: {}", id);
        Partner partner = partnerService.getPartnerById(id);
        return ResponseEntity.ok(partner);
    }

    @PostMapping
    public ResponseEntity<Partner> savePartner(@Valid @RequestBody Partner partner) {
        log.info("Creating partner: {}", partner);
        Partner createdPartner = partnerService.savePartner(partner);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPartner);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deletePartner(@PathVariable Long id) {
//        log.info("Deleting partner with id: {}", id);
//        partnerService.deletePartner(id);
//        return ResponseEntity.noContent().build();
//    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartner(@PathVariable Long id) {
        Optional<Partner> partner = partnerService.findPartnerById(id);
        if (partner.isEmpty()) {
            return ResponseEntity.notFound().build();  // 404 Not Found
        }
        partnerService.deletePartner(id);
        return ResponseEntity.noContent().build();  // 204 No Content
    }
}