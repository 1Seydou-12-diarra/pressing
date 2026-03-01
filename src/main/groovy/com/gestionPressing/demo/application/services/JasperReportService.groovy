package com.gestionPressing.demo.application.services

import com.gestionPressing.demo.domain.models.ArticleCommande
import com.gestionPressing.demo.domain.models.Commande
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperExportManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.JasperPrint
import net.sf.jasperreports.engine.JasperReport
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

@Service
class JasperReportService {

    private static final Logger log = LoggerFactory.getLogger(JasperReportService)

    /**
     * Compile le fichier JRXML
     */
    JasperReport compileTemplate(String jrxmlPath) {

        def resource = new ClassPathResource(jrxmlPath)

        if (!resource.exists()) {
            throw new RuntimeException("Template introuvable : ${jrxmlPath}")
        }

        try {
            resource.inputStream.withCloseable { stream ->
                return JasperCompileManager.compileReport(stream)
            }
        } catch (Throwable e) {   // ⚠️ on capture TOUT
            System.err.println("======================================")
            System.err.println("ERREUR JASPER REPORT")
            System.err.println("Fichier : " + jrxmlPath)
            System.err.println("======================================")
            e.printStackTrace()
            throw e
        }
    }

    /**
     * Génération générique PDF
     */
    byte[] generatePdf(String jrxmlPath, Map<String, Object> params, List<?> data) {

        log.debug("Génération PDF pour template: {}", jrxmlPath)

        JasperReport jasperReport = compileTemplate(jrxmlPath)

        JRBeanCollectionDataSource dataSource =
                new JRBeanCollectionDataSource(data ?: [])

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, params, dataSource)

        return JasperExportManager.exportReportToPdf(jasperPrint)
    }

    /**
     * Ticket de dépôt
     */
    byte[] generateTicketDepot(Commande commande) {

        Map<String, Object> params = [
                "PARAM_CLIENT"    : "${commande.client?.nom ?: ''} ${commande.client?.prenom ?: ''}",
                "PARAM_COMMANDE"  : commande.id as Long,
                "PARAM_AGENCE"    : commande.agence?.nom ?: '',
                "PARAM_DATE_DEBUT": commande.dateDepot
        ]

        return generatePdf("reports/tickets/ticket_depot.jrxml",
                params,
                commande.articles ?: [])
    }

    /**
     * Facture client
     */
    byte[] generateFactureClient(Commande commande) {

        Map<String, Object> params = [
                "PARAM_CLIENT"    : "${commande.client?.nom ?: ''} ${commande.client?.prenom ?: ''}",
                "PARAM_COMMANDE"  : commande.id as Long,
                "PARAM_AGENCE"    : commande.agence?.nom ?: '',
                "PARAM_DATE_DEBUT": commande.dateDepot,
                "PARAM_TOTAL"     : commande.montantTotal ?: 0
        ]

        return generatePdf("reports/factures/facture_client.jrxml",
                params,
                commande.articles ?: [])
    }

    /**
     * CA Journalier
     */
    byte[] generateCaJournalier(List<Commande> commandes, String date) {

        Map<String, Object> params = [
                "PARAM_DATE": date ?: ''
        ]

        return generatePdf("reports/rapports/ca_journalier.jrxml",
                params,
                commandes ?: [])
    }

    /**
     * Étiquette article
     */
    byte[] generateEtiquetteArticle(ArticleCommande article) {

        Map<String, Object> params = [
                "PARAM_CODE_BARRE": article.codeBarres ?: '',
                "PARAM_TYPE"      : article.typeVetement?.toString() ?: '',
                "PARAM_SERVICE"   : article.service?.toString() ?: ''
        ]

        return generatePdf("reports/etiquettes/etiquette_article.jrxml",
                params,
                [article])
    }
}