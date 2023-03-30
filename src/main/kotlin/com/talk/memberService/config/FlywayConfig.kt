package com.talk.memberService.config

import org.flywaydb.core.Flyway
import org.springframework.boot.autoconfigure.flyway.FlywayProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(FlywayProperties::class)
class FlywayConfig(
        private val flywayProperties: FlywayProperties
) {
    @Bean(initMethod = "migrate")
    fun flyway(): Flyway? {
        return Flyway(Flyway.configure()
                .baselineOnMigrate(flywayProperties.isBaselineOnMigrate)
                .dataSource(flywayProperties.url, flywayProperties.user, flywayProperties.password)
        )
    }
}