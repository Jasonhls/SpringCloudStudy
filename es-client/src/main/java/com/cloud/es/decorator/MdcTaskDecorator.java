package com.cloud.es.decorator;

import com.cloud.es.filter.TraceIdFilter;
import com.cloud.es.utils.IdUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

import static com.cloud.es.filter.TraceIdFilter.TRACE_ID;

/**
 * @Author: 何立森
 * @Date: 2023/04/03/11:47
 * @Description:
 */
public class MdcTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> map = MDC.getCopyOfContextMap();
        return () -> {
            try {
                MDC.setContextMap(map);
                String traceId = MDC.get(TRACE_ID);
                if(StringUtils.isBlank(traceId)) {
                    MDC.put(TRACE_ID, IdUtils.getNextIdStr());
                }
                runnable.run();
            }finally {
                MDC.clear();
            }
        };
    }
}
