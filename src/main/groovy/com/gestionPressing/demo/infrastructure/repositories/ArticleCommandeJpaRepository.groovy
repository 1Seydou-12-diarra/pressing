package com.gestionPressing.demo.infrastructure.repositories

import com.gestionPressing.demo.domain.models.ArticleCommande
import jdk.javadoc.doclet.Reporter
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ArticleCommandeJpaRepository extends JpaRepository<ArticleCommande, Long> {

}