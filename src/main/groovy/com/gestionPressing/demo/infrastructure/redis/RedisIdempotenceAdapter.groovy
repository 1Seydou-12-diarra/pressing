package com.gestionPressing.demo.infrastructure.redis

import com.gestionPressing.demo.domain.ports.output.IdempotencePort
import groovy.util.logging.Slf4j
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

import java.time.Duration

/**
 * ═══════════════════════════════════════════════════════════
 *  INFRASTRUCTURE — Adaptateur Idempotence (Redis)
 *  Couche : infrastructure/redis
 *
 *  Implémente IdempotencePort (défini dans le domaine).
 *  Utilise Redis avec TTL 24h pour stocker les eventId déjà traités.
 * ═══════════════════════════════════════════════════════════
 */
@Slf4j
@Component
class RedisIdempotenceAdapter implements IdempotencePort {

    private static final String PREFIX = 'pressing:idem:'
    private static final Duration TTL  = Duration.ofHours(24)

    private final StringRedisTemplate redisTemplate

    RedisIdempotenceAdapter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate
    }

    @Override
    boolean dejaTraite(String eventId) {
        boolean existe = redisTemplate.hasKey("${PREFIX}${eventId}")
        if (existe) {
            log.warn("⚠️ Événement dupliqué détecté - eventId: {}", eventId)
        }
        existe
    }

    @Override
    void marquerCommeTraite(String eventId) {
        redisTemplate.opsForValue().set("${PREFIX}${eventId}", '1', TTL)
        log.debug("🔑 Événement marqué traité - eventId: {}", eventId)
    }
}
