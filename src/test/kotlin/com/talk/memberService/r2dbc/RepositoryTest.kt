package com.talk.memberService.r2dbc

import com.talk.memberService.config.R2dbcConfig
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.context.annotation.Import

@DataR2dbcTest
@Import(value = [R2dbcConfig::class])
annotation class RepositoryTest