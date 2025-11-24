package com.example.sombriyakotlin.domain.usecase.location

data class LocationUseCases (
    val sendCurrentLocation: SendCurrentLocation,

    val isLocationConsentGiven: IsLocationConsentGiven,
    val setLocationConsent: SetLocationConsent
)