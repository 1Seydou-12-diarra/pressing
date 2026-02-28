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

    JasperReport compileTemplate(String jrxmlPath) {
        def resource = new ClassPathResource(jrxmlPath)
        if (!resource.exists()) {
            throw new RuntimeException("Template introuvable : ${jrxmlPath}")
        }
        JasperCompileManager.compileReport(resource.inputStream)
    }

    byte[] generatePdf(String jrxmlPath, Map<String, Object> params, List<?> data) {
        log.debug("Génération PDF pour template: {}", jrxmlPath)
        JasperReport jasperReport = compileTemplate(jrxmlPath)
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data ?: [])
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource)
        return JasperExportManager.exportReportToPdf(jasperPrint)
    }

    byte[] generateTicketDepot(Commande commande) {
        Map<String,Object> params = [
                "PARAM_CLIENT"    : "${commande.client.nom} ${commande.client.prenom}",
                "PARAM_COMMANDE"  : commande.id,
                "PARAM_AGENCE"    : commande.agence?.nom,
                "PARAM_DATE_DEBUT": commande.dateDepot,
                "PARAM_LOGO"      : "/images/logo.png"
        ]
        generatePdf("reports/tickets/ticket_depot.jrxml", params, commande.articles)
    }

    byte[] generateFactureClient(Commande commande) {
        Map<String,Object> params = [
                "PARAM_CLIENT"    : "${commande.client.nom} ${commande.client.prenom}",
                "PARAM_COMMANDE"  : commande.id,
                "PARAM_AGENCE"    : commande.agence?.nom,
                "PARAM_DATE_DEBUT": commande.dateDepot,
                "PARAM_TOTAL"     : commande.montantTotal,
                "PARAM_LOGO"      : "/images/logo.png"
        ]
        generatePdf("reports/factures/facture_client.jrxml", params, commande.articles)
    }

    byte[] generateCaJournalier(List<Commande> commandes, String date) {
        Map<String,Object> params = [
                "PARAM_DATE": date,
                "PARAM_LOGO": "/images/logo.png"
        ]
        generatePdf("reports/rapports/ca_journalier.jrxml", params, commandes)
    }

    byte[] generateEtiquetteArticle(ArticleCommande article) {
        Map<String,Object> params = [
                "PARAM_CODE_BARRE": article.codeBarres,
                "PARAM_TYPE"      : article.typeVetement,
                "PARAM_SERVICE"   : article.service
        ]
        generatePdf("reports/etiquettes/etiquette_article.jrxml", params, [article])
    }
}
