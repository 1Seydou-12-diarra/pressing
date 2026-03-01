package com.gestionPressing.demo.infrastructure.repositories

import com.gestionPressing.demo.domain.models.Client
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface ClientJpaRepository extends JpaRepository<Client, Long>,
                JpaSpecificationExecutor<Client> {
}

