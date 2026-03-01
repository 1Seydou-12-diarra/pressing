package com.gestionPressing.demo.domain.events


import java.time.Instant

/**
 * DOMAINE — Événement de notification versionné V1
 * Couche  : domain/events
 * Topic   : pressing.notification.events
 */
class NotificationEventV1 {

    final String  eventId      = UUID.randomUUID().toString()
    final String  version      = 'v1'
    final Instant occurredAt   = Instant.now()
    final String  correlationId

    // Canal et priorité
    final CanalNotification    canal
    final PrioriteNotification priorite

    // Destinataire
    final String destinataireEmail
    final String destinataireTelephone
    final String destinataireNom

    // Contenu
    final String sujet
    final String corps
    final String templateId

    // Contexte métier
    final String commandeId
    final String typeEvenementSource

    NotificationEventV1(Map args) {
        this.correlationId          = args.correlationId
        this.canal                  = args.canal
        this.priorite               = args.priorite
        this.destinataireEmail      = args.destinataireEmail
        this.destinataireTelephone  = args.destinataireTelephone
        this.destinataireNom        = args.destinataireNom
        this.sujet                  = args.sujet
        this.corps                  = args.corps
        this.templateId             = args.templateId
        this.commandeId             = args.commandeId
        this.typeEvenementSource    = args.typeEvenementSource
    }

    enum CanalNotification {
        SMS, EMAIL, SMS_ET_EMAIL, PUSH
    }

    enum PrioriteNotification {
        HIGH, NORMAL, LOW
    }
}