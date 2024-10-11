package com.d106.arti.storage;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class ElasticResponse {

    private int took;
    private boolean timed_out;
    private Shards _shards;
    private Hits hits;
    private Aggregations aggregations;

    // Getters and Setters
}

@Data
class Shards {

    private int total;
    private int successful;
    private int skipped;
    private int failed;

    // Getters and Setters
}

@Data
class Hits {

    private Total total;
    private List<Hit> hits;

    // Getters and Setters
}

@Data
class Total {

    private int value;
    private String relation;

    // Getters and Setters
}

@Data
class Hit {

    private String _index;
    private String _id;
    private double _score;
    private Map<String, Object> _source;  // _source는 다양한 타입의 데이터가 들어가므로 Map으로 처리

    // Getters and Setters
}

@Data
class Aggregations {

    private RequestUrlCount request_url_count;

    // Getters and Setters
}

@Data
class RequestUrlCount {

    private int doc_count_error_upper_bound;
    private int sum_other_doc_count;
    private List<Bucket> buckets;

    // Getters and Setters
}

@Data
class Bucket {

    private String key;
    private int doc_count;

    // Getters and Setters
}