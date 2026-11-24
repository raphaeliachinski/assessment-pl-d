package com.dachser.profitandloss.infrastructure.config;

import com.dachser.profitandloss.domain.service.ProfitLossCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServiceConfig {

    @Bean
    public ProfitLossCalculator profitLossCalculator() {
        return new ProfitLossCalculator();
    }
}
