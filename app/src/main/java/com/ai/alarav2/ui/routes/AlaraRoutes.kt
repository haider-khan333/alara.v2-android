package com.ai.alarav2.ui.routes

import kotlinx.serialization.Serializable

sealed interface AlaraRoutes {
    @Serializable
    data object AlaraChat : AlaraRoutes

    @Serializable
    data object AlaraDashboard : AlaraRoutes

}