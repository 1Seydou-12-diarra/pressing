package com.gestionPressing.demo.api.controllers

import com.gestionPressing.demo.application.services.CommandeService
import com.gestionPressing.demo.application.services.JasperReportService
import com.gestionPressing.demo.domain.models.Commande
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


    @RestController
    @RequestMapping("/api/reports")
    class ReportingController {

        private final JasperReportService jasperService
        private final CommandeService commandeService

        ReportingController(JasperReportService jasperService,
                            CommandeService commandeService) {
            this.jasperService = jasperService
            this.commandeService = commandeService
        }

        @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CAISSIER','ROLE_MANAGER')")
        @GetMapping("/commandes/{id}/ticket")
        ResponseEntity<byte[]> ticketDepot(@PathVariable Long id) {
            Commande commande = commandeService.findById(id)
            byte[] pdf = jasperService.generateTicketDepot(commande)

            ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=ticket_${id}.pdf")
                    .body(pdf)
        }

        @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CAISSIER','ROLE_MANAGER')")
        @GetMapping("/commandes/{id}/facture")
        ResponseEntity<byte[]> factureClient(@PathVariable Long id) {
            Commande commande = commandeService.findById(id)
            byte[] pdf = jasperService.generateFactureClient(commande)

            ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=facture_${id}.pdf")
                    .body(pdf)
        }

        @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
        @GetMapping("/ca-journalier")
        ResponseEntity<byte[]> caJournalier(@RequestParam String date) {
            def commandes = commandeService.findAllByDate(date)
            byte[] pdf = jasperService.generateCaJournalier(commandes, date)

            ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=ca_journalier_${date}.pdf")
                    .body(pdf)
        }
    }




