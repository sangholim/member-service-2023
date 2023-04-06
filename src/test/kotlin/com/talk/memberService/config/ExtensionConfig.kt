package com.talk.memberService.config

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.extensions.spring.SpringExtension
import org.testcontainers.containers.PostgreSQLContainer

object ExtensionConfig: AbstractProjectConfig() {
    val postgresql = PostgreSQLContainer<Nothing>("postgres:latest").apply {
        this.withUsername("root")
        this.withPassword("password")
        this.withDatabaseName("member_service")
    }
    init {
        postgresql.start()
        val r2dbcUrl = "r2dbc:postgresql://${postgresql.username}:${postgresql.password}@${postgresql.host}:${postgresql.getMappedPort(5432)}/member_service"
        System.setProperty("spring.r2dbc.url", r2dbcUrl)
        System.setProperty("spring.flyway.url", postgresql.jdbcUrl)
        System.setProperty("spring.flyway.username", postgresql.username)
        System.setProperty("spring.flyway.password", postgresql.password)
    }
    override fun extensions(): List<Extension> {
        return super.extensions().plus(SpringExtension)
    }
}