package com.khamroev.springbootfullstack

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TestcontainersTest : AbstractTestcontainers() {

    @Test
    fun `can start postgres DB`() {
        assertThat(container.isRunning).isTrue()
        assertThat(container.isCreated).isTrue()
    }
}
