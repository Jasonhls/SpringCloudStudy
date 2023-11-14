package com.cloud.canal.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: 何立森
 * @Date: 2023/11/14/16:23
 * @Description:
 */
@NoArgsConstructor
@Data
public class CanalMessage<T> {

    @JsonProperty("data")
    private List<T> data;

    @JsonProperty("database")
    private String database;

    @JsonProperty("es")
    private Long es;

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("isDdl")
    private Boolean isDdl;

    @JsonProperty("old")
    private List<T> old;

    @JsonProperty("pkNames")
    private List<String> pkNames;

    @JsonProperty("sql")
    private String sql;

    @JsonProperty("table")
    private String table;

    @JsonProperty("ts")
    private Long ts;

    @JsonProperty("type")
    private String type;
}
