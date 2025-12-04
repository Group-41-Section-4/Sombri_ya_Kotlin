package com.example.sombriyakotlin.domain.usecase.report

import com.example.sombriyakotlin.domain.repository.ReportRepository
import javax.inject.Inject

class GetReportByIdUseCase @Inject constructor(private val reportRepository: ReportRepository) {
    suspend operator fun invoke(reportId: String) = reportRepository.getReportById(reportId)
}