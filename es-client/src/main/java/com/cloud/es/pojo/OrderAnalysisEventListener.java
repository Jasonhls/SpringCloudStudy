package com.cloud.es.pojo;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 何立森
 * @Date: 2023/03/29/14:50
 * @Description:
 */
@Data
@Component
public class OrderAnalysisEventListener extends AnalysisEventListener<OrderExcel2> {

    private List<OrderExcel2> resultList = new ArrayList<>();

    @Override
    public void invoke(OrderExcel2 orderExcel2, AnalysisContext analysisContext) {
        resultList.add(orderExcel2);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
//        resultList.clear();
    }
}
