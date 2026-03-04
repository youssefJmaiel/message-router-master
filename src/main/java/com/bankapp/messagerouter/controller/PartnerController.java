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
        return ResponseEntity.ok(partnerService.getAllPartners());
    }

    @GetMapping("/paged")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<Partner>> getPartnersPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "alias") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return ResponseEntity.ok(partnerService.getPartnersPaginated(page, size, sortBy, direction));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Partner> getPartnerById(@PathVariable Long id) {
        Partner partner = partnerService.getPartnerById(id);
        return ResponseEntity.ok(partner);
    }

    @PostMapping
    public ResponseEntity<Partner> savePartner(@Valid @RequestBody Partner partner) {
        return new ResponseEntity<>(partnerService.savePartner(partner), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartner(@PathVariable Long id) {
        partnerService.deletePartner(id);
        return ResponseEntity.noContent().build();
    }
}