package com.cloud.testclient.limiter.bucket4j.example;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/area")
public class AreaCalculationController {
    @Autowired
    private PricingPlanService pricingPlanService;

    @PostMapping(value = "/test")
    public ResponseEntity rectangle(@RequestHeader(value = "X-api-key") String apiKey,
                                    @RequestBody RectangleDemensionsV1 demensions) {
        Bucket bucket = pricingPlanService.resolveBucket(apiKey);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if(probe.isConsumed()) {
            return ResponseEntity.ok()
                    .header("X-Rate-Limit-Remaining", Long.toString(probe.getRemainingTokens()))
                    .body(new AreaV1("retangle", demensions.getLength(), demensions.getWidth()));
        }
        long waitForRefill = probe.getNanosToWaitForRefill() / 1000000000;
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .header("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill))
                .build();
    }
}
