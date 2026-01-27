package com.ai.alarav2.routes

import kotlinx.serialization.Serializable

sealed interface AlaraRoutes {
    @Serializable
    data object Chat : AlaraRoutes

    @Serializable
    data object Dashboard : AlaraRoutes

}